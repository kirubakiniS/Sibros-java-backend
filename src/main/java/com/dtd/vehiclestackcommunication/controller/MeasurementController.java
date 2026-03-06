package com.dtd.vehiclestackcommunication.controller;

import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.service.MeasurementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/measurement")
public class MeasurementController {

    @Autowired
    MeasurementService measurementService;

    @ApiOperation(value = "ECU list", notes = "Sending list of ECUs for fault management/measurement and monitoring")
    @GetMapping(value = "/ecu-list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<String>> getEcuList() {
        return measurementService.getEcuList();
    }

    @ApiOperation(value = "ECU Parameter List", notes = "Sending ECU parameters list for check box selection in measurement and Monitoring")
    @GetMapping(value = "/ecu-parameters")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<Map<String, List<String>>> getEcuParameters(@RequestParam String ecuName) {
        return measurementService.getEcuParameters(ecuName);
    }

    @ApiOperation(value = "ECU Status List", notes = "Sending ECU status list for check box selection in measurement and Monitoring")
    @GetMapping(value = "/ecu-status")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<Map<String, List<String>>> getEcuStatus(@RequestParam String ecuName) {
        return measurementService.getEcuStatus(ecuName);
    }

    @ApiOperation(value = "ECU Parameters Values", notes = "Sending ECU parameters values for measurement and Monitoring")
    @PostMapping(value = "/ecu-parameter-values")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<EcuParameterDto>> getEcuParameterValues(@RequestBody Map<String, List<String>> ecuParameters) {
        return measurementService.getEcuParameterValues(ecuParameters);
    }

    @ApiOperation(value = "ECU Status Values", notes = "Sending ECU Status values for measurement and Monitoring")
    @PostMapping(value = "/ecu-status-values")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<EcuStatusParamDto>> getEcuStatusValues(@RequestBody Map<String, List<String>> ecuStatuses) {
        return measurementService.getEcuStatusValues(ecuStatuses);
    }

    @ApiOperation(value = "Write ECU Status Values", notes = "Sending ECU Status values for measurement and Monitoring")
    @GetMapping(value = "/write-ecu-status-values")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<WriteEcuStatusDto>> writeEcuStatusValues(@RequestParam String ecuName, String ecuStatus) {
        return measurementService.writeEcuStatusValues(ecuName, ecuStatus);
    }

    @ApiOperation(value = "Get ECU Status Values", notes = "Receiving ECU Status values for measurement and Monitoring")
    @PostMapping(value = "/get-ecu-status-values")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Map<String, Object> getEcuStatusValues(@RequestBody GetEcuStatusDto ecuStatuses) {
        return measurementService.getEcuStatusValuesLevel(ecuStatuses);
    }
}
