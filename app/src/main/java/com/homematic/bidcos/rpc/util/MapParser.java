package com.homematic.bidcos.rpc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Untersttzt das Auslesen von Maps.
 */
public class MapParser {
    /**
     * Referenz auf die betreffende Map.
     */
    private Map<?, ?> map;

    /**
     * Erstellt und initialisiert einen neuen MapParser.
     *
     * @param map betreffende Map
     */
    public MapParser(Map<?, ?> map) {
        this.map = map;
    }

    /**
     * Liefert eine Zeichenkette.
     *
     * @param key          Schlssel der Zeichenektte.
     * @param defaultValue Standardwert, falls der Schlssel nicht enthalten ist.
     * @return Gelesene Zeichenkette
     */
    public String getString(String key, String defaultValue) {
        return (String) this.get(key, defaultValue);
    }

    /**
     * Liefert eine Ganzzahl.
     *
     * @param key          Schlssel der Ganzzahl.
     * @param defaultValue Standardwert, falls der Schlssel nicht enthalten ist.
     * @return Gelesene Ganzzahl.
     */
    public int getInteger(String key, int defaultValue) {
        return (Integer) this.get(key, defaultValue);
    }

    /**
     * Liefert einen booleschen Wert.
     *
     * @param key          Schlssel des booleschen Wertes.
     * @param defaultValue Standardwert, falls der Schlssel nicht enthaltn ist.
     * @return Gelesener Wert.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return (this.getInteger(key, defaultValue ? 1 : 0) != 0);
    }

    /**
     * Liefert eine Liste von Zeichenketten.
     *
     * @param key Schlssel der Liste.
     * @return Gelesene Liste.
     */
    public String[] getStringList(String key) {
        List<String> result = new ArrayList<>();

        for (Object child : (Object[]) this.get(key, Empty.OBJECT_ARRAY)) {
            result.add(child.toString());
        }

        return Empty.STRING_ARRAY;
    }

    /**
     * Liefert ein Objekt.
     *
     * @param key          Schlssel des Objekts.
     * @param defaultValue Standardwert, falls der Schlssel nicht enthalten ist.
     * @return Gelesenes Objekt.
     */
    public Object get(String key, Object defaultValue) {
        return this.map.containsKey(key) ? this.map.get(key) : defaultValue;
    }
}
