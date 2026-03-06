package com.dtd.vehiclestackcommunication.service;

import com.corundumstudio.socketio.listener.DataListener;
import com.dtd.vehiclestackcommunication.dto.SocketRequestPayloadDto;

public interface SocketHandlerService {
    // MEASUREMENT SERVICE
    DataListener<SocketRequestPayloadDto> onGetAvailableVciListRequestReceived();

    DataListener<SocketRequestPayloadDto> onCheckVciConnectivityRequestReceived();

    DataListener<String> onVciInitializationRequestReceived();

    DataListener<SocketRequestPayloadDto> onFetchVciInfoRequestReceived();

    DataListener<SocketRequestPayloadDto> onGetVciDeviceInfoRequestReceived();

    DataListener<SocketRequestPayloadDto> onEcuFlashingRequestReceived();

    DataListener<SocketRequestPayloadDto> onReadDashboardParameterRequestReceived();

    DataListener<SocketRequestPayloadDto> onFetchEcuStatusRequestReceived();

    DataListener<SocketRequestPayloadDto> onReadEcuDtcRequestReceived();

    DataListener<SocketRequestPayloadDto> onClearEcuDtcRequestReceived();

    DataListener<SocketRequestPayloadDto> onFetchEcuParametersReceived();

    DataListener<SocketRequestPayloadDto> onReadEcuParameterValueReceived();

    DataListener<SocketRequestPayloadDto> onFetchEcuStatusParamsReceived();

    DataListener<String> onReadParameterList();

    DataListener<SocketRequestPayloadDto> onReadEcuStatusParamValueReceived();

    DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamsReceived();

    DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamOptionsReceived();

    DataListener<SocketRequestPayloadDto> doFetchEcuActuatorParamResponseReceived();

    DataListener<SocketRequestPayloadDto> onReadEcuRoutineResponseReceived();

    DataListener<SocketRequestPayloadDto> onGetEcuParameters();

    DataListener<SocketRequestPayloadDto> onGetEcuStatus();

    DataListener<SocketRequestPayloadDto> onGetEcuList();

    DataListener<SocketRequestPayloadDto> onGetEcuParameterValues();

    DataListener<SocketRequestPayloadDto> onGetEcuStatusValues();

    DataListener<SocketRequestPayloadDto> onWriteEcuStatusValues();

    DataListener<SocketRequestPayloadDto> onGetEcuStatusValuesLevel();

    // VEHICLE SERVICE
    DataListener<SocketRequestPayloadDto> onGetEcuScanList();

    DataListener<SocketRequestPayloadDto> onGetEcuFaultCodes();

    DataListener<SocketRequestPayloadDto> onGetEcuInfo();

    DataListener<SocketRequestPayloadDto> onGetAccessToken();

    DataListener<SocketRequestPayloadDto> onGetEcuActuatorTestList();

}
