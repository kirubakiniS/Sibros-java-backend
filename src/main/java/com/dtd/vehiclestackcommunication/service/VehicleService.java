package com.dtd.vehiclestackcommunication.service;

import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.response.Response;

import java.util.List;
import java.util.Map;

public interface VehicleService {
    Response<ResponseDto> getVehicleStatus(String accessToken, String vinNumber);

    Response<ResponseTokenDto> getAccessToken(String clientSecret);

    Response<List<EcuDtcDto>> getEcuFaultCodes(String ecuName);

    Response<Map<String, Object>> getEcuScanList();

    Response<List<EcuInfoDto>> getEcuInfo(String ecuName);

    Response<Map<String, List<String>>> getEcuActuatorTestList(String ecuName);

    Response<List<ActuatorTestDto>> getEcuActuatorTestData(ActuatorTestListDto actuatorTestList);

    Response<List<String>> getAllFilesInDirectory(String accessToken, String ecuName);

    Response<String> getLatestFile(String accessToken, String ecuName);

    Response<List<String>> getAllDetectedVciList(String accessToken);

    Response<VCIDeviceInfoDto> getVciDeviceDetail(String accessToken, String ecuName);

    Response<String> initializeVci(String accessToken, String vciName);

    Response<EcuRoutineResponseDto> getEcuRoutineResponse(String accessToken, String ecuName, String ecuRoutine);

    Response<EcuPositionDto> getEcuCoordinates(String ecuName);

    Response<List<EcuDtcDto>> clearEcuDTC(String ecuName);


}
