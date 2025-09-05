package com.itmo.nxzage.common.util.net.response.DTO;

public record StringResponseDTO(
    String status,
    String message,
    String data
) implements ResponseDTO {}
