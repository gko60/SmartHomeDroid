package de.elv.homematic.android;

import com.homematic.bidcos.rpc.ParameterDescription;
import com.homematic.bidcos.rpc.ParameterType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Single parameter of a channel.
 */
public class ChannelParameter implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Key of the channel value.
     * Example: LEVEL (see list of supported channel values).
     */
    private String key;

    /**
     * Type of the channel value (f.e. INTEGER or FLOAT).
     */
    private ParameterType type;

    /**
     * Minimal value of the channel value.
     */
    private Object min;

    /**
     * Maximal value of the channel value.
     */
    private Object max;

    /**
     * Current value.
     */
    private Object value;

    /**
     * A value indicating whether the value is writable or not.
     */
    private boolean writable;

    /**
     * A value indicating whether the value is initialized or not.
     */
    private boolean isInitialized;

    /**
     * Initializes a new instance of the ChannelParameter class.
     *
     * @param key                 Key of the channel value.
     * @param paramsetDescription Description of the channel value.
     */
    public ChannelParameter(String key, ParameterDescription paramsetDescription) {
        this.writable = true;
        this.isInitialized = false;

        this.setKey(key);
        this.parseParamsetDescription(paramsetDescription);
    }

    /**
     * Gets the first channel value with the passed type.
     *
     * @param parameters    List of channel value to browse.
     * @param parameterType Type of the wished channel value.
     * @return First channel value with the passed type.
     */
    public static ChannelParameter getChannelParameters(ArrayList<ChannelParameter> parameters, ParameterType parameterType) {
        for (ChannelParameter channelParameter : parameters) {
            if (channelParameter.getType() == parameterType) {
                return channelParameter;
            }
        }

        return null;
    }

    /**
     * Gets the key of the parameter.
     *
     * @return Key of the parameter.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key of the parameter.
     *
     * @param key Key to set.
     */
    public void setKey(String key) {
        this.key = key;
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
     * Sets the value of the parameter. The value gets
     * checked in regard of correct type, minimal and maximal
     * value.
     *
     * @param value Value to set.
     * @return The Value was set or not.
     */
    public synchronized boolean setValue(Object value) {
        switch (this.getType()) {
            case BOOL:
                if (!(value instanceof Boolean)) {
                    return false;
                }

                this.value = value;
                return true;

            case FLOAT:
                if (value instanceof Float || value instanceof Double || value instanceof Integer) {
                    Object parsedValue;
                    if (value instanceof Float) {
                        parsedValue = value;
                    } else if (value instanceof Double) {
                        parsedValue = Float.parseFloat(value.toString());
                    } else {
                        Integer iValue = (Integer) value;
                        parsedValue = (float) iValue / 100;
                    }

                    if ((Float) parsedValue >= (Float) this.min && (Float) parsedValue <= (Float) this.max) {
                        this.value = parsedValue;
                    } else if ((Float) parsedValue < (Float) this.min) {
                        this.value = this.min;
                    } else {
                        this.value = this.max;
                    }

                    return true;
                } else {
                    return false;
                }

            case INTEGER:
                if (value instanceof Integer) {
                    if ((Integer) value >= (Integer) this.getMin() && (Integer) value <= (Integer) this.getMax()) {
                        this.value = value;
                    } else if ((Integer) value < (Integer) this.min) {
                        this.value = this.min;
                    } else {
                        this.value = this.max;
                    }

                    return true;
                } else {
                    return false;
                }

            default:
                return false;
        }
    }

    /**
     * Gets the type of the parameter.
     *
     * @return Type of the parameter.
     */
    public ParameterType getType() {
        return type;
    }

    /**
     * Sets the type of the parameter.
     *
     * @param type Type to set.
     */
    public void setType(ParameterType type) {
        this.type = type;
    }

    /**
     * Gets the minimal value of the parameter.
     *
     * @return Minimal value of the parameter.
     */
    public Object getMin() {
        return this.min;
    }

    /**
     * Sets the minimal value of the parameter.
     *
     * @param min Minimal value to set.
     */
    public void setMin(Object min) {
        if (min instanceof Double) {
            this.min = ((Number) min).floatValue();
        } else {
            this.min = min;
        }
    }

    /**
     * Gets the maximal value of the channel.
     *
     * @return Maximal value of the channel.
     */
    public Object getMax() {
        return this.max;
    }

    /**
     * Sets the maximal value of the channel.
     *
     * @param max Maximal value to set.
     */
    public void setMax(Object max) {
        if (max instanceof Double) {
            this.max = ((Number) max).floatValue();
        } else {
            this.max = max;
        }
    }

    /**
     * Gets a value indicating whether the parameter value is writable or not.
     *
     * @return A value indicating whether the parameter value is writable or not.
     */
    public boolean isWritable() {
        return this.writable;
    }

    /**
     * Sets a value indicating whether the parameter value is writable or not.
     *
     * @param writable A value indicating whether the parameter value is writable or not.
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     * Gets a value indicating whether the parameter value is initialized or not.
     *
     * @return A value indicating whether the parameter value is initialized or not.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Sets a value indicating whether the parameter value is initialized or not.
     */
    public void setInitialized() {
        this.isInitialized = true;
    }

    /**
     * Parses a parameter description and takes over its preferences.
     *
     * @param paramsetDescription Parameter description to parse.
     */
    private void parseParamsetDescription(ParameterDescription paramsetDescription) {
        this.setType(paramsetDescription.getType());

        this.setMin(paramsetDescription.getMinValue());
        this.setMax(paramsetDescription.getMaxValue());
        this.setWritable(paramsetDescription.isWritable());
    }
}
