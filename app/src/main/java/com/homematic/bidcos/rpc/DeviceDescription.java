/**
 * @author Falk Werner
 */
package com.homematic.bidcos.rpc;

import com.homematic.bidcos.rpc.util.Empty;
import com.homematic.bidcos.rpc.util.MapParser;

import java.util.Map;

/**
 * Description of a device.
 */
public class DeviceDescription {
    /**
     * Flags for visible devices.
     */
    private final static int FLAG_VISIBLE = 0x1;
    private final static int FLAG_INTERNAL = 0x02;
    private final static int FLAG_DONTDELETE = 0x04;

    private String deviceType;
    private String address;
    private String[] children;
    private String parent;
    private String parentType;
    private int index;
    private boolean aesActive;
    private boolean hasMaster;
    private boolean hasValues;
    private boolean hasLink;
    private String firmware;
    private String availableFirmware;
    private int version;
    private boolean visible;
    private boolean internal;
    private boolean dontDelete;
    private String[] linkSourceRoles;
    private String[] linkTargetRoles;
    private Direction direction;
    private String group;
    private String team;
    private String teamTag;
    private String[] teamChannels;
    private String interfaceId;
    private boolean roaming;

    /**
     * Initializes a new instance of the DeviceDescription class.
     *
     * @param map Device description as map read via XML-RPC.
     */
    public DeviceDescription(Map<?, ?> map) {
        final MapParser parser = new MapParser(map);

        int flags = parser.getInteger("FLAGS", 0);
        this.visible = (flags & FLAG_VISIBLE) != 0;
        this.internal = (flags & FLAG_INTERNAL) != 0;
        this.dontDelete = (flags & FLAG_DONTDELETE) != 0;

        this.hasMaster = false;
        this.hasValues = false;
        this.hasLink = false;
        for (Object paramset : (Object[]) parser.get("PARAMSETS", Empty.STRING_ARRAY)) {
            this.hasMaster |= paramset.equals("MASTER");
            this.hasValues |= paramset.equals("VALUES");
            this.hasLink |= paramset.equals("LINK");
        }

        this.deviceType = parser.getString("TYPE", "UNKNOWN");
        this.address = parser.getString("ADDRESS", "ABC1234567");
        this.children = parser.getStringList("CHILDREN");
        this.parent = parser.getString("PARENT", "");
        this.parentType = parser.getString("PARENT_TYPE", "");
        this.index = parser.getInteger("INDEX", 0);
        this.aesActive = parser.getBoolean("AES_ACTIVE", false);
        this.firmware = parser.getString("FIRMWARE", "");
        this.availableFirmware = parser.getString("AVAILABLE_FIRMWARE", "");
        this.version = parser.getInteger("VERSION", 0);
        this.linkSourceRoles = (parser.getString("LINK_SOURCE_ROLES", "")).split("\\s");
        this.linkTargetRoles = (parser.getString("LINK_TARGET_ROLES", "")).split("\\s");
        this.direction = Direction.get((Integer) parser.get("DIRECTION", 0));
        this.group = parser.getString("GROUP", "");
        this.team = parser.getString("TEAM", "");
        this.teamTag = parser.getString("TEAM_TAG", "");
        this.teamChannels = parser.getStringList("TEAM_CHANNELS");
        this.interfaceId = parser.getString("INTERFACE", "");
        this.roaming = parser.getBoolean("ROAMING", false);
    }

    /**
     * Indicates whether this description is a device or not.
     *
     * @return Result of the check.
     */
    public boolean isDevice() {
        final String Empty = "";

        return this.parent.compareTo(Empty) == 0;
    }

    public boolean isChannel() {
        return !this.isDevice();
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public boolean isDontDelete() {
        return this.dontDelete;
    }

    public boolean hasMasterParamset() {
        return this.hasMaster;
    }

    public boolean hasValuesParamset() {
        return this.hasValues;
    }

    public boolean hasLinkParamset() {
        return this.hasLink;
    }

    public String getType() {
        return this.deviceType;
    }

    public String getAddress() {
        return this.address;
    }

    public String[] getChildren() {
        return this.children;
    }

    public String getParent() {
        return this.parent;
    }

    public String getParentType() {
        return this.parentType;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean getAesActive() {
        return this.aesActive;
    }

    public String getFirmware() {
        return this.firmware;
    }

    public String getAvailableFirmware() {
        return this.availableFirmware;
    }

    public int getVersion() {
        return this.version;
    }

    public String[] getLinkSourceRoles() {
        return this.linkSourceRoles;
    }

    public String[] getLinkTargetRoles() {
        return this.linkTargetRoles;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public String getGroup() {
        return this.group;
    }

    public String getTeam() {
        return this.team;
    }

    public String getTeamTag() {
        return this.teamTag;
    }

    public String[] getTeamChannels() {
        return this.teamChannels;
    }

    public String getInterface() {
        return this.interfaceId;
    }

    public boolean getRoaming() {
        return this.roaming;
    }
}
