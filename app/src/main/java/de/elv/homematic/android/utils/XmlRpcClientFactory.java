package de.elv.homematic.android.utils;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Factory for producing XML-RPC clients.
 */
public class XmlRpcClientFactory {
    // list of used XML RPC commands
    public static final String CmdListDevices = "listDevices";
    public static final String CmdGetDeviceDescription = "getDeviceDescription";
    public static final String CmdSetValue = "setValue";
    public static final String CmdGetParamset = "getParamsetDescription";
    public static final String CmdGetValue = "getValue";

    /**
     * Produces a XML-RPC client.
     *
     * @param url  URL of the XML-RPC connection.
     * @param port Port of the XML-RPC connection.
     * @return Produced XML-RPC client.
     */
    public static XmlRpcClient produce(String url, int port) {
        final String urlSuffix = "/";

        String connectionString = url;
        if (port != 0) {
            connectionString += String.format(":%d/", port);
        }

        if (!connectionString.endsWith(urlSuffix)) {
            connectionString += "/";
        }

        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(connectionString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(2 * 1000);
        config.setReplyTimeout(2 * 1000);
        xmlRpcClient.setConfig(config);

        return xmlRpcClient;
    }
}