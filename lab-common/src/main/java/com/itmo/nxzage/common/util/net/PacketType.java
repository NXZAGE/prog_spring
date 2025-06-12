package com.itmo.nxzage.common.util.net;

public enum PacketType {
    REQUEST,
    RESPONSE,
    HEAVY_RESPONSE_HEADER,
    HEAVY_RESPONSE_DATA,
    PING,
    PONG
}
