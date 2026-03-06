package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RequestVehicleFeatureDto {
    @JsonProperty(value = "Features")
    private Map<String, Object> features;
    @JsonProperty(value = "VIN_Number")
    private String vinNumber;
}
