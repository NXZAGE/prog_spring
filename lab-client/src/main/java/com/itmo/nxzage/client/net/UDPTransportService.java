package com.itmo.nxzage.client.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.UUID;
import com.itmo.nxzage.client.exceptions.ResponseTimeOutException;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.common.util.serialization.PacketSerializationUtil;
import com.itmo.nxzage.common.util.serialization.Packetizer;

public class UDPTransportService {
    private static final int BUFFER_SIZE = 10000;
    private final DatagramSocket socket;

    public UDPTransportService(String hostname, int port) {
        try {
            this.socket = new DatagramSocket();
            this.socket.connect (new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to init UDPTransportService: " + e.getMessage(), e);
        }
    }

    public void send(PacketWrapper request) {
        try {
            var packet = PacketSerializationUtil.serializeToDatagram(request);
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException("Failed sending response: " + e.getMessage(), e);
        }
    }

    public byte[] receive(UUID expectedInteractionID, int timeoutMillis) {
        try {
            // TODO покруче таймер поставить
            DatagramPacket datagram = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.setSoTimeout(timeoutMillis);
            socket.receive(datagram);
            byte[] packet = datagram.getData();
            if (Packetizer.getPacketInteractionID(packet).equals(expectedInteractionID)) {
                
                return packet;
            } else {
                System.err.println(Packetizer.getPacketInteractionID(packet).toString());
                System.err.println(expectedInteractionID.toString());
                return receive(expectedInteractionID, Math.max(0, timeoutMillis - 50));
            }
        } catch (SocketTimeoutException e) {
            throw new ResponseTimeOutException("Time limit exceed", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive", e);
        } catch (PacketSerializationException e) {
            throw new RuntimeException("Unable to unpack received packet", e);
        }
    }
}
