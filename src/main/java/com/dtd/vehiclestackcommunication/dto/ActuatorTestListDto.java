package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActuatorTestListDto {
    private String ecuName;
    private List<String> actuatorTestParam;
}
