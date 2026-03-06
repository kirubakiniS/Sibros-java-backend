package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcuPositionDto {
    private Integer id;
    private String ecuName;
    private String ecuStatus;
    private float directionX;
    private float directionY;
    private float dtdDirectionX2D;
    private float dtdDirectionY2D;
    private float directionX3d;
    private float directionY3d;
}
