package at.htl.smarthome.entity;

public class HomematicSensor extends Sensor {

    private String address;
    private String valueKey;

    public HomematicSensor(int id, String viewTag, int decimalPlaces, String unit, String address, String valueKey) {
        super(id, viewTag, decimalPlaces, unit);
        this.address = address;
        this.valueKey = valueKey;
    }

    public String getAddress() {
        return address;
    }

    public String getValueKey() {
        return valueKey;
    }

    public String getMapKey() {
        return address + "-" + valueKey;
    }
}
