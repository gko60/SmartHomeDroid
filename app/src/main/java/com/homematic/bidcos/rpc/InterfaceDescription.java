package com.homematic.bidcos.rpc;

import java.util.Map;

/**
 * Description of a BidCoS interface.
 */
public class InterfaceDescription {
    /**
     * Address of the interface.
     */
    private String address;

    /**
     * Description of the interface.
     */
    private String description;

    /**
     * A value indicating whether the interface is connected or not.
     */
    private boolean isConnected;

    /**
     * A value indicating whether the interface is a default interface or not.
     */
    private boolean isDefault;

    /**
     * Initializes a new instance of the InterfaceDescription class.
     *
     * @param map Interface description as map read via XML-RPC.
     */
    public InterfaceDescription(Map<?, ?> map) {
        this.address = (String) map.get("ADDRESS");
        this.description = (String) map.get("ADDRESS");
        this.isConnected = (Boolean) map.get("CONNECTED");
        this.isDefault = (Boolean) map.get("DEFAULT");
    }

    /**
     * Gets the address of the interface.
     *
     * @return Address of the interface.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets the description of the interface.
     *
     * @return Description of the interface.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets a value indicating whether the interface is connected or not.
     *
     * @return A value indicating whether the interface is connected or not.
     */
    public boolean getIsConnected() {
        return this.isConnected;
    }

    /**
     * Gets a value indicating whether the interface is a default interface or not.
     *
     * @return A value indicating whether the interface is a default interface or not.
     */
    public boolean getIsDefault() {
        return this.isDefault;
    }
}
