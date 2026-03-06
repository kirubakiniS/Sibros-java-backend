package com.dtd.vehiclestackcommunication.util;

import com.dtd.vehiclestackcommunication.dto.SocketRequestPayloadDto;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;

import java.util.Objects;

import static com.dtd.vehiclestackcommunication.util.SocketErrorResponseHelper.logInfo;

public class SocketResponseParameters {

    private SocketResponseParameters() {
//        created private constructor to not instantiate this util class
    }

    public static void setResponseParameters(SocketResponse<StackResponse> response, SocketRequestPayloadDto data) {
        if (Objects.nonNull(response.getData())) {
            response.getData().setDllCallMethod(data.getDllCallMethod());
            response.getData().setEcuName(data.getEcuName());
        }
    }

    public static void sendResponseLog(String requestName, Integer status, String message) {
        logInfo("Sending " + requestName + " response status : " + status + " and message : " + message);
    }
}
