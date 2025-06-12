package com.itmo.nxzage.client.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import com.itmo.nxzage.client.exceptions.ResponseTimeOutException;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.common.util.serialization.PacketSerializationUtil;

public class UDPTransportService {
    private static final int BUFFER_SIZE = 8192;
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

    public PacketWrapper receive(int timeoutMillis) {
        try {
            DatagramPacket datagram = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            socket.setSoTimeout(500);
            socket.receive(datagram);
            return PacketSerializationUtil.deseralize(datagram);
        } catch (SocketTimeoutException e) {
              throw new ResponseTimeOutException("Time limit exceed", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive", e);
        } catch (PacketSerializationException e) {
            throw new RuntimeException("Unable to unpack received packet", e);
        }
    }
}
