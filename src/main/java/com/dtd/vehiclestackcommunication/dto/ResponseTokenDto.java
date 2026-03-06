package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTokenDto {
    @JsonProperty(value = "Token")
    private String token;
}
