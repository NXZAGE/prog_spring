package com.itmo.nxzage.common.util.net.response.DTO;

public record NumberResponseDTO(
    String status,
    String message,
    Integer value
) implements ResponseDTO {}
