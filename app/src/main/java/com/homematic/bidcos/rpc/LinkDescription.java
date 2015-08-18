package com.homematic.bidcos.rpc;

import com.homematic.bidcos.rpc.util.Empty;
import com.homematic.bidcos.rpc.util.MapParser;

import java.util.Map;

/**
 * Description of a link.
 */
public class LinkDescription {
    /**
     * Flag for broken links from sender.
     */
    private static final int SENDER_BROKEN = 0x01;

    /**
     * Flag for broken links from receiver.
     */
    private static final int RECEIVER_BROKEN = 0x02;

    /**
     * Address of the sender.
     */
    private String sender;

    /**
     * Address of the receiver.
     */
    private String receiver;

    /**
     * Name of the link.
     */
    private String name;

    /**
     * Description of the link.
     */
    private String description;

    /**
     * Flag of the link.
     */
    private int flags;

    /**
     * Parameter of the sender.
     */
    private Map<String, ?> senderParamset;

    /**
     * Parameter of the receiver.
     */
    private Map<String, ?> receiverParamset;

    /**
     * Initializes a new instance of the LinkDescription class.
     *
     * @param map Link description as map read via XML-RPC.
     */
    @SuppressWarnings("unchecked")
    public LinkDescription(Map<?, ?> map) {
        final MapParser parser = new MapParser(map);

        this.sender = parser.getString("SENDER", "");
        this.receiver = parser.getString("RECEIVER", "");
        this.name = parser.getString("NAME", "");
        this.description = parser.getString("DESCRIPTION", "");
        this.flags = parser.getInteger("FLAGS", 0);
        this.senderParamset = (Map<String, ?>) parser.get("SENDER_PARAMSET", Empty.MAP);
        this.receiverParamset = (Map<String, ?>) parser.get("RECEIVER_PARAMSET", Empty.MAP);
    }

    /**
     * Gets the address of the sender.
     *
     * @return Address of the sender.
     */
    public String getSender() {
        return this.sender;
    }

    /**
     * Gets the address of the receiver.
     *
     * @return Address of the receiver.
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * Gets the name of the link.
     *
     * @return Name of the link.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the description of the link.
     *
     * @return Description of the link.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets a value indicating whether the link is broken or not.
     *
     * @return A value indicating whether the link is broken or not.
     */
    public boolean isBroken() {
        return (this.flags != 0);
    }

    /**
     * Gets a value indicating whether the link is broken from sender or not.
     *
     * @return A value indicating whether the link is broken from sender or not.
     */
    public boolean isSenderBroken() {
        return ((this.flags & SENDER_BROKEN) != 0);
    }

    /**
     * Gets a value indicating whether the link is broken from receiver or not.
     *
     * @return A value indicating whether the link is broken from receiver or not.
     */
    public boolean isReceiverBroken() {
        return ((this.flags & RECEIVER_BROKEN) != 0);
    }

    /**
     * Gets the parameter set of the sender.
     *
     * @return Parameter set of the sender.
     */
    public Map<?, ?> getSenderParamset() {
        return this.senderParamset;
    }

    /**
     * Gets the parameter set of the receiver.
     *
     * @return Parameter set of the receiver.
     */
    public Map<?, ?> getReceiverParamset() {
        return this.receiverParamset;
    }
}
