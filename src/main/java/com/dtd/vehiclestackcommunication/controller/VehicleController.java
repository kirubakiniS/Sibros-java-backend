/**
 * Vehicle Controller
 * APIs for Getting Vehicle Connected Status and Vehicle Information
 *
 * @name VehicleController
 * @vendor BlueBinaries
 * @version 1.0
 * @author developers@bluebinaries.com
 * @copyright Copyright (C) 2022 BlueBinaries. All rights reserved.
 */

package com.dtd.vehiclestackcommunication.controller;

import com.dtd.vehiclestackcommunication.dto.*;
import com.dtd.vehiclestackcommunication.response.Response;
import com.dtd.vehiclestackcommunication.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "Vehicle Stack Communication API")
@RestController
@RequestMapping(value = "/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @ApiOperation(value = "Access Token", notes = "Getting access token to connect with the client ")
    @PostMapping("/access-token")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<ResponseTokenDto> getAccessToken(@RequestBody RequestDto request) {
        return vehicleService.getAccessToken(request.getClientSecret());
    }

    @ApiOperation(value = "Vehicle Connected Status", notes = "Connecting with VCI to get the VIN")
    @GetMapping("/vehicle-status")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<ResponseDto> getVehicleStatus(@RequestHeader(value = "Authorization") String accessToken, String vinNumber) {
        return vehicleService.getVehicleStatus(accessToken, vinNumber);
    }

    @ApiOperation(value = "Fault Management ECU DTC list", notes = "Sending DTC, it's description and status for listed ECUs")
    @GetMapping(value = "/ecu-dtc-list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<EcuDtcDto>> getEcuFaultCodes(@RequestParam String ecuName) {
        return vehicleService.getEcuFaultCodes(ecuName);
    }

    @ApiOperation(value = "Scan and confirm ECU connectivity with VCI", notes = "Sending list of ECUs scanned from the vehicle and their connectivity status with VCI")
    @GetMapping(value = "/ecu-scan")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<Map<String, Object>> getEcuScanList() {
        return vehicleService.getEcuScanList();
    }

    @ApiOperation(value = "Display Scanned ECU part Information", notes = "Sending scanned ECU part description and software information")
    @GetMapping(value = "/ecu-info")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<EcuInfoDto>> getEcuInfo(String ecuName) {
        return vehicleService.getEcuInfo(ecuName);
    }

    @ApiOperation(value = "ECU active test List", notes = "Sending list of active test actuators or I/O control list for ECU")
    @GetMapping(value = "/ecu-active-test-list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<Map<String, List<String>>> getEcuActuatorTestList(@RequestParam String ecuName) {
        return vehicleService.getEcuActuatorTestList(ecuName);
    }

    @ApiOperation(value = "ECU active test Data", notes = "Sending list of active test actuators or I/O control data for ECU")
    @PostMapping(value = "/ecu-active-test-data")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<ActuatorTestDto>> getEcuActuatorTestData(@RequestBody ActuatorTestListDto actuatorTestList) {
        return vehicleService.getEcuActuatorTestData(actuatorTestList);
    }

    @ApiOperation(value = "File monitor System", notes = "fetch the latest file in the specific directory for component flashing")
    @GetMapping(value = "/get-latest-file")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<String> getLatestFile(@RequestHeader(value = "Authorization") String accessToken, @RequestParam String ecuName) {
        return vehicleService.getLatestFile(accessToken, ecuName);
    }

    @GetMapping(value = "/get-all-files-in-directory")
    public Response<List<String>> getAllFilesInDirectory(@RequestHeader(value = "Authorization") String accessToken, @RequestParam String ecuName) {
        return vehicleService.getAllFilesInDirectory(accessToken, ecuName);
    }

    @GetMapping(value = "/get-detected-vci-list")
    public Response<List<String>> getAllDetectedVciList(@RequestHeader(value = "Authorization") String accessToken) {
        return vehicleService.getAllDetectedVciList(accessToken);
    }

    @GetMapping(value = "/get-vci-device-detail")
    public Response<VCIDeviceInfoDto> getVciDeviceDetail(@RequestHeader(value = "Authorization") String accessToken, @RequestParam String vciName) {
        return vehicleService.getVciDeviceDetail(accessToken, vciName);
    }

    @GetMapping(value = "/initialize-vci")
    public Response<String> initializeVci(@RequestHeader(value = "Authorization") String accessToken, @RequestParam String vciName) {
        return vehicleService.initializeVci(accessToken, vciName);
    }

    @GetMapping(value = "/get-ecu-routine-response")
    public Response<EcuRoutineResponseDto> getEcuRoutineResponse(@RequestHeader(value = "Authorization") String accessToken, String ecuName, String ecuRoutine) {
        return vehicleService.getEcuRoutineResponse(accessToken, ecuName, ecuRoutine);
    }

    @ApiOperation(value = "Get ECU coordinates information", notes = "Sending scanned ECU coordinates information to locate them in the 2D/3D car image")
    @GetMapping(value = "/get-ecu-coordinates")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<EcuPositionDto> getEcuCoordinates(@RequestParam String ecuName) {
        return vehicleService.getEcuCoordinates(ecuName);
    }

    @ApiOperation(value = "Fault management - clear ECU DTCs", notes = "Clearing memorized ECU DTCs and fetch only active and pending DTCs")
    @GetMapping(value = "/clear-ecu-dtc")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = ResponseVehicleFeatureDto.class), @ApiResponse(code = 401, message = "Unauthorized", response = SwaggerOk.class), @ApiResponse(code = 404, message = "Not Found", response = SwaggerOk.class), @ApiResponse(code = 403, message = "Forbidden", response = SwaggerOk.class)})
    public Response<List<EcuDtcDto>> clearEcuDTC(@RequestParam String ecuName) {
        return vehicleService.clearEcuDTC(ecuName);
    }
}
