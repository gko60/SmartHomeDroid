package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.RpcHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

public class ListDevicesHandler implements XmlRpcHandler {
    private RpcHandler handler;

    public ListDevicesHandler(RpcHandler handler) {
        this.handler = handler;
    }

    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        try {
            String interfaceId = (String) request.getParameter(0);

            return this.handler.listDevices(interfaceId);
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }
    }
}
