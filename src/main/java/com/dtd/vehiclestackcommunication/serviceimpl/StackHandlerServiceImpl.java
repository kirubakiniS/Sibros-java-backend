package com.dtd.vehiclestackcommunication.serviceimpl;


import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.dtd.vehiclestackcommunication.service.StackHandlerService;
import com.dtd.vehiclestackcommunication.util.SocketErrorResponseHelper;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StackHandlerServiceImpl implements StackHandlerService {

    public static final String EXCEPTION = "Exception";
    public static final String READ_ECU_PARAMETER_LIST_PS_1 = "D_PDU_READ_ECU_Parameter_List.ps1 ";

    public static final String WrapperDLL_ECU_ParameterListGet = "WrapperDLL_ECU_ParameterListGet.ps1";
    public static final String WrapperDLL_ECU_IOParameterListGet = "WrapperDLL_ECU_IOParameterListGet.ps1";
    public static final String WrapperDLL_ECU_ReadDataByIdentifier = "WrapperDLL_ECU_ReadDataByIdentifier.ps1";
    public static final String STACK_RESPONSE_ERROR = "Error!";
    public static final String RESPONSE_ERROR = "No VCIs connected or related Device Drivers are installed in the Machine";


    public SocketResponse<StackResponse> getAvailableVciList(String dllCallMethod) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDll_Get_VCI_List.ps1 " + dllCallMethod;
        String getVciList = powerShellRunner(script);
        if (!getVciList.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> vciList = objectMapper.readValue(getVciList, new TypeReference<>() {
            });
            stackResponse.setVciList(vciList);
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("List of available VCIs are fetched successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("getVciList data : " + getVciList);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<String> checkVciConnectivityStatus() {
        SocketResponse<String> response = new SocketResponse<>();
        String script = "D_PDU_PCAN_Channel_Condition.ps1";
        String vciConnectivityStatus = powerShellRunner(script);
        if (vciConnectivityStatus.contains("AVAILABLE")) {
            response.setError(false);
            response.setMessage("Vehicle is connected.");
            response.setStatus(HttpStatus.OK.value());
            return response;
        } else {
            log.debug("checkConnectivityStatus data : " + vciConnectivityStatus);
            return SocketErrorResponseHelper.setConnectivityErrorResponse("VCI is disconnected.");
        }
    }

    public SocketResponse<StackResponse> vciInitialization(String dllCallMethod, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        
        String scriptFile = "WrapperDll_VCI_Initialization.ps1";
        List<String> args = new ArrayList<>();

        if (dllCallMethod != null && !dllCallMethod.isBlank()) {
            args.add(dllCallMethod);
        }
        args.add(vciName);
        
        String checkVciInitialization = powerShellRunner(scriptFile,args);
        
        //String checkVciInitialization = executePowerShellScript(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR) && checkVciInitialization.contains("VCI Initialized")) {
            // TimeUnit.SECONDS.sleep(20);
            stackResponse.setVciInitializationStatus(checkVciInitialization);
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("VCI Initialization successful");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<VCIDeviceInfoDto> getVciInformation() {
        SocketResponse<VCIDeviceInfoDto> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        String script = "D_PDU_GetVCIDetails.ps1";
        String vciInfo = powerShellRunner(script);
        VCIDeviceInfoDto vciDeviceInfoDto = new VCIDeviceInfoDto();
        if (!vciInfo.isEmpty() && !vciInfo.contains(STACK_RESPONSE_ERROR)) {
            Map<String, String> deviceDetail = Arrays.stream(vciInfo.split(",")).map(s -> s.split("=")).collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));

            vciDeviceInfoDto.setInterfaceDeviceName(deviceDetail.get("Devicename"));
            vciDeviceInfoDto.setInterfaceVendorName(deviceDetail.get("Vendorname"));
            vciDeviceInfoDto.setInterfaceFirmwareVersion(deviceDetail.get("Firmware version"));
            vciDeviceInfoDto.setInterfacePartNumber(deviceDetail.get("Part number"));

            response.setError(false);
            response.setMessage("VCI information retrieved successfully.");
            response.setStatus(HttpStatus.OK.value());
            response.setData(vciDeviceInfoDto);
        } else {
            log.debug("vciInfo data : " + vciInfo);
            SocketErrorResponseHelper.setSocketErrorResponseForVci("VCI disconnected/malfunctioned - Could not retrieve VCI information");
            errorResponse = SocketErrorResponseHelper.getSocketErrorResponseForVci();
            if (Objects.nonNull(errorResponse)) {
                response.setStatus(errorResponse.getStatus());
                response.setMessage(errorResponse.getMessage());
                response.setError(errorResponse.getError());
            }
        }
        return response;
    }

    public SocketResponse<StackResponse> getVciDeviceInformation(String dllCallMethod, String vciName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDll_GetVCI_DeviceDetails.ps1 " + dllCallMethod + " \"" + vciName + "\"";
        String readVciDeviceInfo = powerShellRunner(script);
        if (!readVciDeviceInfo.isEmpty() && !readVciDeviceInfo.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            VCIDeviceInfoDto vciDeviceInfo = objectMapper.readValue(readVciDeviceInfo, new TypeReference<>() {
            });
            stackResponse.setVciDeviceInfoDto(vciDeviceInfo);
            response.setError(false);
            response.setData(stackResponse);
            response.setMessage("VCI device information retrieved successfully.");
            response.setStatus(HttpStatus.OK.value());

        } else {
            log.debug("vciInfo data : " + readVciDeviceInfo);
            SocketErrorResponseHelper.setSocketErrorResponseForVci("VCI disconnected/malfunctioned - Could not retrieve VCI device information");
            errorResponse = SocketErrorResponseHelper.getSocketErrorResponseForVci();
            if (Objects.nonNull(errorResponse)) {
                response.setStatus(errorResponse.getStatus());
                response.setMessage(errorResponse.getMessage());
                response.setError(errorResponse.getError());
            }
        }
        return response;
    }

    public SocketResponse<StackResponse> readDashboardParameters(String dllCallMethod, String ecuName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_Vehicle_Dashboard_Parameters.ps1 " + dllCallMethod + " " + ecuName;
        String parameterValue = powerShellRunner(script);
        if (!parameterValue.isEmpty() && !parameterValue.contains(EXCEPTION)) {
            response.setError(false);
            stackResponse.setDashboardParameterValue(parameterValue);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle dashboard parameters successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readDashboardParameters : " + parameterValue);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Dashboard parameter value read interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> fetchEcuConnectivityStatus(String dllCallMethod, String ecuName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_ECU_Scan.ps1 " + dllCallMethod + " " + ecuName;
        String ecuConnectivityStatus = powerShellRunner(script);
        if (!ecuConnectivityStatus.isEmpty() && !ecuConnectivityStatus.contains(EXCEPTION)) {
            List<EcuPositionDto> ecuPositionList = VehicleStaticData.getEcuPositionData();
            EcuPositionDto ecuPositionData = ecuPositionList.stream().filter(ecuPosition -> ecuPosition.getEcuName().equals(ecuName)).findAny().orElse(null);
            if (ecuPositionData != null) {
                ecuPositionData.setEcuStatus(ecuConnectivityStatus);
            }
            response.setError(false);
            stackResponse.setEcuPositionData(ecuPositionData);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU status successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("ecuConnectivityStatus status: " + ecuConnectivityStatus);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Ecu connectivity status read interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> readEcuDTC(String dllCallMethod, String ecuName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_ECU_DTC_LIST.ps1 " + dllCallMethod + " " + ecuName;
        String readEcuDtc = powerShellRunner(script);
        if (!readEcuDtc.isEmpty() && !readEcuDtc.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<EcuDtcMapDto> ecuDtcList = objectMapper.readValue(readEcuDtc, new TypeReference<>() {
            });
            response.setError(false);
            stackResponse.setEcuDtcMapList(ecuDtcList);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU DTC list successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuDtc: " + readEcuDtc);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - ECU DTC read interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> clearEcuDTC(String dllCallMethod, String ecuName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CLEAR_ECU_DTC_LIST.ps1 " + dllCallMethod + " " + ecuName;
        String readEcuDtcAfterClear = powerShellRunner(script);
        if (!readEcuDtcAfterClear.isEmpty() && !readEcuDtcAfterClear.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<EcuDtcMapDto> ecuDtcListAfterClear = objectMapper.readValue(readEcuDtcAfterClear, new TypeReference<>() {
            });
            response.setError(false);
            stackResponse.setEcuDtcMapList(ecuDtcListAfterClear);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU DTC list after DTC clear successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuDtc: " + readEcuDtcAfterClear);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - ECU DTC clear interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> fetchEcuParameters(String dllCallMethod, String ecuName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = READ_ECU_PARAMETER_LIST_PS_1 + dllCallMethod + " " + ecuName;
        String readEcuParameters = powerShellRunner(script);
        if (!readEcuParameters.isEmpty() && !readEcuParameters.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> ecuParameters = objectMapper.readValue(readEcuParameters, new TypeReference<>() {
            });
            Map<String, List<String>> ecuParametersMap = new HashMap<>();
            ecuParametersMap.put(ecuName, ecuParameters);
            response.setError(false);
            stackResponse.setEcuParameters(ecuParametersMap);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU parameters list successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuParameters: " + readEcuParameters);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU parameters list interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> readEcuParameterValue(String dllCallMethod, EcuParameterDto ecuParameter) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_ECU_Parameter_Values.ps1 " + dllCallMethod + " " + ecuParameter.getEcuName() + " \"" + ecuParameter.getEcuParameter() + "\"";
        String readEcuParameterValue = powerShellRunner(script);
        if (!readEcuParameterValue.isEmpty() && !readEcuParameterValue.contains(EXCEPTION)) {

            List<EcuParameterDto> ecuParameterValues = VehicleStaticData.getParameterData();
            EcuParameterDto ecuParameterValue = ecuParameterValues.stream().filter(ecuParameterData -> (ecuParameterData.getEcuName().contains(ecuParameter.getEcuName()) && ecuParameterData.getEcuParameter().contains(ecuParameter.getEcuParameter()))).findAny().orElse(null);
            if (ecuParameterValue != null) {
                List<String> ecuParameterValueSplit = Arrays.stream(readEcuParameterValue.split(" ")).collect(Collectors.toList());
                ecuParameterValue.setEcuParameterValue(ecuParameterValueSplit.get(0));
            }
            response.setError(false);
            stackResponse.setEcuParameterValue(ecuParameterValue);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU parameter value successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuParameterValue: " + readEcuParameterValue);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU parameter value interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }
    @Autowired
    private ObjectMapper mapper;
    public SocketResponse<StackResponse> readParameterListValue(
            String vciName,
            String ecuName) {

        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();

        String raw = powerShellRunner(
                "WrapperDLL_ECU_ParameterListGet.ps1",
                vciName,
                ecuName
        );

        log.info("RAW PowerShell OUTPUT >>>{}<<<", raw);

        // Temporary bypass for debugging
        if (raw == null || raw.contains(EXCEPTION)) {
            raw = "[]";
        }

        try {

            String cleanedJson = raw
                    .replace("\uFEFF", "")
                    .trim();

            String onlyArray = extractJsonArray(cleanedJson);

            if (onlyArray != null) {
                cleanedJson = onlyArray;
            }

            log.info("FINAL JSON FOR PARSING >>>{}<<<", cleanedJson);

            List<String> parameterList =
                    mapper.readValue(
                            cleanedJson,
                            new TypeReference<List<String>>() {}
                    );

            Map<String, List<String>> map = new HashMap<>();
            map.put(ecuName, parameterList);

            stackResponse.setEcuStatusParams(map);

            response.setError(false);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle parameter list successfully.");
            response.setStatus(HttpStatus.OK.value());

        } catch (Exception e) {

            log.error("Failed to parse ECU parameter list JSON", e);

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid response format from PowerShell.");
            response.setError(true);
        }

        return response;
    }



    public SocketResponse<StackResponse> readIOParameterListValue(String vciName, String ecuName) {

        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();
        SocketResponse<String> errorResponse;

        String raw = powerShellRunner(
                "WrapperDLL_ECU_IOParameterListGet.ps1",
                vciName,
                ecuName
        );

        log.info("RAW IO PowerShell OUTPUT >>>{}<<<", raw);

        if (raw == null || raw.trim().isEmpty() || raw.contains(EXCEPTION)) {

            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(
                    "VCI disconnected/malfunctioned - Read IO parameter list interrupted with error."
            );

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
            return response;
        }

        try {
            String cleaned = raw.replace("\uFEFF", "").trim();

            // If DLL returns extra logs, try to extract JSON array first
            String onlyArray = extractJsonArray(cleaned);
            List<String> ioParams;

            if (onlyArray != null) {
                // ✅ Case 1: JSON array
                cleaned = onlyArray;
                log.info("FINAL IO JSON ARRAY >>>{}<<<", cleaned);

                ioParams = mapper.readValue(cleaned, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
            } else {
                // ✅ Case 2: single string value (your current case)
                String single = unwrapJsonString(cleaned); // remove quotes if present
                single = single.replace("\\t", "\t");      // if tab is escaped
                single = single.trim();

                log.info("FINAL IO SINGLE VALUE >>>{}<<<", single);

                ioParams = new ArrayList<>();
                ioParams.add(single);
            }

            Map<String, List<String>> ioParamsMap = new HashMap<>();
            ioParamsMap.put(ecuName, ioParams);

            stackResponse.setEcuStatusParams(ioParamsMap);

            response.setData(stackResponse);
            response.setMessage("Retrieved IO parameter list successfully.");
            response.setStatus(HttpStatus.OK.value());
            response.setError(false);

        } catch (Exception e) {

            log.error("Failed to parse IO parameter list. ecuName={}, raw={}", ecuName, raw, e);

            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(
                    "Invalid response format from DLL/PowerShell while parsing IO parameter list."
            );

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }

        return response;
    }

    /**
     * If input is like:
     *   "\"0x1300\\tEnable/disable HV output\""  (with quotes)
     * or
     *   "0x1300\tEnable/disable HV output"      (already plain)
     * return the plain string.
     */
    private String unwrapJsonString(String s) {
        if (s == null) return null;

        String trimmed = s.trim();

        // remove wrapping quotes if present
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }

        // unescape \" if present
        trimmed = trimmed.replace("\\\"", "\"");

        return trimmed;
    }

    private String extractJsonArray(String raw) {

        if (raw == null) return null;

        String s = raw.trim();

        int start = s.indexOf('[');
        int end = s.lastIndexOf(']');

        if (start != -1 && end != -1 && end > start) {

            return s.substring(start, end + 1);
        }

        return null;
    }

    public SocketResponse<StackResponse> readDataByIdentifier(
            String vciName,
            String ecuName,
            String didName) {

        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();

        String raw = powerShellRunner(
                "WrapperDLL_ECU_ReadDataByIdentifier.ps1",
                vciName,
                ecuName,
                didName
        );


        log.info("RAW DID PowerShell OUTPUT >>>{}<<<", raw);

        if (raw != null && !raw.trim().isEmpty() && !raw.contains(EXCEPTION)) {

            try {

                // 🔥 Clean raw output
                String cleaned = raw.replace("\uFEFF", "").trim();

                // Remove wrapping quotes if present
                if (cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
                    cleaned = cleaned.substring(1, cleaned.length() - 1);
                }

                // Replace escaped tab
                cleaned = cleaned.replace("\\t", " - ");

                log.info("FINAL DID VALUE >>>{}<<<", cleaned);

                // Create structured response
                Map<String, String> didResult = new HashMap<>();
                didResult.put("ecuName", ecuName);
                didResult.put("didName", didName);
                didResult.put("value", cleaned);

                stackResponse.setDidData(didResult); // make sure this field exists
                response.setData(stackResponse);
                response.setMessage("Read Data By Identifier successful.");
                response.setStatus(HttpStatus.OK.value());
                response.setError(false);

            } catch (Exception e) {

                log.error("Failed to parse ReadDataByIdentifier. ecuName={}, didName={}, raw={}",
                        ecuName, didName, raw, e);

                errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(
                        "Invalid response format from DLL/PowerShell while parsing DID value."
                );

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(errorResponse.getMessage());
                response.setError(true);
            }

        } else {

            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(
                    "VCI disconnected/malfunctioned - Read Data By Identifier interrupted with error."
            );

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }

        return response;
    }

    public SocketResponse<StackResponse> fetchEcuStatusParams(String dllCallMethod, String ecuName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = READ_ECU_PARAMETER_LIST_PS_1 + dllCallMethod + " " + ecuName;
        String readEcuStatusParams = powerShellRunner(script);
        if (!readEcuStatusParams.isEmpty() && !readEcuStatusParams.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> ecuStatusParams = objectMapper.readValue(readEcuStatusParams, new TypeReference<>() {
            });
            Map<String, List<String>> ecuStatusParamsMap = new HashMap<>();
            ecuStatusParamsMap.put(ecuName, ecuStatusParams);
            response.setError(false);
            stackResponse.setEcuStatusParams(ecuStatusParamsMap);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU status parameter list successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.info("readEcuStatusParams: " + readEcuStatusParams);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU status parameter list interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> readEcuStatusParamValue(String dllCallMethod, EcuStatusParamDto ecuStatusParam) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_ECU_Parameter_Values.ps1 " + dllCallMethod + " " + ecuStatusParam.getEcuName() + " \"" + ecuStatusParam.getEcuStatusParam() + "\"";
        String readEcuStatusParameterValue = powerShellRunner(script);
        if (!readEcuStatusParameterValue.isEmpty() && !readEcuStatusParameterValue.contains(EXCEPTION)) {

            List<EcuStatusParamDto> ecuStatusParamValues = VehicleStaticData.getStatusData();
            EcuStatusParamDto ecuStatusParamValue = ecuStatusParamValues.stream().filter(ecuStatusParamData -> (ecuStatusParamData.getEcuName().contains(ecuStatusParam.getEcuName()) && ecuStatusParamData.getEcuStatusParam().contains(ecuStatusParam.getEcuStatusParam()))).findAny().orElse(null);
            if (ecuStatusParamValue != null) {
                ecuStatusParamValue.setEcuParamState(readEcuStatusParameterValue);
            }
            response.setError(false);
            stackResponse.setEcuStatusParamValue(ecuStatusParamValue);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU status parameter value successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuStatusParameterValue: " + readEcuStatusParameterValue);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU status parameter value interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }


    public SocketResponse<StackResponse> fetchEcuActuatorParams(String dllCallMethod, String ecuName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = READ_ECU_PARAMETER_LIST_PS_1 + dllCallMethod + " " + ecuName;
        String readEcuActuatorParams = powerShellRunner(script);
        if (!readEcuActuatorParams.isEmpty() && !readEcuActuatorParams.contains(EXCEPTION)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> ecuActuatorParameters = objectMapper.readValue(readEcuActuatorParams, new TypeReference<>() {
            });
            Map<String, List<String>> ecuActuatorParamsMap = new HashMap<>();
            ecuActuatorParamsMap.put(ecuName, ecuActuatorParameters);
            response.setError(false);
            stackResponse.setEcuActuatorParams(ecuActuatorParamsMap);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU actuator parameters list successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuActuatorParams: " + readEcuActuatorParams);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU actuator parameters list interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> readEcuRoutineResponse(String dllCallMethod, String ecuName, String ecuRoutine) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_ECU_Routine_Response.ps1 " + dllCallMethod + " " + ecuName + " \"" + ecuRoutine + "\"";
        String readEcuRoutineResponse = powerShellRunner(script);
        if (!readEcuRoutineResponse.isEmpty() && !readEcuRoutineResponse.contains(EXCEPTION)) {
            stackResponse.setEcuRoutineResponse(readEcuRoutineResponse);
            response.setError(false);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU routine response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("readEcuParameterValue: " + readEcuRoutineResponse);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU routine response interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> fetchEcuActuatorParamOptions(String dllCallMethod, ActuatorTestDto ecuActuatorParameter) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_Fetch_ECU_Actuator_Param_Options.ps1 " + dllCallMethod + " " + ecuActuatorParameter.getEcuName() + " \"" + ecuActuatorParameter.getActuatorTestParam() + "\"";
        String fetchEcuActuatorParameterOptions = powerShellRunner(script);
        if (!fetchEcuActuatorParameterOptions.isEmpty() && !fetchEcuActuatorParameterOptions.contains(EXCEPTION)) {

            List<ActuatorTestDto> ecuActuatorParameterValues = VehicleStaticData.getEcuActuatorTestValues();
            ActuatorTestDto ecuActuatorParameterOptions = ecuActuatorParameterValues.stream().filter(ecuActuatorParameterData -> (ecuActuatorParameterData.getEcuName().contains(ecuActuatorParameter.getEcuName()) && ecuActuatorParameterData.getActuatorTestParam().contains(ecuActuatorParameter.getActuatorTestParam()))).findAny().orElse(null);
            if (ecuActuatorParameterOptions != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> ecuActuatorParameterActivities = objectMapper.readValue(fetchEcuActuatorParameterOptions, new TypeReference<>() {
                });
                ecuActuatorParameterOptions.setAction(ecuActuatorParameterActivities);
            }
            response.setError(false);
            stackResponse.setEcuActuatorParameterOptions(ecuActuatorParameterOptions);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU actuator parameter options successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("fetchEcuActuatorParameterOptions: " + fetchEcuActuatorParameterOptions);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU actuator parameter options interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> readEcuActuatorParamResponse(String dllCallMethod, ActuatorTestDto ecuActuatorParameter, String ecuActuatorState) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDll_ECU_Actuator_Param_Response.ps1 " + dllCallMethod + " " + ecuActuatorParameter.getEcuName() + " \"" + ecuActuatorParameter.getActuatorTestParam() + "\" " + ecuActuatorState;
        String readEcuActuatorResponse = powerShellRunner(script);
        if (!readEcuActuatorResponse.isEmpty() && !readEcuActuatorResponse.contains(EXCEPTION)) {
            List<ActuatorTestResponseDto> actuatorTestResponseList = VehicleStaticData.getEcuActuatorTestResponse();
            ActuatorTestResponseDto actuatorTestResponse = actuatorTestResponseList.stream().filter(actuatorTestResponseData -> (actuatorTestResponseData.getEcuName().equals(ecuActuatorParameter.getEcuName()) && actuatorTestResponseData.getActuatorTestParam().equals(ecuActuatorParameter.getActuatorTestParam()) && actuatorTestResponseData.getEcuActuatorState().equals(ecuActuatorState) && actuatorTestResponseData.getEcuActuatorResponse().equals(readEcuActuatorResponse))).findAny().orElse(null);
            if (actuatorTestResponse != null) {
                stackResponse.setEcuActuatorResponse(actuatorTestResponse);
                response.setError(false);
                response.setData(stackResponse);
                response.setMessage("Retrieved vehicle ECU actuator IO response successfully.");
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setError(false);
                response.setMessage("Failed to retrieve vehicle ECU actuator IO response");
                response.setStatus(HttpStatus.NO_CONTENT.value());
            }
        } else {
            log.debug("readEcuActuatorResponse: " + readEcuActuatorResponse);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse("VCI disconnected/malfunctioned - Read ECU actuator IO response interrupted with error.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> ecuFlashing(String dllCallMethod, String ecuName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_ECU_Flashing.ps1 " + dllCallMethod + " " + ecuName;
        String flashingStatus = powerShellRunner(script);
        if (!flashingStatus.isEmpty() && !flashingStatus.contains(EXCEPTION)) {
            response.setError(false);
            stackResponse.setEcuFlashingStatus(flashingStatus);
            response.setData(stackResponse);
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("flashingStatus status: " + flashingStatus);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(flashingStatus);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public String powerShellRunner(String scriptFileName, String... args) {

        StringBuilder output = new StringBuilder();

        try {

            String scriptDirectory =
                    "C:\\Users\\2601-00004\\Downloads\\DTD-KGM-new\\DTD-KGM\\java-dependencies\\stack_dependencies\\PowerShell_Scripts";

            String scriptPath = scriptDirectory + "\\" + scriptFileName;

            String pwshPath =
                    "C:\\Program Files\\PowerShell\\7\\pwsh.exe";

            List<String> command = new ArrayList<>();

            command.add(pwshPath);
            command.add("-ExecutionPolicy");
            command.add("Bypass");
            command.add("-File");
            command.add(scriptPath);

            // add parameters separately
            if (args != null) {
                for (String arg : args) {
                    command.add(arg);
                }
            }

            log.info("Executing PowerShell command: {}", command);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();

            log.info("PowerShell exited with: {}", exitCode);

        } catch (Exception e) {

            log.error("Error running PowerShell script", e);
        }

        return output.toString();
    }

    public SocketResponse<StackResponse> ecuSerialNumber(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_ECU_SERIALNO.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        log.info("checkVciInitialization ecu" + checkVciInitialization);
        if (checkVciInitialization.contains("VCI Initialized")) {
            String originalString = checkVciInitialization.replaceAll("VCI Initialized & filtered", "");
            String searchString = "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
            String replaceString = "000000000000";
            stackResponse.setVciInitializationStatus(originalString.replace(searchString, replaceString));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle ECU SerialNumber response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> ecuVersionNumber(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_ECU_VERSIONNO.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR) && checkVciInitialization.contains("VCI Initialized")) {

            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle ECU VersionNumber response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> ecuVinNumber(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_ECU_VINNO.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR) && checkVciInitialization.contains("VCI Initialized")) {

            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filteredREAD ECU VERSION NUMBER:-0000BD43-1.3.254", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle ECU VinNumber response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }


    public SocketResponse<StackResponse> vciUNInitialization(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDll_VCI_UNInitialization.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR) && checkVciInitialization.contains("VCI Initialized")) {

            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle ECU VinNumber response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;


    }


    public SocketResponse<StackResponse> ecuOfflineDtcList(String dllCallMethod, String ecuName, String vciName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_ECU_DTC_OFFLINE_LIST.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR)) {

            //  stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            // response.setData(stackResponse);
            // response.setError(false);

            ObjectMapper objectMapper = new ObjectMapper();
            List<EcuDtcMapDto> ecuDtcList = objectMapper.readValue(checkVciInitialization, new TypeReference<>() {
            });
            response.setError(false);
            stackResponse.setEcuDtcMapList(ecuDtcList);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU VinNumber response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;


    }


    public SocketResponse<StackResponse> ecuOfflineFlash(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_ECU_OFFLine_Flashing.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR)) {

            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle ECU VinNumber response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;


    }


//    private String powerShellRunnerDups(String script) {
//        String powerShellScriptPath = "powershell.exe -ExecutionPolicy Bypass -File .D:\\Ashif_Project\\DTD-Sibros\\Vehicel-Service\\DTD-Vehicle-Stack-Communication\\java-dependencies\\stack_dependencies\\PowerShell_Scripts\\" + script;
//        log.info("Working Directory = " + System.getProperty("user.dir") + ", PowerShell script path = " + powerShellScriptPath);
//        StringBuilder output = new StringBuilder();
//        try {
//            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", powerShellScriptPath);
//            builder.redirectErrorStream(true);
//            Process process = builder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append(System.lineSeparator());
//            }
//            process.waitFor();
//            log.info("PowerShell exited with: " + process.waitFor());
//        } catch (IOException ex) {
//            return "Powershell execution exception: " + ex.getMessage();
//        } catch (InterruptedException interruptedException) {
//            Thread.currentThread().interrupt();
//            return "Powershell execution exception: " + interruptedException.getMessage();
//        }
//        return output.toString().replaceAll("(\r\n)|(\n)|(,$)", "");
//    }


    //CGW Details API
    public SocketResponse<StackResponse> cgwecuSerialNumber(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_ECU_SERIALNO.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (checkVciInitialization.contains("READ ECU SERIAL NUMBER")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW ECU SerialNumber response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> cgwShopRepairCode(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_REPAIR_CODE.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("READ SHOP REPAIR CODE")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW SHOP Repair Code response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }


    public SocketResponse<StackResponse> cgwKgmPartNumber(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_KGM_PARTNUMBER.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("READ KGM PART NUMBER")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW KGM PART NUMBER response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }


    public SocketResponse<StackResponse> cgwKgmSoftwareVersion(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_KGM_SOFTWARE_VERSION.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("READ KGM SOFTWARE VERSION")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW KGM SOFTWARE VERSION response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> cgwKgmSupplierCode(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_KGM_SUPPLIERCODE.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("READ SUPPLIER CODE")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW KGM SUPPLIER Code response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> cgwKgmManufacturingDate(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_ECU_MANUFACTURING_DATE.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("ECUManufacturingDate")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW KGM ECUManufacturingDate response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> cgwKgmProgrammingDate(String dllCallMethod, String ecuName, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "WrapperDLL_CGW_ECU_PROGRAMMING_DATE.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);

        if (checkVciInitialization.contains("ECU Programming Date")) {
            stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved vehicle CGW KGM ECU Programming Date response successfully.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;
    }

    public SocketResponse<StackResponse> cgwKgmDtcList(String dllCallMethod, String ecuName, String vciName) throws JsonProcessingException {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        SocketResponse<String> errorResponse;
        StackResponse stackResponse = new StackResponse();
        String script = "D_PDU_READ_CGW_ECU_DTC_OFFLINE_LIST.ps1 " + dllCallMethod + " \"" + vciName + "\"" + " \"" + ecuName + "\"";
        String checkVciInitialization = powerShellRunner(script);
        if (!checkVciInitialization.contains(STACK_RESPONSE_ERROR)) {

            //  stackResponse.setVciInitializationStatus(checkVciInitialization.replaceAll("VCI Initialized & filtered", ""));
            // response.setData(stackResponse);
            // response.setError(false)

            //ObjectMapper objectMapper = new ObjectMapper();

            ObjectMapper objectMapper = new ObjectMapper();
            List<EcuDtcMapDto> ecuDtcList = objectMapper.readValue(checkVciInitialization, new TypeReference<>() {
            });

            //List<List<Map<String, Object>>> parsedData = objectMapper.readValue(checkVciInitialization, new TypeReference<List<List<Map<String, Object>>>>() {});

            // Flatten the list by accessing the first item and extracting inner items
            //List<Map<String, Object>> innerList = parsedData.get(0);

            // Output list to store the result
            //List<Map<String, Object>> flattenedList = new ArrayList<>(innerList);

            // For debug: Print out the flattened list
            //String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(flattenedList);

            //List<EcuDtcMapDto> ecuDtcList = objectMapper.readValue(result, new TypeReference<>() {
            //});

            //ObjectMapper objectMapper = new ObjectMapper();
            //List<EcuDtcMapDto> ecuDtcList = objectMapper.readValue(jsonInput, new TypeReference<>() {
            // });
            response.setError(false);
            stackResponse.setEcuDtcMapList(ecuDtcList);
            response.setData(stackResponse);
            response.setMessage("Retrieved vehicle ECU DTC List response successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            log.debug("Is VCI Initialized : " + checkVciInitialization);
            errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }
        return response;


    }

    public String powerShellsRunner(String scriptFileName) {
        StringBuilder output = new StringBuilder();
        try {
            // Update this path if needed
        	String scriptPath = "C:\\Users\\2601-00004\\Downloads\\DTD-KGM-new\\DTD-KGM\\java-dependencies\\stack_dependencies\\PowerShell_Scripts\\" + scriptFileName;
            // Use pwsh.exe (PowerShell 7) instead of legacy powershell.exe
            String pwshPath = "C:\\Program Files\\PowerShell\\7\\pwsh.exe"; // Update if installed elsewhere
            
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pwshPath,
                    "-ExecutionPolicy", "Bypass",
                    "-File", scriptPath
            );

            // Combine stdout and stderr
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            output.append("Exit Code: ").append(exitCode);
        } catch (IOException | InterruptedException e) {
            output.append("❌ Error running PowerShell script: ").append(e.getMessage());
            e.printStackTrace();
        }

        return output.toString();
    }


    public SocketResponse<StackResponse> ecuResetService(String dllCallMethod, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();

        // Name of the PowerShell script
        String script = "ECU_Reset_Service.ps1";

        // Call PowerShell script using pwsh (PowerShell 7)
        String scriptOutput = powerShellsRunner(script);

        System.out.println("PowerShell Output:\n" + scriptOutput);

        if (scriptOutput.contains("ECU reset service with subfunction")) {
            response.setData(stackResponse);
            response.setError(false);
            response.setMessage("Retrieved ECU reset service with subfunction.");
            response.setStatus(HttpStatus.OK.value());
        } else {
            System.err.println("❌ ECU reset service failed or not initialized. Output:\n" + scriptOutput);
            SocketResponse<String> errorResponse = SocketErrorResponseHelper.setConnectivityErrorResponse(RESPONSE_ERROR);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(errorResponse.getMessage());
            response.setError(true);
        }

        return response;
    }

    public SocketResponse<StackResponse> ecuVCIIntilization(String dllCallMethod, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();

        String script = "VCI_Initialization.ps1";
        String scriptOutput = powerShellsRunner(script);

        System.out.println("PowerShell Output:\n" + scriptOutput);

        // Parse the output
        boolean vciInitialized = scriptOutput.contains("VCI Initialization Success: True");
        boolean vinSuccess = scriptOutput.contains("Read VINSuccess: True");

        // Extract VIN data if present
        String vinData = null;
        Pattern vinPattern = Pattern.compile("VIN Data:\\s*(.*)");
        Matcher vinMatcher = vinPattern.matcher(scriptOutput);
        if (vinMatcher.find()) {
            vinData = vinMatcher.group(1).trim();
        }

        // Populate response data
        stackResponse.setVciInitialized(vciInitialized);
        stackResponse.setVinReadSuccess(vinSuccess);
        stackResponse.setVinData(vinData);
        stackResponse.setLogOutput(scriptOutput); // Optional: include full logs

        response.setData(stackResponse);
        response.setError(!(vciInitialized && vinSuccess));
        response.setMessage(vciInitialized && vinSuccess
                ? "Retrieved ECU VCI service and VIN successfully."
                : "VCI or VIN operation failed. Check logs.");
        response.setStatus(vciInitialized && vinSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());

        return response;
    }

    public SocketResponse<StackResponse> ecuDTCRead(String dllCallMethod, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();

        String script = "READ_DTC_SERVICE.ps1";
        String scriptOutput = powerShellsRunner(script);

        System.out.println("PowerShell Output:\n" + scriptOutput);

        // Parse the output
        boolean vciInitialized = scriptOutput.contains("VCI Initialization Success: True");
        boolean vinSuccess = scriptOutput.contains("Read VINSuccess: True");

        // Extract VIN data if present
        String vinData = null;
        Pattern vinPattern = Pattern.compile("VIN Data:\\s*(.*)");
        Matcher vinMatcher = vinPattern.matcher(scriptOutput);
        if (vinMatcher.find()) {
            vinData = vinMatcher.group(1).trim();
        }

        // Populate response data
        stackResponse.setVciInitialized(vciInitialized);
        stackResponse.setVinReadSuccess(vinSuccess);
        stackResponse.setVinData(vinData);
        stackResponse.setLogOutput(scriptOutput); // Optional: include full logs

        response.setData(stackResponse);
        response.setError(!(vciInitialized && vinSuccess));
        response.setMessage(vciInitialized && vinSuccess
                ? "Retrieved ECU VCI service and VIN successfully."
                : "VCI or VIN operation failed. Check logs.");
        response.setStatus(vciInitialized && vinSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());

        return response;
    }

       public SocketResponse<StackResponse> ecuFlashingJob(String dllCallMethod, String vciName) {
        SocketResponse<StackResponse> response = new SocketResponse<>();
        StackResponse stackResponse = new StackResponse();

        String script = "READ_DTC_SERVICE.ps1";
        String scriptOutput = powerShellsRunner(script);

        System.out.println("PowerShell Output:\n" + scriptOutput);

        // Parse the output
        boolean vciInitialized = scriptOutput.contains("VCI Initialization Success: True");
        boolean vinSuccess = scriptOutput.contains("Read VINSuccess: True");

        // Extract VIN data if present
        String vinData = null;
        Pattern vinPattern = Pattern.compile("VIN Data:\\s*(.*)");
        Matcher vinMatcher = vinPattern.matcher(scriptOutput);
        if (vinMatcher.find()) {
            vinData = vinMatcher.group(1).trim();
        }

        // Populate response data
        stackResponse.setVciInitialized(vciInitialized);
        stackResponse.setVinReadSuccess(vinSuccess);
        stackResponse.setVinData(vinData);
        stackResponse.setLogOutput(scriptOutput); // Optional: include full logs

        response.setData(stackResponse);
        response.setError(!(vciInitialized && vinSuccess));
        response.setMessage(vciInitialized && vinSuccess
                ? "Retrieved ECU VCI service and VIN successfully."
                : "VCI or VIN operation failed. Check logs.");
        response.setStatus(vciInitialized && vinSuccess ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());

        return response;
    }

}
