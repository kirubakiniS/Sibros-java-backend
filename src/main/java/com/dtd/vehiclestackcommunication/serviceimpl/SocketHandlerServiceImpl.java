package com.dtd.vehiclestackcommunication.serviceimpl;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.dtd.vehiclestackcommunication.service.SocketHandlerService;
import com.dtd.vehiclestackcommunication.service.StackHandlerService;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.LogUtil;
import com.dtd.vehiclestackcommunication.util.SocketErrorResponseHelper;
import com.dtd.vehiclestackcommunication.util.SocketResponseParameters;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.dtd.vehiclestackcommunication.util.SocketResponseParameters.sendResponseLog;
import static com.dtd.vehiclestackcommunication.util.StackConstantsUtil.*;

@Service
@Slf4j
public class SocketHandlerServiceImpl implements SocketHandlerService {
    public static final String DB = "5 db";
    private static final String JSON_PROCESSING_ERROR = "JSON processing error: ";
    private static final String INVALID_JSON = "Invalid JSON data";
    @Autowired
    StackHandlerService stackHandlerService;
    @Autowired
    VehicleSocketHandlerServiceImpl vehicleSocketHandlerService;
    @Autowired
    ServiceLogger serviceLogger;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public SocketHandlerServiceImpl(SocketIOServer server) {
        SocketIONamespace namespace = server.addNamespace("/ecu-socket");
        if (namespace != null) {
            namespace.addConnectListener(onConnected());
            namespace.addDisconnectListener(onDisconnected());
            namespace.addEventListener(READ_AVAILABLE_VCI_LIST, SocketRequestPayloadDto.class, onGetAvailableVciListRequestReceived());
            namespace.addEventListener(CHECK_VCI_CONNECTIVITY_STATUS, SocketRequestPayloadDto.class, onCheckVciConnectivityRequestReceived());
            namespace.addEventListener(VCI_INITIALIZATION, String.class, onVciInitializationRequestReceived());
            namespace.addEventListener(FETCH_VCI_INFO, SocketRequestPayloadDto.class, onFetchVciInfoRequestReceived());
            namespace.addEventListener(GET_VCI_DEVICE_INFO, SocketRequestPayloadDto.class, onGetVciDeviceInfoRequestReceived());
            namespace.addEventListener(INITIATE_ECU_FLASHING, SocketRequestPayloadDto.class, onEcuFlashingRequestReceived());
            namespace.addEventListener(READ_DASHBOARD_PARAMETER, SocketRequestPayloadDto.class, onReadDashboardParameterRequestReceived());
            namespace.addEventListener(CHECK_ECU_CONNECTIVITY_STATUS, SocketRequestPayloadDto.class, onFetchEcuStatusRequestReceived());
            namespace.addEventListener(READ_ECU_DTC, SocketRequestPayloadDto.class, onReadEcuDtcRequestReceived());
            namespace.addEventListener(CLEAR_ECU_DTC, SocketRequestPayloadDto.class, onClearEcuDtcRequestReceived());
            namespace.addEventListener(FETCH_ECU_PARAMETERS, SocketRequestPayloadDto.class, onFetchEcuParametersReceived());
            namespace.addEventListener(READ_ECU_PARAMETER_VALUE, SocketRequestPayloadDto.class, onReadEcuParameterValueReceived());
            namespace.addEventListener(FETCH_ECU_STATUS_PARAMS, SocketRequestPayloadDto.class, onFetchEcuStatusParamsReceived());
            namespace.addEventListener(READ_ECU_STATUS_PARAM_VALUE, SocketRequestPayloadDto.class, onReadEcuStatusParamValueReceived());
            namespace.addEventListener(READ_ECU_ACTUATOR_PARAMETERS, SocketRequestPayloadDto.class, doFetchEcuActuatorParamsReceived());
            namespace.addEventListener(FETCH_ECU_ACTUATOR_PARAM_OPTIONS, SocketRequestPayloadDto.class, doFetchEcuActuatorParamOptionsReceived());
            namespace.addEventListener(FETCH_ECU_ACTUATOR_PARAM_RESPONSE, SocketRequestPayloadDto.class, doFetchEcuActuatorParamResponseReceived());
            namespace.addEventListener(READ_ECU_ROUTINE_RESPONSE, SocketRequestPayloadDto.class, onReadEcuRoutineResponseReceived());
            namespace.addEventListener(GET_ECU_LIST, SocketRequestPayloadDto.class, onGetEcuList());
            namespace.addEventListener(GET_ECU_STATUS, SocketRequestPayloadDto.class, onGetEcuStatus());
            namespace.addEventListener(GET_ECU_PARAMETERS, SocketRequestPayloadDto.class, onGetEcuParameters());
            namespace.addEventListener(GET_ECU_PARAMETERS_VALUES, SocketRequestPayloadDto.class, onGetEcuParameterValues());
            namespace.addEventListener(GET_ECU_STATUS_VALUE, SocketRequestPayloadDto.class, onGetEcuStatusValues());
            namespace.addEventListener(WRITE_ECU_STATUS_VALUES, SocketRequestPayloadDto.class, onWriteEcuStatusValues());
            namespace.addEventListener(GET_ECU_STATUS_VALUES_LEVEL, SocketRequestPayloadDto.class, onGetEcuStatusValuesLevel());
            namespace.addEventListener(
                    GET_PARAMETER_LIST_VALUE,
                    String.class,
                    (client, data, ackSender) -> doReadParameterList(client, data)
            );
            namespace.addEventListener(
                    GET_IO_PARAMETER_LIST_VALUE,
                    SocketRequestPayloadDto.class,
                    (client, data, ackSender) -> doIOReadParameterList(client, data)
            );
            namespace.addEventListener(
                    GET_READ_DATA_BY_IDENTIFIER,
                    SocketRequestPayloadDto.class,
                    (client, data, ackSender) -> doReadDataByIdentifier(client, data)
            );

//            Vehicle service
            namespace.addEventListener(GET_ECU_SCAN_LIST, SocketRequestPayloadDto.class, onGetEcuScanList());
            namespace.addEventListener(GET_ECU_INFO, SocketRequestPayloadDto.class, onGetEcuInfo());
            namespace.addEventListener(GET_ECU_FAULT_CODES, SocketRequestPayloadDto.class, onGetEcuFaultCodes());
            namespace.addEventListener(GET_ACCESS_TOKEN, SocketRequestPayloadDto.class, onGetAccessToken());
            namespace.addEventListener(GET_ECU_ACTUATOR_TEST_LIST, SocketRequestPayloadDto.class, onGetEcuActuatorTestList());
            namespace.addEventListener(GET_ECU_SERIAL_NUMBER,SocketRequestPayloadDto.class,onGetEcuSerialNumber());
            namespace.addEventListener(GET_ECU_VERSION_NUMBER,SocketRequestPayloadDto.class,onGetEcuVersionNumber());
            namespace.addEventListener(GET_ECU_VIN_NUMBER,SocketRequestPayloadDto.class,onGetEcuVinNumber());
            namespace.addEventListener(VCI_UNINITIALIZATION,SocketRequestPayloadDto.class,onGetEcuUNInitialization());
            namespace.addEventListener(READ_ECU_OFFLINE_DTC,SocketRequestPayloadDto.class,onReadEcuOfflineDtcList());
            namespace.addEventListener(READ_ECU_OFFLINE_FLASH,SocketRequestPayloadDto.class,onReadEcuOfflineFlash());

            //CGW ECU Api
            namespace.addEventListener(READ_CGW_ECU_SERIAL_NUMBER,SocketRequestPayloadDto.class,onGetCgwEcuSerialNumber());
            namespace.addEventListener(READ_CGW_SHOP_REPAIR_CODE,SocketRequestPayloadDto.class,onGetCgwShopRepairCode());
            namespace.addEventListener(READ_CGW_KGM_PART_NUMBER,SocketRequestPayloadDto.class,onGetCgwKgmPartNumber());
            namespace.addEventListener(READ_CGW_SOFTWARE_VERSION,SocketRequestPayloadDto.class,onGetCgwKgmSoftwareVersion());
            namespace.addEventListener(READ_CGW_SUPPLIER_CODE,SocketRequestPayloadDto.class,onGetCgwKgmSupplierCode());
            namespace.addEventListener(READ_CGW_MANUFACTURING_DATE,SocketRequestPayloadDto.class,onGetCgwKgmManufacturingDate());
            namespace.addEventListener(READ_CGW_PROGRAMMING_DATE,SocketRequestPayloadDto.class,onGetCgwKgmProgrammingDate());
            namespace.addEventListener(READ_CGW_DTC_LIST,SocketRequestPayloadDto.class,onGetCgwKgmDtcList());
            namespace.addEventListener(READ_ECU_SERVICE, SocketRequestPayloadDto.class,onGetEcuResetService());
            namespace.addEventListener(READ_ECU_SERVICE_VCI, SocketRequestPayloadDto.class,OnGetVCIIntilization());

        }
    }

