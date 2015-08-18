package at.htl.smarthome.entity;

public class HomematicSensor extends Sensor {

    private String address;
    private String valueKey;

    public HomematicSensor(String name, String unit, String address, String valueKey) {
        super(name, unit);
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
