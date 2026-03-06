package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    @JsonProperty(value = "accessToken")
    private String accessToken;
    @JsonProperty(value = "VIN_Number")
    private String vinNumber;
    @JsonProperty(value = "Status")
    private String status;
}
