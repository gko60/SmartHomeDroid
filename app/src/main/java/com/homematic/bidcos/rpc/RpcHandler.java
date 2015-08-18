package com.homematic.bidcos.rpc;

import java.util.List;

public interface RpcHandler {

    void event(String interfaceId, String address, String valueKey, Object value);

    List<DeviceDescription> listDevices(String interfaceId);

    void newDevices(String interfaceId, DeviceDescription[] descriptions);

    void deleteDevice(String interfaceId, String[] addresses);

    void updateDevice(String interfaceId, String address, int hint);
}
