package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EcuDtcDto {
    private String ecuName;
    private String diagnosticTroubleCode;
    private String description;
    private String dtcState;
}
