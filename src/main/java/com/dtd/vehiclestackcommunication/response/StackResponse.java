package com.dtd.vehiclestackcommunication.response;

import com.dtd.vehiclestackcommunication.dto.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StackResponse {
    private List<String> ecuList;
    private List<EcuDtcMapDto> ecuDtcMapList;
    private List<EcuDtcDto> ecuDtcList;
    private List<EcuInfoDto> ecuInfoList;
    private Map<String, List<String>> ecuParameters;
    private EcuParameterDto ecuParameterValue;
    private List<EcuParameterDto> ecuParameterValues;
    private Map<String, List<String>> ecuStatusParams;
    private EcuStatusParamDto ecuStatusParamValue;
    private List<EcuStatusParamDto> ecuStatusParamValues;
    private Map<String, List<String>> ecuActuatorParams;
    private ActuatorTestDto ecuActuatorParameterOptions;
    private List<WriteEcuStatusDto> writeEcuStatus;
    private Map<String, Object> getEcuStatusValuesLevel;
    private Map<String, List<String>> ecuActuatorTestList;
    private Map<String, Object> ecuScanList;
    private String token;
    private String dashboardParameterValue;
    private EcuPositionDto ecuPositionData;
    private String ecuFlashingStatus;
    private String dllCallMethod;
    private String ecuName;
    private List<String> vciList;
    private String vciInitializationStatus;
    private VCIDeviceInfoDto vciDeviceInfoDto;
    private String ecuRoutineResponse;
    private ActuatorTestResponseDto ecuActuatorResponse;
    private boolean vciInitialized;
    private boolean vinReadSuccess;
    private String vinData;
    private String logOutput;

    private Map<String, String> didData;   // ✅ ADD THIS

    public Map<String, List<String>> getEcuStatusParams() {
        return ecuStatusParams;
    }

    public void setEcuStatusParams(Map<String, List<String>> ecuStatusParams) {
        this.ecuStatusParams = ecuStatusParams;
    }

    public Map<String, String> getDidData() {
        return didData;
    }

    public void setDidData(Map<String, String> didData) {
        this.didData = didData;
    }
}
