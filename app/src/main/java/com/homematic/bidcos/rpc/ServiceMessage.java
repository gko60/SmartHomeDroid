package com.homematic.bidcos.rpc;

public class ServiceMessage {
    /**
     * Address of the device which raised the service message.
     */
    private String address;

    /**
     * ID of the service message.
     */
    private String id;

    /**
     * Value of the service message.
     */
    private Object value;

    /**
     * Initializes a new instance of the ServiceMessage class.
     *
     * @param serviceMessage Service message as map read via XML-RPC.
     */
    public ServiceMessage(Object[] serviceMessage) {
        this.address = (String) serviceMessage[0];
        this.id = (String) serviceMessage[1];
        this.value = serviceMessage[2];
    }

    /**
     * Gets the address of the device which raised the service message.
     *
     * @return Address of the device which raised the service message..
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets the ID of the service message.
     *
     * @return ID of the service message..
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the value of the service message.
     *
     * @return Value of the service message.
     */
    public Object getValue() {
        return this.value;
    }
}
