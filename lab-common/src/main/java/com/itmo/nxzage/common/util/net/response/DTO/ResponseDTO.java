package com.itmo.nxzage.common.util.net.response.DTO;

import java.io.Serializable;

public sealed interface ResponseDTO extends Serializable
    permits DefaultResponseDTO, PersonResponseDTO, NumberResponseDTO, StringResponseDTO, CountryCollectionResponseDTO, PersonCollectionResponseDTO 
        {
    String status();
    String message();
}
