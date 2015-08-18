package de.elv.homematic.android;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import com.homematic.bidcos.rpc.DeviceDescription;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Read device from a HomeMatic CCU.
 */
public class Device implements Serializable {
    public static final String OpcodeAddress = "ADDRESS";
    public static final String OpcodeType = "TYPE";
    public static final String OpcodeChildren = "CHILDREN";
    public static final String OpcodeParamSetValues = "VALUES";

    public static final String DeviceTypeRCV50 = "HM-RCV-50";
    public static final String DeviceTypeLCDim1LPl = "HM-LC-Dim1L-Pl";
    public static final String DeviceTypeHMLCSw4SM = "HM-LC-Sw4-SM";
    public static final String DeviceTypeHMLCSw1Pl = "HM-LC-Sw1-Pl";
    public static final String DeviceTypeHMOUCFMPl = "HM-OU-CFM-Pl";
    public static final String DeviceTypeHMSecKeyS = "HM-Sec-Key-S";
    public static final String DeviceTypeHMLcBl1PbFm = "HM-LC-Bl1-PB-FM";
    public static final String DeviceTypeHMLcDim1PwmCv = "HM-LC-Dim1PWM-CV";
    public static final String DeviceTypeHMSecSFASM = "HM-Sec-SFA-SM";
    public static final String DeviceTypeHMSecWin = "HM-Sec-Win";
    public static final String DeviceTypeHMSecSd = "HM-Sec-SD";
    public static final String DeviceTypeHMCcTc = "HM-CC-TC";

    private static final long serialVersionUID = 1L;

    /**
     * Map of all device types and its description from string resources.
     * F.e.: "HM-LC-Dim1PWM-CV": "Funk-Dimmer"
     */
    private static Map<String, String> deviceDescriptions;

    /**
     * Address / serial number of the device (f.e. IEQ0548270).
     */
    private String address;

    /**
     * Type of the device (f.e. HM-LC-Dim1PWM-CV).
     */
    private String deviceType;

    /**
     * List of available controllable channels.
     */
    private Map<Integer, Channel> channels;

    /**
     * Initializes a new instance of the Device class.
     *
     * @param description Read device description to parse.
     */
    @SuppressLint("UseSparseArrays")
    public Device(DeviceDescription description) {
        this.setAddress(description.getAddress());
        this.setDeviceType(description.getType());

        this.channels = new TreeMap<>();

        for (String child : description.getChildren()) {
            int channelNumber = Device.getChannelNumber(child);
            if (channelNumber != 0) {
                this.channels.put(
                        channelNumber,
                        new Channel());
            }
        }
    }

    /**
     * Splits a address of a device (f.e. IEQ0548270:3 => { "IEQ0548270", "3" }
     *
     * @param address Adress to split.
     * @return Splitted address.
     */
    public static String[] splitAddress(String address) {
        final String DeviceChannelSeperator = ":";

        if (!address.contains(DeviceChannelSeperator)) {
            throw new IllegalArgumentException(String.format("%s does not match the \"address:channel\" pattern", address));
        }

        return address.split(DeviceChannelSeperator);
    }

    /**
     * Gets a map of all device types and its description from string resources.
     * It is generated only one time and gets persisted via Device.deviceDescriptions.
     *
     * @param res Android resources to use.
     * @return Map of all device types and its description from string resources.
     */
    public static Map<String, String> getDeviceDescriptions(Resources res) {
        if (Device.deviceDescriptions != null) {
            return Device.deviceDescriptions;
        }

        Device.deviceDescriptions = new HashMap<>();
//        Device.deviceDescriptions.put(Device.DeviceTypeRCV50, res.getString(R.string.device_desc_bidcos_rf));
//        Device.deviceDescriptions.put(Device.DeviceTypeLCDim1LPl, res.getString(R.string.device_desc_hm_lc_dim1l_pl));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMLCSw4SM, res.getString(R.string.device_desc_hm_lc_sw4_sm));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMLCSw1Pl, res.getString(R.string.device_desc_hm_lc_sw1_pl));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMOUCFMPl, res.getString(R.string.device_desc_hm_ou_cfm_pl));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMSecKeyS, res.getString(R.string.device_desc_hm_sec_key_s));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMLcBl1PbFm, res.getString(R.string.device_desc_hm_lc_bl1_pb_fm));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMLcDim1PwmCv, res.getString(R.string.device_desc_hm_lc_dim1_wpm_cv));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMSecSFASM, res.getString(R.string.device_desc_hm_sec_sfa_sm));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMSecSd, res.getString(R.string.device_desc_hm_sec_sd));
//        Device.deviceDescriptions.put(Device.DeviceTypeHMCcTc, res.getString(R.string.device_desc_hm_cc_tc));

        return Device.getDeviceDescriptions(res);
    }

    /**
     * Gets the channel number of a channel string.
     * F.e.: IEQ0548270:3 => 3
     *
     * @param channel Channel string.
     * @return Channel number
     */
    private static int getChannelNumber(String channel) {
        String[] split = channel.split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException();
        }

        return Integer.parseInt(split[1]);
    }

    /**
     * Gets the address / serial number of the device.
     *
     * @return addresses
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the device.
     *
     * @param address Address to set.
     */
    private void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the device type.
     *
     * @return Device type.
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the device type.
     *
     * @param type Device type to set.
     */
    private void setDeviceType(String type) {
        this.deviceType = type;
    }

    /**
     * Gets the channels of the device.
     *
     * @return Channels of the device.
     */
    public Map<Integer, Channel> getChannels() {
        return this.channels;
    }

    /**
     * Gets the channel of a channel number as string.
     *
     * @param strChannelNumber Channel number as string.
     * @return Wanted channel.
     */
    public Channel getChannel(String strChannelNumber) {
        try {
            return this.getChannel(Integer.parseInt(strChannelNumber));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format("%s is no valid channel number", strChannelNumber));
        }
    }

    /**
     * Gets the channel of a channel number as integer.
     *
     * @param channelNumber Channel number as string.
     * @return Wanted channel.
     */
    public Channel getChannel(int channelNumber) {
        for (Integer key : this.channels.keySet()) {
            if (key == channelNumber) {
                return this.channels.get(key);
            }
        }

        throw new IllegalArgumentException(String.format(
                "the device %s does not have channel #%d",
                this.getAddress(),
                channelNumber));
    }
}