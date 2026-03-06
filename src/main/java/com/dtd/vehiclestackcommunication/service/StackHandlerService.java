package com.dtd.vehiclestackcommunication.service;

import com.dtd.vehiclestackcommunication.dto.ActuatorTestDto;
import com.dtd.vehiclestackcommunication.dto.EcuParameterDto;
import com.dtd.vehiclestackcommunication.dto.EcuStatusParamDto;
import com.dtd.vehiclestackcommunication.dto.VCIDeviceInfoDto;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface StackHandlerService {

    SocketResponse<StackResponse> getAvailableVciList(String dllCallMethod) throws JsonProcessingException;

    SocketResponse<String> checkVciConnectivityStatus();

    SocketResponse<StackResponse> vciInitialization(String dllCallMethod, String vciName);

    SocketResponse<VCIDeviceInfoDto> getVciInformation();

    SocketResponse<StackResponse> getVciDeviceInformation(String dllCallMethod, String vciName) throws JsonProcessingException;

    SocketResponse<StackResponse> readDashboardParameters(String dllCallMethod, String ecuName);

    SocketResponse<StackResponse> fetchEcuConnectivityStatus(String dllCallMethod, String ecuName);

    SocketResponse<StackResponse> readEcuDTC(String dllCallMethod, String ecuName) throws JsonProcessingException;

    SocketResponse<StackResponse> clearEcuDTC(String dllCallMethod, String ecuName) throws JsonProcessingException;

    SocketResponse<StackResponse> fetchEcuParameters(String dllCallMethod, String ecuName) throws JsonProcessingException;

    SocketResponse<StackResponse> readEcuParameterValue(String dllCallMethod, EcuParameterDto ecuParameter);

    SocketResponse<StackResponse> fetchEcuStatusParams(String dllCallMethod, String ecuName) throws JsonProcessingException;

    SocketResponse<StackResponse> readParameterListValue(String vciName, String ecuName);

    SocketResponse<StackResponse> readIOParameterListValue(String vciName, String ecuName);
    SocketResponse<StackResponse> readDataByIdentifier(String vciName, String ecuName, String didName);

    SocketResponse<StackResponse> readEcuStatusParamValue(String dllCallMethod, EcuStatusParamDto ecuStatusParam);

    SocketResponse<StackResponse> fetchEcuActuatorParams(String dllCallMethod, String ecuName) throws JsonProcessingException;

    SocketResponse<StackResponse> fetchEcuActuatorParamOptions(String dllCallMethod, ActuatorTestDto ecuActuatorParameter) throws JsonProcessingException;

    SocketResponse<StackResponse> readEcuActuatorParamResponse(String dllCallMethod, ActuatorTestDto ecuActuatorParameter, String ecuActuatorState);

    SocketResponse<StackResponse> readEcuRoutineResponse(String dllCallMethod, String ecuName, String ecuRoutine);

    SocketResponse<StackResponse> ecuFlashing(String dllCallMethod, String ecuName);

    SocketResponse<StackResponse> ecuSerialNumber(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> ecuVersionNumber(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> ecuVinNumber(String dllCallMethod, String ecuNaGET_ECU_SERIAL_NUMBERme, String vciName);

    SocketResponse<StackResponse> vciUNInitialization(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> ecuOfflineDtcList(String dllCallMethod, String ecuName, String vciName) throws JsonProcessingException;

    SocketResponse<StackResponse> ecuOfflineFlash(String dllCallMethod, String ecuName, String vciName);

    //CGW API Details

    SocketResponse<StackResponse> cgwecuSerialNumber(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwShopRepairCode(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmPartNumber(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmSoftwareVersion(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmSupplierCode(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmManufacturingDate(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmProgrammingDate(String dllCallMethod, String ecuName, String vciName);

    SocketResponse<StackResponse> cgwKgmDtcList(String dllCallMethod, String ecuName, String vciName) throws JsonProcessingException;

    SocketResponse<StackResponse> ecuResetService(String dllCallMethod, String vciName);

    SocketResponse<StackResponse> ecuVCIIntilization(String dllCallMethod, String vciName);

    SocketResponse<StackResponse> ecuDTCRead(String dllCallMethod, String vciName);

    SocketResponse<StackResponse> ecuFlashingJob(String dllCallMethod, String vciName);

}
