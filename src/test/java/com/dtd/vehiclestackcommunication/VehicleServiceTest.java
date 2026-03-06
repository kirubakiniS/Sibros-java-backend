package com.dtd.vehiclestackcommunication;

import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.serviceimpl.VehicleServiceImpl;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
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
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VehicleServiceTest {

    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    ServiceLogger serviceLogger;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    DirectoryStream directoryStream;
    @Mock
    Path path;
    @InjectMocks
    @Spy
    private VehicleServiceImpl vehicleServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenExecute_getAccessToken_returnSuccess() {

        String CLIENT_SECRETS = "DTD1@123*45";

        Response<ResponseTokenDto> response1 = vehicleServiceImpl.getAccessToken(CLIENT_SECRETS);
        Assertions.assertEquals(HttpStatus.OK.value(), response1.getStatus());
        Assertions.assertEquals("Token Retrieved Successfully", response1.getMessage());

        // null

        try {

            vehicleServiceImpl.getAccessToken("");
        } catch (BadDataException e) {
            Assertions.assertEquals("ClientSecret is required", e.getMessage());
        }

        // Invalid
        try {
            vehicleServiceImpl.getAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IkZpc2tlciIsImp0aSI6IjM2MmVmMjg0LTAyNTktNDYzZS1iNzBkLTAyODlmNTEyZjM4NiIsImlhdCI6MTY2MTQxMzU3OCwiZXhwIjoxNjYxNDQyMzc4fQ.gRxepUv0pUCICost9DqzycgKh5cteAYlcmD33oPucAc");
        } catch (BadDataException e) {
            Assertions.assertEquals("Invalid ClientSecret", e.getMessage());
        }
    }

    @Test
    void whenExecute_getAccessToken_returnJsonProcessingException() throws JsonProcessingException {
        String CLIENT_SECRETS = "DTD1@123*45";
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(ResponseTokenDto.class));
        // When and Then
        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getAccessToken(CLIENT_SECRETS));
    }

    @Test
    void whenExecute_getVehicleStatus_returnTokenRequired() {
        Response<ResponseDto> response;
        try {
            response = vehicleServiceImpl.getVehicleStatus("", "");
            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        } catch (BadDataException e) {
            Assertions.assertEquals("Token is Required", e.getMessage());
        }
    }

    @Test
    void whenExecute_getVehicleStatus_returnSuccess() throws JsonProcessingException {
        String accessToken = "Bearer ";
        String vinNumber = "anyVin";

        Response<ResponseDto> response;
        try (MockedStatic<VehicleServiceImpl> vsd = mockStatic(VehicleServiceImpl.class)) {
            vsd.when(() -> VehicleServiceImpl.parseJwt(accessToken)).thenReturn(true);
            Mockito.when(objectMapper.writeValueAsString(Mockito.any(ResponseDto.class))).thenReturn("MockedRequestString");
            response = vehicleServiceImpl.getVehicleStatus(accessToken, vinNumber);
            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
            vsd.clearInvocations();
        }
    }

    @Test
    void whenExecute_getVehicleStatus_BadDataException() {
        String accessToken = "Bearer ";
        String vinNumber = "anyVin";

        try (MockedStatic<VehicleServiceImpl> vsd = mockStatic(VehicleServiceImpl.class)) {
            vsd.when(() -> VehicleServiceImpl.parseJwt(accessToken)).thenReturn(false);
            assertThrows(BadDataException.class, () -> vehicleServiceImpl.getVehicleStatus(accessToken, vinNumber));
            vsd.clearInvocations();
        }
    }

    @Test
    void whenExecute_getEcuFaultCodes_returnSuccess() {
        String ecuName = "BCM";
        // Execution
        Response<List<EcuDtcDto>> response = vehicleServiceImpl.getEcuFaultCodes(ecuName);
        // Result
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("ECU Diagnostic Trouble Codes Retrieved Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuActuatorTestList_returnSuccess() {
        String ecuName = "anyEcuName";
        Response<Map<String, List<String>>> response = vehicleServiceImpl.getEcuActuatorTestList(ecuName);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void whenExecute_getEcuActuatorTestList_returnJsonProcessingException() throws JsonProcessingException {
        String ecuName = "anyEcuName";
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(Map.class));

        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuActuatorTestList(ecuName));
    }

    @Test
    void whenExecute_getEcuActuatorTestData_returnSuccess() throws JsonProcessingException {

        ActuatorTestListDto actuatorTestDto = new EasyRandom().nextObject(ActuatorTestListDto.class);
        ActuatorTestDto actuatorTestDto1 = new EasyRandom().nextObject(ActuatorTestDto.class);
        List<ActuatorTestDto> actuatorTestListDto1 = new ArrayList<>();
        actuatorTestListDto1.add(actuatorTestDto1);

        Mockito.when(objectMapper.writeValueAsString(Mockito.any(ActuatorTestListDto.class))).thenReturn("MockedRequestString");

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuActuatorTestValues).thenReturn(actuatorTestListDto1);

        Response<List<ActuatorTestDto>> response = vehicleServiceImpl.getEcuActuatorTestData(actuatorTestDto);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuActuatorTestData_returnJsonProcessingException() throws JsonProcessingException {
        ActuatorTestListDto actuatorTestDto = new EasyRandom().nextObject(ActuatorTestListDto.class);

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(ActuatorTestListDto.class));

        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuActuatorTestData(actuatorTestDto));

    }

    @Test
    void whenExecute_GetAllFilesInDirectory_INTERNAL_SERVER_ERROR() {
        // Mocking necessary objects and behavior
        String ecuName = "testEcu";
        String accessToken = "mockedAccessToken";
        // Call the method
        Response<List<String>> response = vehicleServiceImpl.getAllFilesInDirectory(accessToken, ecuName);

        // Verify the behavior and assertions for success
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());

    }

    @Test
    void whenExecute_GetLatestFile_Success() {
        // Mocking necessary objects and behavior
        String ecuName = "mockedEcuName";
        String accessToken = "Bearer mockedAccessToken";
        Path mockedDirectoryPath = mock(Path.class);
        File mockedDirectory = mock(File.class);
        File mockedFile = mock(File.class);

        when(mockedFile.getName()).thenReturn("mockedFileName");
        when(mockedFile.lastModified()).thenReturn(12345L);

        when(mockedDirectoryPath.toString()).thenReturn("/valid/directory/path");
        when(mockedDirectory.isDirectory()).thenReturn(true);
        when(mockedDirectory.listFiles()).thenReturn(new File[]{mockedFile});

        when(vehicleServiceImpl.isValidDirectory(anyString())).thenReturn(true);
        when(vehicleServiceImpl.listFilesInDirectory(anyString())).thenReturn(new File[]{mockedFile});

        // Call the method
        Response<String> response = vehicleServiceImpl.getLatestFile(accessToken, ecuName);

        // Verify the behavior and assertions
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void whenExecute_GetLatestFile_UnexpectedError() {
        // Mocking necessary objects and behavior
        String ecuName = "mockedEcuName";
        String accessToken = "Bearer mockedAccessToken";
        Path mockedDirectoryPath = mock(Path.class);

        when(mockedDirectoryPath.toString()).thenReturn("/valid/directory/path");
        when(vehicleServiceImpl.isValidDirectory(anyString())).thenReturn(true);
        when(vehicleServiceImpl.listFilesInDirectory(anyString())).thenThrow(new RuntimeException("Unexpected error"));

        // Call the method
        Response<String> response = vehicleServiceImpl.getLatestFile(accessToken, ecuName);

        // Verify the behavior and assertions for an unexpected error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void whenExecute_GetLatestFile_EmptyDirectory() {
        // Mocking necessary objects and behavior
        String ecuName = "mockedEcuName";
        String accessToken = "Bearer mockedAccessToken";
        Path mockedDirectoryPath = mock(Path.class);

        when(mockedDirectoryPath.toString()).thenReturn("/valid/empty/directory/path");
        when(vehicleServiceImpl.isValidDirectory(anyString())).thenReturn(true);
        when(vehicleServiceImpl.listFilesInDirectory(anyString())).thenReturn(new File[]{});

        // Call the method
        Response<String> response = vehicleServiceImpl.getLatestFile(accessToken, ecuName);

        // Verify the behavior and assertions for an empty directory
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }

    @Test
    void whenExecute_GetLatestFile_InvalidDirectory() {
        // Mocking necessary objects and behavior
        String ecuName = "mockedEcuName";
        String accessToken = "Bearer mockedAccessToken";
        Path mockedDirectoryPath = mock(Path.class);

        when(mockedDirectoryPath.toString()).thenReturn("/invalid/directory/path");
        when(vehicleServiceImpl.isValidDirectory(anyString())).thenReturn(false);

        // Call the method
        Response<String> response = vehicleServiceImpl.getLatestFile(accessToken, ecuName);

        // Verify the behavior and assertions for an invalid directory
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    }


    @Test
    void whenExecute_getAllDetectedVciList_returnSuccess() throws JsonProcessingException {
        String accessToken = "Bearer ";
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(List.class))).thenReturn("MockedRequestString");

        Response<List<String>> response = vehicleServiceImpl.getAllDetectedVciList(accessToken);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    void whenExecute_getAllDetectedVciList_returnJsonProcessingException() throws JsonProcessingException {
        String accessToken = "Bearer ";

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));

        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getAllDetectedVciList(accessToken));

    }

    @Test
    void whenExecute_getVciDeviceDetail_returnNO_CONTENT() {
        String accessToken = "Bearer ";
        String interfaceDeviceName = "anyDevice";

        VCIDeviceInfoDto vciDeviceInfoDto = new EasyRandom().nextObject(VCIDeviceInfoDto.class);
        List<VCIDeviceInfoDto> dtoList = new ArrayList<>();
        dtoList.add(vciDeviceInfoDto);

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getVciDeviceDetail).thenReturn(dtoList);

        Response<VCIDeviceInfoDto> response = vehicleServiceImpl.getVciDeviceDetail(accessToken, interfaceDeviceName);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getVciDeviceDetail_returnSuccess() {
        String accessToken = "Bearer ";
        String interfaceDeviceName = "anyDevice";

        VCIDeviceInfoDto vciDeviceInfoDto = new EasyRandom().nextObject(VCIDeviceInfoDto.class);
        vciDeviceInfoDto.setInterfaceDeviceName("anyDevice");
        List<VCIDeviceInfoDto> dtoList = new ArrayList<>();
        dtoList.add(vciDeviceInfoDto);

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getVciDeviceDetail).thenReturn(dtoList);

        Response<VCIDeviceInfoDto> response = vehicleServiceImpl.getVciDeviceDetail(accessToken, interfaceDeviceName);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getVciDeviceDetail_returnJsonProcessingException() throws JsonProcessingException {
        String accessToken = "Bearer ";
        String interfaceDeviceName = "anyDevice";

        VCIDeviceInfoDto vciDeviceInfoDto = new EasyRandom().nextObject(VCIDeviceInfoDto.class);
        vciDeviceInfoDto.setInterfaceDeviceName("anyDevice");
        List<VCIDeviceInfoDto> dtoList = new ArrayList<>();
        dtoList.add(vciDeviceInfoDto);
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getVciDeviceDetail).thenReturn(dtoList);

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(VCIDeviceInfoDto.class));

        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getVciDeviceDetail(accessToken, interfaceDeviceName));
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_initializeVci_return_NO_CONTENT() {
        String accessToken = "Bearer ";
        String vciName = "anyVciName";
        List<String> dtoList = new ArrayList<>();

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getDetectedVciList).thenReturn(dtoList);


        Response<String> response = vehicleServiceImpl.initializeVci(accessToken, vciName);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_initializeVci_return_Success() {
        String accessToken = "Bearer ";
        String vciName = "anyVciName";
        List<String> dtoList = new ArrayList<>();
        dtoList.add(vciName);
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getDetectedVciList).thenReturn(dtoList);


        Response<String> response = vehicleServiceImpl.initializeVci(accessToken, vciName);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuRoutineResponse_return_Success() throws JsonProcessingException {

        String accessToken = "Bearer ";
        String ecuName = "ecuName";
        String ecuRoutine = "ecuRoutine";
        EcuRoutineResponseDto ecuRoutineResponseDto = new EasyRandom().nextObject(EcuRoutineResponseDto.class);
        ecuRoutineResponseDto.setEcuName(ecuName);
        ecuRoutineResponseDto.setEcuRoutine(ecuRoutine);
        List<EcuRoutineResponseDto> dtoList = new ArrayList<>();
        dtoList.add(ecuRoutineResponseDto);

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuRoutineResponse).thenReturn(dtoList);

        Mockito.when(objectMapper.writeValueAsString(Mockito.any(EcuRoutineResponseDto.class))).thenReturn("MockedRequestString");

        Response<EcuRoutineResponseDto> response = vehicleServiceImpl.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuRoutineResponse_return_NO_CONTENT() throws JsonProcessingException {

        String accessToken = "Bearer ";
        String ecuName = "ecuName";
        String ecuRoutine = "ecuRoutine";
        EcuRoutineResponseDto ecuRoutineResponseDto = new EasyRandom().nextObject(EcuRoutineResponseDto.class);
        List<EcuRoutineResponseDto> dtoList = new ArrayList<>();
        dtoList.add(ecuRoutineResponseDto);

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuRoutineResponse).thenReturn(dtoList);

        Mockito.when(objectMapper.writeValueAsString(Mockito.any(EcuRoutineResponseDto.class))).thenReturn("MockedRequestString");

        Response<EcuRoutineResponseDto> response = vehicleServiceImpl.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuRoutineResponse_returnJsonProcessingException() throws JsonProcessingException {
        String accessToken = "Bearer ";
        String ecuName = "ecuName";
        String ecuRoutine = "ecuRoutine";
        EcuRoutineResponseDto ecuRoutineResponseDto = new EasyRandom().nextObject(EcuRoutineResponseDto.class);
        ecuRoutineResponseDto.setEcuName(ecuName);
        ecuRoutineResponseDto.setEcuRoutine(ecuRoutine);
        List<EcuRoutineResponseDto> dtoList = new ArrayList<>();
        dtoList.add(ecuRoutineResponseDto);
        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuRoutineResponse).thenReturn(dtoList);

        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(EcuRoutineResponseDto.class));
        // When and Then
        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine));
        vsd.clearInvocations();
        vsd.close();

    }

    @Test
    void whenExecute_getEcuFaultCodes_returnJsonProcessingException() {
        // Setup
        String ecuName = "ENGINE";
        // When and Then
        when(vehicleServiceImpl.serializeToJson(any(List.class))).thenThrow(JsonProcessingException.class);
        when(vehicleServiceImpl.getEcuFaultCodes(ecuName)).thenThrow(BadDataException.class);
        // Result
        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuFaultCodes(ecuName));
    }

    @Test
    void whenExecute_getEcuScanList_returnSuccess() {
        // Execution
        Response<Map<String, Object>> response = vehicleServiceImpl.getEcuScanList();
        // Result
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("ECU List Scanned Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuScanList_returnJsonProcessingException() throws JsonProcessingException {
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(Map.class));
        // When and Then
        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuScanList());
    }

    @Test
    void whenExecute_getEcuInfo_returnSuccess() {
        // Setup
        String ecuName = new EasyRandom().nextObject(String.class);
        // Execution
        Response<List<EcuInfoDto>> response = vehicleServiceImpl.getEcuInfo(ecuName);
        // Result
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("ECU Part Information Retrieved Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuInfo_returnJsonProcessingException() throws JsonProcessingException {
        String ecuName = "anyEcu";
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));
        // When and Then
        assertThrows(BadDataException.class, () -> vehicleServiceImpl.getEcuInfo(ecuName));
    }
}
