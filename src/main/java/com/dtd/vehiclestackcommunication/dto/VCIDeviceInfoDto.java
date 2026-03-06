package com.dtd.vehiclestackcommunication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VCIDeviceInfoDto {
    private String interfaceDeviceName;
    private String interfaceVendorName;
    private String interfaceFirmwareVersion;
    private String interfacePartNumber;
}
