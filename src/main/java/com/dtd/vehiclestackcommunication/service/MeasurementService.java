package com.dtd.vehiclestackcommunication.service;

import com.dtd.vehiclestackcommunication.dto.EcuParameterDto;
import com.dtd.vehiclestackcommunication.dto.EcuStatusParamDto;
import com.dtd.vehiclestackcommunication.dto.GetEcuStatusDto;
import com.dtd.vehiclestackcommunication.dto.WriteEcuStatusDto;
import com.dtd.vehiclestackcommunication.response.Response;

import java.util.List;
import java.util.Map;

public interface MeasurementService {
    Response<List<String>> getEcuList();

    Response<Map<String, List<String>>> getEcuParameters(String ecuName);

    Response<Map<String, List<String>>> getEcuStatus(String ecuName);

    Response<List<EcuParameterDto>> getEcuParameterValues(Map<String, List<String>> ecuParameters);

    Response<List<EcuStatusParamDto>> getEcuStatusValues(Map<String, List<String>> ecuStatuses);

    Response<List<WriteEcuStatusDto>> writeEcuStatusValues(String ecuName, String ecuStatus);

    Map<String, Object> getEcuStatusValuesLevel(GetEcuStatusDto ecuStatuses);
}
