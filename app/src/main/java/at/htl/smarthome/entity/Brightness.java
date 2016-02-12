package at.htl.smarthome.entity;

/**
 * Ermittelt die Helligkeit laut Homematic.
 * Umrechnung in Prozent. Der gesamte Wertebereich liegt zwischen 0 und 255
 */
public class Brightness extends HomematicSensor {

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
        super.addValue(newValue / 255.0);
    }
}
