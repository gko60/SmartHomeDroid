package com.homematic.bidcos.rpc;

import com.homematic.bidcos.rpc.xmlrpc.XmlRpcServer;

public class RpcServerFactory {

    public static RpcServer create(int port, RpcHandler listener) {
        return new XmlRpcServer(port, listener);
    }

}
