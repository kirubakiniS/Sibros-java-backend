package com.dtd.vehiclestackcommunication.dto;

import lombok.Data;

@Data
public class EcuDtcMapDto {
    private String isDtcFound;
    private EcuDtcDto readDtc;
}
