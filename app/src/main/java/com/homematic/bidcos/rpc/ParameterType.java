package com.homematic.bidcos.rpc;

public enum ParameterType {
    BOOL("BOOL"),
    INTEGER("INTEGER"),
    FLOAT("FLOAT"),
    ENUM("ENUM"),
    STRING("STRING"),
    ACTION("ACTION");

    private String name;

    ParameterType(String name) {
        this.name = name;
    }

    public static ParameterType parse(String name) {
        for (ParameterType type : ParameterType.values()) {
            if (name.equals(type.name)) {
                return type;
            }
        }

        return ParameterType.INTEGER;
    }

}
