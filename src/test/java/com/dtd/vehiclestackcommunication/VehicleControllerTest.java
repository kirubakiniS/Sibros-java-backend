package com.dtd.vehiclestackcommunication;

import com.dtd.vehiclestackcommunication.controller.VehicleController;
import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.serviceimpl.VehicleServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    VehicleServiceImpl service;

    @InjectMocks
    VehicleController vehicle;

    @Test
    void whenExecute_getAccessToken_returnSuccess() {
        Response<ResponseTokenDto> tokenDto = new Response<>();
        RequestDto request = new EasyRandom().nextObject(RequestDto.class);
        tokenDto.setMessage("Token Retrieved Successfully");

        when(service.getAccessToken(anyString())).thenReturn(tokenDto);

        Response<ResponseTokenDto> response = vehicle.getAccessToken(request);

        assertThat(response.getMessage()).isEqualTo("Token Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuFaultCodes_returnSuccess() {
        // Setup
        Response<List<EcuDtcDto>> response = new Response<>();
        response.setMessage("ECU Diagnostic Trouble Codes Retrieved Successfully");
        // Mock
        when(service.getEcuFaultCodes(anyString())).thenReturn(response);
        // Execution
        Response<List<EcuDtcDto>> result = vehicle.getEcuFaultCodes(anyString());
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Diagnostic Trouble Codes Retrieved Successfully");
    }

    @Test
    void whenExecute_getVehicleStatus_returnSuccess() {
        // Setup
        String token = new EasyRandom().nextObject(String.class);
        String vinNumber = "VIN-0012";
        Response<ResponseDto> response = new Response<>();
        response.setMessage("Connected Status Retrieved Successfully");
        // Mock
        when(service.getVehicleStatus(anyString(), anyString())).thenReturn(response);
        // Execution
        Response<ResponseDto> result = vehicle.getVehicleStatus(token, vinNumber);
        // Result
        assertThat(result.getMessage()).isEqualTo("Connected Status Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuScanList_returnSuccess() {
        // Setup
        Response<Map<String, Object>> response = new Response<>();
        response.setMessage("ECU List Scanned Successfully");
        // Mock
        when(service.getEcuScanList()).thenReturn(response);
        // Execution
        Response<Map<String, Object>> result = vehicle.getEcuScanList();
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU List Scanned Successfully");
    }

    @Test
    void whenExecute_getEcuInfo_returnSuccess() {
        // Setup
        String ecuName = new EasyRandom().nextObject(String.class);
        Response<List<EcuInfoDto>> response = new Response<>();
        response.setMessage("ECU Part Information Retrieved Successfully");
        // Mock
        when(service.getEcuInfo(anyString())).thenReturn(response);
        // Execution
        Response<List<EcuInfoDto>> result = vehicle.getEcuInfo(ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Part Information Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuActuatorTestList_returnSuccess() {
        // Setup
        String ecuName = "anyEcuName";
        Response<Map<String, List<String>>> response = new Response<>();
        response.setMessage("ECU Actuator Test List Retrieved Successfully");
        // Mock
        when(service.getEcuActuatorTestList(anyString())).thenReturn(response);
        // Execution
        Response<Map<String, List<String>>> result = vehicle.getEcuActuatorTestList(ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Actuator Test List Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuActuatorTestData_returnSuccess() {
        // Setup
        ActuatorTestListDto actuatorTestList = new EasyRandom().nextObject(ActuatorTestListDto.class);
        Response<List<ActuatorTestDto>> response = new Response<>();
        response.setMessage("ECU Actuator Test Data Retrieved Successfully");
        // Mock
        when(service.getEcuActuatorTestData(actuatorTestList)).thenReturn(response);
        // Execution
        Response<List<ActuatorTestDto>> result = vehicle.getEcuActuatorTestData(actuatorTestList);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Actuator Test Data Retrieved Successfully");
    }

    @Test
    void whenExecute_getLatestFile_returnSuccess() {
        // Setup
        String accessToken = "Bearer testToken";
        String ecuName = "anyEcuName";
        Response<String> response = new Response<>();
        response.setMessage("Latest created or modified file has been retrieved successfully");
        // Mock
        when(service.getLatestFile(accessToken, ecuName)).thenReturn(response);
        // Execution
        Response<String> result = vehicle.getLatestFile(accessToken, ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("Latest created or modified file has been retrieved successfully");
    }

    @Test
    void whenExecute_getAllDetectedVciList_returnSuccess() {
        // Setup
        String accessToken = "Bearer TestToken";
        Response<List<String>> response = new Response<>();
        response.setMessage("List of detected VCIs Retrieved Successfully");
        // Mock
        when(service.getAllDetectedVciList(accessToken)).thenReturn(response);
        // Execution
        Response<List<String>> result = vehicle.getAllDetectedVciList(accessToken);
        // Result
        assertThat(result.getMessage()).isEqualTo("List of detected VCIs Retrieved Successfully");
    }

    @Test
    void whenExecute_getVciDeviceDetail_returnSuccess() {
        // Setup
        String accessToken = "Bearer TestToken";
        String vciName = "anyVciName";
        Response<VCIDeviceInfoDto> response = new Response<>();
        response.setMessage("VCI device detail retrieved successfully");
        // Mock
        when(service.getVciDeviceDetail(accessToken, vciName)).thenReturn(response);
        // Execution
        Response<VCIDeviceInfoDto> result = vehicle.getVciDeviceDetail(accessToken, vciName);
        // Result
        assertThat(result.getMessage()).isEqualTo("VCI device detail retrieved successfully");
    }

    @Test
    void whenExecute_initializeVci_returnSuccess() {
        // Setup
        String accessToken = "Bearer TestToken";
        String vciName = "anyVciName";
        Response<String> response = new Response<>();
        response.setMessage("VCI Initialized - 81");
        // Mock
        when(service.initializeVci(accessToken, vciName)).thenReturn(response);
        // Execution
        Response<String> result = vehicle.initializeVci(accessToken, vciName);
        // Result
        assertThat(result.getMessage()).isEqualTo("VCI Initialized - 81");
    }

    @Test
    void whenExecute_getAllFilesInDirectory_returnSuccess() {
        // Setup
        String accessToken = "Bearer TestToken";
        String ecuName = "anyEcuName";
        Response<List<String>> response = new Response<>();
        response.setMessage("List of available file names in the directory retrieved successfully");
        // Mock
        when(service.getAllFilesInDirectory(accessToken, ecuName)).thenReturn(response);
        // Execution
        Response<List<String>> result = vehicle.getAllFilesInDirectory(accessToken, ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("List of available file names in the directory retrieved successfully");
    }

    @Test
    void whenExecute_getEcuRoutineResponse_returnSuccess() {
        // Setup
        String accessToken = "Bearer TestToken";
        String ecuName = "anyVciName";
        String ecuRoutine = "anyEcuRoutine";
        Response<EcuRoutineResponseDto> response = new Response<>();
        response.setMessage("ECU routine response retrieved successfully");
        // Mock
        when(service.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine)).thenReturn(response);
        // Execution
        Response<EcuRoutineResponseDto> result = vehicle.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU routine response retrieved successfully");
    }

}
