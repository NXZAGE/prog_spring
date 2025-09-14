package com.itmo.nxzage.common.util.net;

import java.io.Serializable;
import java.util.UUID;

public final record PacketHeader(
    UUID id, 
    Integer packageCount
) implements Serializable {};
