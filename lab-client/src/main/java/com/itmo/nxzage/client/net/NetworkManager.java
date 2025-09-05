package com.itmo.nxzage.client.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.exceptions.ResponseTimeOutException;
import com.itmo.nxzage.client.exceptions.ServerInteractionFailedException;
import com.itmo.nxzage.common.util.net.PacketWrapper;
import com.itmo.nxzage.common.util.net.response.DTO.ResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.ResponseHeader;
import com.itmo.nxzage.common.util.serialization.Packetizer;
import com.itmo.nxzage.common.util.serialization.SerializationUtil;

public class NetworkManager {
    private static int REQUEST_SENDING_ATTEMPTS_LIMIT = 2;
    private static int RESPONSE_WAITING_TIME_LIMIT = 200;
    // TODO make static timeout val
    private UDPTransportService transportService;
    // TODO make init

    public NetworkManager(String hostname, int port) {
        transportService = new UDPTransportService(hostname, port);
    }

    public ResponseDTO interact(Command command) {
        PacketWrapper packet = CommandPacker.pack(command);
        UUID intercationID = packet.getInteractionID();
        int attemptsLeft = REQUEST_SENDING_ATTEMPTS_LIMIT;
        while (attemptsLeft > 0) {
            try {
                attemptsLeft--;
                sendRequest(packet);
                return receiveResponse(intercationID);
            } catch (ResponseTimeOutException e) {
                continue;
            }
        }
        throw new ServerInteractionFailedException("Failed to interact with server");
    }

    public void sendRequest(PacketWrapper request) {
        transportService.send(request);
    }

    public ResponseDTO receiveResponse(UUID interactionID) {
        // TODO realisation
        SortedMap<Integer, byte[]> packets = new TreeMap<>();
        ResponseDTO response = null;
        boolean headerReceived = false;
        int expectingPackets = 1;        
        while (expectingPackets > packets.size()) {
            byte[] packet = transportService.receive(interactionID, RESPONSE_WAITING_TIME_LIMIT);
            Integer packetNumber = Packetizer.getPacketNumber(packet);
            packet = Arrays.copyOfRange(packet, 20, packet.length);
            if (packetNumber == 0) {    // header packet processing
                try {
                    ResponseHeader header = SerializationUtil.deserialize(packet, ResponseHeader.class);
                    expectingPackets = header.packageCount();
                    headerReceived = true;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to assemble server response", e);
                } catch (ClassNotFoundException e) {
                    throw new Error("CRITICAL! Failed to link response header class");
                }
            } else {                    // payload packet processing
                if (!headerReceived) {
                    expectingPackets++;
                }
                packets.put(packetNumber, packet);
            }      
        }
        // payload assembling
        if (headerReceived && packets.size() == expectingPackets) {
            try {
                response = SerializationUtil.deserialize(assemblePayload(packets), ResponseDTO.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to assemble server response");
            } catch (ClassNotFoundException e) {
                throw new Error("CRITICAL! Failed to link response header class");
            }
        }
        return response;
    }

    public static byte[] assemblePayload(SortedMap<Integer, byte[]> packets) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] chunk : packets.values()) {
            try {
                baos.write(chunk);
            } catch (IOException e) {
                // unreal exception
                continue;
            }
        }
        return baos.toByteArray();
    }

    // public DataContainer receiveLightResponse(UUID interactionID) {
    //     PacketWrapper packet = transportService.receive(REQUEST_SENDING_ATTEMPTS_LIMIT);
    //     if (!packet.getInteractionID().equals(interactionID)) {
    //         // skipping trash packets
    //         return receiveHeavyResponse(interactionID);
    //     }
    //     if (!packet.getType().equals(PacketType.RESPONSE)) {
    //         throw new RuntimeException("Unexpected response type: " + packet.getType().toString());
    //     }
    //     return packet.getPayload();
    // }

    // public DataContainer receiveHeavyResponse(UUID intercationID) {
    //     boolean headerReceived = false;
    //     int expectingPackets = HEAVY_RESPONSE_HEADER_COPIES_COUNT;
    //     var response = new DataContainer();
    //     var dataPackets = new ArrayList<DataContainer>();
    //     try {
    //         while (expectingPackets > 0) {
    //             PacketWrapper packet = transportService.receive(RESPONSE_WAITING_TIME_LIMIT);
    //             if (!packet.getInteractionID().equals(intercationID)) {
    //                 continue;
    //             }
    //             PacketType type = packet.getType();
    //             switch (type) {
    //                 case HEAVY_RESPONSE_HEADER -> {
    //                     if (headerReceived) {
    //                         continue;
    //                     }
    //                     expectingPackets +=
    //                             applyHeavyResponseHeadaer(response, packet.getPayload());
    //                     headerReceived = true;
    //                 }
    //                 case HEAVY_RESPONSE_DATA -> {
    //                     dataPackets.add(packet.getPayload());
    //                 }
    //                 case RESPONSE -> {
    //                     // TODO ошоибочный респонс
    //                     return packet.getPayload();
    //                 }
    //                 default -> {
    //                     continue;
    //                 }
    //             }
    //         }
    //     } catch (ResponseTimeOutException e) {
    //         if (!headerReceived) {
    //             throw e;
    //         }
    //     }

    //     applyHeavyDataToResponse(response, dataPackets);
    //     return response;
    // }

    // /**
    //  * 
    //  * @param response
    //  * @param packetPayload
    //  * @return количество ожидаемых дата-пакетов
    //  */
    // private static int applyHeavyResponseHeadaer(DataContainer response,
    //         DataContainer packetPayload) {
    //     packetPayload.assertType("status", String.class);
    //     packetPayload.assertType("message", String.class);
    //     packetPayload.assertType("data_packets_count", Integer.class);
    //     packetPayload.assertType("data_element_type", Class.class);
    //     // TODO отбрасываение лишних ключей
    //     response.putAll(packetPayload);
    //     return response.get("data_packets_count", Integer.class);
    // }

    // // TODO move to sub-module (unpacker)
    // private static void applyHeavyDataToResponse(DataContainer response,
    //         List<DataContainer> heavyData) {
    //     Class<?> type = response.get("data_element_type", Class.class);
    //     String heavyDataKey;
    //     if (type == Person.class) {
    //         heavyDataKey = "person_collection";
    //     } else if (type == Country.class) {
    //         heavyDataKey = "nationalities_collection";
    //     } else {
    //         throw new RuntimeException("Unsupported heavy data type: " + type.toString());
    //     }

    //     heavyData.sort(Comparator.comparingInt(dc -> dc.get("index", Integer.class)));
    //     switch (heavyDataKey) {
    //         case "person_collection" -> {
    //             response.put(heavyDataKey, heavyData.stream().map(dc -> dc.get("element", Person.class)).toList());
    //         }
    //         case "nationalities_collection" -> {
    //             response.put(heavyDataKey, heavyData.stream().map(dc -> dc.get("element", Country.class)).toList());
    //         }
    //         default -> {}
    //     }
    // }

}
