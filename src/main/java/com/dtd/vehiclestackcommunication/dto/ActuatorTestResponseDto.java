package com.dtd.vehiclestackcommunication.dto;

import lombok.Data;

@Data
public class ActuatorTestResponseDto {
    private String ecuName;
    private String actuatorTestParam;
    private String ecuActuatorState;
    private String ecuActuatorResponse;
    private String ecuActuatorMessage;
}
