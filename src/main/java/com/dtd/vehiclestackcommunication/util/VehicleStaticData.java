package com.dtd.vehiclestackcommunication.util;

import com.dtd.vehiclestackcommunication.dto.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class VehicleStaticData {

    public static final String DEG_C = "°C";
    public static final String VOLTS = "volts";
    public static final String PRESSED = "Pressed";
    public static final String RELEASED = "Released";
    public static final String LEVEL_1 = "Level 1";
    public static final String DB = "5 db";
    public static final String LEVEL_2 = "Level 2";
    public static final String LEVEL_3 = "Level 3";
    public static final String PENDING_FAULT = "Pending fault";
    public static final String MEMORISED_FAULT = "Memorised fault";
    public static final String ACTIVE_FAULT = "Active fault";
    public static final String FAULT = "Fault";
    public static final String DEACTIVATE = "Deactivate";
    public static final String ACTIVATE = "Activate";
    public static final String IGNITION_ON = "Ignition on";
    public static final String ENGINE_OFF = "Engine off";
    public static final String PARKING_BRAKE_ENGAGED = "Parking brake engaged";
    public static final String NO_ACCESSORIES_TO_BE_SWITCHED_ON = "No accessories to be switched ON.";
    public static final String RUNNING_CONDITION_IDLE_SPEED = "Engine should be in running condition (Idle speed)";
    public static final String ON = "On";
    public static final String OFF = "Off";
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";
    public static final String ACTIVE = "Active";
    public static final String INACTIVE = "Inactive";
    public static final String VIN_NUMBER = "VCF1ZBU2XPG001062";
    public static final String ACTIVATE_AND_DEACTIVATE_BUTTONS = "Activate and deactivate buttons to be displayed.";
    public static final String CLICKING_ON_ACTIVATE_BUTTON = "Clicking on activate button will start the routine. The button will be disabled till routine is completed.";
    public static final String CLICKING_ON_DEACTIVATE_BUTTON = "Clicking on deactivate button will stop the routine.";
    public static final String DEACTIVATE_BUTTON_WILL_STAY_DISABLED = "Deactivate button will stay disabled until activate button is disabled. It will be enabled only after the activate button is disabled.";
    public static final String SECONDS_10 = "10 seconds";
    public static final String UNLOCK = "Unlock";
    public static final String BOOTLOADER_VERSION_NUMBER = "BT278034S102G";
    public static final String ECU_MANUFACTURING_DATE = "28-09-2023";
    public static final String CORRECT = "Correct";
    private static List<String> ecuList;
    private static Map<String, List<String>> ecuParametersMap;
    private static Map<String, List<String>> ecuStatusMap;
    private static List<EcuParameterDto> ecuParameterValues;
    private static List<EcuStatusParamDto> ecuStatusValues;
    private static List<EcuDtcDto> ecuDtcValues;
    private static List<EcuPositionDto> ecuPositionData;
    private static List<EcuInfoDto> ecuInfoDto;
    private static List<WriteEcuStatusDto> ecuStatusDtoValues;
    private static List<ActuatorTestDto> actuatorTestValues;
    private static Map<String, List<String>> ecuActuatorTestList;
    private static List<String> detectedVciList;
    private static List<VCIDeviceInfoDto> vciDeviceInfoList;
    private static List<EcuRoutineResponseDto> ecuRoutineResponses;
    private static List<ActuatorTestResponseDto> ecuActuatorTestResponse;

    private VehicleStaticData() {
//        created private constructor to not instantiate this util class
    }

    public static void setEcuList() {
        ecuList = new ArrayList<>();
        ecuList.add("ENGINE");
        ecuList.add("BCM");
        ecuList.add("CLUSTER");
        ecuList.add("ABS");
        ecuList.add("ACU");
    }

    public static List<String> getEcuList() {
        setEcuList();
        return ecuList;
    }

    public static void setEcuParametersMap() {
        ecuParametersMap = new HashMap<>();

        List<String> engineParameters = new ArrayList<>();
        engineParameters.add("Vin Number");
        engineParameters.add("ECU Software Number");
        engineParameters.add("ECU Hardware Number");
        engineParameters.add("Throttle Position Sensor");
        engineParameters.add("Knock Sensor");
        engineParameters.add("Engine Speed");
        engineParameters.add("Bootloader Version Number");
        engineParameters.add("Manufacturing Date");
        ecuParametersMap.put(getEcuList().get(0), engineParameters);

        List<String> bcmParameters = new ArrayList<>();
        bcmParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(0));
        bcmParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(1));
        bcmParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(2));
        bcmParameters.add("Battery Voltage");
        bcmParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(6));
        bcmParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(7));
        ecuParametersMap.put(getEcuList().get(1), bcmParameters);

        List<String> clusterParameters = new ArrayList<>();
        clusterParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(0));
        clusterParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(1));
        clusterParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(2));
        clusterParameters.add("Odometer");
        clusterParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(6));
        clusterParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(7));
        ecuParametersMap.put(getEcuList().get(2), clusterParameters);

        List<String> absParameters = new ArrayList<>();
        absParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(0));
        absParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(1));
        absParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(2));
        absParameters.add("Vehicle Speed");
        absParameters.add("Steering Wheel Angle Sensor");
        absParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(6));
        absParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(7));
        ecuParametersMap.put(getEcuList().get(3), absParameters);

        List<String> acuParameters = new ArrayList<>();
        acuParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(0));
        acuParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(1));
        acuParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(2));
        acuParameters.add("Passenger Knee Airbag Squib");
        acuParameters.add("Driver Knee Airbag Squib");
        acuParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(6));
        acuParameters.add(getEcuParametersMap().get(getEcuList().get(0)).get(7));
        ecuParametersMap.put(getEcuList().get(4), acuParameters);
    }

    public static Map<String, List<String>> getEcuParametersMap() {
        return ecuParametersMap;
    }

    public static void setParameterData() {
        setEcuParametersMap();
        ecuParameterValues = new ArrayList<>();

        EcuParameterDto ecu1Param1 = new EcuParameterDto();
        ecu1Param1.setEcuName(getEcuList().get(0));
        ecu1Param1.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(0));
        ecu1Param1.setEcuParameterValue(VIN_NUMBER);
        ecuParameterValues.add(ecu1Param1);

        EcuParameterDto ecu1Param2 = new EcuParameterDto();
        ecu1Param2.setEcuName(getEcuList().get(0));
        ecu1Param2.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(1));
        ecu1Param2.setEcuParameterValue("20985763TT");
        ecuParameterValues.add(ecu1Param2);

        EcuParameterDto ecu1Param3 = new EcuParameterDto();
        ecu1Param3.setEcuName(getEcuList().get(0));
        ecu1Param3.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(2));
        ecu1Param3.setEcuParameterValue("4098022THR");
        ecuParameterValues.add(ecu1Param3);

        EcuParameterDto ecu1Param4 = new EcuParameterDto();
        ecu1Param4.setEcuName(getEcuList().get(0));
        ecu1Param4.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(3));
        ecu1Param4.setEcuParameterValue(String.valueOf(120F));
        ecu1Param4.setEcuParameterUnit("ohms");
        ecu1Param4.setEcuParameterMinValue(25F);
        ecu1Param4.setGetEcuParameterMaxValue(175F);
        ecuParameterValues.add(ecu1Param4);

        EcuParameterDto ecu1Param5 = new EcuParameterDto();
        ecu1Param5.setEcuName(getEcuList().get(0));
        ecu1Param5.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(4));
        ecu1Param5.setEcuParameterValue(String.valueOf(2F));
        ecu1Param5.setEcuParameterUnit("ohms");
        ecu1Param5.setEcuParameterMinValue(1.2F);
        ecu1Param5.setGetEcuParameterMaxValue(2.3F);
        ecuParameterValues.add(ecu1Param5);

        EcuParameterDto ecu1Param6 = new EcuParameterDto();
        ecu1Param6.setEcuName(getEcuList().get(0));
        ecu1Param6.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(5));
        ecu1Param6.setEcuParameterValue(String.valueOf(2000F));
        ecu1Param6.setEcuParameterUnit("rpm");
        ecu1Param6.setEcuParameterMinValue(0F);
        ecu1Param6.setGetEcuParameterMaxValue(0F);
        ecuParameterValues.add(ecu1Param6);

        EcuParameterDto ecu1Param7 = new EcuParameterDto();
        ecu1Param7.setEcuName(getEcuList().get(0));
        ecu1Param7.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(6));
        ecu1Param7.setEcuParameterValue(BOOTLOADER_VERSION_NUMBER);
        ecuParameterValues.add(ecu1Param7);

        EcuParameterDto ecu1Param8 = new EcuParameterDto();
        ecu1Param8.setEcuName(getEcuList().get(0));
        ecu1Param8.setEcuParameter(getEcuParametersMap().get(getEcuList().get(0)).get(7));
        ecu1Param8.setEcuParameterValue(ECU_MANUFACTURING_DATE);
        ecuParameterValues.add(ecu1Param8);

        EcuParameterDto ecu2Param1 = new EcuParameterDto();
        ecu2Param1.setEcuName(getEcuList().get(1));
        ecu2Param1.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(0));
        ecu2Param1.setEcuParameterValue(VIN_NUMBER);
        ecuParameterValues.add(ecu2Param1);

        EcuParameterDto ecu2Param2 = new EcuParameterDto();
        ecu2Param2.setEcuName(getEcuList().get(1));
        ecu2Param2.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(1));
        ecu2Param2.setEcuParameterValue("12398774IU");
        ecuParameterValues.add(ecu2Param2);

        EcuParameterDto ecu2Param3 = new EcuParameterDto();
        ecu2Param3.setEcuName(getEcuList().get(1));
        ecu2Param3.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(2));
        ecu2Param3.setEcuParameterValue("2436707ILK");
        ecuParameterValues.add(ecu2Param3);

        EcuParameterDto ecu2Param4 = new EcuParameterDto();
        ecu2Param4.setEcuName(getEcuList().get(1));
        ecu2Param4.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(3));
        ecu2Param4.setEcuParameterValue(String.valueOf(13.6F));
        ecu2Param4.setEcuParameterUnit(VOLTS);
        ecu2Param4.setEcuParameterMinValue(11F);
        ecu2Param4.setGetEcuParameterMaxValue(14.5F);
        ecuParameterValues.add(ecu2Param4);

        EcuParameterDto ecu2Param5 = new EcuParameterDto();
        ecu2Param5.setEcuName(getEcuList().get(1));
        ecu2Param5.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(4));
        ecu2Param5.setEcuParameterValue(BOOTLOADER_VERSION_NUMBER);
        ecuParameterValues.add(ecu2Param5);

        EcuParameterDto ecu2Param6 = new EcuParameterDto();
        ecu2Param6.setEcuName(getEcuList().get(1));
        ecu2Param6.setEcuParameter(getEcuParametersMap().get(getEcuList().get(1)).get(5));
        ecu2Param6.setEcuParameterValue(ECU_MANUFACTURING_DATE);
        ecuParameterValues.add(ecu2Param6);

        EcuParameterDto ecu3Param1 = new EcuParameterDto();
        ecu3Param1.setEcuName(getEcuList().get(2));
        ecu3Param1.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(0));
        ecu3Param1.setEcuParameterValue(VIN_NUMBER);
        ecuParameterValues.add(ecu3Param1);

        EcuParameterDto ecu3Param2 = new EcuParameterDto();
        ecu3Param2.setEcuName(getEcuList().get(2));
        ecu3Param2.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(1));
        ecu3Param2.setEcuParameterValue("4092QWERTI");
        ecuParameterValues.add(ecu3Param2);

        EcuParameterDto ecu3Param3 = new EcuParameterDto();
        ecu3Param3.setEcuName(getEcuList().get(2));
        ecu3Param3.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(2));
        ecu3Param3.setEcuParameterValue("398309367H");
        ecuParameterValues.add(ecu3Param3);

        EcuParameterDto ecu3Param4 = new EcuParameterDto();
        ecu3Param4.setEcuName(getEcuList().get(2));
        ecu3Param4.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(3));
        ecu3Param4.setEcuParameterValue(String.valueOf(26335F));
        ecu3Param4.setEcuParameterUnit("km");
        ecu3Param4.setEcuParameterMinValue(0F);
        ecu3Param4.setGetEcuParameterMaxValue(0F);
        ecuParameterValues.add(ecu3Param4);

        EcuParameterDto ecu3Param5 = new EcuParameterDto();
        ecu3Param5.setEcuName(getEcuList().get(2));
        ecu3Param5.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(4));
        ecu3Param5.setEcuParameterValue(BOOTLOADER_VERSION_NUMBER);
        ecuParameterValues.add(ecu3Param5);

        EcuParameterDto ecu3Param6 = new EcuParameterDto();
        ecu3Param6.setEcuName(getEcuList().get(2));
        ecu3Param6.setEcuParameter(getEcuParametersMap().get(getEcuList().get(2)).get(5));
        ecu3Param6.setEcuParameterValue(ECU_MANUFACTURING_DATE);
        ecuParameterValues.add(ecu3Param6);

        EcuParameterDto ecu4Param1 = new EcuParameterDto();
        ecu4Param1.setEcuName(getEcuList().get(3));
        ecu4Param1.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(0));
        ecu4Param1.setEcuParameterValue(VIN_NUMBER);
        ecuParameterValues.add(ecu4Param1);

        EcuParameterDto ecu4Param2 = new EcuParameterDto();
        ecu4Param2.setEcuName(getEcuList().get(3));
        ecu4Param2.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(1));
        ecu4Param2.setEcuParameterValue("23840211IR");
        ecuParameterValues.add(ecu4Param2);

        EcuParameterDto ecu4Param3 = new EcuParameterDto();
        ecu4Param3.setEcuName(getEcuList().get(3));
        ecu4Param3.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(2));
        ecu4Param3.setEcuParameterValue("398309367H");
        ecuParameterValues.add(ecu4Param3);

        EcuParameterDto ecu4Param4 = new EcuParameterDto();
        ecu4Param4.setEcuName(getEcuList().get(3));
        ecu4Param4.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(3));
        ecu4Param4.setEcuParameterValue(String.valueOf(40F));
        ecu4Param4.setEcuParameterUnit("km/h");
        ecu4Param4.setEcuParameterMinValue(0F);
        ecu4Param4.setGetEcuParameterMaxValue(0F);
        ecuParameterValues.add(ecu4Param4);

        EcuParameterDto ecu4Param5 = new EcuParameterDto();
        ecu4Param5.setEcuName(getEcuList().get(3));
        ecu4Param5.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(4));
        ecu4Param5.setEcuParameterValue(String.valueOf(10F));
        ecu4Param5.setEcuParameterUnit(DEG_C);
        ecu4Param5.setEcuParameterMinValue(2F);
        ecu4Param5.setGetEcuParameterMaxValue(180F);
        ecuParameterValues.add(ecu4Param5);

        EcuParameterDto ecu4Param6 = new EcuParameterDto();
        ecu4Param6.setEcuName(getEcuList().get(3));
        ecu4Param6.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(5));
        ecu4Param6.setEcuParameterValue(BOOTLOADER_VERSION_NUMBER);
        ecuParameterValues.add(ecu4Param6);

        EcuParameterDto ecu4Param7 = new EcuParameterDto();
        ecu4Param7.setEcuName(getEcuList().get(3));
        ecu4Param7.setEcuParameter(getEcuParametersMap().get(getEcuList().get(3)).get(6));
        ecu4Param7.setEcuParameterValue(ECU_MANUFACTURING_DATE);
        ecuParameterValues.add(ecu4Param7);

        EcuParameterDto ecu5Param1 = new EcuParameterDto();
        ecu5Param1.setEcuName(getEcuList().get(4));
        ecu5Param1.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(0));
        ecu5Param1.setEcuParameterValue(VIN_NUMBER);
        ecuParameterValues.add(ecu5Param1);

        EcuParameterDto ecu5Param2 = new EcuParameterDto();
        ecu5Param2.setEcuName(getEcuList().get(4));
        ecu5Param2.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(1));
        ecu5Param2.setEcuParameterValue("223ULM20AL");
        ecuParameterValues.add(ecu5Param2);

        EcuParameterDto ecu5Param3 = new EcuParameterDto();
        ecu5Param3.setEcuName(getEcuList().get(4));
        ecu5Param3.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(2));
        ecu5Param3.setEcuParameterValue("3378744YKA");
        ecuParameterValues.add(ecu5Param3);

        EcuParameterDto ecu5Param4 = new EcuParameterDto();
        ecu5Param4.setEcuName(getEcuList().get(4));
        ecu5Param4.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(3));
        ecu5Param4.setEcuParameterValue(String.valueOf(15F));
        ecu5Param4.setEcuParameterUnit("bar");
        ecu5Param4.setEcuParameterMinValue(10F);
        ecu5Param4.setGetEcuParameterMaxValue(40F);
        ecuParameterValues.add(ecu5Param4);

        EcuParameterDto ecu5Param5 = new EcuParameterDto();
        ecu5Param5.setEcuName(getEcuList().get(4));
        ecu5Param5.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(4));
        ecu5Param5.setEcuParameterValue(String.valueOf(6F));
        ecu5Param5.setEcuParameterUnit("bar");
        ecu5Param5.setEcuParameterMinValue(5F);
        ecu5Param5.setGetEcuParameterMaxValue(10F);
        ecuParameterValues.add(ecu5Param5);

        EcuParameterDto ecu5Param6 = new EcuParameterDto();
        ecu5Param6.setEcuName(getEcuList().get(4));
        ecu5Param6.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(5));
        ecu5Param6.setEcuParameterValue(BOOTLOADER_VERSION_NUMBER);
        ecuParameterValues.add(ecu5Param6);

        EcuParameterDto ecu5Param7 = new EcuParameterDto();
        ecu5Param7.setEcuName(getEcuList().get(4));
        ecu5Param7.setEcuParameter(getEcuParametersMap().get(getEcuList().get(4)).get(6));
        ecu5Param7.setEcuParameterValue(ECU_MANUFACTURING_DATE);
        ecuParameterValues.add(ecu5Param7);
    }

    public static List<EcuParameterDto> getParameterData() {
        setParameterData();
        return ecuParameterValues;
    }

    public static void setEcuStatusMap() {
        ecuStatusMap = new HashMap<>();

        List<String> engineStatus = new ArrayList<>();
        engineStatus.add("Accelerator Pedal Position");
        engineStatus.add("Clutch Pedal Position");
        engineStatus.add("Ignition Switch Status");
        engineStatus.add("Turbocharger Solenoid Valve");
        ecuStatusMap.put(getEcuList().get(0), engineStatus);

        List<String> bcmStatus = new ArrayList<>();
        bcmStatus.add("Central Door Lock Switch");
        bcmStatus.add("Door Switch Driver Door");
        bcmStatus.add("Door Switch Passenger Door");
        bcmStatus.add("Door Switch Rear Left Door");
        bcmStatus.add("Door Switch Rear Right Door");
        ecuStatusMap.put(getEcuList().get(1), bcmStatus);

        List<String> clusterStatus = new ArrayList<>();
        clusterStatus.add("Fuel Level Indicator");
        clusterStatus.add("Gear Level Switch");
        clusterStatus.add(getEcuStatusMap().get(getEcuList().get(0)).get(2));
        ecuStatusMap.put(getEcuList().get(2), clusterStatus);

        List<String> absStatus = new ArrayList<>();
        absStatus.add("Brake Caliper");
        absStatus.add("Brake Pedal Position");
        absStatus.add("Brake Booster");
        ecuStatusMap.put(getEcuList().get(3), absStatus);

        List<String> acuStatus = new ArrayList<>();
        acuStatus.add("Airbag Cut Off Switch");
        acuStatus.add("Driver Seat Belt Status");
        acuStatus.add("Passenger Seat Belt Status");
        acuStatus.add("Rear Passenger Seat Belt Status");
        ecuStatusMap.put(getEcuList().get(4), acuStatus);
    }

    public static Map<String, List<String>> getEcuStatusMap() {
        return ecuStatusMap;
    }

    public static void setStatusData() {
        setEcuStatusMap();
        ecuStatusValues = new ArrayList<>();

        EcuStatusParamDto ecu1Status1 = new EcuStatusParamDto();
        ecu1Status1.setEcuName(getEcuList().get(0));
        ecu1Status1.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(0)).get(0));
        ecu1Status1.setEcuParamState(PRESSED);
        ecuStatusValues.add(ecu1Status1);

        EcuStatusParamDto ecu1Status2 = new EcuStatusParamDto();
        ecu1Status2.setEcuName(getEcuList().get(0));
        ecu1Status2.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(0)).get(1));
        ecu1Status2.setEcuParamState(PRESSED);
        ecuStatusValues.add(ecu1Status2);

        EcuStatusParamDto ecu1Status3 = new EcuStatusParamDto();
        ecu1Status3.setEcuName(getEcuList().get(0));
        ecu1Status3.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(0)).get(2));
        ecu1Status3.setEcuParamState(ON);
        ecuStatusValues.add(ecu1Status3);

        EcuStatusParamDto ecu1Status4 = new EcuStatusParamDto();
        ecu1Status4.setEcuName(getEcuList().get(0));
        ecu1Status4.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(0)).get(3));
        ecu1Status4.setEcuParamState("Activated");
        ecuStatusValues.add(ecu1Status4);

        EcuStatusParamDto ecu2Status1 = new EcuStatusParamDto();
        ecu2Status1.setEcuName(getEcuList().get(1));
        ecu2Status1.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(1)).get(0));
        ecu2Status1.setEcuParamState("Locked");
        ecuStatusValues.add(ecu2Status1);

        EcuStatusParamDto ecu2Status2 = new EcuStatusParamDto();
        ecu2Status2.setEcuName(getEcuList().get(1));
        ecu2Status2.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(1)).get(1));
        ecu2Status2.setEcuParamState(CLOSED);
        ecuStatusValues.add(ecu2Status2);

        EcuStatusParamDto ecu2Status3 = new EcuStatusParamDto();
        ecu2Status3.setEcuName(getEcuList().get(1));
        ecu2Status3.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(1)).get(2));
        ecu2Status3.setEcuParamState(CLOSED);
        ecuStatusValues.add(ecu2Status3);

        EcuStatusParamDto ecu2Status4 = new EcuStatusParamDto();
        ecu2Status4.setEcuName(getEcuList().get(1));
        ecu2Status4.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(1)).get(3));
        ecu2Status4.setEcuParamState(CLOSED);
        ecuStatusValues.add(ecu2Status4);

        EcuStatusParamDto ecu2Status5 = new EcuStatusParamDto();
        ecu2Status5.setEcuName(getEcuList().get(1));
        ecu2Status5.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(1)).get(4));
        ecu2Status5.setEcuParamState(CLOSED);
        ecuStatusValues.add(ecu2Status5);

        EcuStatusParamDto ecu3Status1 = new EcuStatusParamDto();
        ecu3Status1.setEcuName(getEcuList().get(2));
        ecu3Status1.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(2)).get(0));
        ecu3Status1.setEcuParamState("50 %");
        ecuStatusValues.add(ecu3Status1);

        EcuStatusParamDto ecu3Status2 = new EcuStatusParamDto();
        ecu3Status2.setEcuName(getEcuList().get(2));
        ecu3Status2.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(2)).get(1));
        ecu3Status2.setEcuParamState("Neutral");
        ecuStatusValues.add(ecu3Status2);

        EcuStatusParamDto ecu3Status3 = new EcuStatusParamDto();
        ecu3Status3.setEcuName(getEcuList().get(2));
        ecu3Status3.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(2)).get(2));
        ecu3Status3.setEcuParamState(ON);
        ecuStatusValues.add(ecu3Status3);

        EcuStatusParamDto ecu4Status1 = new EcuStatusParamDto();
        ecu4Status1.setEcuName(getEcuList().get(3));
        ecu4Status1.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(3)).get(0));
        ecu4Status1.setEcuParamState(OPEN);
        ecuStatusValues.add(ecu4Status1);

        EcuStatusParamDto ecu4Status2 = new EcuStatusParamDto();
        ecu4Status2.setEcuName(getEcuList().get(3));
        ecu4Status2.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(3)).get(1));
        ecu4Status2.setEcuParamState(PRESSED);
        ecuStatusValues.add(ecu4Status2);

        EcuStatusParamDto ecu4Status3 = new EcuStatusParamDto();
        ecu4Status3.setEcuName(getEcuList().get(3));
        ecu4Status3.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(3)).get(2));
        ecu4Status3.setEcuParamState(ACTIVE);
        ecuStatusValues.add(ecu4Status3);

        EcuStatusParamDto ecu5Status1 = new EcuStatusParamDto();
        ecu5Status1.setEcuName(getEcuList().get(4));
        ecu5Status1.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(4)).get(0));
        ecu5Status1.setEcuParamState(ON);
        ecuStatusValues.add(ecu5Status1);

        EcuStatusParamDto ecu5Status2 = new EcuStatusParamDto();
        ecu5Status2.setEcuName(getEcuList().get(4));
        ecu5Status2.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(4)).get(1));
        ecu5Status2.setEcuParamState(UNLOCK);
        ecuStatusValues.add(ecu5Status2);

        EcuStatusParamDto ecu5Status3 = new EcuStatusParamDto();
        ecu5Status3.setEcuName(getEcuList().get(4));
        ecu5Status3.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(4)).get(2));
        ecu5Status3.setEcuParamState(UNLOCK);
        ecuStatusValues.add(ecu5Status3);

        EcuStatusParamDto ecu5Status4 = new EcuStatusParamDto();
        ecu5Status4.setEcuName(getEcuList().get(4));
        ecu5Status4.setEcuStatusParam(getEcuStatusMap().get(getEcuList().get(4)).get(3));
        ecu5Status4.setEcuParamState(UNLOCK);
        ecuStatusValues.add(ecu5Status4);
    }

    public static List<EcuStatusParamDto> getStatusData() {
        setStatusData();
        return ecuStatusValues;
    }

    public static void setWriteStatusData() {
        setEcuStatusMap();
        ecuStatusDtoValues = new ArrayList<>();

        WriteEcuStatusDto writeEcu1Status1Dto = new WriteEcuStatusDto();
        writeEcu1Status1Dto.setEcuName(getEcuList().get(0));
        List<String> ecu1StatusActionOptions1 = new ArrayList<>();
        ecu1StatusActionOptions1.add("2 dB");
        ecu1StatusActionOptions1.add("5 dB");
        ecu1StatusActionOptions1.add("7 dB");
        writeEcu1Status1Dto.setEcuStatusActionOptions(ecu1StatusActionOptions1);
        List<String> ecu1StatusActions1 = new ArrayList<>();
        ecu1StatusActions1.add(LEVEL_1);
        ecu1StatusActions1.add(LEVEL_2);
        ecu1StatusActions1.add(LEVEL_3);
        writeEcu1Status1Dto.setEcuStatusActionList(ecu1StatusActions1);
        writeEcu1Status1Dto.setEcuStatus(getEcuStatusMap().get(getEcuList().get(0)).get(0));
        ecuStatusDtoValues.add(writeEcu1Status1Dto);

        WriteEcuStatusDto writeEcu2Status1Dto = new WriteEcuStatusDto();
        writeEcu2Status1Dto.setEcuName(getEcuList().get(1));
        List<String> ecu2StatusActionOptions1 = new ArrayList<>();
        ecu2StatusActionOptions1.add("2 dB");
        ecu2StatusActionOptions1.add("5 dB");
        ecu2StatusActionOptions1.add("7 dB");
        writeEcu2Status1Dto.setEcuStatusActionOptions(ecu2StatusActionOptions1);
        List<String> ecu2StatusActions1 = new ArrayList<>();
        ecu2StatusActions1.add(LEVEL_1);
        ecu2StatusActions1.add(LEVEL_2);
        ecu2StatusActions1.add(LEVEL_3);
        writeEcu2Status1Dto.setEcuStatusActionList(ecu2StatusActions1);
        writeEcu2Status1Dto.setEcuStatus(getEcuStatusMap().get(getEcuList().get(0)).get(0));
        ecuStatusDtoValues.add(writeEcu2Status1Dto);
    }

    public static List<WriteEcuStatusDto> getWriteStatusData() {
        setWriteStatusData();
        return ecuStatusDtoValues;
    }

    public static void setEcuDtcData() {
        ecuDtcValues = new ArrayList<>();

        EcuDtcDto ecu1Dtc1 = new EcuDtcDto();
        ecu1Dtc1.setEcuName(getEcuList().get(0));
        ecu1Dtc1.setDtcState("No DTC found");
        ecuDtcValues.add(ecu1Dtc1);

        EcuDtcDto ecu2Dtc1 = new EcuDtcDto();
        ecu2Dtc1.setEcuName(getEcuList().get(1));
        ecu2Dtc1.setDiagnosticTroubleCode("B219F-12");
        ecu2Dtc1.setDescription("CL30s output short to battery");
        ecu2Dtc1.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu2Dtc1);

        EcuDtcDto ecu2Dtc2 = new EcuDtcDto();
        ecu2Dtc2.setEcuName(getEcuList().get(1));
        ecu2Dtc2.setDiagnosticTroubleCode("B21A0-16");
        ecu2Dtc2.setDescription("Battery voltage abnormal low");
        ecu2Dtc2.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu2Dtc2);


        EcuDtcDto ecu2Dtc3 = new EcuDtcDto();
        ecu2Dtc3.setEcuName(getEcuList().get(1));
        ecu2Dtc3.setDiagnosticTroubleCode("B21AB-15");
        ecu2Dtc3.setDescription("Horn control relay output circuit open load or short to battery");
        ecu2Dtc3.setDtcState(MEMORISED_FAULT);
        ecuDtcValues.add(ecu2Dtc3);

        EcuDtcDto ecu2Dtc4 = new EcuDtcDto();
        ecu2Dtc4.setEcuName(getEcuList().get(1));
        ecu2Dtc4.setDiagnosticTroubleCode("B21AC-12");
        ecu2Dtc4.setDescription("Front washer control relay output circuit short to battery");
        ecu2Dtc4.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu2Dtc4);

        EcuDtcDto ecu2Dtc5 = new EcuDtcDto();
        ecu2Dtc5.setEcuName(getEcuList().get(1));
        ecu2Dtc5.setDiagnosticTroubleCode("B21AE-11");
        ecu2Dtc5.setDescription("Hazard LED circuit short to battery");
        ecu2Dtc5.setDtcState(PENDING_FAULT);
        ecuDtcValues.add(ecu2Dtc5);

        EcuDtcDto ecu3Dtc4 = new EcuDtcDto();
        ecu3Dtc4.setEcuName(getEcuList().get(2));
        ecu3Dtc4.setDiagnosticTroubleCode("U1A3E-87");
        ecu3Dtc4.setDescription("ICC lost with BCM");
        ecu3Dtc4.setDtcState(PENDING_FAULT);
        ecuDtcValues.add(ecu3Dtc4);

        EcuDtcDto ecu3Dtc5 = new EcuDtcDto();
        ecu3Dtc5.setEcuName(getEcuList().get(2));
        ecu3Dtc5.setDiagnosticTroubleCode("U1A3F-87");
        ecu3Dtc5.setDescription("ICC lost with BMS");
        ecu3Dtc5.setDtcState(PENDING_FAULT);
        ecuDtcValues.add(ecu3Dtc5);

        EcuDtcDto ecu3Dtc6 = new EcuDtcDto();
        ecu3Dtc6.setEcuName(getEcuList().get(2));
        ecu3Dtc6.setDiagnosticTroubleCode("P19F2-96");
        ecu3Dtc6.setDescription("ICC WIFI module error");
        ecu3Dtc6.setDtcState(MEMORISED_FAULT);
        ecuDtcValues.add(ecu3Dtc6);

        EcuDtcDto ecu3Dtc7 = new EcuDtcDto();
        ecu3Dtc7.setEcuName(getEcuList().get(2));
        ecu3Dtc7.setDiagnosticTroubleCode("P19F3-96");
        ecu3Dtc7.setDescription("ICC BT module error");
        ecu3Dtc7.setDtcState(MEMORISED_FAULT);
        ecuDtcValues.add(ecu3Dtc7);

        EcuDtcDto ecu3Dtc8 = new EcuDtcDto();
        ecu3Dtc8.setEcuName(getEcuList().get(2));
        ecu3Dtc8.setDiagnosticTroubleCode("P19F1-07");
        ecu3Dtc8.setDescription("ICC DAB module error");
        ecu3Dtc8.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu3Dtc8);

        EcuDtcDto ecu4Dtc1 = new EcuDtcDto();
        ecu4Dtc1.setEcuName(getEcuList().get(3));
        ecu4Dtc1.setDiagnosticTroubleCode("C1100-11");
        ecu4Dtc1.setDescription("Wheel speed sensor FL short circuit to ground");
        ecu4Dtc1.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu4Dtc1);

        EcuDtcDto ecu4Dtc3 = new EcuDtcDto();
        ecu4Dtc3.setEcuName(getEcuList().get(3));
        ecu4Dtc3.setDiagnosticTroubleCode("C3202-11");
        ecu4Dtc3.setDescription("Wheel speed sensor RL short circuit to ground");
        ecu4Dtc3.setDtcState(MEMORISED_FAULT);
        ecuDtcValues.add(ecu4Dtc3);

        EcuDtcDto ecu4Dtc4 = new EcuDtcDto();
        ecu4Dtc4.setEcuName(getEcuList().get(3));
        ecu4Dtc4.setDiagnosticTroubleCode("C3202-13");
        ecu4Dtc4.setDescription("Wheel speed sensor RL open circuit");
        ecu4Dtc4.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu4Dtc4);

        EcuDtcDto ecu4Dtc5 = new EcuDtcDto();
        ecu4Dtc5.setEcuName(getEcuList().get(3));
        ecu4Dtc5.setDiagnosticTroubleCode("C3567-29");
        ecu4Dtc5.setDescription("Brake pad temperature too high");
        ecu4Dtc5.setDtcState(PENDING_FAULT);
        ecuDtcValues.add(ecu4Dtc5);

        EcuDtcDto ecu4Dtc9 = new EcuDtcDto();
        ecu4Dtc9.setEcuName(getEcuList().get(3));
        ecu4Dtc9.setDiagnosticTroubleCode("C0238-97");
        ecu4Dtc9.setDescription("Wheel speed mismatch");
        ecu4Dtc9.setDtcState(ACTIVE_FAULT);
        ecuDtcValues.add(ecu4Dtc9);

        EcuDtcDto ecu5Dtc1 = new EcuDtcDto();
        ecu5Dtc1.setEcuName(getEcuList().get(4));
        ecu5Dtc1.setDtcState("No DTC found");
        ecuDtcValues.add(ecu5Dtc1);
    }

    public static List<EcuDtcDto> getEcuDtcData() {
        setEcuDtcData();
        return ecuDtcValues;
    }

    public static void setEcuPositionData() {
        ecuPositionData = new ArrayList<>();

        EcuPositionDto ecu1Position = new EcuPositionDto();
        ecu1Position.setId(1);
        ecu1Position.setEcuName(getEcuList().get(0));
        ecu1Position.setEcuStatus(ACTIVE);
        ecu1Position.setDirectionX(10F);
        ecu1Position.setDirectionY(64F);
        ecu1Position.setDtdDirectionX2D(64F);
        ecu1Position.setDtdDirectionY2D(11F);
        ecu1Position.setDirectionX3d(50F);
        ecu1Position.setDirectionY3d(63F);
        ecuPositionData.add(ecu1Position);

        EcuPositionDto ecu2Position = new EcuPositionDto();
        ecu2Position.setId(2);
        ecu2Position.setEcuName(getEcuList().get(3));
        ecu2Position.setEcuStatus(FAULT);
        ecu2Position.setDirectionX(35F);
        ecu2Position.setDirectionY(67F);
        ecu2Position.setDtdDirectionX2D(57F);
        ecu2Position.setDtdDirectionY2D(20F);
        ecu2Position.setDirectionX3d(42F);
        ecu2Position.setDirectionY3d(55F);
        ecuPositionData.add(ecu2Position);

        EcuPositionDto ecu3Position = new EcuPositionDto();
        ecu3Position.setId(4);
        ecu3Position.setEcuName(getEcuList().get(2));
        ecu3Position.setEcuStatus(FAULT);
        ecu3Position.setDirectionX(21F);
        ecu3Position.setDirectionY(59F);
        ecu3Position.setDtdDirectionX2D(68F);
        ecu3Position.setDtdDirectionY2D(34F);
        ecu3Position.setDirectionX3d(55F);
        ecu3Position.setDirectionY3d(50F);
        ecuPositionData.add(ecu3Position);

        EcuPositionDto ecu4Position = new EcuPositionDto();
        ecu4Position.setId(3);
        ecu4Position.setEcuName(getEcuList().get(1));
        ecu4Position.setEcuStatus(FAULT);
        ecu4Position.setDirectionX(32F);
        ecu4Position.setDirectionY(28F);
        ecu4Position.setDtdDirectionX2D(31F);
        ecu4Position.setDtdDirectionY2D(32F);
        ecu4Position.setDirectionX3d(32F);
        ecu4Position.setDirectionY3d(42F);
        ecuPositionData.add(ecu4Position);

        EcuPositionDto ecu5Position = new EcuPositionDto();
        ecu5Position.setId(5);
        ecu5Position.setEcuName(getEcuList().get(4));
        ecu5Position.setEcuStatus(ACTIVE);
        ecu5Position.setDirectionX(44F);
        ecu5Position.setDirectionY(47F);
        ecu5Position.setDtdDirectionX2D(44F);
        ecu5Position.setDtdDirectionY2D(47F);
        ecu5Position.setDirectionX3d(55F);
        ecu5Position.setDirectionY3d(30F);
        ecuPositionData.add(ecu5Position);
    }

    public static List<EcuPositionDto> getEcuPositionData() {
        setEcuPositionData();
        return ecuPositionData;
    }

    public static Map<String, Object> getImageData(String url, float width, float height) {
        HashMap<String, Object> imageData = new HashMap<>();
        imageData.put("url", url);
        imageData.put("width", width);
        imageData.put("height", height);
        return imageData;
    }

    public static void setEcuInfoData() {
        ecuInfoDto = new ArrayList<>();

        EcuInfoDto ecu1Info = new EcuInfoDto();
        ecu1Info.setEcuName(getEcuList().get(0));
        ecu1Info.setEcuDescription("Engine control unit");
        ecu1Info.setEcuHardwarePartNo("FM298033H005C");
        ecu1Info.setEcuSoftwarePartNo("FM298033S005G");
        ecu1Info.setSoftwareVersion("1.0.2");
        ecu1Info.setImgUrl("ENGINE_ECU.png");
        ecu1Info.setImgWidth(0F);
        ecu1Info.setImgHeight(0F);
        ecuInfoDto.add(ecu1Info);

        EcuInfoDto ecu2Info = new EcuInfoDto();
        ecu2Info.setEcuName(getEcuList().get(1));
        ecu2Info.setEcuDescription("Body control module");
        ecu2Info.setEcuHardwarePartNo("FM298033H003C");
        ecu2Info.setEcuSoftwarePartNo("FM298033S003G");
        ecu2Info.setSoftwareVersion("1.4.5");
        ecu2Info.setImgUrl("BCM_ECU.png");
        ecu2Info.setImgWidth(0F);
        ecu2Info.setImgHeight(0F);
        ecuInfoDto.add(ecu2Info);

        EcuInfoDto ecu3Info = new EcuInfoDto();
        ecu3Info.setEcuName(getEcuList().get(2));
        ecu3Info.setEcuDescription("Combination meter/Cluster unit");
        ecu3Info.setEcuHardwarePartNo("FM298033H002C");
        ecu3Info.setEcuSoftwarePartNo("FM298033S002G");
        ecu3Info.setSoftwareVersion("3.0.1");
        ecu3Info.setImgUrl("CLUSTER_ECU.png");
        ecu3Info.setImgWidth(0F);
        ecu3Info.setImgHeight(0F);
        ecuInfoDto.add(ecu3Info);

        EcuInfoDto ecu4Info = new EcuInfoDto();
        ecu4Info.setEcuName(getEcuList().get(3));
        ecu4Info.setEcuDescription("Anti-lock braking system");
        ecu4Info.setEcuHardwarePartNo("FM298033H004C");
        ecu4Info.setEcuSoftwarePartNo("FM298033S004G");
        ecu4Info.setSoftwareVersion("1.1.4");
        ecu4Info.setImgUrl("ABS_ECU.png");
        ecu4Info.setImgWidth(0F);
        ecu4Info.setImgHeight(0F);
        ecuInfoDto.add(ecu4Info);

        EcuInfoDto ecu5Info = new EcuInfoDto();
        ecu5Info.setEcuName(getEcuList().get(4));
        ecu5Info.setEcuDescription("Airbag control unit");
        ecu5Info.setEcuHardwarePartNo("FM298033H001C");
        ecu5Info.setEcuSoftwarePartNo("FM298033S001G");
        ecu5Info.setSoftwareVersion("2.0.1");
        ecu5Info.setImgUrl("ACU_ECU.png");
        ecu5Info.setImgWidth(0F);
        ecu5Info.setImgHeight(0F);
        ecuInfoDto.add(ecu5Info);
    }

    public static List<EcuInfoDto> getEcuInfoData() {
        setEcuInfoData();
        return ecuInfoDto;
    }


    public static void setEcuActuatorTestList(String ecuName) {
        ecuActuatorTestList = new HashMap<>();

        if (Objects.equals(ecuName, getEcuList().get(0))) {
            List<String> ecu1ActuatorTest = new ArrayList<>();
            ecu1ActuatorTest.add("Turbocharger Solenoid Valve");
            ecu1ActuatorTest.add("Throttle Valve Actuator");
            ecu1ActuatorTest.add("Fuel Pump Relay");
            ecu1ActuatorTest.add("Starter Motor Actuator");
            ecu1ActuatorTest.add("Engine Cooling Fan");
            ecuActuatorTestList.put(getEcuList().get(0), ecu1ActuatorTest);
        }

        if (Objects.equals(ecuName, getEcuList().get(1))) {
            List<String> ecu2ActuatorTest = new ArrayList<>();
            ecu2ActuatorTest.add("Front Wiper");
            ecu2ActuatorTest.add("Rear Wiper");
            ecu2ActuatorTest.add("Horn");
            ecu2ActuatorTest.add("Headlamp LH Low Beam");
            ecu2ActuatorTest.add("Headlamp RH Low Beam");
            ecu2ActuatorTest.add("Headlamp LH High Beam");
            ecu2ActuatorTest.add("Headlamp RH High Beam");
            ecuActuatorTestList.put(getEcuList().get(1), ecu2ActuatorTest);
        }

        if (Objects.equals(ecuName, getEcuList().get(2))) {
            List<String> ecu3ActuatorTest = new ArrayList<>();
            ecu3ActuatorTest.add("Display Illumination");
            ecu3ActuatorTest.add("Warning Buzzer");
            ecuActuatorTestList.put(getEcuList().get(2), ecu3ActuatorTest);
        }

        if (Objects.equals(ecuName, getEcuList().get(3))) {
            List<String> ecu4ActuatorTest = new ArrayList<>();
            ecu4ActuatorTest.add("Brake Caliper");
            ecu4ActuatorTest.add("Actuator Solenoid");
            ecuActuatorTestList.put(getEcuList().get(3), ecu4ActuatorTest);
        }

    }

    public static Map<String, List<String>> getEcuActuatorTestList(String ecuName) {
        setEcuActuatorTestList(ecuName);
        return ecuActuatorTestList;
    }

    public static void setEcuActuatorTestValues() {
        actuatorTestValues = new ArrayList<>();

        ActuatorTestDto ecu1actuatorTest1 = new ActuatorTestDto();
        ecu1actuatorTest1.setEcuName(getEcuList().get(0));
        ecu1actuatorTest1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(0));
        List<String> ecu1Action1 = new ArrayList<>();
        ecu1Action1.add(DEACTIVATE);
        ecu1Action1.add(ACTIVATE);
        ecu1actuatorTest1.setAction(ecu1Action1);
        List<String> ecu1Condition1 = new ArrayList<>();
        ecu1Condition1.add(IGNITION_ON);
        ecu1Condition1.add(ENGINE_OFF);
        ecu1Condition1.add(PARKING_BRAKE_ENGAGED);
        ecu1actuatorTest1.setCondition(ecu1Condition1);
        List<String> ecu1TestDescription1 = new ArrayList<>();
        ecu1TestDescription1.add("The turbocharger's solenoid valve will be activated and then deactivated.");
        ecu1TestDescription1.add("This test can be used to check if the solenoid is working correctly or not.");
        ecu1actuatorTest1.setActuatorTestDescription(ecu1TestDescription1);
        ecu1actuatorTest1.setActuatorTestTimeDuration("5 minutes for whole test");
        List<String> ecu1ActivationMethod1 = new ArrayList<>();
        ecu1ActivationMethod1.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu1ActivationMethod1.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu1ActivationMethod1.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu1ActivationMethod1.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu1actuatorTest1.setActuatorActivationMethod(ecu1ActivationMethod1);
        actuatorTestValues.add(ecu1actuatorTest1);

        ActuatorTestDto ecu1actuatorTest2 = new ActuatorTestDto();
        ecu1actuatorTest2.setEcuName(getEcuList().get(0));
        ecu1actuatorTest2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        List<String> ecu1Action2 = new ArrayList<>();
        ecu1Action2.add("0%");
        ecu1Action2.add("20%");
        ecu1Action2.add("40%");
        ecu1Action2.add("60%");
        ecu1Action2.add("80%");
        ecu1Action2.add("Idle Speed");
        ecu1actuatorTest2.setAction(ecu1Action2);
        List<String> ecu1Condition2 = new ArrayList<>();
        ecu1Condition2.add(IGNITION_ON);
        ecu1Condition2.add(ENGINE_OFF);
        ecu1Condition2.add(PARKING_BRAKE_ENGAGED);
        ecu1actuatorTest2.setCondition(ecu1Condition2);
        List<String> ecu1TestDescription2 = new ArrayList<>();
        ecu1TestDescription2.add("The throttle valve is actuated to various levels from 0% to 80%.");
        ecu1TestDescription2.add("The level can be varied during the test continuously to check for proper operation.");
        ecu1actuatorTest2.setActuatorTestDescription(ecu1TestDescription2);
        ecu1actuatorTest2.setActuatorTestTimeDuration("5 minutes for whole test");
        List<String> ecu1ActivationMethod2 = new ArrayList<>();
        ecu1ActivationMethod2.add("Activate and deactivate button for each throttle level.");
        ecu1ActivationMethod2.add("If activate button is clicked for one throttle level then other activate buttons should be disabled until the respective deactivate button is clicked.");
        ecu1actuatorTest2.setActuatorActivationMethod(ecu1ActivationMethod2);
        actuatorTestValues.add(ecu1actuatorTest2);

        ActuatorTestDto ecu1actuatorTest3 = new ActuatorTestDto();
        ecu1actuatorTest3.setEcuName(getEcuList().get(0));
        ecu1actuatorTest3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(2));
        List<String> ecu1Action3 = new ArrayList<>();
        ecu1Action3.add(OFF);
        ecu1Action3.add(ON);
        ecu1actuatorTest3.setAction(ecu1Action3);
        List<String> ecu1Condition3 = new ArrayList<>();
        ecu1Condition3.add(IGNITION_ON);
        ecu1Condition3.add(ENGINE_OFF);
        ecu1Condition3.add(PARKING_BRAKE_ENGAGED);
        ecu1actuatorTest3.setCondition(ecu1Condition3);
        List<String> ecu1TestDescription3 = new ArrayList<>();
        ecu1TestDescription3.add("This test is used to check if the fuel pump's relay is working correctly or not.");
        ecu1TestDescription3.add("The relay is actuated which in turn will switch ON the fuel pump.");
        ecu1TestDescription3.add("This can be confirmed by a buzzing sound which can be heard from the engine bay.");
        ecu1actuatorTest3.setActuatorTestDescription(ecu1TestDescription3);
        ecu1actuatorTest3.setActuatorTestTimeDuration("2 minutes");
        List<String> ecu1ActivationMethod3 = new ArrayList<>();
        ecu1ActivationMethod3.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu1ActivationMethod3.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu1ActivationMethod3.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu1ActivationMethod3.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu1actuatorTest3.setActuatorActivationMethod(ecu1ActivationMethod3);
        actuatorTestValues.add(ecu1actuatorTest3);

        ActuatorTestDto ecu1actuatorTest4 = new ActuatorTestDto();
        ecu1actuatorTest4.setEcuName(getEcuList().get(0));
        ecu1actuatorTest4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(3));
        List<String> ecu1Action4 = new ArrayList<>();
        ecu1Action4.add("Disengage");
        ecu1Action4.add("Engage");
        ecu1actuatorTest4.setAction(ecu1Action4);
        List<String> ecu1Condition4 = new ArrayList<>();
        ecu1Condition4.add("Wheel chocks");
        ecu1Condition4.add(IGNITION_ON);
        ecu1Condition4.add(PARKING_BRAKE_ENGAGED);
        ecu1actuatorTest4.setCondition(ecu1Condition4);
        List<String> ecu1TestDescription4 = new ArrayList<>();
        ecu1TestDescription4.add("This test is used to check the starter motor actuator is working correctly or not.");
        ecu1TestDescription4.add("The starter motor's actuator solenoid will be activated and then deactivated during this test.");
        ecu1actuatorTest4.setActuatorTestDescription(ecu1TestDescription4);
        ecu1actuatorTest4.setActuatorTestTimeDuration("30 seconds");
        List<String> ecu1ActivationMethod4 = new ArrayList<>();
        ecu1ActivationMethod4.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu1ActivationMethod4.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu1ActivationMethod4.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu1ActivationMethod4.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu1actuatorTest4.setActuatorActivationMethod(ecu1ActivationMethod4);
        actuatorTestValues.add(ecu1actuatorTest4);

        ActuatorTestDto ecu1actuatorTest5 = new ActuatorTestDto();
        ecu1actuatorTest5.setEcuName(getEcuList().get(0));
        ecu1actuatorTest5.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(4));
        List<String> ecu1Action5 = new ArrayList<>();
        ecu1Action5.add(OFF);
        ecu1Action5.add("Low Speed");
        ecu1Action5.add("High Speed");
        ecu1actuatorTest5.setAction(ecu1Action5);
        List<String> ecu1Condition5 = new ArrayList<>();
        ecu1Condition5.add(IGNITION_ON);
        ecu1Condition5.add(ENGINE_OFF);
        ecu1Condition5.add(PARKING_BRAKE_ENGAGED);
        List<String> ecu1TestDescription5 = new ArrayList<>();
        ecu1TestDescription5.add("This test is used to check if the engine cooling fan runs at various speeds to support the radiator in cooling.");
        ecu1TestDescription5.add("The test will switch on the fan and then the user can control the fan in two different speeds.");
        ecu1actuatorTest5.setActuatorTestDescription(ecu1TestDescription5);
        ecu1actuatorTest5.setActuatorTestTimeDuration("1 minute");
        List<String> ecu1ActivationMethod5 = new ArrayList<>();
        ecu1ActivationMethod5.add("Activate and deactivate button for each speed level.");
        ecu1ActivationMethod5.add("If activate button is clicked for one speed level then other activate buttons should be disabled until the respective deactivate button is clicked.");
        ecu1actuatorTest5.setActuatorActivationMethod(ecu1ActivationMethod5);
        ecu1actuatorTest5.setCondition(ecu1Condition5);
        actuatorTestValues.add(ecu1actuatorTest5);

        ActuatorTestDto ecu2ActuatorTest1 = new ActuatorTestDto();
        ecu2ActuatorTest1.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(0));
        List<String> ecu2Action1 = new ArrayList<>();
        ecu2Action1.add(DEACTIVATE);
        ecu2Action1.add(ACTIVATE);
        ecu2ActuatorTest1.setAction(ecu2Action1);
        List<String> ecu2Condition1 = new ArrayList<>();
        ecu2Condition1.add(IGNITION_ON);
        ecu2Condition1.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition1.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition1.add(ENGINE_OFF);
        ecu2ActuatorTest1.setCondition(ecu2Condition1);
        List<String> ecu2TestDescription1 = new ArrayList<>();
        ecu2TestDescription1.add("Front wiper will be activated for 3 wiping cycles and then turned off.");
        ecu2ActuatorTest1.setActuatorTestDescription(ecu2TestDescription1);
        ecu2ActuatorTest1.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod1 = new ArrayList<>();
        ecu2ActivationMethod1.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod1.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod1.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod1.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest1.setActuatorActivationMethod(ecu2ActivationMethod1);
        actuatorTestValues.add(ecu2ActuatorTest1);

        ActuatorTestDto ecu2ActuatorTest2 = new ActuatorTestDto();
        ecu2ActuatorTest2.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(1));
        List<String> ecu2Action2 = new ArrayList<>();
        ecu2Action2.add(DEACTIVATE);
        ecu2Action2.add(ACTIVATE);
        ecu2ActuatorTest2.setAction(ecu2Action2);
        List<String> ecu2Condition2 = new ArrayList<>();
        ecu2Condition2.add(IGNITION_ON);
        ecu2Condition2.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition2.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition2.add(ENGINE_OFF);
        ecu2ActuatorTest2.setCondition(ecu2Condition2);
        List<String> ecu2TestDescription2 = new ArrayList<>();
        ecu2TestDescription2.add("Rear wiper will be activated for 3 wiping cycles and then turned off.");
        ecu2ActuatorTest2.setActuatorTestDescription(ecu2TestDescription2);
        ecu2ActuatorTest2.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod2 = new ArrayList<>();
        ecu2ActivationMethod2.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod2.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod2.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod2.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest2.setActuatorActivationMethod(ecu2ActivationMethod2);
        actuatorTestValues.add(ecu2ActuatorTest2);

        ActuatorTestDto ecu2ActuatorTest3 = new ActuatorTestDto();
        ecu2ActuatorTest3.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(2));
        List<String> ecu2Action3 = new ArrayList<>();
        ecu2Action3.add(OFF);
        ecu2Action3.add("Low");
        ecu2Action3.add("High");
        ecu2ActuatorTest3.setAction(ecu2Action3);
        List<String> ecu2Condition3 = new ArrayList<>();
        ecu2Condition3.add(IGNITION_ON);
        ecu2Condition3.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition3.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition3.add(ENGINE_OFF);
        ecu2ActuatorTest3.setCondition(ecu2Condition3);
        List<String> ecu2TestDescription3 = new ArrayList<>();
        ecu2TestDescription3.add("Horn will be activated and then deactivated");
        ecu2ActuatorTest3.setActuatorTestDescription(ecu2TestDescription3);
        ecu2ActuatorTest3.setActuatorTestTimeDuration("5 seconds");
        List<String> ecu2ActivationMethod3 = new ArrayList<>();
        ecu2ActivationMethod3.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod3.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod3.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod3.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest3.setActuatorActivationMethod(ecu2ActivationMethod3);
        actuatorTestValues.add(ecu2ActuatorTest3);

        ActuatorTestDto ecu2ActuatorTest4 = new ActuatorTestDto();
        ecu2ActuatorTest4.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(3));
        List<String> ecu2Action4 = new ArrayList<>();
        ecu2Action4.add(OFF);
        ecu2Action4.add(ON);
        ecu2ActuatorTest4.setAction(ecu2Action4);
        List<String> ecu2Condition4 = new ArrayList<>();
        ecu2Condition4.add(IGNITION_ON);
        ecu2Condition4.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition4.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition4.add(ENGINE_OFF);
        ecu2ActuatorTest4.setCondition(ecu2Condition4);
        List<String> ecu2TestDescription4 = new ArrayList<>();
        ecu2TestDescription4.add("Passenger (LH) side head lamp low beam will be switched ON and then OFF.");
        ecu2ActuatorTest4.setActuatorTestDescription(ecu2TestDescription4);
        ecu2ActuatorTest4.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod4 = new ArrayList<>();
        ecu2ActivationMethod4.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod4.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod4.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod4.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest4.setActuatorActivationMethod(ecu2ActivationMethod4);
        actuatorTestValues.add(ecu2ActuatorTest4);

        ActuatorTestDto ecu2ActuatorTest5 = new ActuatorTestDto();
        ecu2ActuatorTest5.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest5.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(4));
        List<String> ecu2Action5 = new ArrayList<>();
        ecu2Action5.add(OFF);
        ecu2Action5.add(ON);
        ecu2ActuatorTest5.setAction(ecu2Action5);
        List<String> ecu2Condition5 = new ArrayList<>();
        ecu2Condition5.add(IGNITION_ON);
        ecu2Condition5.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition5.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition5.add(ENGINE_OFF);
        ecu2ActuatorTest5.setCondition(ecu2Condition5);
        List<String> ecu2TestDescription5 = new ArrayList<>();
        ecu2TestDescription5.add("Passenger (RH) side head lamp low beam will be switched ON and then OFF.");
        ecu2ActuatorTest5.setActuatorTestDescription(ecu2TestDescription5);
        ecu2ActuatorTest5.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod5 = new ArrayList<>();
        ecu2ActivationMethod5.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod5.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod5.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod5.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest5.setActuatorActivationMethod(ecu2ActivationMethod5);
        actuatorTestValues.add(ecu2ActuatorTest5);

        ActuatorTestDto ecu2ActuatorTest6 = new ActuatorTestDto();
        ecu2ActuatorTest6.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest6.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(5));
        List<String> ecu2Action6 = new ArrayList<>();
        ecu2Action6.add(OFF);
        ecu2Action6.add(ON);
        ecu2ActuatorTest6.setAction(ecu2Action6);
        List<String> ecu2Condition6 = new ArrayList<>();
        ecu2Condition6.add(IGNITION_ON);
        ecu2Condition6.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition6.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition6.add(ENGINE_OFF);
        ecu2ActuatorTest6.setCondition(ecu2Condition6);
        List<String> ecu2TestDescription6 = new ArrayList<>();
        ecu2TestDescription6.add("Passenger (LH) side head lamp high beam will be switched ON and then OFF.");
        ecu2ActuatorTest6.setActuatorTestDescription(ecu2TestDescription6);
        ecu2ActuatorTest6.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod6 = new ArrayList<>();
        ecu2ActivationMethod6.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod6.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod6.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod6.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest6.setActuatorActivationMethod(ecu2ActivationMethod6);
        actuatorTestValues.add(ecu2ActuatorTest6);

        ActuatorTestDto ecu2ActuatorTest7 = new ActuatorTestDto();
        ecu2ActuatorTest7.setEcuName(getEcuList().get(1));
        ecu2ActuatorTest7.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(6));
        List<String> ecu2Action7 = new ArrayList<>();
        ecu2Action7.add(OFF);
        ecu2Action7.add(ON);
        ecu2ActuatorTest7.setAction(ecu2Action7);
        List<String> ecu2Condition7 = new ArrayList<>();
        ecu2Condition7.add(IGNITION_ON);
        ecu2Condition7.add(PARKING_BRAKE_ENGAGED);
        ecu2Condition7.add(NO_ACCESSORIES_TO_BE_SWITCHED_ON);
        ecu2Condition7.add(ENGINE_OFF);
        ecu2ActuatorTest7.setCondition(ecu2Condition7);
        List<String> ecu2TestDescription7 = new ArrayList<>();
        ecu2TestDescription7.add("Passenger (RH) side head lamp high beam will be switched ON and then OFF.");
        ecu2ActuatorTest7.setActuatorTestDescription(ecu2TestDescription7);
        ecu2ActuatorTest7.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu2ActivationMethod7 = new ArrayList<>();
        ecu2ActivationMethod7.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu2ActivationMethod7.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu2ActivationMethod7.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu2ActivationMethod7.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu2ActuatorTest7.setActuatorActivationMethod(ecu2ActivationMethod7);
        actuatorTestValues.add(ecu2ActuatorTest7);

        ActuatorTestDto ecu3ActuatorTest1 = new ActuatorTestDto();
        ecu3ActuatorTest1.setEcuName(getEcuList().get(2));
        ecu3ActuatorTest1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(0));
        List<String> ecu3Action1 = new ArrayList<>();
        ecu3Action1.add(OFF);
        ecu3Action1.add("Dark");
        ecu3Action1.add("Light");
        ecu3Action1.add("Brighter");
        ecu3ActuatorTest1.setAction(ecu3Action1);
        List<String> ecu3Condition1 = new ArrayList<>();
        ecu3Condition1.add(IGNITION_ON);
        ecu3Condition1.add(PARKING_BRAKE_ENGAGED);
        ecu3Condition1.add(ENGINE_OFF);
        ecu3ActuatorTest1.setCondition(ecu3Condition1);
        List<String> ecu3TestDescription1 = new ArrayList<>();
        ecu3TestDescription1.add("Illumination brightness will be increased or decreased based on the active test selection.");
        ecu3TestDescription1.add("Off ==> active test will be in neutral position");
        ecu3TestDescription1.add("Dark ==> Brightness will be low");
        ecu3TestDescription1.add("Light ==> Brightness will be medium");
        ecu3TestDescription1.add("Brighter ==> Brightness will be High");
        ecu3ActuatorTest1.setActuatorTestDescription(ecu3TestDescription1);
        ecu3ActuatorTest1.setActuatorTestTimeDuration("10 seconds per brightness level");
        List<String> ecu3ActivationMethod1 = new ArrayList<>();
        ecu3ActivationMethod1.add("Activate and deactivate button for each brightness level.");
        ecu3ActivationMethod1.add("If activate button is clicked for one brightness level then other activate buttons should be disabled until the respective deactivate button is clicked.");
        ecu3ActuatorTest1.setActuatorActivationMethod(ecu3ActivationMethod1);
        actuatorTestValues.add(ecu3ActuatorTest1);

        ActuatorTestDto ecu3ActuatorTest2 = new ActuatorTestDto();
        ecu3ActuatorTest2.setEcuName(getEcuList().get(2));
        ecu3ActuatorTest2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(1));
        List<String> ecu3Action2 = new ArrayList<>();
        ecu3Action2.add(OFF);
        ecu3Action2.add("Low");
        ecu3Action2.add("Medium");
        ecu3Action2.add("High");
        ecu3ActuatorTest2.setAction(ecu3Action2);
        List<String> ecu3Condition2 = new ArrayList<>();
        ecu3Condition2.add(IGNITION_ON);
        ecu3Condition2.add(PARKING_BRAKE_ENGAGED);
        ecu3Condition2.add(ENGINE_OFF);
        ecu3ActuatorTest2.setCondition(ecu3Condition2);
        List<String> ecu3TestDescription2 = new ArrayList<>();
        ecu3TestDescription2.add("Different buzzer sounds from the instrument cluster can be activated and checked for its working condition from the list below:");
        ecu3TestDescription2.add("Power on");
        ecu3TestDescription2.add("Door open");
        ecu3TestDescription2.add("Turn indicator");
        ecu3TestDescription2.add("Reverse beep");
        ecu3TestDescription2.add("Headlamp ON without IGN OFF");
        ecu3ActuatorTest2.setActuatorTestDescription(ecu3TestDescription2);
        ecu3ActuatorTest2.setActuatorTestTimeDuration("10 seconds per buzzer type");
        List<String> ecu3ActivationMethod2 = new ArrayList<>();
        ecu3ActivationMethod2.add("Activate and deactivate button for each buzzer type.");
        ecu3ActivationMethod2.add("If activate button is clicked for one type then other Activate buttons should be disabled until the respective deactivate button is clicked.");
        ecu3ActuatorTest2.setActuatorActivationMethod(ecu3ActivationMethod2);
        actuatorTestValues.add(ecu3ActuatorTest2);

        ActuatorTestDto ecu4ActuatorTest1 = new ActuatorTestDto();
        ecu4ActuatorTest1.setEcuName(getEcuList().get(3));
        ecu4ActuatorTest1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(0));
        List<String> ecu4Action1 = new ArrayList<>();
        ecu4Action1.add(INACTIVE);
        ecu4Action1.add(ACTIVE);
        ecu4ActuatorTest1.setAction(ecu4Action1);
        List<String> ecu4Condition1 = new ArrayList<>();
        ecu4Condition1.add("Wheel chocks/stopper to be placed on the wheel");
        ecu4Condition1.add("Engine running");
        ecu4ActuatorTest1.setCondition(ecu4Condition1);
        List<String> ecu4TestDescription1 = new ArrayList<>();
        ecu4TestDescription1.add("The brake calliper will be activated and then deactivated");
        ecu4ActuatorTest1.setActuatorTestDescription(ecu4TestDescription1);
        ecu4ActuatorTest1.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu4ActivationMethod1 = new ArrayList<>();
        ecu4ActivationMethod1.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu4ActivationMethod1.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu4ActivationMethod1.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu4ActivationMethod1.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu4ActuatorTest1.setActuatorActivationMethod(ecu4ActivationMethod1);
        actuatorTestValues.add(ecu4ActuatorTest1);

        ActuatorTestDto ecu4ActuatorTest2 = new ActuatorTestDto();
        ecu4ActuatorTest2.setEcuName(getEcuList().get(3));
        ecu4ActuatorTest2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(1));
        List<String> ecu4Action2 = new ArrayList<>();
        ecu4Action2.add(INACTIVE);
        ecu4Action2.add(ACTIVE);
        ecu4ActuatorTest2.setAction(ecu4Action2);
        List<String> ecu4Condition2 = new ArrayList<>();
        ecu4Condition2.add("Wheel chocks/stopper to be placed on the wheel");
        ecu4Condition2.add("Engine running");
        ecu4ActuatorTest2.setCondition(ecu4Condition2);
        List<String> ecu4TestDescription2 = new ArrayList<>();
        ecu4TestDescription2.add("The brake solenoid will be activated and then deactivated");
        ecu4ActuatorTest2.setActuatorTestDescription(ecu4TestDescription2);
        ecu4ActuatorTest2.setActuatorTestTimeDuration(SECONDS_10);
        List<String> ecu4ActivationMethod2 = new ArrayList<>();
        ecu4ActivationMethod2.add(ACTIVATE_AND_DEACTIVATE_BUTTONS);
        ecu4ActivationMethod2.add(CLICKING_ON_ACTIVATE_BUTTON);
        ecu4ActivationMethod2.add(CLICKING_ON_DEACTIVATE_BUTTON);
        ecu4ActivationMethod2.add(DEACTIVATE_BUTTON_WILL_STAY_DISABLED);
        ecu4ActuatorTest2.setActuatorActivationMethod(ecu4ActivationMethod2);
        actuatorTestValues.add(ecu4ActuatorTest2);
    }

    public static List<ActuatorTestDto> getEcuActuatorTestValues() {
        setEcuActuatorTestValues();
        return actuatorTestValues;
    }

    public static void setDetectedVciList() {
        detectedVciList = new ArrayList<>();
        detectedVciList.add("J2534 for Kvaser Hardware");
        detectedVciList.add("PCANPT32");
    }

    public static List<String> getDetectedVciList() {
        setDetectedVciList();
        return detectedVciList;
    }

    private static void setVciDeviceDetail() {
        vciDeviceInfoList = new ArrayList<>();

        VCIDeviceInfoDto vciDeviceInfo1 = new VCIDeviceInfoDto();
        vciDeviceInfo1.setInterfaceDeviceName(getDetectedVciList().get(0));
        vciDeviceInfo1.setInterfaceVendorName("Vendor: Kvaser AB");
        vciDeviceInfo1.setInterfaceFirmwareVersion("0.0.0 (channel 0)");
        vciDeviceInfo1.setInterfacePartNumber("8.41.341");
        vciDeviceInfoList.add(vciDeviceInfo1);

        VCIDeviceInfoDto vciDeviceInfo2 = new VCIDeviceInfoDto();
        vciDeviceInfo2.setInterfaceDeviceName(getDetectedVciList().get(1));
        vciDeviceInfo2.setInterfaceVendorName("Vendor: PEAK-SystemTechnik GmbH");
        vciDeviceInfo2.setInterfaceFirmwareVersion("4.6.1.728");
        vciDeviceInfo2.setInterfacePartNumber("3.0.2.153");
        vciDeviceInfoList.add(vciDeviceInfo2);
    }

    public static List<VCIDeviceInfoDto> getVciDeviceDetail() {
        setVciDeviceDetail();
        return vciDeviceInfoList;
    }

    public static void setEcuRoutineResponse() {
        ecuRoutineResponses = new ArrayList<>();

        EcuRoutineResponseDto ecu1RoutineResponse1 = new EcuRoutineResponseDto();
        ecu1RoutineResponse1.setEcuName(getEcuList().get(0));
        ecu1RoutineResponse1.setEcuRoutine("Static Test");
        ecu1RoutineResponse1.setEcuRoutineResponse("Not Activate");
        ecuRoutineResponses.add(ecu1RoutineResponse1);

        EcuRoutineResponseDto ecu1RoutineResponse2 = new EcuRoutineResponseDto();
        ecu1RoutineResponse2.setEcuName(getEcuList().get(0));
        ecu1RoutineResponse2.setEcuRoutine("Fuel Feed Inhibition");
        ecu1RoutineResponse2.setEcuRoutineResponse("Not Inhibit");
        ecuRoutineResponses.add(ecu1RoutineResponse2);

        EcuRoutineResponseDto ecu1RoutineResponse3 = new EcuRoutineResponseDto();
        ecu1RoutineResponse3.setEcuName(getEcuList().get(0));
        ecu1RoutineResponse3.setEcuRoutine("Rail Pressure Test");
        ecu1RoutineResponse3.setEcuRoutineResponse("0 bar");
        ecuRoutineResponses.add(ecu1RoutineResponse3);

        EcuRoutineResponseDto ecu2RoutineResponse1 = new EcuRoutineResponseDto();
        ecu2RoutineResponse1.setEcuName(getEcuList().get(1));
        ecu2RoutineResponse1.setEcuRoutine("Erase Memory");
        ecu2RoutineResponse1.setEcuRoutineResponse(CORRECT);
        ecuRoutineResponses.add(ecu2RoutineResponse1);

        EcuRoutineResponseDto ecu2RoutineResponse2 = new EcuRoutineResponseDto();
        ecu2RoutineResponse2.setEcuName(getEcuList().get(1));
        ecu2RoutineResponse2.setEcuRoutine("Check Routine");
        ecu2RoutineResponse2.setEcuRoutineResponse(CORRECT);
        ecuRoutineResponses.add(ecu2RoutineResponse2);

        EcuRoutineResponseDto ecu2RoutineResponse3 = new EcuRoutineResponseDto();
        ecu2RoutineResponse3.setEcuName(getEcuList().get(1));
        ecu2RoutineResponse3.setEcuRoutine("Check Programming Dependencies");
        ecu2RoutineResponse3.setEcuRoutineResponse(CORRECT);
        ecuRoutineResponses.add(ecu2RoutineResponse3);

        EcuRoutineResponseDto ecu3RoutineResponse1 = new EcuRoutineResponseDto();
        ecu3RoutineResponse1.setEcuName(getEcuList().get(2));
        ecu3RoutineResponse1.setEcuRoutine("Check Coding State");
        ecu3RoutineResponse1.setEcuRoutineResponse("CDS Valid");
        ecuRoutineResponses.add(ecu3RoutineResponse1);
    }

    public static List<EcuRoutineResponseDto> getEcuRoutineResponse() {
        setEcuRoutineResponse();
        return ecuRoutineResponses;
    }

    public static void setEcuActuatorTestResponse() {
        ecuActuatorTestResponse = new ArrayList<>();

        ActuatorTestResponseDto actuator1TestResponse1 = new ActuatorTestResponseDto();
        actuator1TestResponse1.setEcuName(getEcuList().get(0));
        actuator1TestResponse1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(0));
        actuator1TestResponse1.setEcuActuatorState(ACTIVATE);
        actuator1TestResponse1.setEcuActuatorResponse("True");
        actuator1TestResponse1.setEcuActuatorMessage("Turbocharger solenoid valve Activated");
        ecuActuatorTestResponse.add(actuator1TestResponse1);

        ActuatorTestResponseDto actuator1TestResponse2 = new ActuatorTestResponseDto();
        actuator1TestResponse2.setEcuName(getEcuList().get(0));
        actuator1TestResponse2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(0));
        actuator1TestResponse2.setEcuActuatorState(DEACTIVATE);
        actuator1TestResponse2.setEcuActuatorResponse("True");
        actuator1TestResponse2.setEcuActuatorMessage("Turbocharger solenoid valve De-activated");
        ecuActuatorTestResponse.add(actuator1TestResponse2);

        ActuatorTestResponseDto actuator1TestResponse3 = new ActuatorTestResponseDto();
        actuator1TestResponse3.setEcuName(getEcuList().get(0));
        actuator1TestResponse3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse3.setEcuActuatorState("0%");
        actuator1TestResponse3.setEcuActuatorResponse("True");
        actuator1TestResponse3.setEcuActuatorMessage("Throttle valve actuator set to 0%");
        ecuActuatorTestResponse.add(actuator1TestResponse3);

        ActuatorTestResponseDto actuator1TestResponse4 = new ActuatorTestResponseDto();
        actuator1TestResponse4.setEcuName(getEcuList().get(0));
        actuator1TestResponse4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse4.setEcuActuatorState("20%");
        actuator1TestResponse4.setEcuActuatorResponse("True");
        actuator1TestResponse4.setEcuActuatorMessage("Throttle valve actuator set to 20%");
        ecuActuatorTestResponse.add(actuator1TestResponse4);

        ActuatorTestResponseDto actuator1TestResponse5 = new ActuatorTestResponseDto();
        actuator1TestResponse5.setEcuName(getEcuList().get(0));
        actuator1TestResponse5.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse5.setEcuActuatorState("40%");
        actuator1TestResponse5.setEcuActuatorResponse("True");
        actuator1TestResponse5.setEcuActuatorMessage("Throttle valve actuator set to 40%");
        ecuActuatorTestResponse.add(actuator1TestResponse5);

        ActuatorTestResponseDto actuator1TestResponse6 = new ActuatorTestResponseDto();
        actuator1TestResponse6.setEcuName(getEcuList().get(0));
        actuator1TestResponse6.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse6.setEcuActuatorState("60%");
        actuator1TestResponse6.setEcuActuatorResponse("True");
        actuator1TestResponse6.setEcuActuatorMessage("Throttle valve actuator set to 60%");
        ecuActuatorTestResponse.add(actuator1TestResponse6);

        ActuatorTestResponseDto actuator1TestResponse7 = new ActuatorTestResponseDto();
        actuator1TestResponse7.setEcuName(getEcuList().get(0));
        actuator1TestResponse7.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse7.setEcuActuatorState("80%");
        actuator1TestResponse7.setEcuActuatorResponse("True");
        actuator1TestResponse7.setEcuActuatorMessage("Throttle valve actuator set to 80%");
        ecuActuatorTestResponse.add(actuator1TestResponse7);

        ActuatorTestResponseDto actuator1TestResponse8 = new ActuatorTestResponseDto();
        actuator1TestResponse8.setEcuName(getEcuList().get(0));
        actuator1TestResponse8.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(1));
        actuator1TestResponse8.setEcuActuatorState("Idle Speed");
        actuator1TestResponse8.setEcuActuatorResponse("True");
        actuator1TestResponse8.setEcuActuatorMessage("Throttle valve actuator set to Idle Speed");
        ecuActuatorTestResponse.add(actuator1TestResponse8);

        ActuatorTestResponseDto actuator1TestResponse9 = new ActuatorTestResponseDto();
        actuator1TestResponse9.setEcuName(getEcuList().get(0));
        actuator1TestResponse9.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(2));
        actuator1TestResponse9.setEcuActuatorState(ON);
        actuator1TestResponse9.setEcuActuatorResponse("True");
        actuator1TestResponse9.setEcuActuatorMessage("Fuel pump relay turned On");
        ecuActuatorTestResponse.add(actuator1TestResponse9);

        ActuatorTestResponseDto actuator1TestResponse10 = new ActuatorTestResponseDto();
        actuator1TestResponse10.setEcuName(getEcuList().get(0));
        actuator1TestResponse10.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(2));
        actuator1TestResponse10.setEcuActuatorState(OFF);
        actuator1TestResponse10.setEcuActuatorResponse("True");
        actuator1TestResponse10.setEcuActuatorMessage("Fuel pump relay turned Off");
        ecuActuatorTestResponse.add(actuator1TestResponse10);

        ActuatorTestResponseDto actuator1TestResponse11 = new ActuatorTestResponseDto();
        actuator1TestResponse11.setEcuName(getEcuList().get(0));
        actuator1TestResponse11.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(3));
        actuator1TestResponse11.setEcuActuatorState("Engage");
        actuator1TestResponse11.setEcuActuatorResponse("True");
        actuator1TestResponse11.setEcuActuatorMessage("Starter motor actuator Engaged");
        ecuActuatorTestResponse.add(actuator1TestResponse11);

        ActuatorTestResponseDto actuator1TestResponse12 = new ActuatorTestResponseDto();
        actuator1TestResponse12.setEcuName(getEcuList().get(0));
        actuator1TestResponse12.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(3));
        actuator1TestResponse12.setEcuActuatorState("Disengage");
        actuator1TestResponse12.setEcuActuatorResponse("True");
        actuator1TestResponse12.setEcuActuatorMessage("Starter motor actuator Dis-engaged");
        ecuActuatorTestResponse.add(actuator1TestResponse12);

        ActuatorTestResponseDto actuator1TestResponse13 = new ActuatorTestResponseDto();
        actuator1TestResponse13.setEcuName(getEcuList().get(0));
        actuator1TestResponse13.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(4));
        actuator1TestResponse13.setEcuActuatorState("High Speed");
        actuator1TestResponse13.setEcuActuatorResponse("True");
        actuator1TestResponse13.setEcuActuatorMessage("Engine cooling fan set to High Speed");
        ecuActuatorTestResponse.add(actuator1TestResponse13);

        ActuatorTestResponseDto actuator1TestResponse14 = new ActuatorTestResponseDto();
        actuator1TestResponse14.setEcuName(getEcuList().get(0));
        actuator1TestResponse14.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(4));
        actuator1TestResponse14.setEcuActuatorState("Low Speed");
        actuator1TestResponse14.setEcuActuatorResponse("True");
        actuator1TestResponse14.setEcuActuatorMessage("Engine cooling fan set to Low Speed");
        ecuActuatorTestResponse.add(actuator1TestResponse14);

        ActuatorTestResponseDto actuator1TestResponse15 = new ActuatorTestResponseDto();
        actuator1TestResponse15.setEcuName(getEcuList().get(0));
        actuator1TestResponse15.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(0)).get(getEcuList().get(0)).get(4));
        actuator1TestResponse15.setEcuActuatorState(OFF);
        actuator1TestResponse15.setEcuActuatorResponse("True");
        actuator1TestResponse15.setEcuActuatorMessage("Engine cooling fan turned Off");
        ecuActuatorTestResponse.add(actuator1TestResponse15);

        ActuatorTestResponseDto actuator2TestResponse1 = new ActuatorTestResponseDto();
        actuator2TestResponse1.setEcuName(getEcuList().get(1));
        actuator2TestResponse1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(0));
        actuator2TestResponse1.setEcuActuatorState(ACTIVATE);
        actuator2TestResponse1.setEcuActuatorResponse("True");
        actuator2TestResponse1.setEcuActuatorMessage("Front wiper Activated");
        ecuActuatorTestResponse.add(actuator2TestResponse1);

        ActuatorTestResponseDto actuator2TestResponse2 = new ActuatorTestResponseDto();
        actuator2TestResponse2.setEcuName(getEcuList().get(1));
        actuator2TestResponse2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(0));
        actuator2TestResponse2.setEcuActuatorState(DEACTIVATE);
        actuator2TestResponse2.setEcuActuatorResponse("True");
        actuator2TestResponse2.setEcuActuatorMessage("Front wiper De-activated");
        ecuActuatorTestResponse.add(actuator2TestResponse2);

        ActuatorTestResponseDto actuator2TestResponse3 = new ActuatorTestResponseDto();
        actuator2TestResponse3.setEcuName(getEcuList().get(1));
        actuator2TestResponse3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(1));
        actuator2TestResponse3.setEcuActuatorState(ACTIVATE);
        actuator2TestResponse3.setEcuActuatorResponse("True");
        actuator2TestResponse3.setEcuActuatorMessage("Rear wiper Activated");
        ecuActuatorTestResponse.add(actuator2TestResponse3);

        ActuatorTestResponseDto actuator2TestResponse4 = new ActuatorTestResponseDto();
        actuator2TestResponse4.setEcuName(getEcuList().get(1));
        actuator2TestResponse4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(1));
        actuator2TestResponse4.setEcuActuatorState(DEACTIVATE);
        actuator2TestResponse4.setEcuActuatorResponse("True");
        actuator2TestResponse4.setEcuActuatorMessage("Rear wiper De-activated");
        ecuActuatorTestResponse.add(actuator2TestResponse4);

        ActuatorTestResponseDto actuator2TestResponse5 = new ActuatorTestResponseDto();
        actuator2TestResponse5.setEcuName(getEcuList().get(1));
        actuator2TestResponse5.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(2));
        actuator2TestResponse5.setEcuActuatorState("High");
        actuator2TestResponse5.setEcuActuatorResponse("True");
        actuator2TestResponse5.setEcuActuatorMessage("Horn set to High");
        ecuActuatorTestResponse.add(actuator2TestResponse5);

        ActuatorTestResponseDto actuator2TestResponse6 = new ActuatorTestResponseDto();
        actuator2TestResponse6.setEcuName(getEcuList().get(1));
        actuator2TestResponse6.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(2));
        actuator2TestResponse6.setEcuActuatorState("Low");
        actuator2TestResponse6.setEcuActuatorResponse("True");
        actuator2TestResponse6.setEcuActuatorMessage("Horn set to Low");
        ecuActuatorTestResponse.add(actuator2TestResponse6);

        ActuatorTestResponseDto actuator2TestResponse7 = new ActuatorTestResponseDto();
        actuator2TestResponse7.setEcuName(getEcuList().get(1));
        actuator2TestResponse7.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(2));
        actuator2TestResponse7.setEcuActuatorState(OFF);
        actuator2TestResponse7.setEcuActuatorResponse("True");
        actuator2TestResponse7.setEcuActuatorMessage("Horn turned Off");
        ecuActuatorTestResponse.add(actuator2TestResponse7);

        ActuatorTestResponseDto actuator2TestResponse8 = new ActuatorTestResponseDto();
        actuator2TestResponse8.setEcuName(getEcuList().get(1));
        actuator2TestResponse8.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(3));
        actuator2TestResponse8.setEcuActuatorState(ON);
        actuator2TestResponse8.setEcuActuatorResponse("True");
        actuator2TestResponse8.setEcuActuatorMessage("Headlamp lh low beam turned On");
        ecuActuatorTestResponse.add(actuator2TestResponse8);

        ActuatorTestResponseDto actuator2TestResponse9 = new ActuatorTestResponseDto();
        actuator2TestResponse9.setEcuName(getEcuList().get(1));
        actuator2TestResponse9.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(3));
        actuator2TestResponse9.setEcuActuatorState(OFF);
        actuator2TestResponse9.setEcuActuatorResponse("True");
        actuator2TestResponse9.setEcuActuatorMessage("Headlamp lh low beam turned Off");
        ecuActuatorTestResponse.add(actuator2TestResponse9);

        ActuatorTestResponseDto actuator2TestResponse10 = new ActuatorTestResponseDto();
        actuator2TestResponse10.setEcuName(getEcuList().get(1));
        actuator2TestResponse10.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(4));
        actuator2TestResponse10.setEcuActuatorState(ON);
        actuator2TestResponse10.setEcuActuatorResponse("True");
        actuator2TestResponse10.setEcuActuatorMessage("Headlamp rh low beam turned On");
        ecuActuatorTestResponse.add(actuator2TestResponse10);

        ActuatorTestResponseDto actuator2TestResponse11 = new ActuatorTestResponseDto();
        actuator2TestResponse11.setEcuName(getEcuList().get(1));
        actuator2TestResponse11.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(4));
        actuator2TestResponse11.setEcuActuatorState(OFF);
        actuator2TestResponse11.setEcuActuatorResponse("True");
        actuator2TestResponse11.setEcuActuatorMessage("Headlamp rh low beam turned Off");
        ecuActuatorTestResponse.add(actuator2TestResponse11);

        ActuatorTestResponseDto actuator2TestResponse12 = new ActuatorTestResponseDto();
        actuator2TestResponse12.setEcuName(getEcuList().get(1));
        actuator2TestResponse12.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(5));
        actuator2TestResponse12.setEcuActuatorState(ON);
        actuator2TestResponse12.setEcuActuatorResponse("True");
        actuator2TestResponse12.setEcuActuatorMessage("Headlamp lh high beam turned On");
        ecuActuatorTestResponse.add(actuator2TestResponse12);

        ActuatorTestResponseDto actuator2TestResponse13 = new ActuatorTestResponseDto();
        actuator2TestResponse13.setEcuName(getEcuList().get(1));
        actuator2TestResponse13.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(5));
        actuator2TestResponse13.setEcuActuatorState(OFF);
        actuator2TestResponse13.setEcuActuatorResponse("True");
        actuator2TestResponse13.setEcuActuatorMessage("Headlamp lh high beam turned Off");
        ecuActuatorTestResponse.add(actuator2TestResponse13);

        ActuatorTestResponseDto actuator2TestResponse14 = new ActuatorTestResponseDto();
        actuator2TestResponse14.setEcuName(getEcuList().get(1));
        actuator2TestResponse14.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(6));
        actuator2TestResponse14.setEcuActuatorState(ON);
        actuator2TestResponse14.setEcuActuatorResponse("True");
        actuator2TestResponse14.setEcuActuatorMessage("Headlamp rh high beam turned On");
        ecuActuatorTestResponse.add(actuator2TestResponse14);

        ActuatorTestResponseDto actuator2TestResponse15 = new ActuatorTestResponseDto();
        actuator2TestResponse15.setEcuName(getEcuList().get(1));
        actuator2TestResponse15.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(1)).get(getEcuList().get(1)).get(6));
        actuator2TestResponse15.setEcuActuatorState(OFF);
        actuator2TestResponse15.setEcuActuatorResponse("True");
        actuator2TestResponse15.setEcuActuatorMessage("Headlamp rh high beam turned Off");
        ecuActuatorTestResponse.add(actuator2TestResponse15);

        ActuatorTestResponseDto actuator3TestResponse1 = new ActuatorTestResponseDto();
        actuator3TestResponse1.setEcuName(getEcuList().get(2));
        actuator3TestResponse1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(0));
        actuator3TestResponse1.setEcuActuatorState("Brighter");
        actuator3TestResponse1.setEcuActuatorResponse("True");
        actuator3TestResponse1.setEcuActuatorMessage("Display illumination set to Brighter");
        ecuActuatorTestResponse.add(actuator3TestResponse1);

        ActuatorTestResponseDto actuator3TestResponse2 = new ActuatorTestResponseDto();
        actuator3TestResponse2.setEcuName(getEcuList().get(2));
        actuator3TestResponse2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(0));
        actuator3TestResponse2.setEcuActuatorState("Light");
        actuator3TestResponse2.setEcuActuatorResponse("True");
        actuator3TestResponse2.setEcuActuatorMessage("Display illumination set to Light");
        ecuActuatorTestResponse.add(actuator3TestResponse2);

        ActuatorTestResponseDto actuator3TestResponse3 = new ActuatorTestResponseDto();
        actuator3TestResponse3.setEcuName(getEcuList().get(2));
        actuator3TestResponse3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(0));
        actuator3TestResponse3.setEcuActuatorState("Dark");
        actuator3TestResponse3.setEcuActuatorResponse("True");
        actuator3TestResponse3.setEcuActuatorMessage("Display illumination set to Dark");
        ecuActuatorTestResponse.add(actuator3TestResponse3);

        ActuatorTestResponseDto actuator3TestResponse4 = new ActuatorTestResponseDto();
        actuator3TestResponse4.setEcuName(getEcuList().get(2));
        actuator3TestResponse4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(0));
        actuator3TestResponse4.setEcuActuatorState(OFF);
        actuator3TestResponse4.setEcuActuatorResponse("True");
        actuator3TestResponse4.setEcuActuatorMessage("Display illumination turned Off");
        ecuActuatorTestResponse.add(actuator3TestResponse4);

        ActuatorTestResponseDto actuator3TestResponse5 = new ActuatorTestResponseDto();
        actuator3TestResponse5.setEcuName(getEcuList().get(2));
        actuator3TestResponse5.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(1));
        actuator3TestResponse5.setEcuActuatorState("High");
        actuator3TestResponse5.setEcuActuatorResponse("True");
        actuator3TestResponse5.setEcuActuatorMessage("Warning buzzer set to High");
        ecuActuatorTestResponse.add(actuator3TestResponse5);

        ActuatorTestResponseDto actuator3TestResponse6 = new ActuatorTestResponseDto();
        actuator3TestResponse6.setEcuName(getEcuList().get(2));
        actuator3TestResponse6.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(1));
        actuator3TestResponse6.setEcuActuatorState("Medium");
        actuator3TestResponse6.setEcuActuatorResponse("True");
        actuator3TestResponse6.setEcuActuatorMessage("Warning buzzer set to Medium");
        ecuActuatorTestResponse.add(actuator3TestResponse6);

        ActuatorTestResponseDto actuator3TestResponse7 = new ActuatorTestResponseDto();
        actuator3TestResponse7.setEcuName(getEcuList().get(2));
        actuator3TestResponse7.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(1));
        actuator3TestResponse7.setEcuActuatorState("Low");
        actuator3TestResponse7.setEcuActuatorResponse("True");
        actuator3TestResponse7.setEcuActuatorMessage("Warning buzzer set to Low");
        ecuActuatorTestResponse.add(actuator3TestResponse7);

        ActuatorTestResponseDto actuator3TestResponse8 = new ActuatorTestResponseDto();
        actuator3TestResponse8.setEcuName(getEcuList().get(2));
        actuator3TestResponse8.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(2)).get(getEcuList().get(2)).get(1));
        actuator3TestResponse8.setEcuActuatorState(OFF);
        actuator3TestResponse8.setEcuActuatorResponse("True");
        actuator3TestResponse8.setEcuActuatorMessage("Warning buzzer turned Off");
        ecuActuatorTestResponse.add(actuator3TestResponse8);

        ActuatorTestResponseDto actuator4TestResponse1 = new ActuatorTestResponseDto();
        actuator4TestResponse1.setEcuName(getEcuList().get(3));
        actuator4TestResponse1.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(0));
        actuator4TestResponse1.setEcuActuatorState(ACTIVE);
        actuator4TestResponse1.setEcuActuatorResponse("True");
        actuator4TestResponse1.setEcuActuatorMessage("Brake caliper is Active");
        ecuActuatorTestResponse.add(actuator4TestResponse1);

        ActuatorTestResponseDto actuator4TestResponse2 = new ActuatorTestResponseDto();
        actuator4TestResponse2.setEcuName(getEcuList().get(3));
        actuator4TestResponse2.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(0));
        actuator4TestResponse2.setEcuActuatorState(INACTIVE);
        actuator4TestResponse2.setEcuActuatorResponse("True");
        actuator4TestResponse2.setEcuActuatorMessage("Brake caliper is Inactive");
        ecuActuatorTestResponse.add(actuator4TestResponse2);

        ActuatorTestResponseDto actuator4TestResponse3 = new ActuatorTestResponseDto();
        actuator4TestResponse3.setEcuName(getEcuList().get(3));
        actuator4TestResponse3.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(1));
        actuator4TestResponse3.setEcuActuatorState(ACTIVE);
        actuator4TestResponse3.setEcuActuatorResponse("True");
        actuator4TestResponse3.setEcuActuatorMessage("Actuator solenoid is Active");
        ecuActuatorTestResponse.add(actuator4TestResponse3);

        ActuatorTestResponseDto actuator4TestResponse4 = new ActuatorTestResponseDto();
        actuator4TestResponse4.setEcuName(getEcuList().get(3));
        actuator4TestResponse4.setActuatorTestParam(getEcuActuatorTestList(getEcuList().get(3)).get(getEcuList().get(3)).get(1));
        actuator4TestResponse4.setEcuActuatorState(INACTIVE);
        actuator4TestResponse4.setEcuActuatorResponse("True");
        actuator4TestResponse4.setEcuActuatorMessage("Actuator solenoid is Inactive");
        ecuActuatorTestResponse.add(actuator4TestResponse4);
    }

    public static List<ActuatorTestResponseDto> getEcuActuatorTestResponse() {
        setEcuActuatorTestResponse();
        return ecuActuatorTestResponse;
    }
}
