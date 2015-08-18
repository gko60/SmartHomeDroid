package com.homematic.bidcos.rpc;

import com.homematic.bidcos.rpc.xmlrpc.XmlRpcProxy;

/**
 * Factory for creating RpcProxy's.
 */
public class RpcProxyFactory {
    /**
     * Erstellt einen neuen Proxy fuer die Kommunikation.
     *
     * @param url Url des BidCoS-Services.
     * @return Proxy fuer die Kommunikation.
     */
    public static RpcProxy create(String url) {
        return new XmlRpcProxy(url);
    }
}
