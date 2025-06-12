package com.itmo.nxzage.server.services.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.exceptions.DatagramDeserializationException;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.common.util.serialization.PacketSerializationUtil;
import com.itmo.nxzage.server.exceptions.InvalidRequestException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;

public class UDPTransportService {
    private final DatagramSocket socket;
    private final int bufferSize;
    private final RequestFilterService filter;
    private final Logger logger = ServerLogger.getLogger("UDPTransportService");

    {
        filter = new RequestFilterService();
    }

    public UDPTransportService(int port, int bufferSize) throws SocketException {
        this.socket = new DatagramSocket(port);
        this.bufferSize = bufferSize;
    }

    public InteractionContext receiveRequest() {
        DatagramPacket datagram = new DatagramPacket(new byte[bufferSize], bufferSize);
        try {
            socket.receive(datagram);
            logger.info("Received datagram");
        } catch (IOException e) {
            // TODO custom exception
            logger.severe("IOException during receiving. Details: " + e.getMessage());
            throw new RuntimeException("IOException during receiving", e);
        }
        PacketWrapper packet;
        try {
            packet = PacketSerializationUtil.deseralize(datagram);
            InetSocketAddress address = (InetSocketAddress) datagram.getSocketAddress();
            InteractionContext interaction = new InteractionContext(packet, address);
            filter.filter(interaction);
            return interaction;
        } catch (DatagramDeserializationException e) {
            logger.info("Datagram deserialization failed. Datagram skipped." + e.getMessage());
            e.printStackTrace();
            return receiveRequest();
        } catch (InvalidRequestException e) {
            logger.info("RequestFilter didn\'t pass the packet. Details: " + e.getMessage());
            return receiveRequest();
        }
    }

    public void sendResponse(InteractionContext context) {
        context.getResponses().forEach(response -> send(response, context.getClientAddress()));
    }

    private void send(PacketWrapper packet, InetSocketAddress address) {
        try {
            var datagram = PacketSerializationUtil.serializeToDatagram(packet, address);
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
