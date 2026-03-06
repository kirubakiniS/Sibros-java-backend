package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEcuStatusDto {
    private String ecuName;
    private String ecuStatus;
    private String ecuStatusActionList;
    private String ecuStatusActionOptions;
}
