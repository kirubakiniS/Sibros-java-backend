package com.dtd.vehiclestackcommunication;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.SocketResponse;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.dtd.vehiclestackcommunication.service.StackHandlerService;
import com.dtd.vehiclestackcommunication.serviceimpl.SocketHandlerServiceImpl;
import com.dtd.vehiclestackcommunication.serviceimpl.VehicleSocketHandlerServiceImpl;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.SocketResponseParameters;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SocketHandlerServiceTest {
    @Mock
    SocketIOClient client;
    @Mock
    StackHandlerService stackHandlerService;
    @Mock
    VehicleSocketHandlerServiceImpl vehicleSocketHandlerService;
    @Mock
    ServiceLogger serviceLogger;
    @Mock
    private SocketIOServer socketIOServerMock;
    @Mock
    private HandshakeData handshakeDataMock;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private StackHandlerService stackHandlerServiceMock;

    @InjectMocks
    private SocketHandlerServiceImpl socketHandlerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenExecute_OnConnected_returnSuccess() {
        UUID sessionId = UUID.randomUUID();
        when(client.getSessionId()).thenReturn(sessionId);
        when(client.getHandshakeData()).thenReturn(handshakeDataMock);
        when(handshakeDataMock.getUrl()).thenReturn("sampleUrl");
        socketHandlerService.doOnConnected(client);
        verify(client, times(1)).getSessionId();
    }

    @Test
    void whenExecute_testOnDisconnected_returnSuccess() {
        UUID sessionId = UUID.randomUUID();
        when(client.getSessionId()).thenReturn(sessionId);

        socketHandlerService.doOnDisconnected(client);
        verify(client, times(1)).getSessionId();
        // Add assertions or verify specific behavior as needed
    }

    @Test
    void whenExecute_onGetEcuList_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuList();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_onGetEcuParameters_returnSuccess() {
        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuParameters();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_doOnGetAvailableVciListRequestReceived_returnSuccess() throws JsonProcessingException {

        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        when(objectMapper.writeValueAsString(any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.getAvailableVciList(data.getDllCallMethod())).thenReturn(response);
        when(objectMapper.writeValueAsString(any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doOnGetAvailableVciListRequestReceived(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doOnGetAvailableVciListRequestReceived_returnJsonProcessingException() throws JsonProcessingException {

        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doOnGetAvailableVciListRequestReceived(client, data));
    }

    @Test
    void whenExecute_doOnCheckConnectivityRequestReceived_returnSuccess() throws JsonProcessingException {

        // Mock
        SocketResponse<String> response = new SocketResponse<>();
        when(stackHandlerService.checkVciConnectivityStatus()).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.doOnCheckVciConnectivityRequestReceived(client);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doOnCheckConnectivityRequestReceived_returnJsonProcessingException() throws JsonProcessingException {
        SocketResponse<String> response = new SocketResponse<>();
        when(stackHandlerService.checkVciConnectivityStatus()).thenReturn(response);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketResponse.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doOnCheckVciConnectivityRequestReceived(client));
    }

    @Test
    void whenExecute_doOnVciInitializationRequestReceived_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        when(objectMapper.writeValueAsString(any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.vciInitialization(data.getDllCallMethod(), data.getVciName())).thenReturn(response);
        when(objectMapper.writeValueAsString(any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doOnVciInitializationRequestReceived(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());


    }

    @Test
    void whenExecute_doOnVciInitializationRequestReceived_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doOnVciInitializationRequestReceived(client, data));
    }

    @Test
    void whenExecute_doFetchVciInfo_returnSuccess() throws JsonProcessingException {
        //setup
        SocketResponse<VCIDeviceInfoDto> response = new SocketResponse<>();
        when(stackHandlerService.getVciInformation()).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doFetchVciInfo(client);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doFetchVciInfo_returnJsonProcessingException() throws JsonProcessingException {
        SocketResponse<VCIDeviceInfoDto> response = new SocketResponse<>();
        when(stackHandlerService.getVciInformation()).thenReturn(response);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketResponse.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchVciInfo(client));
    }

    @Test
    void whenExecute_doOnGetVciDeviceInfo_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.getVciDeviceInformation(data.getDllCallMethod(), data.getVciName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doOnGetVciDeviceInfo(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doOnGetVciDeviceInfo_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doOnGetVciDeviceInfo(client, data));
    }

    @Test
    void whenExecute_doEcuFlashing_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.ecuFlashing(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doEcuFlashing(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doEcuFlashing_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doEcuFlashing(client, data));

    }

    @Test
    void whenExecute_doReadDashboardParameters_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readDashboardParameters(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadDashboardParameters(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doReadDashboardParameters_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadDashboardParameters(client, data));

    }

    @Test
    void whenExecute_doFetchEcuStatus_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.fetchEcuConnectivityStatus(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doFetchEcuStatus(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doFetchEcuStatus_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchEcuStatus(client, data));
    }

    @Test
    void whenExecute_doReadEcuDtc_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readEcuDTC(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadEcuDtc(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doReadEcuDtc_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadEcuDtc(client, data));
    }

    @Test
    void whenExecute_doClearEcuDtc_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.clearEcuDTC(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doClearEcuDtc(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doClearEcuDtc_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doClearEcuDtc(client, data));
    }

    @Test
    void whenExecute_fetchEcuParameters_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.fetchEcuParameters(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        MockedStatic<SocketResponseParameters> vsd = Mockito.mockStatic(SocketResponseParameters.class);
        vsd.when(() -> SocketResponseParameters.sendResponseLog(any(), any(), any())).then(invocationOnMock -> null);
        vsd.when(() -> SocketResponseParameters.sendResponseLog(any(), any(), any())).then(invocationOnMock -> null);

        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedRequestString");

        doNothing().when(client).sendEvent(anyString(), any());

        // Execution
        socketHandlerService.doFetchEcuParameters(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_fetchEcuParameters_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchEcuParameters(client, data));

    }

    @Test
    void whenExecute_onCheckConnectivityRequestReceived_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onCheckVciConnectivityRequestReceived();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_onGetEcuStatus_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuStatus();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_fetchEcuStatus_returnSuccess() throws JsonProcessingException {

        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedRequestString");
        // Set up your ecuList and ECU status map
        List<String> ecuNameList = Arrays.asList("ENGINE", "BCM", "CLUSTER", "ABS", "ACU"); // Example list of ECUs
        Map<String, List<String>> ecuStatusMap = new HashMap<>();
        List<String> engineStatus = Arrays.asList("Status1", "Status2", "Status3"); // Example engine status
        ecuStatusMap.put(ecuNameList.get(0), engineStatus);
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuList).thenReturn(ecuNameList);
        vsd.when(VehicleStaticData::getEcuStatusMap).thenReturn(ecuStatusMap);
        doNothing().when(client).sendEvent(anyString(), any());

        for (String ecu : ecuNameList) {
            // Mock the SocketRequestPayloadDto with the current EcuName
            SocketRequestPayloadDto data = new SocketRequestPayloadDto();
            data.setEcuName(ecu);
            ecuStatusMap.put(ecu, engineStatus);
            // Invoke the method under test
            socketHandlerService.getEcuStatus(client, data);
        }
        verify(client, times(5)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_fetchEcuStatus_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuStatus(client, data));

    }

    @Test
    void whenExecute_getEcuParameterValues_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onCheckVciConnectivityRequestReceived();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_fetchEcuParameterValues_returnSuccess() throws JsonProcessingException {
        //      setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        List<EcuParameterDto> ecuParameterData = new ArrayList<>();
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getParameterData).thenReturn(ecuParameterData);

        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.getEcuParameterValues(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_fetchEcuParameterValues_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuParameterValues(client, data));

    }

    @Test
    void whenExecute_getEcuStatusValues_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuStatusValues();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_fetchEcuStatusValues_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        List<EcuStatusParamDto> ecuParameterData = new ArrayList<>();
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getStatusData).thenReturn(ecuParameterData);
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.getEcuStatusValues(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_fetchEcuStatusValues_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuStatusValues(client, data));

    }

    @Test
    void whenExecute_writeEcuStatusValue_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onWriteEcuStatusValues();
        // Result
        Assertions.assertNotNull(result);
    }


    @Test
    void whenExecute_getEcuStatus_returnSuccess() throws JsonProcessingException {
        // setup
        //  SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        List<String> ecuList = Arrays.asList("ENGINE", "BCM", "CLUSTER", "ABS", "ACU");
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuList).thenReturn(ecuList);

        doNothing().when(client).sendEvent(anyString(), any());
        for (String ecu : ecuList) {
            // Mock the SocketRequestPayloadDto with the current EcuName
            SocketRequestPayloadDto data = new SocketRequestPayloadDto();
            data.setEcuName(ecu);

            // Invoke the method under test
            socketHandlerService.getEcuStatus(client, data);
        }
        verify(client, times(5)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_doReadEcuParameterValue_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readEcuParameterValue(data.getDllCallMethod(), data.getEcuParameter())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadEcuParameterValue(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());

    }

    @Test
    void whenExecute_doReadEcuParameterValue_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadEcuParameterValue(client, data));

    }

    @Test
    void whenExecute_doFetchEcuStatusParams_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.fetchEcuStatusParams(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doFetchEcuStatusParams(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doFetchEcuStatusParams_returnJsonProcessingException() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchEcuStatusParams(client, data));
    }

    @Test
    void whenExecute_doReadEcuStatusParamValue_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readEcuStatusParamValue(data.getDllCallMethod(), data.getEcuStatusParam())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadEcuStatusParamValue(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());

    }

    @Test
    void whenExecute_doReadEcuStatusParamValue_returnJsonProcessingException() throws JsonProcessingException {

        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadEcuStatusParamValue(client, data));
    }

    @Test
    void whenExecute_doFetchEcuActuatorParams_returnSuccess() throws JsonProcessingException {
        //setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.fetchEcuActuatorParams(data.getDllCallMethod(), data.getEcuName())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doFetchEcuActuatorParams(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());

    }

    @Test
    void whenExecute_doFetchEcuActuatorParams_returnJsonProcessingException() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchEcuActuatorParams(client, data));


    }

    @Test
    void whenExecute_doFetchEcuActuatorParamOptions_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.fetchEcuActuatorParamOptions(data.getDllCallMethod(), data.getEcuActuatorParameter())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doFetchEcuActuatorParamOptions(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doFetchEcuActuatorParamOptions_returnJsonProcessingException() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doFetchEcuActuatorParamOptions(client, data));
    }

    @Test
    void whenExecute_doReadEcuActuatorParamResponse_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readEcuActuatorParamResponse(data.getDllCallMethod(), data.getEcuActuatorParameter(), data.getEcuActuatorState())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadEcuActuatorParamResponse(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_doReadEcuActuatorParamResponse_returnJsonProcessingException() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadEcuActuatorParamResponse(client, data));
    }

    @Test
    void whenExecute_doReadEcuRoutineResponse_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        SocketResponse<StackResponse> response = new SocketResponse<>();
        when(stackHandlerService.readEcuRoutineResponse(data.getDllCallMethod(), data.getEcuName(), data.getEcuRoutine())).thenReturn(response);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedResponseString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Invoke the method under test
        socketHandlerService.doReadEcuRoutineResponse(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());

    }

    @Test
    void whenExecute_doReadEcuRoutineResponse_returnJsonProcessingException() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.doReadEcuRoutineResponse(client, data));
    }

    @Test
    void whenExecute_getEcuList_returnSuccess() throws JsonProcessingException {

        List<String> ecuList = new ArrayList<>();
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuList).thenReturn(ecuList);

        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketResponse.class))).thenReturn("MockedRequestString");

        doNothing().when(client).sendEvent(anyString(), any());

        // Execution
        socketHandlerService.getEcuList(client);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuList_returnJsonProcessingException() throws JsonProcessingException {

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketResponse.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuList(client));
    }

    @Test
    void whenExecute_getEcuParameters_returnSuccess() throws JsonProcessingException {

        List<String> ecuList = Arrays.asList("ENGINE", "BCM", "CLUSTER", "ABS", "ACU");
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        Map<String, List<String>> ecuParameterList = new HashMap<>();
        StackResponse stackResponse = new StackResponse();
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::setEcuParametersMap).then(invocationOnMock -> null);
        vsd.when(VehicleStaticData::getEcuList).thenReturn(ecuList);
        stackResponse.setEcuParameters(ecuParameterList);
        doNothing().when(client).sendEvent(anyString(), any());
        for (String ecu : ecuList) {
            // Mock the SocketRequestPayloadDto with the current EcuName
            SocketRequestPayloadDto data = new SocketRequestPayloadDto();
            data.setEcuName(ecu);
            // Invoke the method under test
            socketHandlerService.getEcuParameters(client, data);
        }
        verify(client, times(5)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuParameters_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuParameters(client, data));


    }

    @Test
    void whenExecute_writeEcuStatusValues_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        data.setEcuName("ENGINE");
        data.setEcuStatus("Status");
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        List<WriteEcuStatusDto> writeEcuStatusDtos = new ArrayList<>();
        WriteEcuStatusDto writeEcuStatusDto = new EasyRandom().nextObject(WriteEcuStatusDto.class);
        writeEcuStatusDto.setEcuName("ENGINE");
        writeEcuStatusDto.setEcuStatus("Status");
        writeEcuStatusDtos.add(writeEcuStatusDto);
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getWriteStatusData).thenReturn(writeEcuStatusDtos);
        // Mock
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.writeEcuStatusValues(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_writeEcuStatusValues_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.writeEcuStatusValues(client, data));

    }

    @Test
    void whenExecute_getEcuStatusValuesLevel_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuStatusValuesLevel();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_getEcuStatusValuesLevels_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.getEcuStatusValuesLevel(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_getEcuStatusValuesLevels_EcuStatusActionListNull_returnSuccess() throws JsonProcessingException {
        // setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        data.setEcuStatusActionList(null);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        socketHandlerService.getEcuStatusValuesLevel(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_getEcuStatusValuesLevels_JsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new SocketRequestPayloadDto();
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> socketHandlerService.getEcuStatusValuesLevel(client, data));

    }

    @Test
    void whenExecute_getEcuScanList_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuScanList();
        // Result
        Assertions.assertNotNull(result);
    }


    @Test
    void whenExecute_getEcuInfo_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuInfo();
        // Result
        Assertions.assertNotNull(result);
    }


    @Test
    void whenExecute_getEcuFaultCodes_returnSuccess() {
        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetEcuFaultCodes();
        // Result
        Assertions.assertNotNull(result);
    }

    @Test
    void whenExecute_getAccessToken_returnSuccess() {

        // Execution
        DataListener<SocketRequestPayloadDto> result = socketHandlerService.onGetAccessToken();
        // Result
        Assertions.assertNotNull(result);
    }


}
