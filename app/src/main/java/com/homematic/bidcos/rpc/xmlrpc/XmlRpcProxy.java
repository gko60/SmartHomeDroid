package com.homematic.bidcos.rpc.xmlrpc;

import com.homematic.bidcos.rpc.DeviceDescription;
import com.homematic.bidcos.rpc.InterfaceDescription;
import com.homematic.bidcos.rpc.LinkDescription;
import com.homematic.bidcos.rpc.LogLevel;
import com.homematic.bidcos.rpc.ParameterDescription;
import com.homematic.bidcos.rpc.RpcException;
import com.homematic.bidcos.rpc.RpcProxy;
import com.homematic.bidcos.rpc.ServiceMessage;
import com.homematic.bidcos.rpc.util.Empty;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlRpcProxy implements RpcProxy {
    public static final String DEFAULT_URL = "http://127.0.0.1:2001/";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    public static final int DEFAULT_REPLY_TIMEOUT = 15000;

    private XmlRpcClientConfigImpl config;
    private XmlRpcClient client;

    public XmlRpcProxy(String url) {
        this.config = new XmlRpcClientConfigImpl();
        this.setUrl(DEFAULT_URL);
        this.config.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        this.config.setReplyTimeout(DEFAULT_REPLY_TIMEOUT);

        this.client = new XmlRpcClient();
        this.client.setConfig(config);

        this.setUrl(url);
    }

    public String getUrl() {
        return this.config.getServerURL().toString();
    }

    public void setUrl(String pUrl) {
        try {
            this.config.setServerURL(new URL(pUrl));
        } catch (Exception ex) {
            // leer
        }
    }

    public int getConnectionTimeout() {
        return this.config.getConnectionTimeout();
    }

    public void setConnectionTimeout(int pConnectionTimeout) {
        this.config.setConnectionTimeout(pConnectionTimeout);
    }

    public int getReplyTimeout() {
        return this.config.getReplyTimeout();
    }

    public void setReplyTimeout(int pReplyTimeout) {
        this.config.setReplyTimeout(pReplyTimeout);
    }

    public int getTimeout() {
        return this.getReplyTimeout();
    }

    public void setTimeout(int timeout) {
        this.setConnectionTimeout(timeout);
        this.setReplyTimeout(timeout);
    }

    public List<String> systemListMethods() throws RpcException {
        List<String> result = new ArrayList<>();

        for (Object methodName : (Object[]) this.execute("system.listMethods", null)) {
            result.add(methodName.toString());
        }

        return result;
    }

    public String systemMethodHelp(String methodName) throws RpcException {
        return (String) this.execute("system.methodHelp", new Object[]{methodName});
    }

    private synchronized Object execute(String methodName, Object[] args) throws RpcException {
        Object result;

        try {
            result = this.client.execute(methodName, args);
        } catch (XmlRpcException ex) {
            throw new RpcException(ex.code, ex);
        }

        return result;
    }

    @Override
    public void activateLinkParamset(String address, String peerAddress,
                                     boolean longPress) throws RpcException {
        this.execute("activeteLinkParamset", new Object[]{address, peerAddress, longPress});
    }

    @Override
    public DeviceDescription addDevice(String serialNumber) throws RpcException {
        return new DeviceDescription((Map<?, ?>) this.execute("addDevice", new Object[]{serialNumber}));
    }

    @Override
    public void addlink(String sender, String receiver, String name,
                        String description) throws RpcException {
        this.execute("addLink", new Object[]{sender, receiver, name, description});
    }

    @Override
    public void changeKey(String passphrase) throws RpcException {
        this.execute("changeKey", new Object[]{passphrase});
    }

    @Override
    public void clearConfigCache(String address) throws RpcException {
        this.execute("clearConfigCache", new Object[]{address});
    }

    @Override
    public void deleteDevice(String address, int flags) throws RpcException {
        this.execute("deleteDevice", new Object[]{address, flags});
    }

    @Override
    public void determineParameter(String address, String paramsetKey,
                                   String parameterId) throws RpcException {
        this.execute("determineParameter", new Object[]{address, paramsetKey, parameterId});
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getAllMetadata(String objectId)
            throws RpcException {
        return (Map<String, Object>) this.execute("getAllMetadata", new Object[]{objectId});
    }

    @Override
    public DeviceDescription getDeviceDescription(String address)
            throws RpcException {
        return new DeviceDescription((Map<?, ?>) this.execute("getDeviceDescription", new Object[]{address}));
    }

    @Override
    public int getInstallMode() throws RpcException {
        return (Integer) this.execute("getInstallMode", Empty.OBJECT_ARRAY);
    }

    @Override
    public void setInstallMode(boolean on) throws RpcException {
        this.execute("setInstallMode", new Object[]{on});
    }

    @Override
    public String getKeyMissmatchDevice(boolean reset) throws RpcException {
        return (String) this.execute("getKeyMissmatchDevice", new Object[]{reset});
    }

    @Override
    public List<String> getLinkInfo(String senderAddress, String receiverAddress)
            throws RpcException {
        List<String> result = new ArrayList<>();

        for (Object linkInfo : (Object[]) this.execute("getLinkInfo", new Object[]{senderAddress, receiverAddress})) {
            result.add((String) linkInfo);
        }

        return result;
    }

    @Override
    public List<String> getLinkPeer(String address) throws RpcException {
        List<String> result = new ArrayList<>();

        for (Object linkInfo : (Object[]) this.execute("getLinkPeer", new Object[]{address})) {
            result.add((String) linkInfo);
        }

        return result;
    }

    @Override
    public List<LinkDescription> getLinks(String address, int flags)
            throws RpcException {
        List<LinkDescription> result = new ArrayList<>();

        for (Object item : (Object[]) this.execute("getLinks", new Object[]{address, flags})) {
            result.add(new LinkDescription((Map<?, ?>) item));
        }

        return result;
    }

    @Override
    public Object getMetadata(String objectId, String dataId)
            throws RpcException {
        return this.execute("getMetadata", new Object[]{objectId, dataId});
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getParamset(String address, String paramsetKey)
            throws RpcException {
        return (Map<String, Object>) this.execute("getParamset", new Object[]{address, paramsetKey});
    }

    @Override
    public Map<String, ParameterDescription> getParamsetDescription(
            String address, String paramsetType) throws RpcException {
        Map<String, ParameterDescription> result = new HashMap<>();

        Map<?, ?> description = (Map<?, ?>) this.execute("getParamsetDescription", new Object[]{address, paramsetType});
        for (Object key : description.keySet()) {
            result.put(key.toString(), new ParameterDescription(key.toString(), (Map<?, ?>) description.get(key)));
        }

        return result;
    }

    @Override
    public String getParamsetId(String address, String type)
            throws RpcException {
        return (String) this.execute("getParamsetId", new Object[]{address, type});
    }

    @Override
    public List<ServiceMessage> getServiceMessages() throws RpcException {
        List<ServiceMessage> result = new ArrayList<>();

        for (Object serviceMessage : (Object[]) this.execute("getServiceMessages", Empty.OBJECT_ARRAY)) {
            result.add(new ServiceMessage((Object[]) serviceMessage));
        }


        return result;
    }

    @Override
    public Object getValue(String address, String valueKey) throws RpcException {
        return this.execute("getValue", new Object[]{address, valueKey});
    }

    @Override
    public void init(String url, String interfaceId) throws RpcException {
        this.execute("init", new Object[]{url, interfaceId});
    }

    public void shutdown(String url) throws RpcException {
        this.init(url, "");
    }

    @Override
    public List<InterfaceDescription> listBidcosInterfaces()
            throws RpcException {
        List<InterfaceDescription> result = new ArrayList<>();

        for (Object item : (Object[]) this.execute("listBidcosInterfaces", Empty.OBJECT_ARRAY)) {
            result.add(new InterfaceDescription((Map<?, ?>) item));
        }

        return result;
    }

    @Override
    public List<DeviceDescription> listDevces() throws RpcException {
        List<DeviceDescription> result = new ArrayList<>();

        for (Object description : (Object[]) this.execute("listDevices", Empty.OBJECT_ARRAY)) {
            result.add(new DeviceDescription((Map<?, ?>) description));
        }

        return result;
    }

    @Override
    public List<DeviceDescription> listTeams() throws RpcException {
        List<DeviceDescription> result = new ArrayList<>();

        for (Object description : (Object[]) this.execute("listTeams", Empty.OBJECT_ARRAY)) {
            result.add(new DeviceDescription((Map<?, ?>) description));
        }

        return result;
    }

    @Override
    public LogLevel logLevel() throws RpcException {
        return LogLevel.parse((Integer) this.execute("logLevel", Empty.OBJECT_ARRAY));
    }

    @Override
    public LogLevel logLevel(LogLevel level) throws RpcException {
        return LogLevel.parse((Integer) this.execute("logLevel", new Object[]{level.getValue()}));
    }

    @Override
    public void putParamset(String address, String paramsetKey,
                            Map<String, Object> paramset) throws RpcException {
        this.execute("putParamset", new Object[]{address, paramsetKey, paramset});
    }

    @Override
    public void removeLink(String sender, String receiver) throws RpcException {
        this.execute("removeLink", new Object[]{sender, receiver});
    }

    @Override
    public boolean reportValueUsage(String address, String valueId,
                                    int refCounter) throws RpcException {
        return (Boolean) this.execute("reportValueUsage", new Object[]{address, valueId, refCounter});
    }

    @Override
    public void restoreConfigToDevice(String address) throws RpcException {
        this.execute("restoreConfigToDevice", new Object[]{address});
    }

    @Override
    public Object rssiInfo() throws RpcException {
        return this.execute("rssiInfo", Empty.OBJECT_ARRAY);
    }

    @Override
    public int searchDevices() throws RpcException {
        return (Integer) this.execute("searchDevices", Empty.OBJECT_ARRAY);
    }

    @Override
    public void setBidcosInterface(String deviceAddress,
                                   String interfaceAddress, boolean roaming) throws RpcException {
        this.execute("setBidcosInterface", new Object[]{deviceAddress, interfaceAddress, roaming});
    }

    @Override
    public void setLinkInfo(String sender, String receiver, String name,
                            String description) throws RpcException {
        this.execute("setLinkInfo", new Object[]{sender, receiver, name, description});
    }

    @Override
    public void setMetadata(String objectId, String dataId, Object value)
            throws RpcException {
        this.execute("setMetadata", new Object[]{objectId, dataId, value});
    }

    @Override
    public void setTeam(String channelAddress, String teamName)
            throws RpcException {
        this.execute("setTeam", new Object[]{channelAddress, teamName});
    }

    @Override
    public void setTempKey(String passphrase) throws RpcException {
        this.execute("setTempKey", new Object[]{passphrase});
    }

    @Override
    public void setValue(String address, String valueKey, Object value)
            throws RpcException {
        this.execute("setValue", new Object[]{address, valueKey, value});
    }

    @Override
    public List<Boolean> updateFirmware(List<String> devices)
            throws RpcException {
        List<Boolean> result = new ArrayList<>();

        for (Object status : (Object[]) this.execute("updateFirmware", new Object[]{devices})) {
            result.add((Boolean) status);
        }

        return result;
    }
}
