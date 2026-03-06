package com.dtd.vehiclestackcommunication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocketRequestPayloadDto {
    private String ecuName;
    private String ecuStatus;
    private String vciName;
    private String didName;

    private Map<String, List<String>> ecuParameters;
    private EcuParameterDto ecuParameter;
    private Map<String, List<String>> ecuStatuses;
    private EcuStatusParamDto ecuStatusParam;
    private ActuatorTestDto ecuActuatorParameter;
    private String clientSecret;
    private GetEcuStatusDto status;
    private String ecuStatusActionList;
    private String ecuStatusActionOptions;
    private String requestToken;
    private String dllCallMethod;
    private String ecuRoutine;
    private List<String> dllArgs;
    private String ecuActuatorState;
}
