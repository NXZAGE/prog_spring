package com.itmo.nxzage.common.util.net.response.DTO;

import java.util.List;
import com.itmo.nxzage.common.util.data.Person;

public record PersonCollectionResponseDTO(
    String status,
    String message,
    List<Person> data
) implements ResponseDTO {}
