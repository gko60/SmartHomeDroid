package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.RpcHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

public class EventHandler implements XmlRpcHandler {
    private RpcHandler handler;

    public EventHandler(RpcHandler handler) {
        this.handler = handler;
    }

    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        try {
            String interfaceId = (String) request.getParameter(0);
            String address = (String) request.getParameter(1);
            String valueKey = (String) request.getParameter(2);
            Object value = request.getParameter(3);

            this.handler.event(interfaceId, address, valueKey, value);
            return 1;
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }
    }

}
