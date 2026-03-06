package com.dtd.vehiclestackcommunication.dto;

import lombok.Data;

import java.util.List;

@Data
public class EcuParamListPsResponse {
    private boolean error;
    private int status;
    private String message;
    private String vciName;
    private String ecuName;
    private Integer vciStatus;
    private List<String> data; // parameter list
}