    public ConnectListener onConnected() {
        return this::doOnConnected;
    }

    public DisconnectListener onDisconnected() {
        return this::doOnDisconnected;
    }

    public void doOnConnected(SocketIOClient client) {
        UUID sessionId = client.getSessionId();
        HandshakeData handshakeData = client.getHandshakeData();
        log.info("Client[" + sessionId + "] - Connected to socket through " + handshakeData.getUrl());
    }

    public void doOnDisconnected(SocketIOClient client) {
        UUID sessionId = client.getSessionId();
        log.info("Client[" + sessionId + "] - Disconnected from ecu socket.");
    }

    public DataListener<SocketRequestPayloadDto> onGetAvailableVciListRequestReceived() {
        return (client, data, ackSender) -> doOnGetAvailableVciListRequestReceived(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onCheckVciConnectivityRequestReceived() {
        return (client, data, ackSender) -> doOnCheckVciConnectivityRequestReceived(client);
    }

    public DataListener<String> onVciInitializationRequestReceived() {
        return (client, data, ackSender) -> doOnVciInitializationRequestReceived(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onFetchVciInfoRequestReceived() {
        return (client, data, ackSender) -> doFetchVciInfo(client);
    }

    public DataListener<SocketRequestPayloadDto> onGetVciDeviceInfoRequestReceived() {
        return (client, data, ackSender) -> doOnGetVciDeviceInfo(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onEcuFlashingRequestReceived() {
        return (client, data, ackSender) -> doEcuFlashing(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadDashboardParameterRequestReceived() {
        return (client, data, ackSender) -> doReadDashboardParameters(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onFetchEcuStatusRequestReceived() {
        return (client, data, ackSender) -> doFetchEcuStatus(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuDtcRequestReceived() {
        return (client, data, ackSender) -> doReadEcuDtc(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onClearEcuDtcRequestReceived() {
        return (client, data, ackSender) -> doClearEcuDtc(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onFetchEcuParametersReceived() {
        return (client, data, ackSender) -> doFetchEcuParameters(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuParameterValueReceived() {
        return (client, data, ackSender) -> doReadEcuParameterValue(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onFetchEcuStatusParamsReceived() {
        return (client, data, ackSender) -> doFetchEcuStatusParams(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuStatusParamValueReceived() {
        return (client, data, ackSender) -> doReadEcuStatusParamValue(client, data);
    }

    public DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamsReceived() {
        return (client, data, ackSender) -> doFetchEcuActuatorParams(client, data);
    }

    public DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamOptionsReceived() {
        return (client, data, ackSender) -> doFetchEcuActuatorParamOptions(client, data);
    }

    public DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamResponseReceived() {
        return (client, data, ackSender) -> doReadEcuActuatorParamResponse(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuRoutineResponseReceived() {
        return (client, data, ackSender) -> doReadEcuRoutineResponse(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuList() {
        return (client, data, ackSender) -> getEcuList(client);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuParameters() {
        return (client, data, ackSender) -> getEcuParameters(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuStatus() {
        return (client, data, ackSender) -> getEcuStatus(client, data);
    }

    public DataListener<String> onReadParameterList() {
        return (client, data, ackSender) -> doReadParameterList(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadIOParameterList() {
        return (client, data, ackSender) -> doIOReadParameterList(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadDataByIdentifier() {
        return (client, data, ackSender) -> doReadDataByIdentifier(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuParameterValues() {
        return (client, data, ackSender) -> getEcuParameterValues(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuStatusValues() {
        return (client, data, ackSender) -> getEcuStatusValues(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onWriteEcuStatusValues() {
        return (client, data, ackSender) -> writeEcuStatusValues(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuStatusValuesLevel() {
        return (client, data, ackSender) -> getEcuStatusValuesLevel(client, data);
    }

    //    vehicle service
    public DataListener<SocketRequestPayloadDto> onGetEcuScanList() {
        return (client, data, ackSender) -> vehicleSocketHandlerService.getEcuScanList(client);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuInfo() {
        return (client, data, ackSender) -> vehicleSocketHandlerService.getEcuInfo(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuFaultCodes() {
        return (client, data, ackSender) -> vehicleSocketHandlerService.getEcuFaultCodes(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetAccessToken() {
        return (client, data, ackSender) -> vehicleSocketHandlerService.getAccessToken(client, data);

    }

    public DataListener<SocketRequestPayloadDto> onGetEcuActuatorTestList() {
        return (client, data, ackSender) -> vehicleSocketHandlerService.getEcuActuatorTestList(client, data);

    }


    public DataListener<SocketRequestPayloadDto> onGetEcuSerialNumber() {
        return (client, data, ackSender) -> doOnGetEcuSerialNumber(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuVersionNumber() {
        return (client, data, ackSender) -> doOnGetEcuVersionNumber(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuVinNumber() {
        return (client, data, ackSender) -> doOnGetEcuVinNumber(client, data);
    }


    public DataListener<SocketRequestPayloadDto> onGetEcuUNInitialization() {
        return (client, data, ackSender) -> doOnGetEcuUNInitialization(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuOfflineDtcList() {
        return (client, data, ackSender) -> doOnReadEcuOfflineDtcList(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onReadEcuOfflineFlash() {
        return (client, data, ackSender) -> doOnReadEcuOfflineFlash(client, data);
    }
    //CGW Ecu Details
    public DataListener<SocketRequestPayloadDto> onGetCgwEcuSerialNumber() {
        return (client, data, ackSender) -> doOnReadCgwEcuSerialNumber(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwShopRepairCode() {
        return (client, data, ackSender) -> doOnReadCgwShopRepairCode(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmPartNumber() {
        return (client, data, ackSender) -> doOnReadCgwKgmPartNumber(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmSoftwareVersion() {
        return (client, data, ackSender) -> doOnReadCgwKgmSoftwareVersion(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmSupplierCode() {
        return (client, data, ackSender) -> doOnReadCgwKgmSupplierCode(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmManufacturingDate() {
        return (client, data, ackSender) -> doOnReadCgwKgmManufacturingDate(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmProgrammingDate() {
        return (client, data, ackSender) -> doOnReadCgwKgmProgrammingDate(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetCgwKgmDtcList() {
        return (client, data, ackSender) -> doOnReadCgwKgmDtcList(client, data);
    }

    public DataListener<SocketRequestPayloadDto> onGetEcuResetService() {
        return (client, data, ackSender) -> doOnReadecuResetService(client, data);
    }

     public DataListener<SocketRequestPayloadDto> OnGetVCIIntilization() {
            return (client, data, ackSender) -> doOnVCIIntilization(client, data);
        }



    public void doOnGetAvailableVciListRequestReceived(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.getAvailableVciList(data.getDllCallMethod());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - List of available VCIs : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyAvailableVciList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnCheckVciConnectivityRequestReceived(SocketIOClient client) {
        serviceLogger.logRequest(LogUtil.getMethodName());
        String responseString = null;
        try {
            SocketResponse<String> response = stackHandlerService.checkVciConnectivityStatus();
            log.info("Sending message - Connectivity status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciConnectivityStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnVciInitializationRequestReceived(SocketIOClient client, String data) {
        String responseString = null;
        try {
            SocketRequestPayloadDto requestDto = objectMapper.readValue(data, SocketRequestPayloadDto.class);

            String requestString = objectMapper.writeValueAsString(requestDto);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            
            SocketResponse<StackResponse> response = stackHandlerService.vciInitialization(requestDto.getDllCallMethod(), requestDto.getVciName());
            
            SocketResponseParameters.setResponseParameters(response, requestDto);
            
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInitializationStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchVciInfo(SocketIOClient client) {
        serviceLogger.logRequest(LogUtil.getMethodName());
        String responseString = null;
        try {
            SocketResponse<VCIDeviceInfoDto> response = stackHandlerService.getVciInformation();
            log.info("VCI info " + response.getStatus() + " " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInfo", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnGetVciDeviceInfo(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.getVciDeviceInformation(data.getDllCallMethod(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("VCI device information " + response.getStatus() + " " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciDeviceInfo", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doEcuFlashing(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuFlashing(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("ECU flashing status " + response.getStatus() + " " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuFlashing", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadDashboardParameters(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readDashboardParameters(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read dashboard parameters", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyDashboardParameters", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchEcuStatus(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.fetchEcuConnectivityStatus(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Fetch ECU connectivity status", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuConnectivityStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadEcuDtc(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readEcuDTC(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU DTC list", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuDtcList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doClearEcuDtc(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.clearEcuDTC(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Clear ECU DTC list", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyClearEcuDtcList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchEcuParameters(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.fetchEcuParameters(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Fetch ECU parameter list", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuParameterList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadEcuParameterValue(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readEcuParameterValue(data.getDllCallMethod(), data.getEcuParameter());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU parameter Value", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuParameterValue", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    // SocketHandlerServiceImpl (or wherever you register listeners)



    @Autowired
    private ObjectMapper mapper;
    public void doReadParameterList(SocketIOClient client, String rawData) {

        String responseString = null;

        try {
        	SocketRequestPayloadDto data =
                    mapper.readValue(rawData, SocketRequestPayloadDto.class);

            //SocketRequestPayloadDto data = parsePayload(rawData);
            String requestString = mapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);

            SocketResponse<StackResponse> response =
                    stackHandlerService.readParameterListValue(
                    		data.getVciName(),
                    		data.getEcuName()
                    );
            
            SocketResponseParameters.setResponseParameters(response, data);

            sendResponseLog(
                    "Read ECU parameter List Value",
                    response.getStatus(),
                    response.getMessage()
            );

            responseString = mapper.writeValueAsString(response);

            client.sendEvent("replyParameterListValue", response);

        } catch (Exception e) {

            log.error("Error processing readParameterList", e);

            SocketResponse<String> error = new SocketResponse<>();
            error.setError(true);
            error.setStatus(400);
            error.setMessage("Invalid request payload.");

            client.sendEvent("replyParameterListValue", error);

            try {
                responseString = mapper.writeValueAsString(error);
            } catch (Exception ignore) {
            }
        }

        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }
    
    private SocketRequestPayloadDto parsePayload(Object rawData) throws Exception {

        if (rawData == null) {
            throw new IllegalArgumentException("Payload is null");
        }

        if (rawData instanceof String) {

            String cleaned = ((String) rawData)
                    .replace("\uFEFF", "")
                    .trim();

            return mapper.readValue(cleaned, SocketRequestPayloadDto.class);
        }

        return mapper.convertValue(rawData, SocketRequestPayloadDto.class);
    }

    public void doIOReadParameterList(SocketIOClient client, Object rawData) {

        String responseString = null;

        try {

            SocketRequestPayloadDto data = parsePayload(rawData);

            String requestString = mapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);

            SocketResponse<StackResponse> response =
                    stackHandlerService.readIOParameterListValue(
                            data.getVciName(),
                            data.getEcuName()
                    );

            SocketResponseParameters.setResponseParameters(response, data);

            sendResponseLog(
                    "Read IO ECU parameter List Value",
                    response.getStatus(),
                    response.getMessage()
            );

            responseString = mapper.writeValueAsString(response);

            client.sendEvent("replyIOParameterListValue", response);

        } catch (Exception e) {

            log.error("Error processing readIOParameterList", e);

            SocketResponse<String> error = new SocketResponse<>();
            error.setError(true);
            error.setStatus(400);
            error.setMessage("Invalid request payload.");

            client.sendEvent("replyIOParameterListValue", error);

            try {
                responseString = mapper.writeValueAsString(error);
            } catch (Exception ignore) {}
        }

        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadDataByIdentifier(SocketIOClient client, Object rawData) {

        String responseString = null;

        try {
            SocketRequestPayloadDto data = parsePayload(rawData);

            // validate
            if (data.getVciName() == null || data.getEcuName() == null || data.getDidName() == null) {
                throw new IllegalArgumentException("vciName, ecuName and didName are required.");
            }

            String requestString = mapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);

            SocketResponse<StackResponse> response =
                    stackHandlerService.readDataByIdentifier(
                            data.getVciName(),
                            data.getEcuName(),
                            data.getDidName()
                    );

            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read Data By Identifier", response.getStatus(), response.getMessage());

            responseString = mapper.writeValueAsString(response);
            client.sendEvent("replyReadDataByIdentifier", response);

        } catch (Exception e) {
            log.error("Error processing readDataByIdentifier", e);

            SocketResponse<String> err = new SocketResponse<>();
            err.setStatus(400);
            err.setError(true);
            err.setMessage("Invalid request payload.");

            client.sendEvent("replyReadDataByIdentifier", err);

            try { responseString = mapper.writeValueAsString(err); } catch (Exception ignore) {}
        }

        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchEcuStatusParams(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.fetchEcuStatusParams(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Fetch ECU Status Parameters list", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyParameterList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadEcuStatusParamValue(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readEcuStatusParamValue(data.getDllCallMethod(), data.getEcuStatusParam());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU status parameter Value", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuStatusParamValue", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchEcuActuatorParams(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.fetchEcuActuatorParams(data.getDllCallMethod(), data.getEcuName());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Fetch ECU actuator parameter list", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuActuatorParams", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doFetchEcuActuatorParamOptions(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.fetchEcuActuatorParamOptions(data.getDllCallMethod(), data.getEcuActuatorParameter());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU actuator parameter Options", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuActuatorParamOptions", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadEcuActuatorParamResponse(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readEcuActuatorParamResponse(data.getDllCallMethod(), data.getEcuActuatorParameter(), data.getEcuActuatorState());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU actuator parameter Response", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuActuatorParamResponse", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doReadEcuRoutineResponse(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.readEcuRoutineResponse(data.getDllCallMethod(), data.getEcuName(), data.getEcuRoutine());
            SocketResponseParameters.setResponseParameters(response, data);
            sendResponseLog("Read ECU Routine Response", response.getStatus(), response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuRoutineResponse", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuList(SocketIOClient client) {
        serviceLogger.logRequest(LogUtil.getMethodName());
        String responseString = null;
        try {
            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();
            response.setError(false);
            response.setStatus(HttpStatus.OK.value());

            List<String> ecuList = VehicleStaticData.getEcuList();

            stackResponse.setEcuList(ecuList);
            response.setData(stackResponse);
            response.setMessage("ECU list retrieved successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuParameters(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = new SocketResponse<>();
            response.setError(false);
            response.setStatus(HttpStatus.OK.value());
            StackResponse stackResponse = new StackResponse();

            VehicleStaticData.setEcuParametersMap();
            Map<String, List<String>> ecuParameterList = new HashMap<>();
            List<String> ecuList = VehicleStaticData.getEcuList();

            if (Objects.equals(data.getEcuName(), ecuList.get(0))) {
                List<String> engineParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(0));
                ecuParameterList.put(data.getEcuName(), engineParameters);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(1))) {
                List<String> bcmParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(1));
                ecuParameterList.put(data.getEcuName(), bcmParameters);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(2))) {
                List<String> clusterParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(2));
                ecuParameterList.put(data.getEcuName(), clusterParameters);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(3))) {
                List<String> absParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(3));
                ecuParameterList.put(data.getEcuName(), absParameters);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(4))) {
                List<String> acuParameters = VehicleStaticData.getEcuParametersMap().get(ecuList.get(4));
                ecuParameterList.put(data.getEcuName(), acuParameters);
            }
            stackResponse.setEcuParameters(ecuParameterList);
            response.setData(stackResponse);
            response.setMessage("ECU parameter list retrieved successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuParameters", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuStatus(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();

            VehicleStaticData.setEcuStatusMap();
            Map<String, List<String>> ecuStatusList = new HashMap<>();
            List<String> ecuList = VehicleStaticData.getEcuList();

            if (Objects.equals(data.getEcuName(), ecuList.get(0))) {
                List<String> engineStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(0));
                ecuStatusList.put(data.getEcuName(), engineStatus);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(1))) {
                List<String> bcmStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(1));
                ecuStatusList.put(data.getEcuName(), bcmStatus);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(2))) {
                List<String> clusterStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(2));
                ecuStatusList.put(data.getEcuName(), clusterStatus);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(3))) {
                List<String> absStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(3));
                ecuStatusList.put(data.getEcuName(), absStatus);
            }

            if (Objects.equals(data.getEcuName(), ecuList.get(4))) {
                List<String> acuStatus = VehicleStaticData.getEcuStatusMap().get(ecuList.get(4));
                ecuStatusList.put(data.getEcuName(), acuStatus);
            }
            stackResponse.setEcuStatusParams(ecuStatusList);
            response.setStatus(HttpStatus.OK.value());
            response.setData(stackResponse);
            response.setMessage("ECU Status List Retrieved Successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuParameterValues(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            List<EcuParameterDto> ecuParameterValues = VehicleStaticData.getParameterData();
            List<EcuParameterDto> ecuParameterData = ecuParameterValues.stream().filter(ecuParameterDto -> (data.getEcuParameters().containsKey(ecuParameterDto.getEcuName()) && data.getEcuParameters().get(ecuParameterDto.getEcuName()).contains(ecuParameterDto.getEcuParameter()))).collect(Collectors.toList());

            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();
            stackResponse.setEcuParameterValues(ecuParameterData);
            response.setStatus(HttpStatus.OK.value());
            response.setData(stackResponse);
            response.setMessage("ECU parameter values retrieved successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuParameterValues", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuStatusValues(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            List<EcuStatusParamDto> ecuStatusValues = VehicleStaticData.getStatusData();
            List<EcuStatusParamDto> ecuStatusData = ecuStatusValues.stream().filter(ecuStatusDto -> (data.getEcuStatuses().containsKey(ecuStatusDto.getEcuName()) && data.getEcuStatuses().get(ecuStatusDto.getEcuName()).contains(ecuStatusDto.getEcuStatusParam()))).collect(Collectors.toList());

            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();
            response.setStatus(HttpStatus.OK.value());
            stackResponse.setEcuStatusParamValues(ecuStatusData);
            response.setData(stackResponse);
            response.setMessage("ECU status values retrieved successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuStatusValues", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void writeEcuStatusValues(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            List<WriteEcuStatusDto> ecuStatusList = VehicleStaticData.getWriteStatusData();
            List<WriteEcuStatusDto> ecuParameterData = ecuStatusList.stream().filter(ecuStatusDto -> (data.getEcuName().equals(ecuStatusDto.getEcuName()) && data.getEcuStatus().equals(ecuStatusDto.getEcuStatus()))).collect(Collectors.toList());

            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();
            stackResponse.setWriteEcuStatus(ecuParameterData);
            response.setStatus(HttpStatus.OK.value());
            response.setData(stackResponse);
            response.setMessage("ECU status values written successfully");
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyWriteEcuStatusValues", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void getEcuStatusValuesLevel(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            LinkedHashMap<String, Object> ecuStatusValue = new LinkedHashMap<>();
            SocketResponse<StackResponse> response = new SocketResponse<>();
            StackResponse stackResponse = new StackResponse();

            if (data.getEcuStatusActionList() != null && !data.getEcuStatusActionList().isEmpty() && data.getEcuStatusActionOptions() != null && !data.getEcuStatusActionOptions().isEmpty()) {
                ecuStatusValue.put("ecuName", data.getEcuName());
                ecuStatusValue.put("ecuStatus", data.getEcuStatus());
                ecuStatusValue.put("ecuStatusAction", data.getEcuStatusActionList());
                ecuStatusValue.put("ecuStatusActionOption", data.getEcuStatusActionOptions());
                stackResponse.setGetEcuStatusValuesLevel(ecuStatusValue);
                response.setStatus(HttpStatus.OK.value());
                response.setData(stackResponse);
                response.setMessage("Get Ecu Status Values Level Successfully");
                responseString = objectMapper.writeValueAsString(response);
                client.sendEvent("replyGetEcuStatusValuesLevel", response);
            } else {
                ecuStatusValue.put("ecuName", data.getEcuName());
                ecuStatusValue.put("ecuStatus", data.getEcuStatus());
                ecuStatusValue.put("ecuStatusAction", MeasurementServiceImpl.LEVEL_1);
                ecuStatusValue.put("ecuStatusActionOption", DB);
                stackResponse.setGetEcuStatusValuesLevel(ecuStatusValue);
                response.setStatus(HttpStatus.OK.value());
                response.setData(stackResponse);
                response.setMessage("Get ecu status values level successfully");
                responseString = objectMapper.writeValueAsString(response);
                client.sendEvent("replyGetEcuStatusValuesLevel", response);
            }
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnGetEcuSerialNumber(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuSerialNumber(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInitializationStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnGetEcuVersionNumber(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuVersionNumber(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInitializationStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }
    public void doOnGetEcuVinNumber(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuVinNumber(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInitializationStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnGetEcuUNInitialization(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.vciUNInitialization(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyVciInitializationStatus", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }
    public void doOnReadEcuOfflineDtcList(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuOfflineDtcList(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyreadEcuOfflineDtcList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


    public void doOnReadEcuOfflineFlash(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuOfflineFlash(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyreadEcuOfflineDtcList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


    //CGW Details API


    public void doOnReadCgwEcuSerialNumber(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwecuSerialNumber(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwEcuSerialNumbe", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnReadCgwShopRepairCode(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwShopRepairCode(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwShopRepairCode", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


    public void doOnReadCgwKgmPartNumber(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmPartNumber(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmPartNumber", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnReadCgwKgmSoftwareVersion(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmSoftwareVersion(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmSoftwareVersion", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnReadCgwKgmSupplierCode(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmSupplierCode(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmSupplierCode", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


    public void doOnReadCgwKgmManufacturingDate(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmManufacturingDate(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmManufacturingDate", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


    public void doOnReadCgwKgmProgrammingDate(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmProgrammingDate(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmProgrammingDate", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }



    public void doOnReadCgwKgmDtcList(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.cgwKgmDtcList(data.getDllCallMethod(),  data.getEcuName(), data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyCgwKgmDtcList", response);
        } catch (JsonProcessingException e) {
            log.error(JSON_PROCESSING_ERROR + e.getMessage(), e);
            throw new BadDataException(INVALID_JSON);
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnReadecuResetService(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuResetService(data.getDllCallMethod(),  data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyEcuResetService", response);

            log.info("Sending message - ECU Reset status: " + response.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JSON processing error: " + e.getMessage(), e);
            throw new BadDataException("Invalid JSON");
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnVCIIntilization(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuVCIIntilization(data.getDllCallMethod(),  data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyECUVCIService", response);

            log.info("Sending message - ECU Reset status: " + response.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JSON processing error: " + e.getMessage(), e);
            throw new BadDataException("Invalid JSON");
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnDTCRead(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuDTCRead(data.getDllCallMethod(),  data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyECUVCIService", response);

            log.info("Sending message - ECU Reset status: " + response.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JSON processing error: " + e.getMessage(), e);
            throw new BadDataException("Invalid JSON");
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }

    public void doOnFlashingJob(SocketIOClient client, SocketRequestPayloadDto data) {
        String responseString = null;
        try {
            String requestString = objectMapper.writeValueAsString(data);
            serviceLogger.logRequest(LogUtil.getMethodName(), requestString);
            SocketResponse<StackResponse> response = stackHandlerService.ecuDTCRead(data.getDllCallMethod(),  data.getVciName());
            SocketResponseParameters.setResponseParameters(response, data);
            log.info("Sending message - VCI initialization status : " + response.getMessage());
            responseString = objectMapper.writeValueAsString(response);
            client.sendEvent("replyECUVCIService", response);

            log.info("Sending message - ECU Reset status: " + response.getMessage());
        } catch (JsonProcessingException e) {
            log.error("JSON processing error: " + e.getMessage(), e);
            throw new BadDataException("Invalid JSON");
        }
        serviceLogger.logResponse(LogUtil.getMethodName(), responseString);
    }


}