package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.RpcHandler;
import com.homematic.bidcos.rpc.RpcServer;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class XmlRpcServer implements RpcServer, XmlRpcHandlerMapping {
    private WebServer server;
    private Map<String, XmlRpcHandler> handlers;

    public XmlRpcServer(int port, RpcHandler handler) {
        this.handlers = new HashMap<>();
        this.handlers.put("event", new EventHandler(handler));
        this.handlers.put("listDevices", new ListDevicesHandler(handler));
        this.handlers.put("newDevices", new NewDevicesHandler(handler));
        this.handlers.put("deleteDevices", new DeleteDevicesHandler(handler));
        this.handlers.put("updateDevice", new UpdateDeviceHandler(handler));
        this.handlers.put("system.multicall", new MultiCallHandler(handler));

        try {
            this.server = new WebServer(port, InetAddress.getByName(XmlRpcServer.getLocalAddress()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }

        this.server.getXmlRpcServer().setHandlerMapping(this);
    }

    public static String getLocalAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return "127.0.0.1";
    }

    public String getUrl() {
        return "http://" + XmlRpcServer.getLocalAddress() + ":" + this.server.getPort() + "/";
    }

    public int getPort() {
        return this.server.getPort();
    }

    public void start() {
        try {
            this.server.start();
        } catch (IOException e) {
            // leer
        }
    }

    public void stop() {
        this.server.shutdown();
    }

    public XmlRpcHandler getHandler(String handlerName)
            throws XmlRpcException {
        if (this.handlers.containsKey(handlerName)) {
            return this.handlers.get(handlerName);
        } else {
            throw new XmlRpcNoSuchHandlerException(handlerName);
        }
    }
}
