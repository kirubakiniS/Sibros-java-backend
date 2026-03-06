package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResponseVehicleFeatureDto {
    @JsonProperty(value = "Status")
    private Integer status;
    @JsonProperty(value = "Message")
    private String message;
    @JsonProperty(value = "Data")
    private Map<String, Object> data;
}
