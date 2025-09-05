package com.itmo.nxzage.common.util.net.response.DTO;

import java.io.Serializable;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public final record ResponseHeader(
    ResponseType type,
    Integer packageCount
) implements Serializable {};
