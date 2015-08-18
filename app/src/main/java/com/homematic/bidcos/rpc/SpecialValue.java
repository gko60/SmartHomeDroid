package com.homematic.bidcos.rpc;

import java.util.Map;

/**
 * Special value (additional value with a speacial meaning).
 */
public class SpecialValue {

    /**
     * ID of the special value.
     */
    private String id;

    /**
     * Associated value.
     */
    private Object value;

    /**
     * Initializes a new instance of the SpecialValue class.
     *
     * @param map ID of the special value.
     */
    public SpecialValue(Map<?, ?> map) {
        this.id = (String) map.get("ID");
        this.value = map.get("VALUES");
    }

    /**
     * Gets the ID of the special value.
     *
     * @return ID of the special value.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the associated value of the special value.
     *
     * @return Associated value of the special value.
     */
    public Object getValue() {
        return this.value;
    }
}
