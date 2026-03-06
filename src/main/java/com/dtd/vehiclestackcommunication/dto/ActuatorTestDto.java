package com.dtd.vehiclestackcommunication.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActuatorTestDto {
    private String ecuName;
    private String actuatorTestParam;
    private List<String> action;
    private List<String> condition;
    private List<String> actuatorTestDescription;
    private List<String> actuatorActivationMethod;
    private String actuatorTestTimeDuration;
}
