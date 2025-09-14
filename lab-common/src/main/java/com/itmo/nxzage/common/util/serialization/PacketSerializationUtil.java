package com.itmo.nxzage.common.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketHeader;
import com.itmo.nxzage.common.util.exceptions.DatagramDeserializationException;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
// TODO move to client (если там все норм и channels это тоже датаграммы) 
public class PacketSerializationUtil {
    static {
        // TODO erase? important
        // var filter = ObjectInputFilter.Config.createFilter("com.itmo.nxzage.common.util.net.*");
        // ObjectInputFilter.Config.setSerialFilter(filter);
    }

    private PacketSerializationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Packet deseralize(DatagramPacket packet) {
        byte[] data = packet.getData();
        return deseralize(data, data.length);
    }

    public static Packet deseralize(ByteBuffer packet) {
        byte[] data = packet.array();
        return deseralize(data, data.length);
    }

    public static List<byte[]> getPacketFrames(Packet packet, Packetizer packetizer) throws IOException {
        UUID id = packet.getInteractionID();
        List<byte[]> frames = packetizer.split(SerializationUtil.serialize(packet), id);
        PacketHeader header = new PacketHeader(id, frames.size());
        frames.add(packetizer.buildHeader(header, id));
        return frames;
    }

    /**
     * Сериализует пакет в датаграмму
     *
     * @param packet пакет, который нужно сериализовать
     * @return датаграмма, содержащая сериализованный пакет
     * @throws PacketSerializationException если сериализация не удалась
     *         или пакет не является сериализуемым
     */
    public static DatagramPacket serializeToDatagram(Packet packet, InetSocketAddress address) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        return new DatagramPacket(data, data.length, address);
    }

    // autoaddress
    public static DatagramPacket serializeToDatagram(Packet packet) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        return new DatagramPacket(data, data.length);
    }

    public static ByteBuffer serializeToByteBuffer(Packet packet) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        // TODO возможно нужен buffer.flip()
        return ByteBuffer.wrap(data);
    }

    private static byte[] serializeToBbytes(Packet packet) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        try {
            var byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            var objectOutputStream = new java.io.ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            return data;
        } catch (Exception e) {
            throw new PacketSerializationException("Failed to serialize packet: " + e.getMessage(), e);
        }
    }

    private static Packet deseralize(byte[] buffer, int length) {
        try {
            var byteArrayInputStream = new ByteArrayInputStream(buffer, 0, length);
            var objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Packet) objectInputStream.readObject();
        } catch (Exception e) {
            throw new DatagramDeserializationException(
                    "Failed to deserialize packet: " + e.getMessage(), e);
        }
    }
}
