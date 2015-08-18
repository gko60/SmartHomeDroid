package com.homematic.bidcos.rpc;

/**
 * Type of logging.
 */
public enum LogLevel {
    /**
     * Log everything.
     */
    LOG_ALL(0),

    /**
     * Log debug messages.
     */
    LOG_DEBUG(1),


    /**
     * Log information messages.
     */
    LOG_INFO(2),

    /**
     * Log notice messages.
     */
    LOG_NOTICE(3),

    /**
     * Log warning messages.
     */
    LOG_WARNING(4),

    /**
     * Log error messages.
     */
    LOG_ERROR(5),

    /**
     * Log fatal error messages.
     */
    LOG_FATAL_ERROR(6);

    /**
     * Current value of the enumeration.
     */
    private int value;

    /**
     * Initializes a new instance of the LogLevel enum.
     *
     * @param value Value of logging.
     */
    LogLevel(int value) {
        this.value = value;
    }

    /**
     * Parses value from integer.
     *
     * @param value Value of logging.
     * @return Parsed logging level.
     */
    public static LogLevel parse(int value) {
        for (LogLevel level : LogLevel.values()) {
            if (level.value == value) {
                return level;
            }
        }

        return LogLevel.LOG_ALL;
    }

    /**
     * Gets the current value.
     *
     * @return Current value.
     */
    public int getValue() {
        return this.value;
    }
}
