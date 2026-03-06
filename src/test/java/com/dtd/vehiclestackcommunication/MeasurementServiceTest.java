package com.dtd.vehiclestackcommunication;

import com.dtd.vehiclestackcommunication.dto.EcuParameterDto;
import com.dtd.vehiclestackcommunication.dto.EcuStatusParamDto;
import com.dtd.vehiclestackcommunication.dto.GetEcuStatusDto;
import com.dtd.vehiclestackcommunication.dto.WriteEcuStatusDto;
import com.dtd.vehiclestackcommunication.exception.BadDataException;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.serviceimpl.MeasurementServiceImpl;
import com.dtd.vehiclestackcommunication.servicelogger.ServiceLogger;
import com.dtd.vehiclestackcommunication.util.VehicleStaticData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class MeasurementServiceTest {
    @InjectMocks
    MeasurementServiceImpl measurementServiceImpl;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    ServiceLogger serviceLogger;
    @Mock
    ObjectMapper objectMapper;

    @Test
    void whenExecute_getEcuList_returnSuccess() {
        //Execution
        Response<List<String>> response = measurementServiceImpl.getEcuList();
        //Result
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("ECU List Retrieved Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuList_returnJsonProcessingException() throws JsonProcessingException {

        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuList());
    }

    @Test
    void whenExecute_getEcuParameters_returnSuccess() {
        //Setup
        List<String> ecuName = new ArrayList<>();
        ecuName.add("ENGINE");
        ecuName.add("BCM");
        ecuName.add("CLUSTER");
        ecuName.add("ABS");
        ecuName.add("HAC");

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getEcuList).thenReturn(ecuName);
        //Execution
        Response<Map<String, List<String>>> response0 = measurementServiceImpl.getEcuParameters(ecuName.get(0));
        Response<Map<String, List<String>>> response1 = measurementServiceImpl.getEcuParameters(ecuName.get(1));
        Response<Map<String, List<String>>> response2 = measurementServiceImpl.getEcuParameters(ecuName.get(2));
        Response<Map<String, List<String>>> response3 = measurementServiceImpl.getEcuParameters(ecuName.get(3));
        Response<Map<String, List<String>>> response4 = measurementServiceImpl.getEcuParameters(ecuName.get(4));
        //Result
        assertEquals(HttpStatus.OK.value(), response0.getStatus());
        assertEquals("ECU Parameter List Retrieved Successfully", response0.getMessage());
        assertEquals(HttpStatus.OK.value(), response1.getStatus());
        assertEquals("ECU Parameter List Retrieved Successfully", response1.getMessage());
        assertEquals(HttpStatus.OK.value(), response2.getStatus());
        assertEquals("ECU Parameter List Retrieved Successfully", response2.getMessage());
        assertEquals(HttpStatus.OK.value(), response3.getStatus());
        assertEquals("ECU Parameter List Retrieved Successfully", response3.getMessage());
        assertEquals(HttpStatus.OK.value(), response4.getStatus());
        assertEquals("ECU Parameter List Retrieved Successfully", response4.getMessage());

        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_getEcuParameters_returnJsonProcessingException() throws JsonProcessingException {
        String ecuName = "anyEcu";
        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(Map.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuParameters(ecuName));
    }

    @Test
    void whenExecute_getEcuStatus_returnSuccess() {
        //Setup
        List<String> ecuName = new ArrayList<>();
        ecuName.add("ENGINE");
        ecuName.add("BCM");
        ecuName.add("CLUSTER");
        ecuName.add("ABS");
        ecuName.add("ACU");

        //Execution
        Response<Map<String, List<String>>> response0 = measurementServiceImpl.getEcuStatus(ecuName.get(0));
        Response<Map<String, List<String>>> response1 = measurementServiceImpl.getEcuStatus(ecuName.get(1));
        Response<Map<String, List<String>>> response2 = measurementServiceImpl.getEcuStatus(ecuName.get(2));
        Response<Map<String, List<String>>> response3 = measurementServiceImpl.getEcuStatus(ecuName.get(3));
        Response<Map<String, List<String>>> response4 = measurementServiceImpl.getEcuStatus(ecuName.get(4));
        //Result
        assertEquals(HttpStatus.OK.value(), response0.getStatus());
        assertEquals("ECU Status List Retrieved Successfully", response0.getMessage());
        assertEquals(HttpStatus.OK.value(), response1.getStatus());
        assertEquals("ECU Status List Retrieved Successfully", response1.getMessage());
        assertEquals(HttpStatus.OK.value(), response2.getStatus());
        assertEquals("ECU Status List Retrieved Successfully", response2.getMessage());
        assertEquals(HttpStatus.OK.value(), response3.getStatus());
        assertEquals("ECU Status List Retrieved Successfully", response3.getMessage());
        assertEquals(HttpStatus.OK.value(), response4.getStatus());
        assertEquals("ECU Status List Retrieved Successfully", response4.getMessage());
    }

    @Test
    void whenExecute_getEcuStatus_returnJsonProcessingException() throws JsonProcessingException {
        String ecuName = "anyEcu";
        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(Map.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuStatus(ecuName));
    }

    @Test
    void whenExecute_getEcuParameterValues_returnSuccess() {
        // Setup
        Map<String, List<String>> ecuParameters = new HashMap<>();
        ecuParameters.put("ENGINE", Arrays.asList("Engine Speed", "Vehicle Speed"));
        ecuParameters.put("BCM", Arrays.asList("Battery Voltage", "Vehicle Speed"));
        // Execution
        Response<List<EcuParameterDto>> response = measurementServiceImpl.getEcuParameterValues(ecuParameters);
        // Result
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("ECU Parameter Values Retrieved Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuParameterValues_returnJsonProcessingException() throws JsonProcessingException {
        Map<String, List<String>> ecuParameters = new HashMap<>();
        ecuParameters.put("ENGINE", Arrays.asList("Engine Speed", "Vehicle Speed"));
        ecuParameters.put("BCM", Arrays.asList("Battery Voltage", "Vehicle Speed"));
        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuParameterValues(ecuParameters));
    }

    @Test
    void whenExecute_getEcuStatusValues_returnSuccess() {
        // Setup
        Map<String, List<String>> ecuStatuses = new HashMap<>();
        ecuStatuses.put("ENGINE", Arrays.asList("Accelerator Pedal Position", "Turbocharger Solenoid Valve"));
        ecuStatuses.put("ABS", Arrays.asList("Brake Booster", "Brake Pedal Position"));
        // Execution
        Response<List<EcuStatusParamDto>> response = measurementServiceImpl.getEcuStatusValues(ecuStatuses);
        // Result
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("ECU Status Values Retrieved Successfully", response.getMessage());
    }

    @Test
    void whenExecute_getEcuStatusValues_returnJsonProcessingException() throws JsonProcessingException {
        Map<String, List<String>> ecuParameters = new HashMap<>();
        ecuParameters.put("ENGINE", Arrays.asList("Engine Speed", "Vehicle Speed"));
        ecuParameters.put("BCM", Arrays.asList("Battery Voltage", "Vehicle Speed"));
        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuStatusValues(ecuParameters));
    }

    @Test
    void whenExecute_getEcuStatusValuesLevel_returnSuccess() {
        // Mock input
        GetEcuStatusDto ecuStatuses = new GetEcuStatusDto();
        ecuStatuses.setEcuName("TestEcu");
        ecuStatuses.setEcuStatus("TestStatus");
        try {
            when(objectMapper.writeValueAsString(Mockito.any(GetEcuStatusDto.class))).thenReturn("MockedRequestString");

            // Call the method
            Map<String, Object> result = measurementServiceImpl.getEcuStatusValuesLevel(ecuStatuses);

            // Verify that objectMapper.writeValueAsString was called with the correct argument
            Mockito.verify(objectMapper).writeValueAsString(ecuStatuses);

            // Verify the result
            assertEquals("TestEcu", result.get("ecuName"));
            assertEquals("TestStatus", result.get("ecuStatus"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void whenExecute_getEcuStatusValuesLevel_returnJsonProcessingException() throws JsonProcessingException {
        GetEcuStatusDto ecuStatuses = new EasyRandom().nextObject(GetEcuStatusDto.class);
        // Mocking ObjectMapper to throw JsonProcessingException
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(Map.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.getEcuStatusValuesLevel(ecuStatuses));
    }

    @Test
    void whenExecute_WriteEcuStatusValues_returnSuccess() throws JsonProcessingException {
        // Mocking input values
        String ecuName = "TestEcu";
        String ecuStatus = "TestStatus";
        List<WriteEcuStatusDto> writeEcuStatusDtos = new ArrayList<>();
        // Mocking the data returned by VehicleStaticData.getWriteStatusData()
        WriteEcuStatusDto mockEcuStatusList = new EasyRandom().nextObject(WriteEcuStatusDto.class);
        writeEcuStatusDtos.add(mockEcuStatusList);
        // Mocking the behavior of VehicleStaticData.getWriteStatusData()

        MockedStatic<VehicleStaticData> vsd = Mockito.mockStatic(VehicleStaticData.class);
        vsd.when(VehicleStaticData::getWriteStatusData).thenReturn(writeEcuStatusDtos);
        //  when(VehicleStaticData.getWriteStatusData()).thenReturn(writeEcuStatusDtos);
        Mockito.when(objectMapper.writeValueAsString(Mockito.any(List.class))).thenReturn("MockedRequestString");
        // Invoking the method under test
        Response<List<WriteEcuStatusDto>> response = measurementServiceImpl.writeEcuStatusValues(ecuName, ecuStatus);

        //  Mockito.verify(objectMapper).writeValueAsString(writeEcuStatusDtos);
        // Assertions
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        vsd.clearInvocations();
        vsd.close();
    }

    @Test
    void whenExecute_WriteEcuStatusValues_returnJsonProcessingException() throws JsonProcessingException {

        String ecuName = "TestEcu";
        String ecuStatus = "TestStatus";
        doThrow(JsonProcessingException.class).when(objectMapper).writeValueAsString(Mockito.any(List.class));
        // When and Then
        assertThrows(BadDataException.class, () -> measurementServiceImpl.writeEcuStatusValues(ecuName, ecuStatus));
    }
}