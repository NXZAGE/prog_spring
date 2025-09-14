package com.itmo.nxzage.server.services.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketHeader;
import com.itmo.nxzage.common.util.net.response.DTO.ResponseDTO;
import com.itmo.nxzage.common.util.serialization.Packetizer;
import com.itmo.nxzage.common.util.serialization.SerializationUtil;
import com.itmo.nxzage.server.exceptions.InvalidRequestException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;
import com.itmo.nxzage.server.net.PacketFramesCollector;

public class UDPTransportService {
    private static final int MAX_PACKET_SIZE = 1024;
    private final DatagramSocket socket;
    private final int bufferSize;
    private final RequestFilterService filter;
    private final Packetizer packetizer;
    private final Logger logger = ServerLogger.getLogger("UDPTransportService");
    private final Map<UUID, PacketFramesCollector> requestCollectors;
    private final Set<UUID> readyRequests;
    private final ClientRegister clientRegister;

    {
        filter = new RequestFilterService();
        packetizer = new Packetizer(MAX_PACKET_SIZE);
        requestCollectors = new HashMap<UUID, PacketFramesCollector>();
        readyRequests = new HashSet<>();
        clientRegister = new ClientRegister();
    }

    public UDPTransportService(int port, int bufferSize) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.bufferSize = bufferSize;
        
    }

    // public InteractionContext receiveRequest() {
    //     DatagramPacket datagram = new DatagramPacket(new byte[bufferSize], bufferSize);
    //     try {
    //         socket.receive(datagram);
    //         logger.info("Received datagram");
    //     } catch (IOException e) {
    //         // TODO custom exception
    //         logger.severe("IOException during receiving. Details: " + e.getMessage());
    //         throw new RuntimeException("IOException during receiving", e);
    //     }
    //     Packet packet;
    //     try {
    //         packet = PacketSerializationUtil.deseralize(datagram);
    //         InetSocketAddress address = (InetSocketAddress) datagram.getSocketAddress();
    //         InteractionContext interaction = new InteractionContext(packet, address);
    //         filter.filter(interaction);
    //         return interaction;
    //     } catch (DatagramDeserializationException e) {
    //         logger.info("Datagram deserialization failed. Datagram skipped." + e.getMessage());
    //         e.printStackTrace();
    //         return receiveRequest();
    //     } catch (InvalidRequestException e) {
    //         logger.info("RequestFilter didn\'t pass the packet. Details: " + e.getMessage());
    //         return receiveRequest();
    //     }
    // }

    public Packet receiveRequest() {
        while (readyRequests.size() == 0) {
            byte[] frame = receiveFrame();
            UUID id = Packetizer.getPacketInteractionID(frame);
            PacketFramesCollector collector = requestCollectors.get(id);
            if (collector == null) {
                requestCollectors.put(id, new PacketFramesCollector(frame));
                collector = requestCollectors.get(id);
            } else {
                collector.addFrame(frame);
            }
            if (collector.isReady()) {
                readyRequests.add(id);
            }
        }
        // TODO move while to another method 
        UUID readyRequestID = readyRequests.iterator().next();
        var packet = requestCollectors.get(readyRequestID).getPacket();
        requestCollectors.remove(readyRequestID);
        readyRequests.remove(readyRequestID);
        try {
            filter.filter(packet);
            logger.info("Request %s passed request filter".formatted(packet.getInteractionID().toString()));
        } catch (InvalidRequestException e) {
            logger.info("Invalid request assembled: " + e.getMessage());
            packet = receiveRequest(); // recursion 
        }
        return packet;
    }

    private byte[] receiveFrame() {
        DatagramPacket datagram = new DatagramPacket(new byte[bufferSize], bufferSize);

        try {
            socket.receive(datagram);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = datagram.getData();
        // min size check
        if (data.length <= Packetizer.HEADER_SIZE) {
            logger.info("Received too small packet (size=%d)".formatted(data.length));
            return receiveFrame();
        }
        registerClientAddress(datagram);
        return data;
    }

    private void registerClientAddress(DatagramPacket datagram) {
        byte[] data = datagram.getData();
        if (data.length <= Packetizer.HEADER_SIZE) {
            throw new IllegalArgumentException("datagram is too short");
        }
        UUID id = Packetizer.getPacketInteractionID(data);
        var address = new InetSocketAddress(datagram.getAddress(), datagram.getPort());
        clientRegister.registerInteraction(id, address);
        return;
    }

    public void sendResponse(Packet response) {
        // TODO realise
        UUID id = response.getInteractionID();
        InetSocketAddress address = clientRegister.getClientAddress(id);
        // ResponseType responseType = 
        // TODO important оставить(сделать) или убрать PacketType
        try {
            byte[] serializedResponse = SerializationUtil.serialize(response);
            List<byte[]> packets = packetizer.split(serializedResponse, id);
            packets.add(packetizer.buildHeader(new PacketHeader(null, packets.size()), id));
            packets.forEach(packet -> send(packet, address));
        } catch (IOException e) {
            logger.severe("Response serialization failed: " + e.getMessage());
            System.err.println(e.getStackTrace());
            e.printStackTrace();
        }
    }

    private void send(byte[] packet, InetSocketAddress address) {
        try {
            var datagram = new DatagramPacket(packet, packet.length, address);
            socket.send(datagram);
            logger.info("Packet successfully sent");
        } catch(IOException e) {
            // TODO custom exception
            logger.warning("Failied to send packet: " + e.getMessage());
            throw new RuntimeException("IOException during sending", e);
        } catch (PacketSerializationException e) {
            logger.warning("Failed to serialize packet: " + e.getMessage()); 
            throw e;
        }
    }
}
