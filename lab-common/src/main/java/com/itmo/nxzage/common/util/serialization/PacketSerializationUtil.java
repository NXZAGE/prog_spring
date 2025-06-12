package com.itmo.nxzage.common.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.common.util.exceptions.DatagramDeserializationException;
import com.itmo.nxzage.common.util.exceptions.PacketSerializationException;
// TODO move to client (если там все норм и channels это тоже датаграммы) 
public class PacketSerializationUtil {
    static {
        var filter = ObjectInputFilter.Config.createFilter("com.itmo.nxzage.common.util.net.*");
        ObjectInputFilter.Config.setSerialFilter(filter);
    }

    private PacketSerializationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static PacketWrapper deseralize(DatagramPacket packet) {
                byte[] data = packet.getData();
        return deseralize(data, data.length);
    }

    public static PacketWrapper deseralize(ByteBuffer packet) {
        byte[] data = packet.array();
        return deseralize(data, data.length);
    }

    /**
     * Сериализует пакет в датаграмму
     *
     * @param packet пакет, который нужно сериализовать
     * @return датаграмма, содержащая сериализованный пакет
     * @throws PacketSerializationException если сериализация не удалась
     *         или пакет не является сериализуемым
     */
    public static DatagramPacket serializeToDatagram(PacketWrapper packet, InetSocketAddress address) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        return new DatagramPacket(data, data.length, address);
    }

    // autoaddress
    public static DatagramPacket serializeToDatagram(PacketWrapper packet) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        return new DatagramPacket(data, data.length);
    }

    public static ByteBuffer serializeToByteBuffer(PacketWrapper packet) {
        if (packet == null) {
            throw new PacketSerializationException("Packet cannot be null");
        }
        byte[] data = serializeToBbytes(packet);
        // TODO возможно нужен buffer.flip()
        return ByteBuffer.wrap(data);
    }

    private static byte[] serializeToBbytes(PacketWrapper packet) {
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

    private static PacketWrapper deseralize(byte[] buffer, int length) {
        try {
            var byteArrayInputStream = new ByteArrayInputStream(buffer, 0, length);
            var objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (PacketWrapper) objectInputStream.readObject();
        } catch (Exception e) {
            throw new DatagramDeserializationException(
                    "Failed to deserialize packet: " + e.getMessage(), e);
        }
    }
}
