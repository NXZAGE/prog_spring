package com.itmo.nxzage.client.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketHeader;
import com.itmo.nxzage.common.util.serialization.PacketSerializationUtil;
import com.itmo.nxzage.common.util.serialization.Packetizer;
import com.itmo.nxzage.common.util.serialization.SerializationUtil;

public class UDPTransportService {
    private static final int BUFFER_SIZE = 1024;
    private final DatagramSocket socket;
    private final Packetizer packetizer;

    public UDPTransportService(String hostname, int port) {
        try {
            this.socket = new DatagramSocket();
            this.socket.connect (new InetSocketAddress(hostname, port));
            this.packetizer = new Packetizer(BUFFER_SIZE);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to init UDPTransportService: " + e.getMessage(), e);
        }
    }

    public void send(Packet request) {
        try {
            PacketSerializationUtil.getPacketFrames(request, packetizer).forEach(frame -> sendFrame(frame));
        } catch (IOException e) {
            throw new RuntimeException("Failed sending response: " + e.getMessage(), e);
        }
    }


    private void sendFrame(byte[] frame) {
        DatagramPacket datagram = new DatagramPacket(frame, frame.length);
        try{ 
            socket.send(datagram);
        } catch (IOException e) {
            throw new RuntimeException("Failed sending frame", e);
        }
    }

    public Packet receive(UUID expectedInteractionID, int totalTimeout) {
        SortedMap<Integer, byte[]> frames = new TreeMap<>();
        int expectingFrameCount = 1;
        long deadline = System.currentTimeMillis() + totalTimeout;
        while (expectingFrameCount > frames.size()) {
            if (System.currentTimeMillis() > deadline) {
                throw new RuntimeException("Failed to receive response: time out");
            }
            byte[] frame = receiveExpectedFrame(expectedInteractionID, totalTimeout); // TODO  считать current timeout
            if (Packetizer.isFrameHeader(frame)) {
                try {
                    PacketHeader header = SerializationUtil.deserialize(Packetizer.getPacketPayload(frame), PacketHeader.class);
                    expectingFrameCount = header.packageCount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new Error("CRITICAL: no header class", e);
                }
            } else {
                frames.put(Packetizer.getPacketNumber(frame), Packetizer.getPacketPayload(frame));
                expectingFrameCount++;
            }
        }
        // TODO вынести в метод 
        byte[] payload = Packetizer.assemblePayload(frames); 
        try {
            return SerializationUtil.deserialize(payload, Packet.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new Error("CRITICAL: no packet class", e);
        }
    }

    private byte[] receiveExpectedFrame(UUID expectedInteractionID, int totalTimeout) {
        long deadline = System.currentTimeMillis() + totalTimeout;
        long currentTimeout = totalTimeout;
        do {
            byte[] frame = receiveFrame((int) currentTimeout);
            if (isFrameExpected(frame, expectedInteractionID)) {
                return frame;
            }
        } while ((currentTimeout = deadline - System.currentTimeMillis()) > 0);
        throw new RuntimeException("Failed to get expected frame: time out");
    }

    private boolean isFrameExpected(byte[] frame, UUID expectedInteractionID) {
        if (frame.length < Packetizer.HEADER_SIZE) {
            return false;
        }
        return Packetizer.getPacketInteractionID(frame).equals(expectedInteractionID);
    }


    private byte[] receiveFrame(int timeout) {
        DatagramPacket datagram = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        try {
            socket.receive(datagram);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagram.getData();
    }


    // public byte[] receive(UUID expectedInteractionID, int timeoutMillis) {
    //     try {
    //         // TODO покруче таймер поставить
    //         DatagramPacket datagram = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
    //         socket.setSoTimeout(timeoutMillis);
    //         socket.receive(datagram);
    //         byte[] packet = datagram.getData();
    //         if (Packetizer.getPacketInteractionID(packet).equals(expectedInteractionID)) {
                
    //             return packet;
    //         } else {
    //             System.err.println(Packetizer.getPacketInteractionID(packet).toString());
    //             System.err.println(expectedInteractionID.toString());
    //             return receive(expectedInteractionID, Math.max(0, timeoutMillis - 50));
    //         }
    //     } catch (SocketTimeoutException e) {
    //         throw new ResponseTimeOutException("Time limit exceed", e);
    //     } catch (IOException e) {
    //         throw new RuntimeException("Failed to receive", e);
    //     } catch (PacketSerializationException e) {
    //         throw new RuntimeException("Unable to unpack received packet", e);
    //     }
    // }
}
