package com.itmo.nxzage.common.util.net.response.DTO;

public record DefaultResponseDTO(
    String status,
    String message
) implements ResponseDTO {}
