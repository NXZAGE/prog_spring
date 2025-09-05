package com.itmo.nxzage.common.util.net.response.DTO;

import com.itmo.nxzage.common.util.data.Person;

public record PersonResponseDTO(
    String status,
    String message,
    Person data
) implements ResponseDTO {}