package com.dtd.vehiclestackcommunication;

import com.dtd.vehiclestackcommunication.controller.MeasurementController;
import com.dtd.vehiclestackcommunication.dto.EcuParameterDto;
import com.dtd.vehiclestackcommunication.dto.EcuStatusParamDto;
import com.dtd.vehiclestackcommunication.dto.GetEcuStatusDto;
import com.dtd.vehiclestackcommunication.dto.WriteEcuStatusDto;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.service.MeasurementService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class MeasurementControllerTest {
    @Mock
    MeasurementService measurementService;

    @InjectMocks
    MeasurementController measurementController;

    @Test
    void whenExecute_getEcuList_returnSuccess() {
        // Setup
        Response<List<String>> response = new Response<>();
        response.setMessage("ECU List Retrieved Successfully");
        // Mock
        when(measurementService.getEcuList()).thenReturn(response);
        // Execution
        Response<List<String>> result = measurementController.getEcuList();
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU List Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuParameters_returnSuccess() {
        // Setup
        String ecuName = new EasyRandom().nextObject(String.class);
        Response<Map<String, List<String>>> response = new Response<>();
        response.setMessage("ECU Parameter List Retrieved Successfully");
        // Mock
        when(measurementService.getEcuParameters(anyString())).thenReturn(response);
        // Execution
        Response<Map<String, List<String>>> result = measurementController.getEcuParameters(ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Parameter List Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuStatus_returnSuccess() {
        // Setup
        String ecuName = new EasyRandom().nextObject(String.class);
        Response<Map<String, List<String>>> response = new Response<>();
        response.setMessage("ECU Status List Retrieved Successfully");
        // Mock
        when(measurementService.getEcuStatus(anyString())).thenReturn(response);
        // Execution
        Response<Map<String, List<String>>> result = measurementController.getEcuStatus(ecuName);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Status List Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuParameterValues_returnSuccess() {
        // Setup
        Map<String, List<String>> ecuParameters = new HashMap<>();
        ecuParameters.put("ENGINE", Arrays.asList("Engine Speed", "Vehicle Speed"));
        ecuParameters.put("BCM", Arrays.asList("Battery Voltage", "Vehicle Speed"));
        Response<List<EcuParameterDto>> response = new Response<>();
        response.setMessage("ECU Status List Retrieved Successfully");
        // Mock
        when(measurementService.getEcuParameterValues(anyMap())).thenReturn(response);
        // Execution
        Response<List<EcuParameterDto>> result = measurementController.getEcuParameterValues(ecuParameters);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Status List Retrieved Successfully");
    }

    @Test
    void whenExecute_getEcuStatusValues_returnSuccess() {
        // Setup
        Map<String, List<String>> ecuStatuses = new HashMap<>();
        ecuStatuses.put("ENGINE", Arrays.asList("Accelerator Pedal Position", "Turbocharger Solenoid Valve"));
        ecuStatuses.put("ABS", Arrays.asList("Brake Booster", "Brake Pedal Position"));
        Response<List<EcuStatusParamDto>> response = new Response<>();
        response.setMessage("ECU Status Values Retrieved Successfully");
        // Mock
        when(measurementService.getEcuStatusValues(anyMap())).thenReturn(response);
        // Execution
        Response<List<EcuStatusParamDto>> result = measurementController.getEcuStatusValues(ecuStatuses);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Status Values Retrieved Successfully");
    }

    @Test
    void whenExecute_writeEcuStatusValues_returnSuccess() {
        String ecuName = "testEcuName";
        String ecuStatus = "testStatus";
        Response<List<WriteEcuStatusDto>> response = new Response<>();
        response.setMessage("ECU Status Values write Successfully");
        // Mock
        when(measurementService.writeEcuStatusValues(anyString(), anyString())).thenReturn(response);
        Response<List<WriteEcuStatusDto>> result = measurementController.writeEcuStatusValues(ecuName, ecuStatus);
        // Result
        assertThat(result.getMessage()).isEqualTo("ECU Status Values write Successfully");
    }

    @Test
    void whenExecute_getEcuStatusValuesLevel_returnSuccess() {
        //setup
        GetEcuStatusDto ecuStatuses = new EasyRandom().nextObject(GetEcuStatusDto.class);
        Map<String, Object> response = new HashMap<>();
        // Mock
        when(measurementService.getEcuStatusValuesLevel(ecuStatuses)).thenReturn(response);
        Map<String, Object> result = measurementController.getEcuStatusValues(ecuStatuses);
        // Result
        Assertions.assertNotNull(result);
    }
}
