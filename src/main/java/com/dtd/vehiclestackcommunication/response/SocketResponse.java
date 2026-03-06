package com.dtd.vehiclestackcommunication.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocketResponse<T> {
    private Integer status;
    private String message;
    private Boolean error;
    private T data;
    @JsonProperty(value = "Vehicle_Parameters")
    private Map<String, Object> vehicleInfo;
    private Map<String, Object> dtc;
}
