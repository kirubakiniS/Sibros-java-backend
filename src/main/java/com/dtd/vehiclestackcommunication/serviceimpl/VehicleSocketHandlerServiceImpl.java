package com.dtd.vehiclestackcommunication.serviceimpl;

import com.corundumstudio.socketio.SocketIOClient;
import com.dtd.vehiclestackcommunication.dto.EcuDtcDto;
import com.dtd.vehiclestackcommunication.dto.EcuInfoDto;
import com.dtd.vehiclestackcommunication.dto.SocketRequestPayloadDto;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.LogUtil;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleSocketHandlerServiceImpl {
    static final String CLIENT_SECRETS = "DTD1@123*45";
    private static final String USER_NAME = "DTD";
    private static final byte[] JWT_SECRETS = getJwtSecret();
    private static final String JSON_PROCESSING_ERROR = "JSON processing error: ";
    private static final String INVALID_JSON = "Invalid JSON data";
    private static String token = null;
    @Autowired
    ServiceLogger serviceLogger;
    @Autowired
    ObjectMapper objectMapper;

    public static byte[] getJwtSecret() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            SecretKey myKey = keyGenerator.generateKey();
            return myKey.getEncoded();
        } catch (Exception ex) {
            throw new BadDataException(ex.getMessage());
        }
    }

    public static void createJwtSignedHMAC() {

        Key hmacKey = new SecretKeySpec(JWT_SECRETS, SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        token = Jwts.builder().claim("username", USER_NAME).setId(UUID.randomUUID().toString()).setIssuedAt(Date.from(now)).setExpiration(Date.from(now.plus(8, ChronoUnit.HOURS))).signWith(hmacKey).compact();
    }

    private void processSocketResponse(SocketIOClient client, StackResponse stackResponse, String methodName, String logMessage) {
        String responseString = serializeToJson(stackResponse);
        SocketResponse<StackResponse> response = new SocketResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(stackResponse);
        response.setMessage(logMessage);

        log.info(methodName + " successfully");
        client.sendEvent("reply" + methodName.substring(3), response);
        serviceLogger.logResponse(methodName, responseString);
    }

    private String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
    }

    public void getEcuScanList(SocketIOClient client) {
        serviceLogger.logRequest(LogUtil.getMethodName());
        StackResponse stackResponse = new StackResponse();
        Map<String, Object> ecuScanList = new HashMap<>();
        ecuScanList.put("ecuData", VehicleStaticData.getEcuPositionData());
        ecuScanList.put("imgSrc", VehicleStaticData.getImageData("car.png", 0F, 0F));
        stackResponse.setEcuScanList(ecuScanList);
        processSocketResponse(client, stackResponse, "getEcuScanList", "ECU list scanned successfully");
    }

    public void getEcuInfo(SocketIOClient client, SocketRequestPayloadDto data) {
        String requestString = serializeToJson(data);
        serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
        StackResponse stackResponse = new StackResponse();
        VehicleStaticData.setEcuInfoData();
        List<EcuInfoDto> ecuInfoValues = VehicleStaticData.getEcuInfoData();
        List<EcuInfoDto> ecuInfoData = ecuInfoValues.stream().filter(ecuInfoDto -> ecuInfoDto.getEcuName().equals(data.getEcuName())).collect(Collectors.toList());
        stackResponse.setEcuInfoList(ecuInfoData);
        processSocketResponse(client, stackResponse, "getEcuInfo", "ECU part information retrieved successfully");
    }

    public void getEcuFaultCodes(SocketIOClient client, SocketRequestPayloadDto data) {
        String requestString = serializeToJson(data);
        serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
        StackResponse stackResponse = new StackResponse();
        VehicleStaticData.setEcuDtcData();
        List<EcuDtcDto> ecuDtcValues = VehicleStaticData.getEcuDtcData();
        List<EcuDtcDto> ecuDtcData = ecuDtcValues.stream().filter(ecuDtcDto -> data.getEcuName().contains(ecuDtcDto.getEcuName())).collect(Collectors.toList());
        stackResponse.setEcuDtcList(ecuDtcData);
        processSocketResponse(client, stackResponse, "getEcuFaultCodes", "ECU diagnostic trouble codes retrieved successfully");
    }

    public void getAccessToken(SocketIOClient client, SocketRequestPayloadDto data) {
        String requestString = serializeToJson(data);
        serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
        StackResponse stackResponse = new StackResponse();
        if (data.getClientSecret() == null || data.getClientSecret().isEmpty()) {
            log.error("ClientSecret is required");
            throw new BadDataException("ClientSecret is required");
        }
        if (data.getClientSecret().equalsIgnoreCase(CLIENT_SECRETS)) {
            createJwtSignedHMAC();
            stackResponse.setToken(token);
        } else {
            log.error("Invalid ClientSecret");
            throw new BadDataException("Invalid ClientSecret");
        }
        processSocketResponse(client, stackResponse, "getAccessToken", "Token retrieved successfully");

    }

    public void getEcuActuatorTestList(SocketIOClient client, SocketRequestPayloadDto data) {
        String requestString = serializeToJson(data);
        serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
        Map<String, List<String>> ecuActuatorTestList = VehicleStaticData.getEcuActuatorTestList(data.getEcuName());
        StackResponse stackResponse = new StackResponse();
        stackResponse.setEcuActuatorTestList(ecuActuatorTestList);
        processSocketResponse(client, stackResponse, "getEcuActuatorTestList", "ECU Actuator Test List Retrieved Successfully");

    }
}
