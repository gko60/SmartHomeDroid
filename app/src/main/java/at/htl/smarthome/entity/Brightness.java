package at.htl.smarthome.entity;

import android.util.Log;

/**
 * Ermittelt die Helligkeit laut Homematic.
 * Umrechnung in Prozent. Der gesamte Wertebereich liegt zwischen 0 und 255
 */
public class Brightness extends HomematicSensor {
    private static final String LOG_TAG = Brightness.class.getSimpleName();

    private Sensor brightness;  // verständigt den Tageszähler bei jeder Meldung

    public Brightness(int id, String viewTag, int decimalPlaces, String unit, String address, String valueKey) {
        super(id, viewTag, decimalPlaces, unit, address, valueKey);
    }

    /**
     * Der Helligkeitszähler wird bei Änderungen  verständigt.
     *
     * @param newValue
     */
    @Override
    public void addValue(double newValue) {
        Log.e(LOG_TAG, "addValue(), newValue: " + newValue);
        super.addValue(newValue / 2.55);
    }
}
