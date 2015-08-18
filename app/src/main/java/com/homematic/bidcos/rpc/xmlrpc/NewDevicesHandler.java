package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.DeviceDescription;
import com.homematic.bidcos.rpc.RpcHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewDevicesHandler implements XmlRpcHandler {
    private static final DeviceDescription[] EMPTY_LIST = new DeviceDescription[]{};
    private RpcHandler handler;

    public NewDevicesHandler(RpcHandler handler) {
        this.handler = handler;
    }

    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        try {
            String interfaceId = (String) request.getParameter(0);
            List<DeviceDescription> devices = new ArrayList<>();
            for (Object item : (Object[]) request.getParameter(1)) {
                devices.add(new DeviceDescription((Map<?, ?>) item));
            }

            this.handler.newDevices(interfaceId, EMPTY_LIST);
            return 1;
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }
    }
}
