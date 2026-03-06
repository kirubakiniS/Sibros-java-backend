package com.dtd.vehiclestackcommunication.dto;

import lombok.Data;

@Data
public class EcuRoutineResponseDto {
    private String ecuName;
    private String ecuRoutine;
    private String ecuRoutineResponse;
}
