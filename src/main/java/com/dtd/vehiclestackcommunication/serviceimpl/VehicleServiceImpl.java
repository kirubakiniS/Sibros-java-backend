/**
 * VehicleServiceImpl
 * Service class to manage functionalities related to Vehicle Controller
 *
 * @name VehicleServiceImpl
 * @vendor BlueBinaries
 * @version 1.0
 * @author developers@bluebinaries.com
 * @copyright Copyright (C) 2022 BlueBinaries. All rights reserved.
 */

package com.dtd.vehiclestackcommunication.serviceimpl;

import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.exception.EmptyDirectoryException;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.service.VehicleService;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.ConstantsUtil;
import com.dtd.vehiclestackcommunication.util.LogUtil;
import com.dtd.vehiclestackcommunication.util.TokenValidationUtil;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    static final String CLIENT_SECRETS = "DTD1@123*45";
    static final String INVALID_TOKEN = "Invalid Token";
    private static final String USER_NAME = "DTD";
    private static final byte[] JWT_SECRETS = VehicleSocketHandlerServiceImpl.getJwtSecret();
    private static final String ERROR = "Error: ";
    private static final String NO_FILES_FOUND = "No files found in the directory.";
    private static final String JSON_PROCESSING_ERROR = "JSON processing error: ";
    private static final String INVALID_JSON = "Invalid JSON data";
    private static final String TOKEN = null;
    @Autowired
    ServiceLogger serviceLogger;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    ObjectMapper objectMapper;

    public static boolean parseJwt(String jwtString) {

        Key hmacKey = new SecretKeySpec(JWT_SECRETS, SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(jwtString);

        return jwt.getBody().get("username").toString().equals(USER_NAME);
    }

    /**
     * This is the method to get token.
     *
     * @param clientSecret get client secret code
     * @return ResponseTokenDto
     */
    @Override
    public Response<ResponseTokenDto> getAccessToken(String clientSecret) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());

        Response<ResponseTokenDto> response = new Response<>();
        ResponseTokenDto tokenResponse = new ResponseTokenDto();
        String responseString;
        if (clientSecret == null || clientSecret.isEmpty()) {
            log.error("ClientSecret is required");
            throw new BadDataException("ClientSecret is required");
        }
        if (clientSecret.equalsIgnoreCase(CLIENT_SECRETS)) {
            VehicleSocketHandlerServiceImpl.createJwtSignedHMAC();
            tokenResponse.setToken(TOKEN);
        } else {
            log.error("Invalid ClientSecret");
            throw new BadDataException("Invalid ClientSecret");
        }

        response.setStatus(HttpStatus.OK.value());
        response.setData(tokenResponse);
        response.setMessage("Token Retrieved Successfully");
        log.info("Token Retrieved Successfully");
        responseString = serializeToJson(tokenResponse);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    /**
     * This is the method to get token, connected status and VIN.
     *
     * @return ResponseDto
     */
    @Override
    public Response<ResponseDto> getVehicleStatus(String accessToken, String vinNumber) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        ResponseDto responseData = new ResponseDto();
        Response<ResponseDto> response = new Response<>();

        if (accessToken == null || accessToken.isEmpty()) {
            log.error("Token is Required");
            throw new BadDataException("Token is Required");
        }
        String responseString;
        try {
            if (parseJwt(accessToken)) {
                responseData.setVinNumber(vinNumber);
                responseData.setStatus(!vinNumber.isEmpty() ? "Connected" : "DisConnected");
                responseData.setAccessToken(TOKEN);
                response.setStatus(HttpStatus.OK.value());
                response.setData(responseData);
                response.setMessage("Connected Status Retrieved Successfully");
                log.info("Connected Status Retrieved Successfully");
                responseString = objectMapper.writeValueAsString(responseData);
            } else {
                throw new BadDataException(INVALID_TOKEN);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadDataException(e.getMessage());
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<EcuDtcDto>> getEcuFaultCodes(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        String responseString;
        List<EcuDtcDto> ecuDtcValues = VehicleStaticData.getEcuDtcData();
        List<EcuDtcDto> ecuDtcData = ecuDtcValues.stream().filter(ecuDtcDto -> ecuName.contains(ecuDtcDto.getEcuName())).collect(Collectors.toList());
        Response<List<EcuDtcDto>> response = processEcuDtcDataRequest(ecuDtcData, "ECU Diagnostic Trouble Codes Retrieved Successfully", "No DTC found for the requested ECU");
        responseString = serializeToJson(ecuDtcData);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    private Response<List<EcuDtcDto>> processEcuDtcDataRequest(List<EcuDtcDto> ecuDtcData, String okMessage, String noContentMessage) {
        Response<List<EcuDtcDto>> response = new Response<>();
        if (!ecuDtcData.isEmpty()) {
            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuDtcData);
            response.setMessage(okMessage);
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage(noContentMessage);
        }
        return response;
    }

    public String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
    }

    @Override
    public Response<Map<String, Object>> getEcuScanList() {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        VehicleStaticData.setEcuPositionData();
        Response<Map<String, Object>> response = new Response<>();
        String responseString;
        Map<String, Object> ecuScanList = new HashMap<>();
        ecuScanList.put("ecuData", VehicleStaticData.getEcuPositionData());
        ecuScanList.put("imgSrc", VehicleStaticData.getImageData("car.png", 0F, 0F));
        response.setStatus(HttpStatus.OK.value());
        response.setData(ecuScanList);
        response.setMessage("ECU List Scanned Successfully");
        log.info("ECU List Scanned Successfully");
        responseString = serializeToJson(ecuScanList);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<EcuInfoDto>> getEcuInfo(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        VehicleStaticData.setEcuInfoData();
        Response<List<EcuInfoDto>> response = new Response<>();
        String responseString;
        List<EcuInfoDto> ecuInfoValues = VehicleStaticData.getEcuInfoData();
        List<EcuInfoDto> ecuInfoData = ecuInfoValues.stream().filter(ecuInfoDto -> ecuInfoDto.getEcuName().equals(ecuName)).collect(Collectors.toList());
        response.setStatus(HttpStatus.OK.value());
        response.setData(ecuInfoData);
        response.setMessage("ECU Part Information Retrieved Successfully");
        log.info("ECU Part Information Retrieved Successfully");
        responseString = serializeToJson(ecuInfoData);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<Map<String, List<String>>> getEcuActuatorTestList(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<Map<String, List<String>>> response = new Response<>();
        String responseString;
        Map<String, List<String>> ecuActuatorTestList = VehicleStaticData.getEcuActuatorTestList(ecuName);
        response.setStatus(HttpStatus.OK.value());
        response.setData(ecuActuatorTestList);
        response.setMessage("ECU Actuator Test List Retrieved Successfully");
        log.info("ECU Actuator Test List Retrieved Successfully");
        responseString = serializeToJson(ecuActuatorTestList);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<ActuatorTestDto>> getEcuActuatorTestData(ActuatorTestListDto actuatorTestList) {
        String requestString;
        Response<List<ActuatorTestDto>> response = new Response<>();
        String responseString;
        requestString = serializeToJson(actuatorTestList);
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters(), requestString);
        List<ActuatorTestDto> ecuActuatorTestValues = VehicleStaticData.getEcuActuatorTestValues();
        List<ActuatorTestDto> ecuActuatorTestData = ecuActuatorTestValues.stream().filter(ecuActuatorTestDto -> ecuActuatorTestDto.getEcuName().equals(actuatorTestList.getEcuName()) && actuatorTestList.getActuatorTestParam().contains(ecuActuatorTestDto.getActuatorTestParam())).collect(Collectors.toList());
        response.setStatus(HttpStatus.OK.value());
        response.setData(ecuActuatorTestData);
        response.setMessage("ECU Actuator Test Data Retrieved Successfully");
        log.info("ECU Actuator Test Data Retrieved Successfully");
        responseString = serializeToJson(ecuActuatorTestData);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<String>> getAllFilesInDirectory(String accessToken, String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Path directoryPath = Paths.get(ConstantsUtil.DIAGNOSTICS_FLASH_FILES + ecuName);
        List<String> fileNames = new ArrayList<>();
        Response<List<String>> response = new Response<>();
        String responseString = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path entry : stream) {
                fileNames.add(entry.getFileName().toString());
            }
            if (fileNames.isEmpty()) {
                throw new EmptyDirectoryException(NO_FILES_FOUND);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setData(fileNames);
            response.setMessage("List of available file names in the directory retrieved successfully");
            log.info("List of available file names in the directory retrieved successfully");
            responseString = serializeToJson(fileNames);
        } catch (Exception e) {
            handleException(response, e);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<String> getLatestFile(String accessToken, String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        TokenValidationUtil.validateAccessToken(accessToken);
        Response<String> response = new Response<>();
        String responseString = null;
        try {
            String latestFilePath = ConstantsUtil.DIAGNOSTICS_FLASH_FILES + ecuName;
            response.setStatus(HttpStatus.OK.value());
            response.setData(latestUpdatedFile(latestFilePath));
            response.setMessage("Latest created or modified file has been retrieved successfully");
            log.info("Latest created or modified file has been retrieved successfully");
            responseString = serializeToJson(latestUpdatedFile(latestFilePath));
        } catch (Exception e) {
            handleException(response, e);
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    private <T> void handleException(Response<T> response, Exception e) {
        log.error(ERROR + e.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof EmptyDirectoryException) {
            response.setMessage("No files found in the directory");
        } else if (e instanceof IllegalArgumentException) {
            response.setMessage("Invalid directory path");
        } else {
            response.setMessage("An unexpected error occurred");
        }
    }

    private String latestUpdatedFile(String directoryPath) {
        if (!isValidDirectory(directoryPath)) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }
        File[] files = listFilesInDirectory(directoryPath);
        if (files == null || files.length == 0) {
            throw new EmptyDirectoryException(NO_FILES_FOUND);
        }
        File latestFile = findLatestFile(files);
        if (latestFile != null) {
            return latestFile.getName();
        } else {
            log.info(NO_FILES_FOUND);
            return NO_FILES_FOUND;
        }
    }

    public boolean isValidDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.isDirectory();
    }

    public File[] listFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.listFiles();
    }

    private File findLatestFile(File[] files) {
        File latestFile = null;
        long latestTimestamp = Long.MIN_VALUE;
        for (File file : files) {
            long timestamp = file.lastModified();
            if (timestamp > latestTimestamp) {
                latestTimestamp = timestamp;
                latestFile = file;
            }
        }
        return latestFile;
    }

    @Override
    public Response<List<String>> getAllDetectedVciList(String accessToken) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        TokenValidationUtil.validateAccessToken(accessToken);
        Response<List<String>> response = new Response<>();
        String responseString;
        List<String> detectedVciList = VehicleStaticData.getDetectedVciList();

        response.setStatus(HttpStatus.OK.value());
        response.setData(detectedVciList);
        response.setMessage("List of detected VCIs Retrieved Successfully");
        log.info("List of detected VCIs Retrieved Successfully");
        responseString = serializeToJson(detectedVciList);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<VCIDeviceInfoDto> getVciDeviceDetail(String accessToken, String interfaceDeviceName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        TokenValidationUtil.validateAccessToken(accessToken);
        List<VCIDeviceInfoDto> vciDeviceInfoList = VehicleStaticData.getVciDeviceDetail();
        VCIDeviceInfoDto vciDeviceInfo = vciDeviceInfoList.stream().filter(vciDeviceInfoDto -> (vciDeviceInfoDto.getInterfaceDeviceName().equals(interfaceDeviceName))).findAny().orElse(null);
        Response<VCIDeviceInfoDto> response = new Response<>();
        String responseString = null;
        if (vciDeviceInfo != null) {
            response.setStatus(HttpStatus.OK.value());
            response.setData(vciDeviceInfo);
            response.setMessage("VCI device detail retrieved successfully");
            log.info("VCI device detail retrieved successfully");
            responseString = serializeToJson(vciDeviceInfo);
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("Vci device detail not found");
            log.error("Vci device detail not found");
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<String> initializeVci(String accessToken, String vciName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        TokenValidationUtil.validateAccessToken(accessToken);
        List<String> detectedVciList = VehicleStaticData.getDetectedVciList();
        String vciDeviceName = detectedVciList.stream().filter(detectedVciName -> detectedVciName.contains(vciName)).findAny().orElse(null);
        Response<String> response = new Response<>();
        if (vciDeviceName != null) {
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("VCI Initialized - 81");
            log.info("VCI Initialized - 81");
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("VCI not detected");
            log.error("VCI not detected");
        }
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), response.getMessage());
        return response;
    }

    @Override
    public Response<EcuRoutineResponseDto> getEcuRoutineResponse(String accessToken, String ecuName, String ecuRoutine) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        TokenValidationUtil.validateAccessToken(accessToken);
        List<EcuRoutineResponseDto> ecuRoutineResponses = VehicleStaticData.getEcuRoutineResponse();
        EcuRoutineResponseDto ecuRoutineResponse = ecuRoutineResponses.stream().filter(ecuRoutineResponseDto -> (ecuName.equals(ecuRoutineResponseDto.getEcuName()) && ecuRoutine.equals(ecuRoutineResponseDto.getEcuRoutine()))).findAny().orElse(null);
        Response<EcuRoutineResponseDto> response = new Response<>();

        if (ecuRoutineResponse != null) {
            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuRoutineResponse);
            response.setMessage("ECU routine response retrieved successfully");
            log.info("ECU routine response retrieved successfully");

        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("ECU routine response interrupted with error or not found");
            log.error("ECU routine response interrupted with error or not found");
        }
        String responseString = serializeToJson(ecuRoutineResponse);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<EcuPositionDto> getEcuCoordinates(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        Response<EcuPositionDto> response = new Response<>();
        String responseString;
        List<EcuPositionDto> ecuCoordinatesList = VehicleStaticData.getEcuPositionData();
        EcuPositionDto ecuCoordinates = ecuCoordinatesList.stream().filter(ecuPositionDto -> ecuPositionDto.getEcuName().equals(ecuName)).findAny().orElse(null);
        if (ecuCoordinates != null) {
            response.setStatus(HttpStatus.OK.value());
            response.setData(ecuCoordinates);
            response.setMessage("ECU coordinates data retrieved Successfully");
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No data found for the requested ECU");
        }
        responseString = serializeToJson(ecuCoordinates);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }

    @Override
    public Response<List<EcuDtcDto>> clearEcuDTC(String ecuName) {
        serviceLogger.logRequest(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), LogUtil.getRequestParameters());
        String responseString;
        List<EcuDtcDto> ecuDtcValues = VehicleStaticData.getEcuDtcData();
        List<EcuDtcDto> ecuDtcData = ecuDtcValues.stream().filter(ecuDtcDto -> ecuName.contains(ecuDtcDto.getEcuName()) && !ecuDtcDto.getDtcState().equals("Memorised fault")).collect(Collectors.toList());
        Response<List<EcuDtcDto>> response = processEcuDtcDataRequest(ecuDtcData, "Memorised DTCs has been cleared and retrieved active and pending DTCs Successfully", "No memorised DTC found for the requested ECU to clear");
        responseString = serializeToJson(ecuDtcData);
        serviceLogger.logResponse(httpServletRequest.getMethod(), LogUtil.getMethodName(), httpServletRequest.getRequestURI(), responseString);
        return response;
    }
}
