package com.itmo.nxzage.common.util.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.PacketHeader;

public class Packetizer {

    public static final int HEADER_SIZE = 4 + 16; // number + UUID
    private final int maxPacketSize;

    public Packetizer(int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    public List<byte[]> split(byte[] payload, UUID uuid) {
        int chunkSize = maxPacketSize - HEADER_SIZE;
        int totalChunks = (int) Math.ceil(payload.length / (double) chunkSize);

        List<byte[]> packets = new ArrayList<>();

        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, payload.length);
            byte[] chunk = Arrays.copyOfRange(payload, start, end);

            ByteBuffer bb = ByteBuffer.allocate(HEADER_SIZE + chunk.length);
            bb.putInt(i + 1); // packet number (1..N)
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            bb.put(chunk);

            packets.add(bb.array());
        }

        return packets;
    }

    public byte[] buildHeader(PacketHeader header, UUID uuid) throws IOException {
        byte[] serialized = SerializationUtil.serialize(header);

        ByteBuffer bb = ByteBuffer.allocate(HEADER_SIZE + serialized.length);
        bb.putInt(0); // header packet → 0
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        bb.put(serialized);

        return bb.array();
    }

    public static Integer getPacketNumber(byte[] packet) {
        return ByteBuffer.wrap(packet).getInt();
    }

    public static UUID getPacketInteractionID(byte[] packet) {
        ByteBuffer bb = ByteBuffer.wrap(packet);
        bb.getInt();
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }

    public static byte[] getPacketPayload(byte[] packet) {
        return Arrays.copyOfRange(packet, HEADER_SIZE, packet.length);
    }

    public static boolean isFrameHeader(byte[] frame) {
        return getPacketNumber(frame).equals(0);
    }

    public static byte[] assemblePayload(SortedMap<Integer, byte[]> packets) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] chunk : packets.values()) {
            try {
                baos.write(chunk);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return baos.toByteArray();
    }

}
