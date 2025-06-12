package com.itmo.nxzage.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.common.util.net.PacketWrapper;

public class ExecutionResponsePacker {
    private static int HEAVY_RESPONSE_MAIN_PACKET_MULTIPLIER = 3;

    public static List<PacketWrapper> pack(ExecutionResponse response) {
        return response.isHeavy() ? packHeavyResponse(response) : packLightRecponse(response);
    }

    private static List<PacketWrapper> packLightRecponse(ExecutionResponse response) {
        DataContainer payload = new DataContainer();
        payload.put("status", response.getStatus()).put("message", response.getMessage())
                .putAll(response.getData());
        var packet = new PacketWrapper(PacketType.RESPONSE, payload);
        return Stream.of(packet).toList();
    }

    private static List<PacketWrapper> packHeavyResponse(ExecutionResponse response) {
        DataContainer responseData = response.getData();
        String heavyDataKey = responseData.get("heavy_key", String.class);
        Class<?> heavyDataType = responseData.get("heavy_type", Class.class);
        Collection<?> heavyData = responseData.get(heavyDataKey, Collection.class);
        List<PacketWrapper> dataPackets = frameHeavyData(heavyData);
        var headerPayload = new DataContainer().put("status", response.getStatus())
                .put("message", response.getMessage())
                .put("data_packets_count", Integer.valueOf(dataPackets.size()))
                .put("data_element_type", heavyDataType);
        var headerPacket = new PacketWrapper(PacketType.HEAVY_RESPONSE_HEADER, headerPayload);
        List<PacketWrapper> headerPackets =
                Collections.nCopies(HEAVY_RESPONSE_MAIN_PACKET_MULTIPLIER, headerPacket);
        var packets = new ArrayList<PacketWrapper>();
        packets.addAll(headerPackets);
        packets.addAll(dataPackets);
        return packets;
    }

    private static <T> List<PacketWrapper> frameHeavyData(Collection<T> heavyData) {
        List<PacketWrapper> packets = new ArrayList<PacketWrapper>();
        Integer index = 0;
        for (T element : heavyData) {
            var payload = new DataContainer().put("index", index).put("element", element);
            packets.add(new PacketWrapper(PacketType.HEAVY_RESPONSE_DATA, payload));
            ++index;
        }
        return packets;
    }
}
