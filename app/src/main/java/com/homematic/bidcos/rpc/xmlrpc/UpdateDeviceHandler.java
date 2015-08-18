package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.RpcHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

public class UpdateDeviceHandler implements XmlRpcHandler {
    private RpcHandler handler;

    public UpdateDeviceHandler(RpcHandler handler) {
        this.handler = handler;
    }

    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        try {
            String interfaceId = (String) request.getParameter(0);
            String address = (String) request.getParameter(1);
            int hint = (Integer) request.getParameter(2);

            this.handler.updateDevice(interfaceId, address, hint);
            return 1;
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }
    }
}
