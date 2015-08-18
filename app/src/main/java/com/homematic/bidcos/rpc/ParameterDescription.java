package com.homematic.bidcos.rpc;

import com.homematic.bidcos.rpc.util.Empty;
import com.homematic.bidcos.rpc.util.MapParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterDescription {
    private static final int OPERATION_READ = 0x01;
    private static final int OPERATION_WRITE = 0x02;
    private static final int OPERATION_EVENT = 0x04;

    private static final int FLAG_VISIBLE = 0x01;
    private static final int FLAG_INTERNAL = 0x02;
    private static final int FLAG_TRANSFORM = 0x04;
    private static final int FLAG_SERVICE = 0x08;
    private static final int FLAG_STICKY = 0x10;

    private static final SpecialValue[] EMPTY_LIST = new SpecialValue[]{};

    private String name;
    private ParameterType parameterType;
    private boolean readable;
    private boolean writable;
    private boolean event;
    private boolean visible;
    private boolean internal;
    private boolean transform;
    private boolean service;
    private boolean sticky;
    private Object defaultValue;
    private Object maxValue;
    private Object minValue;
    private String unit;
    private int tabOrder;
    private String control;
    private SpecialValue[] specialValues;
    private String[] valueList;

    public ParameterDescription(String name, Map<?, ?> map) {
        final MapParser parser = new MapParser(map);

        int operations = parser.getInteger("OPERATIONS", 0);
        int flags = parser.getInteger("FLAGS", 0);

        List<SpecialValue> specialValues = new ArrayList<>();
        for (Object item : (Object[]) parser.get("SPECIAL", Empty.OBJECT_ARRAY)) {
            specialValues.add(new SpecialValue((Map<?, ?>) item));
        }

        this.name = name;
        this.parameterType = ParameterType.parse(parser.getString("TYPE", "INTEGER"));
        this.readable = ((operations & OPERATION_READ) != 0);
        this.writable = ((operations & OPERATION_WRITE) != 0);
        this.event = ((operations & OPERATION_EVENT) != 0);
        this.visible = ((flags & FLAG_VISIBLE) != 0);
        this.internal = ((flags & FLAG_INTERNAL) != 0);
        this.transform = ((flags & FLAG_TRANSFORM) != 0);
        this.service = ((flags & FLAG_SERVICE) != 0);
        this.sticky = ((flags & FLAG_STICKY) != 0);
        this.defaultValue = parser.get("DEFAULT", 0);
        this.maxValue = parser.get("MAX", 1);
        this.minValue = parser.get("MIN", 0);
        this.unit = parser.getString("UNIT", "");
        this.tabOrder = parser.getInteger("TAB_ORDER", 0);
        this.control = parser.getString("CONTROL", "");
        this.valueList = parser.getStringList("VALUE_LIST");
        this.specialValues = EMPTY_LIST;
    }

    public String getName() {
        return this.name;
    }

    public ParameterType getType() {
        return this.parameterType;
    }

    public boolean isReadable() {
        return this.readable;
    }

    public boolean isWritable() {
        return this.writable;
    }

    public boolean isEvent() {
        return this.event;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public boolean isTranform() {
        return this.transform;
    }

    public boolean isService() {
        return this.service;
    }

    public boolean isSticky() {
        return this.sticky;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public Object getMaxValue() {
        return this.maxValue;
    }

    public Object getMinValue() {
        return this.minValue;
    }

    public String getUnit() {
        return this.unit;
    }

    public int getTabOrder() {
        return this.tabOrder;
    }

    public String getControl() {
        return this.control;
    }

    public SpecialValue[] getSpecialValues() {
        return this.specialValues;
    }

    public String[] getValueList() {
        return this.valueList;
    }
}
