package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcuStatusParamDto {
    private String ecuName;
    private String ecuStatusParam;
    private String ecuParamState;
}
