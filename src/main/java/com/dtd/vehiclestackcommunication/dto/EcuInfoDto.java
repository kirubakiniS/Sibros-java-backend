package com.dtd.vehiclestackcommunication.dto;

import lombok.Data;

@Data
public class EcuInfoDto {
    private String ecuName;
    private String ecuDescription;
    private String ecuHardwarePartNo;
    private String ecuSoftwarePartNo;
    private String softwareVersion;
    private String imgUrl;
    private float imgWidth;
    private float imgHeight;
}
