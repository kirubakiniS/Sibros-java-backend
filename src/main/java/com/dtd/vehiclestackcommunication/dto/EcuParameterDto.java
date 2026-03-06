package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EcuParameterDto {
    private String ecuName;
    private String ecuParameter;
    private String ecuParameterValue;
    private String ecuParameterUnit;
    private Float ecuParameterMinValue;
    private Float getEcuParameterMaxValue;
}
