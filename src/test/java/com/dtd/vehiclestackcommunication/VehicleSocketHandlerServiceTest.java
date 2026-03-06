package com.dtd.vehiclestackcommunication;

import com.corundumstudio.socketio.SocketIOClient;
import com.dtd.vehiclestackcommunication.dto.SocketRequestPayloadDto;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.StackResponse;
import com.dtd.vehiclestackcommunication.serviceimpl.SocketHandlerServiceImpl;
import com.dtd.vehiclestackcommunication.serviceimpl.VehicleSocketHandlerServiceImpl;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(MockitoExtension.class)
class VehicleSocketHandlerServiceTest {

    @Mock
    SocketIOClient client;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    ServiceLogger serviceLogger;
    @Mock
    ObjectMapper objectMapper;

    @Mock
    SocketHandlerServiceImpl socketHandlerService;

    @Spy
    @InjectMocks
    VehicleSocketHandlerServiceImpl vehicleSocketHandlerService;

    @Test
    void whenExecute_fetchAccessToken_returnSuccess() throws JsonProcessingException {
        //Setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        data.setClientSecret("DTD1@123*45");
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        vehicleSocketHandlerService.getAccessToken(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_fetchAccessToken_returnClientSecretNull() {

        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        data.setClientSecret(null);
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getAccessToken(client, data));
    }

    @Test
    void whenExecute_fetchAccessToken_returnInvalidClientSecret() {

        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getAccessToken(client, data));
    }

    @Test
    void whenExecute_fetchAccessToken_returnJsonProcessingException() throws JsonProcessingException {
        //Setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        data.setClientSecret("DTD1@123*45");
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getAccessToken(client, data));
    }

    @Test
    void whenExecute_fetchEcuFaultCodes_returnSuccess() throws JsonProcessingException {
        //Setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");

        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        vehicleSocketHandlerService.getEcuFaultCodes(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_fetchEcuFaultCodes_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getEcuFaultCodes(client, data));
    }

    @Test
    void whenExecute_fetchEcuInfo_returnSuccess() throws JsonProcessingException {
        //Setup
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        vehicleSocketHandlerService.getEcuInfo(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_fetchEcuInfo_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getEcuInfo(client, data));
    }

    @Test
    void whenExecute_fetchEcuScanList_returnSuccess() throws JsonProcessingException {
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(StackResponse.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        vehicleSocketHandlerService.getEcuScanList(client);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_fetchEcuScanList_returnJsonProcessingException() throws JsonProcessingException {

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(StackResponse.class));
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getEcuScanList(client));
    }

    @Test
    void whenExecute_getEcuActuatorTestList_returnSuccess() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        // Mock
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(SocketRequestPayloadDto.class))).thenReturn("MockedRequestString");
        doNothing().when(client).sendEvent(anyString(), any());
        // Execution
        vehicleSocketHandlerService.getEcuActuatorTestList(client, data);
        // Result
        verify(client, times(1)).sendEvent(anyString(), any());
    }

    @Test
    void whenExecute_getEcuActuatorTestList_returnJsonProcessingException() throws JsonProcessingException {
        SocketRequestPayloadDto data = new EasyRandom().nextObject(SocketRequestPayloadDto.class);
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(SocketRequestPayloadDto.class));
        assertThrows(BadDataException.class, () -> vehicleSocketHandlerService.getEcuActuatorTestList(client, data));
    }

    @Test
    void whenExecute_getJwtSecret_returnSuccess() {
        MockedStatic<KeyGenerator> keyGeneratorMockedStatic = mockStatic(KeyGenerator.class);
        keyGeneratorMockedStatic.when(() -> KeyGenerator.getInstance(anyString())).thenThrow(NoSuchAlgorithmException.class);
        assertThrows(BadDataException.class, VehicleSocketHandlerServiceImpl::getJwtSecret);
        keyGeneratorMockedStatic.clearInvocations();
        keyGeneratorMockedStatic.close();
    }

}