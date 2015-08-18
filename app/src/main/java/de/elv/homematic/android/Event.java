package de.elv.homematic.android;

import java.io.Serializable;

/**
 * Received event from a XML RPC server.
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Address ([serial number]:[channel number]) of firing device.
     */
    private String address;

    /**
     * Changed value key / parameter of the firing device.
     */
    private String valueKey;

    /**
     * New value of the parameter.
     */
    private Object value;

    /**
     * Initializes a new instance of the Event class.
     *
     * @param address  Address ([serial number]:[channel number]) of firing device.
     * @param valueKey Value key / changed parameter of the firing device.
     * @param value    New value of the parameter.
     */
    public Event(String address, String valueKey, Object value) {
        this.address = address;
        this.valueKey = valueKey;
        this.value = value;
    }

    /**
     * Gets the address of the firing device.
     *
     * @return Address of the firing device.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the firing device.
     *
     * @param address Address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Changed value key / parameter of the firing device.
     *
     * @return Changed value key / parameter key to set.
     */
    public String getValueKey() {
        return valueKey;
    }

    /**
     * Sets the value key / parameter of the firing device.
     *
     * @param valueKey Value key / parameter of the firing device.
     */
    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    /**
     * Gets the value of the parameter.
     *
     * @return Value of the parameter.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the parameter.
     *
     * @param value Value to set.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
