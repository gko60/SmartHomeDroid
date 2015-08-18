package com.homematic.bidcos.rpc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Zugriff auf leere Objekte.
 * Es soll vermieden werden, dass leere Objekte
 * mehrfach instanziert werden.
 */
public class Empty {
    /**
     * Leere Objektliste.
     */
    public static final Object[] OBJECT_ARRAY = new Object[]{};

    /**
     * Leere Liste von Zeichenketten.
     */
    public static final String[] STRING_ARRAY = new String[]{};

    /**
     * Leere Map.
     */
    public static final Map<?, ?> MAP = new HashMap<Object, Object>();
}
