package com.itmo.nxzage.common.util.net.response.DTO;

import java.util.List;
import com.itmo.nxzage.common.util.data.Country;

public record CountryCollectionResponseDTO(
    String status,
    String message,
    List<Country> data
) implements ResponseDTO {}