package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WriteEcuStatusDto {
    private String ecuName;
    private String ecuStatus;
    private List<String> ecuStatusActionList;
    private List<String> ecuStatusActionOptions;
}
