package com.homematic.bidcos.rpc.xmlrpc;

import android.util.Log;

import com.homematic.bidcos.rpc.RpcHandler;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;

import java.util.Map;

public class MultiCallHandler implements XmlRpcHandler {
    private RpcHandler handler;

    public MultiCallHandler(RpcHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object execute(XmlRpcRequest request) throws XmlRpcException {
        Log.d("MultiCallHandler.execute", request.toString());
        try {
            Object[] requestParameter = (Object[]) request.getParameter(0);
            for (Object o : requestParameter) {
                Map<?, ?> receivedCommand = (Map<?, ?>) o;
                if (receivedCommand.get("methodName").equals("event")) {
                    Object[] params = (Object[]) receivedCommand.get("params");
                    this.handler.event(
                            params[0].toString(),
                            params[1].toString(),
                            params[2].toString(),
                            params[3]);
                }
            }
        } catch (Exception ex) {
            throw new XmlRpcException("EventHandler.execute", ex);
        }

        return 1;
    }
}
