package at.htl.smarthome.entity;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * Verwaltet die Messwerte eines Sensors. Quelle kann die Homematic oder auch Werte aus dem Netz
 * sein.
 * Als Messwerte werden Viertelstundenwerte abgespeichert. Die Viertelstundenwerte ergeben
 * sich aus dem Durchschnitt der, in der letzten Viertelstunde eingelangten Messwerte.
 */
public class Sensor {
    private static int DAY_BUFFER_LENGTH = 7;  // eine Woche wird im Hauptspeicher gepuffert
    double[][] values;
    int id;
    private String viewTag;        // Name des Sensors
    private int decimalPlaces;
    private String unit;        // Einheit
    private DateTime actDate;   // Aktuelles Datum
    private int actQuarterOfAnHour; // Aktuelle Viertelstunde
    private List<Double> actQuarterOfAnHourValues;  // aktuelle Werte innerhalb der Viertelstunde
    // ==> werden dann gemittelt

    public Sensor(int id, String viewTag, int decimalPlaces, String unit) {
        this.id = id;
        this.viewTag = viewTag;
        this.decimalPlaces = decimalPlaces;
        this.unit = unit;
        actDate = new DateTime();
        actQuarterOfAnHour = getTimeSlotIndex(actDate);
        actQuarterOfAnHourValues = new LinkedList<>();
        values = new double[DAY_BUFFER_LENGTH][24 * 4];
    }

    /**
     * Aus der aktuellen Zeit wird der Index der Viertelstunde ermittelt
     *
     * @param time -
     * @return Viertelstundenindex
     */
    public static int getTimeSlotIndex(DateTime time) {
        return time.getMinuteOfDay() / 15;
    }

    public String getViewTag() {
        return viewTag;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public String getUnit() {
        return unit;
    }

    /**
     * Füllt das Wertearray am aktuellen Tag im Bereich from - to (exkl)
     * mit dem Wert
     *
     * @param value einzufügender Messwert
     * @param from von-Viertelstunde
     * @param to  bis-Viertelstunde (exklusive)
     */
    private void fillValues(double value, int from, int to) {
        for (int i = from + 1; i < to; i++) {
            values[0][i] = value;
        }

    }

    /**
     * Messwert zu Sensor hinzufügen
     *
     * @param newValue neuer Messwert
     */
    public void addValue(double newValue) {
        DateTime dateTime = new DateTime();
        double lastValue = getValue();
        int quarterOfAnHour = getTimeSlotIndex(dateTime);
        if (dateTime.toDate().after(actDate.toDate())) {  // Tageswechsel
            // Werte des letzten Tages bis Mitternacht auffüllen
            fillValues(lastValue, actQuarterOfAnHour, 24 * 4);
            // ältesten Tag persisitieren
            persistLastDay();
            // Tage verschieben
            for (int i = DAY_BUFFER_LENGTH - 1; i > 0; i--) {
                values[i] = values[i - 1];
            }
            // neuen Tag anlegen
            values[0] = new double[24 * 4];
            // getDayStartValue
            lastValue = getDayStartValue(lastValue);
            // bis zur aktuellen Viertelstunde füllen
            fillValues(lastValue, 0, quarterOfAnHour);
            values[0][quarterOfAnHour] = newValue;
            actQuarterOfAnHourValues.clear();
            actQuarterOfAnHourValues.add(newValue);
            actQuarterOfAnHour = quarterOfAnHour;
        } else {  // Viertelstunde im aktuellen Tag
            if (quarterOfAnHour == actQuarterOfAnHour) {  // neuer Wert für aktuelle Viertelstunde
                actQuarterOfAnHourValues.add(newValue);  // eintragen und Mittelwert neu berechnen
                double sumOfValues = 0;
                for (double v : actQuarterOfAnHourValues) {
                    sumOfValues += v;
                }
                values[0][actQuarterOfAnHour] = sumOfValues / actQuarterOfAnHourValues.size();
            } else {  // Viertelstunde hat sich geändert
                // Viertelstundenwerte, die zwischen der letzten Änderung und jetzt liegen
                // werden mit dem letzten Wert befüllt
                // neue Mittelwertbildung beginnen
                fillValues(newValue, actQuarterOfAnHour, quarterOfAnHour);
                values[0][quarterOfAnHour] = newValue;
                actQuarterOfAnHourValues.clear();
                actQuarterOfAnHourValues.add(newValue);
                actQuarterOfAnHour = quarterOfAnHour;
            }
        }
    }

    /**
     * Mit welchem Wert startet ein Sensor in einen neuen Tag.
     * Reine aktuelle Messwerte behalten den letzten Sensorwertb bei. Kummulierte Werte
     * (Sonnenscheindauer, Regenmenge, ...) werden auf 0 gesetzt. Wird vom jeweiligen
     * Sensor bei Bedarf überschrieben
     *
     * @param actValue defaultmässig geht es mit dem aktuellen Messwert weiter
     * @return Messwert, mit dem der Tag begonnen wird
     */
    protected double getDayStartValue(double actValue) {
        return actValue;
    }

    /**
     * Messwert der aktuellen Viertelstunde zurückliefern
     *
     * @return aktuellen Messwert liefern
     */
    public double getValue() {
        return getValue(actQuarterOfAnHour);
    }

    /**
     * Liefert den Messwert für die gegebene Viertelstunde
     *
     * @param index  Viertelstundenindex
     * @return Messwert der Viertelstunde
     */
    public double getValue(int index) {
        return values[0][index];
    }

    /**
     * Der letzte Tag wird auf die SD-Karte persistiert.
     */
    public void persistLastDay() {
    }
}
