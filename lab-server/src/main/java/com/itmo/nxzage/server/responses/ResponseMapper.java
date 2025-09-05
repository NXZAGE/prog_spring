package com.itmo.nxzage.server.responses;

import com.itmo.nxzage.common.util.net.response.DTO.CountryCollectionResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.DefaultResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.NumberResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.PersonCollectionResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.PersonResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.ResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.StringResponseDTO;

public class ResponseMapper {
    public static ResponseDTO toDTO(ExecutionResponse resp) {
        if (resp instanceof NumberResponse r) {
            return new NumberResponseDTO(r.getStatus(), r.getMessage(), r.getValue());
        } else if (resp instanceof StringResponse r) {
            return new StringResponseDTO(r.getStatus(), r.getMessage(), r.getData());
        } else if (resp instanceof CountryCollectionResponse r) {
            return new CountryCollectionResponseDTO(r.getStatus(), r.getMessage(), r.getData());
        } else if (resp instanceof PersonCollectionResponse r) {
            return new PersonCollectionResponseDTO(r.getStatus(), r.getMessage(), r.getData());
        } else if (resp instanceof PersonResponse r) {
            return new PersonResponseDTO(r.getStatus(), r.getMessage(), r.getData());
        } else {
            return new DefaultResponseDTO(resp.getStatus(), resp.getMessage());
        }
    }
}