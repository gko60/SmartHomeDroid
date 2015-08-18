package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.RpcHandler;
import com.homematic.bidcos.rpc.util.Empty;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

import java.util.ArrayList;
import java.util.List;

public class DeleteDevicesHandler implements XmlRpcHandler {

    private RpcHandler handler;

    public DeleteDevicesHandler(RpcHandler handler) {
        this.handler = handler;
    }

    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        try {
            String interfaceId = (String) request.getParameter(0);
            List<String> addresses = new ArrayList<>();
            for (Object item : (Object[]) request.getParameter(1)) {
                addresses.add((String) item);
            }

            this.handler.deleteDevice(interfaceId, Empty.STRING_ARRAY);
            return 1;
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }
    }
}
