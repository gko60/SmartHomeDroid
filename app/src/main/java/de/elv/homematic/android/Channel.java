package de.elv.homematic.android;

import com.homematic.bidcos.rpc.ParameterDescription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Channel of a device.
 */
public class Channel implements Serializable {
    /**
     * Supported channel values which can be triggered. Other
     * unknown value keys are rejected.
     */
    public static final String[] SupportedChannelParameters = new String[]
            {
                    "STATE",
                    "LEVEL",
                    "HUMIDITY",
                    "TEMPERATURE",
                    "SETPOINT"
            };

    private static final long serialVersionUID = 1L;

    /**
     * All read supported channel value keys.
     */
    private ArrayList<ChannelParameter> parameters;

    /**
     * Initializes a new instance of the Channel class.
     */
    public Channel() {
        this.parameters = new ArrayList<>();
    }

    /**
     * Gets the number of a channel from a device.
     * Example: IEQ0548270:3 -> channel #3.
     *
     * @param device  Device which contains the channel.
     * @param channel Channel to get the number from.
     * @return Number of the passed channel.
     */
    public static int getChannelNumber(Device device, Channel channel) {
        int channelNumber = -1;
        Map<Integer, Channel> channels = device.getChannels();
        for (Integer currentChannelNumber : channels.keySet()) {
            if (channels.get(currentChannelNumber) == channel) {
                channelNumber = currentChannelNumber;
                break;
            }
        }

        return channelNumber;
    }

    /**
     * Gets the list of supported channel parameters as generic managed list.
     *
     * @return List of supported channel parameters as generic managed list.
     */
    public static ArrayList<String> getSupportedChannelParameters() {
        return new ArrayList<>(Arrays.asList(Channel.SupportedChannelParameters));
    }

    /**
     * Gets a list of all current channel values.
     *
     * @return List of all current channel values.
     */
    public ArrayList<ChannelParameter> getParameters() {
        return this.parameters;
    }

    /**
     * Adds parameters to this channel. Only supported parameters will be added.
     *
     * @param paramsetDescriptions List of parameters to add.
     */
    public void addParameters(Map<String, ParameterDescription> paramsetDescriptions) {
        ArrayList<String> supportedValueKeys = Channel.getSupportedChannelParameters();

        for (String key : paramsetDescriptions.keySet()) {
            if (supportedValueKeys.contains(key)) {
                this.parameters.add(new ChannelParameter(key, paramsetDescriptions.get(key)));
            }
        }
    }

    /**
     * Checks whether the channel has only one channel value or not.
     *
     * @return Result of the check.
     */
    public boolean hasSingleValue() {
        return this.parameters.size() == 1;
    }
}