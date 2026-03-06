package com.dtd.vehiclestackcommunication.util;

public class StackConstantsUtil {
    //    Incoming event names
    public static final String READ_AVAILABLE_VCI_LIST = "readAvailableVciList";
    public static final String CHECK_VCI_CONNECTIVITY_STATUS = "checkVciConnectivityStatus";
    public static final String VCI_INITIALIZATION = "vciInitialization";
    public static final String FETCH_VCI_INFO = "fetchVciInfo";
    public static final String GET_VCI_DEVICE_INFO = "getVciDeviceInfo";
    public static final String INITIATE_ECU_FLASHING = "initiateEcuFlashing";
    public static final String READ_DASHBOARD_PARAMETER = "readDashboardParameter";
    public static final String CHECK_ECU_CONNECTIVITY_STATUS = "checkEcuConnectivityStatus";
    public static final String READ_ECU_DTC = "readEcuDtcList";
    public static final String CLEAR_ECU_DTC = "clearEcuDtcList";
    public static final String FETCH_ECU_PARAMETERS = "fetchEcuParameters";
    public static final String READ_ECU_PARAMETER_VALUE = "readEcuParameterValue";
    public static final String FETCH_ECU_STATUS_PARAMS = "fetchEcuStatusParams";
    public static final String READ_ECU_STATUS_PARAM_VALUE = "readEcuStatusParamValue";
    public static final String READ_ECU_ACTUATOR_PARAMETERS = "readEcuActuatorParams";
    public static final String FETCH_ECU_ACTUATOR_PARAM_OPTIONS = "fetchEcuActuatorParamOptions";
    public static final String FETCH_ECU_ACTUATOR_PARAM_RESPONSE = "fetchEcuActuatorParamResponse";
    public static final String READ_ECU_ROUTINE_RESPONSE = "readEcuRoutineResponse";
    public static final String GET_ECU_LIST = "getEcuList";
    public static final String GET_ECU_STATUS = "getEcuStatus";
    public static final String GET_ECU_PARAMETERS = "getEcuParameters";
    public static final String GET_ECU_PARAMETERS_VALUES = "getEcuParametersValues";
    public static final String GET_ECU_STATUS_VALUE = "getEcuStatusValues";
    public static final String WRITE_ECU_STATUS_VALUES = "writeEcuStatusValues";
    public static final String GET_ECU_STATUS_VALUES_LEVEL = "getEcuStatusValuesLevel";

    public static final String GET_PARAMETER_LIST_VALUE = "readParameterList";
    public static final String GET_IO_PARAMETER_LIST_VALUE = "readIOParameterList";
    public static final String GET_READ_DATA_BY_IDENTIFIER = "readDataByIdentifier";
    public static final String GET_ECU_SCAN_LIST = "getEcuScanList";
    public static final String GET_ECU_INFO = "getEcuInfo";
    public static final String GET_ECU_FAULT_CODES = "getEcuFaultCodes";
    public static final String GET_ACCESS_TOKEN = "getAccessToken";
    public static final String GET_ECU_ACTUATOR_TEST_LIST = "getEcuActuatorTestList";

    public static final String GET_ECU_SERIAL_NUMBER = "getEcuSerialNumber";

    public static final String GET_ECU_VERSION_NUMBER = "getEcuVersionNumber";

    public static final String GET_ECU_VIN_NUMBER = "getEcuVinNumber";

    public static final String VCI_UNINITIALIZATION = "vciUNInitialization";

    public static final String READ_ECU_OFFLINE_DTC = "readEcuOfflineDtcList";

    public static final String READ_ECU_OFFLINE_FLASH = "readEcuOfflineFlash";

    public static final String READ_CGW_ECU_SERIAL_NUMBER = "readCgwEcuSerialNumber";

    public static final String READ_CGW_SHOP_REPAIR_CODE = "readCgwShopRepairCode";

    public static final String READ_CGW_KGM_PART_NUMBER = "readCgwKgmPartNumber";

    public static final String READ_CGW_SOFTWARE_VERSION = "readCgwKgmSoftwareVersion";

    public static final String READ_CGW_SUPPLIER_CODE = "readCgwKgmSupplierCode";

    public static final String READ_CGW_MANUFACTURING_DATE = "readCgwKgmManufacturingDate";

    public static final String READ_CGW_PROGRAMMING_DATE = "readCgwKgmProgrammingDate";

    public static final String READ_CGW_DTC_LIST = "readCgwKgmDtcList";

    public static final String READ_ECU_SERVICE = "readEcuResetService";

    public static final String READ_ECU_SERVICE_VCI = "readECUVCIService";

    public static final String READ_ECU_SERVICE_DTC = "readECUDTCService";

    public static final String READ_ECU_SERVICE_FLASHING = "readECUFlashingService";


    private StackConstantsUtil() {
//        created private constructor to not instantiate this util class
    }
}
