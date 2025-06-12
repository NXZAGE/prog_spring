package com.itmo.nxzage.client.net;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.exceptions.ResponseTimeOutException;
import com.itmo.nxzage.client.exceptions.ServerInteractionFailedException;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.common.util.net.PacketWrapper;

public class NetworkManager {
    private static int REQUEST_SENDING_ATTEMPTS_LIMIT = 2;
    private static int RESPONSE_WAITING_TIME_LIMIT = 1000;
    private static int HEAVY_RESPONSE_HEADER_COPIES_COUNT = 3;
    // TODO make static timeout val
    private UDPTransportService transportService;
    // TODO make init

    public NetworkManager(String hostname, int port) {
        transportService = new UDPTransportService(hostname, port);
    }

    public DataContainer interact(Command command) {
        PacketWrapper packet = CommandPacker.pack(command);
        UUID intercationID = packet.getInteractionID();
        int attemptsLeft = REQUEST_SENDING_ATTEMPTS_LIMIT;
        boolean heavyResponseExpecting = command.getType().equals(Command.Type.SERVER_HEAVY);
        while (attemptsLeft > 0) {
            try {
                attemptsLeft--;
                sendRequest(packet);
                return heavyResponseExpecting ? receiveHeavyResponse(intercationID)
                        : receiveLightResponse(intercationID);
            } catch (ResponseTimeOutException e) {
                continue;
            }
        }
        throw new ServerInteractionFailedException("Failed to interact with server");
    }

    public void sendRequest(PacketWrapper request) {
        transportService.send(request);
    }

    public DataContainer receiveLightResponse(UUID interactionID) {
        PacketWrapper packet = transportService.receive(REQUEST_SENDING_ATTEMPTS_LIMIT);
        if (!packet.getInteractionID().equals(interactionID)) {
            // skipping trash packets
            return receiveHeavyResponse(interactionID);
        }
        if (!packet.getType().equals(PacketType.RESPONSE)) {
            throw new RuntimeException("Unexpected response type: " + packet.getType().toString());
        }
        return packet.getPayload();
    }

    public DataContainer receiveHeavyResponse(UUID intercationID) {
        boolean headerReceived = false;
        int expectingPackets = HEAVY_RESPONSE_HEADER_COPIES_COUNT;
        var response = new DataContainer();
        var dataPackets = new ArrayList<DataContainer>();
        try {
            while (expectingPackets > 0) {
                PacketWrapper packet = transportService.receive(RESPONSE_WAITING_TIME_LIMIT);
                if (!packet.getInteractionID().equals(intercationID)) {
                    continue;
                }
                PacketType type = packet.getType();
                switch (type) {
                    case HEAVY_RESPONSE_HEADER -> {
                        if (headerReceived) {
                            continue;
                        }
                        expectingPackets +=
                                applyHeavyResponseHeadaer(response, packet.getPayload());
                        headerReceived = true;
                    }
                    case HEAVY_RESPONSE_DATA -> {
                        dataPackets.add(packet.getPayload());
                    }
                    case RESPONSE -> {
                        // TODO ошоибочный респонс
                        return packet.getPayload();
                    }
                    default -> {
                        continue;
                    }
                }
            }
        } catch (ResponseTimeOutException e) {
            if (!headerReceived) {
                throw e;
            }
        }

        applyHeavyDataToResponse(response, dataPackets);
        return response;
    }

    /**
     * 
     * @param response
     * @param packetPayload
     * @return количество ожидаемых дата-пакетов
     */
    private static int applyHeavyResponseHeadaer(DataContainer response,
            DataContainer packetPayload) {
        packetPayload.assertType("status", String.class);
        packetPayload.assertType("message", String.class);
        packetPayload.assertType("data_packets_count", Integer.class);
        packetPayload.assertType("data_element_type", Class.class);
        // TODO отбрасываение лишних ключей
        response.putAll(packetPayload);
        return response.get("data_packets_count", Integer.class);
    }

    // TODO move to sub-module (unpacker)
    private static void applyHeavyDataToResponse(DataContainer response,
            List<DataContainer> heavyData) {
        Class<?> type = response.get("data_element_type", Class.class);
        String heavyDataKey;
        if (type == Person.class) {
            heavyDataKey = "person_collection";
        } else if (type == Country.class) {
            heavyDataKey = "nationalities_collection";
        } else {
            throw new RuntimeException("Unsupported heavy data type: " + type.toString());
        }

        heavyData.sort(Comparator.comparingInt(dc -> dc.get("index", Integer.class)));
        switch (heavyDataKey) {
            case "person_collection" -> {
                response.put(heavyDataKey, heavyData.stream().map(dc -> dc.get("element", Person.class)).toList());
            }
            case "nationalities_collection" -> {
                response.put(heavyDataKey, heavyData.stream().map(dc -> dc.get("element", Country.class)).toList());
            }
            default -> {}
        }
    }

}
