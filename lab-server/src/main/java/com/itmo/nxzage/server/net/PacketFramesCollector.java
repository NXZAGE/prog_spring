package com.itmo.nxzage.server.net;

import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketHeader;
import com.itmo.nxzage.common.util.serialization.Packetizer;
import com.itmo.nxzage.common.util.serialization.SerializationUtil;

public class PacketFramesCollector {
    private final SortedMap<Integer, byte[]> frames;
    private int expectedFrames;
    private Date created;
    private UUID id;

    {
        frames = new TreeMap<Integer, byte[]>();
        expectedFrames = 1;
        created = new Date();
    }

    public PacketFramesCollector (byte[] firstFrame) {
        if (firstFrame.length < Packetizer.HEADER_SIZE) {
            throw new IllegalArgumentException("Incorrect frame format: no header");
        }
        this.id = Packetizer.getPacketInteractionID(firstFrame);
        addFrame(firstFrame);
    }

    public void addFrame(byte[] frame) {
        if (expectedFrames <= frames.size()) {
            throw new IllegalStateException("Unable to add frame: expected count reached");
        }
        Integer frameNumber = Packetizer.getPacketNumber(frame);
        if (frameNumber.equals(0)) {
            addHeaderFrame(frame);
        } else {
            addPayloadFrame(frame, frameNumber);
        }
    }

    public boolean isReady() {
        return frames.size() == expectedFrames;
    }

    public Packet getPacket() {
        if (!isReady()) {
            throw new IllegalStateException("Packet is not ready");
        }
        try {
            return SerializationUtil.deserialize(Packetizer.assemblePayload(frames), Packet.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to assemble packet", e);
        } catch (ClassNotFoundException e) {
            throw new Error("CRITICAL! Packet class not found", e);
        }
    }

    public long receivingTime() {
        return System.currentTimeMillis() - created.getTime();
    }

    private void addPayloadFrame(byte[] frame, Integer frameNumber) {
        if (!Packetizer.getPacketInteractionID(frame).equals(id)) {
            throw new IllegalArgumentException("Frame UUID is not capable");
        }
        frames.put(frameNumber, Packetizer.getPacketPayload(frame));
        expectedFrames++;
    }

    private void addHeaderFrame(byte[] frame) {
        if (!Packetizer.getPacketInteractionID(frame).equals(id)) {
            throw new IllegalArgumentException("Frame UUID is not capable");
        }
        try {
            PacketHeader header = SerializationUtil.deserialize(Packetizer.getPacketPayload(frame), PacketHeader.class);
            expectedFrames = header.packageCount();
        } catch(IOException e) {
            throw new RuntimeException("Failed to deserialize header", e);
        } catch (ClassNotFoundException e) {
            throw new Error("CRITICAL! PacketHeader class not found", e);
        }
    }
}
