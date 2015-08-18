package com.homematic.bidcos.rpc;

import java.util.ArrayList;
import java.util.List;

public class BaseRpcHandler implements RpcHandler {
    private static final List<DeviceDescription> EmptyList = new ArrayList<>();

    @Override
    public void deleteDevice(String interfaceId, String[] addresses) {
        // do nothing
    }

    @Override
    public void event(String interfaceId, String address, String valueKey,
                      Object value) {
        // do nothing
    }

    @Override
    public List<DeviceDescription> listDevices(String interfaceId) {
        return EmptyList;
    }

    @Override
    public void newDevices(String interfaceId, DeviceDescription[] descriptions) {
        // do nothing
    }

    public void updateDevice(String interfaceId, String address, int hint) {
        // do nothing
    }
}
