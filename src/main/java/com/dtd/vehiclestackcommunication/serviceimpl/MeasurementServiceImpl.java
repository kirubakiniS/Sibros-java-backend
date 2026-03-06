package com.dtd.vehiclestackcommunication.serviceimpl;

import com.dtd.vehiclestackcommunication.dto.EcuParameterDto;
import com.dtd.vehiclestackcommunication.dto.EcuStatusParamDto;
import com.dtd.vehiclestackcommunication.dto.GetEcuStatusDto;
import com.dtd.vehiclestackcommunication.dto.WriteEcuStatusDto;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.service.MeasurementService;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.LogUtil;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MeasurementServiceImpl implements MeasurementService {
    public static final String LEVEL_1 = "Level 1";
    public static final String DB = "5 db";
    private static final String JSON_PROCESSING_ERROR = "JSON processing error: ";
    private static final String INVALID_JSON = "Invalid JSON data";
    @Autowired
    ServiceLogger serviceLogger;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Response<List<String>> getEcuList() {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<List<String>> response = new Response<>();
        String responseString = null;
        try {
            List<String> ecuList = VehicleStaticData.getEcuList();

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuList);
            response.setMessage("ECU List Retrieved Successfully");
            log.info("ECU List Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(ecuList);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<Map<String, List<String>>> getEcuParameters(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        VehicleStaticData.setEcuParametersMap();
        Map<String, List<String>> ecuParameterList = new HashMap<>();
        Response<Map<String, List<String>>> response = new Response<>();
        String responseString = null;
        try {
            List<String> ecuList = VehicleStaticData.getEcuList();

            if (Objects.equals(ecuName, ecuList.get(0))) {
                List<String> engineParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(0));
                ecuParameterList.put(ecuName, engineParameters);
            }

            if (Objects.equals(ecuName, ecuList.get(1))) {
                List<String> bcmParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(1));
                ecuParameterList.put(ecuName, bcmParameters);
            }

            if (Objects.equals(ecuName, ecuList.get(2))) {
                List<String> clusterParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(2));
                ecuParameterList.put(ecuName, clusterParameters);
            }

            if (Objects.equals(ecuName, ecuList.get(3))) {
                List<String> absParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(3));
                ecuParameterList.put(ecuName, absParameters);
            }

            if (Objects.equals(ecuName, ecuList.get(4))) {
                List<String> acuParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(4));
                ecuParameterList.put(ecuName, acuParameters);
            }

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuParameterList);
            response.setMessage("ECU Parameter List Retrieved Successfully");
            log.info("ECU Parameter List Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(ecuParameterList);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<Map<String, List<String>>> getEcuStatus(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        VehicleStaticData.setEcuStatusMap();
        Map<String, List<String>> ecuStatusList = new HashMap<>();
        Response<Map<String, List<String>>> response = new Response<>();
        String responseString = null;
        try {
            List<String> ecuList = VehicleStaticData.getEcuList();

            if (Objects.equals(ecuName, ecuList.get(0))) {
                List<String> engineStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(0));
                ecuStatusList.put(ecuName, engineStatus);
            }

            if (Objects.equals(ecuName, ecuList.get(1))) {
                List<String> bcmStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(1));
                ecuStatusList.put(ecuName, bcmStatus);
            }

            if (Objects.equals(ecuName, ecuList.get(2))) {
                List<String> clusterStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(2));
                ecuStatusList.put(ecuName, clusterStatus);
            }

            if (Objects.equals(ecuName, ecuList.get(3))) {
                List<String> absStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(3));
                ecuStatusList.put(ecuName, absStatus);
            }

            if (Objects.equals(ecuName, ecuList.get(4))) {
                List<String> acuStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(4));
                ecuStatusList.put(ecuName, acuStatus);
            }

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuStatusList);
            response.setMessage("ECU Status List Retrieved Successfully");
            log.info("ECU Status List Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(ecuStatusList);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<EcuParameterDto>> getEcuParameterValues(Map<String, List<String>> ecuParameters) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<List<EcuParameterDto>> response = new Response<>();
        String responseString = null;
        try {
            List<EcuParameterDto> ecuParameterValues = VehicleStaticData.getParameterData();
            List<EcuParameterDto> ecuParameterData = ecuParameterValues.stream().filter(ecuParameterDto -> (ecuParameters.containsKey(ecuParameterDto.getEcuName()) && ecuParameters.get(ecuParameterDto.getEcuName()).contains(ecuParameterDto.getEcuParameter()))).collect(Collectors.toList());

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuParameterData);
            response.setMessage("ECU Parameter Values Retrieved Successfully");
            log.info("ECU Parameter Values Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(ecuParameterData);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<EcuStatusParamDto>> getEcuStatusValues(Map<String, List<String>> ecuStatuses) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<List<EcuStatusParamDto>> response = new Response<>();
        String responseString = null;
        try {
            List<EcuStatusParamDto> ecuStatusValues = VehicleStaticData.getStatusData();
            List<EcuStatusParamDto> ecuStatusData = ecuStatusValues.stream().filter(ecuStatusDto -> (ecuStatuses.containsKey(ecuStatusDto.getEcuName()) && ecuStatuses.get(ecuStatusDto.getEcuName()).contains(ecuStatusDto.getEcuStatusParam()))).collect(Collectors.toList());

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuStatusData);
            response.setMessage("ECU Status Values Retrieved Successfully");
            log.info("ECU Status Values Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(ecuStatusData);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }


    @Override
    public Response<List<WriteEcuStatusDto>> writeEcuStatusValues(String ecuName, String ecuStatus) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<List<WriteEcuStatusDto>> response = new Response<>();
        String responseString = null;
        try {
            List<WriteEcuStatusDto> ecuStatusList = VehicleStaticData.getWriteStatusData();
            List<WriteEcuStatusDto> ecuParameterData = ecuStatusList.stream().filter(ecuStatusDto -> (ecuName.equals(ecuStatusDto.getEcuName()) && ecuStatus.equals(ecuStatusDto.getEcuStatus()))).collect(Collectors.toList());

            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuParameterData);
            response.setMessage("ECU Status Values write Successfully");
            log.info("ECU Status Values write Successfully");
            responseString = objectMapper.writeValueAsString(ecuParameterData);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Map<String, Object> getEcuStatusValuesLevel(GetEcuStatusDto ecuStatuses) {
        String requestString = null;
        String responseString = null;
        LinkedHashMap<String, Object> ecuStatusValue = new LinkedHashMap<>();
        try {
            requestString = objectMapper.writeValueAsString(ecuStatuses);
            serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters(), requestString);
            if (ecuStatuses.getEcuStatusActionList() != null && !ecuStatuses.getEcuStatusActionList().isEmpty() && ecuStatuses.getEcuStatusActionOptions() != null && !ecuStatuses.getEcuStatusActionOptions().isEmpty()) {
                ecuStatusValue.put("ecuName", ecuStatuses.getEcuName());
                ecuStatusValue.put("ecuStatus", ecuStatuses.getEcuStatus());
                ecuStatusValue.put("ecuStatusAction", ecuStatuses.getEcuStatusActionList());
                ecuStatusValue.put("ecuStatusActionOption", ecuStatuses.getEcuStatusActionOptions());
            } else {
                ecuStatusValue.put("ecuName", ecuStatuses.getEcuName());
                ecuStatusValue.put("ecuStatus", ecuStatuses.getEcuStatus());
                ecuStatusValue.put("ecuStatusAction", MeasurementServiceImpl.LEVEL_1);
                ecuStatusValue.put("ecuStatusActionOption", DB);
            }
            responseString = objectMapper.writeValueAsString(ecuStatusValue);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return ecuStatusValue;
    }
}
