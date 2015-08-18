package com.homematic.bidcos.rpc;

import java.util.List;
import java.util.Map;

/**
 * Interface for a communication proxy.
 */
public interface RpcProxy {

    String getUrl();

    int getTimeout();

    void setTimeout(int timeout);

    List<String> systemListMethods() throws RpcException;

    String systemMethodHelp(String methodName) throws RpcException;

    void init(String url, String interfaceId) throws RpcException;

    void shutdown(String url) throws RpcException;

    List<DeviceDescription> listDevces() throws RpcException;

    DeviceDescription getDeviceDescription(String address) throws RpcException;

    Map<String, ParameterDescription> getParamsetDescription(String address, String paramsetType) throws RpcException;

    String getParamsetId(String address, String type) throws RpcException;

    Map<String, Object> getParamset(String address, String paramsetKey) throws RpcException;

    void putParamset(String address, String paramsetKey, Map<String, Object> paramset) throws RpcException;

    Object getValue(String address, String valueKey) throws RpcException;

    void setValue(String address, String valueKey, Object value) throws RpcException;

    List<LinkDescription> getLinks(String address, int flags) throws RpcException;

    void addlink(String sender, String receiver, String name, String description) throws RpcException;

    void removeLink(String sender, String receiver) throws RpcException;

    void setLinkInfo(String sender, String receiver, String name, String description) throws RpcException;

    List<String> getLinkInfo(String senderAddress, String receiverAddress) throws RpcException;

    void activateLinkParamset(String address, String peerAddress, boolean longPress) throws RpcException;

    /**
     * @deprecated
     */
    void determineParameter(String address, String paramsetKey, String parameterId) throws RpcException;

    void deleteDevice(String address, int flags) throws RpcException;

    int getInstallMode() throws RpcException;

    void setInstallMode(boolean on) throws RpcException;

    String getKeyMissmatchDevice(boolean reset) throws RpcException;

    void setTempKey(String passphrase) throws RpcException;

    DeviceDescription addDevice(String serialNumber) throws RpcException;

    int searchDevices() throws RpcException;

    void changeKey(String passphrase) throws RpcException;

    List<DeviceDescription> listTeams() throws RpcException;

    void setTeam(String channelAddress, String teamName) throws RpcException;

    void restoreConfigToDevice(String address) throws RpcException;

    void clearConfigCache(String address) throws RpcException;

    Object rssiInfo() throws RpcException;

    List<Boolean> updateFirmware(List<String> devices) throws RpcException;

    List<String> getLinkPeer(String address) throws RpcException;

    LogLevel logLevel() throws RpcException;

    LogLevel logLevel(LogLevel level) throws RpcException;

    boolean reportValueUsage(String address, String valueId, int refCounter) throws RpcException;

    void setBidcosInterface(String deviceAddress, String interfaceAddress, boolean roaming) throws RpcException;

    List<InterfaceDescription> listBidcosInterfaces() throws RpcException;

    List<ServiceMessage> getServiceMessages() throws RpcException;

    void setMetadata(String objectId, String dataId, Object value) throws RpcException;

    Object getMetadata(String objectId, String dataId) throws RpcException;

    Map<String, Object> getAllMetadata(String objectId) throws RpcException;
}

