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
    private String name;        // Name des Sensors
    private String unit;        // Einheit
    private DateTime actDate;   // Aktuelles Datum
    private int actQuarterOfAnHour; // Aktuelle Viertelstunde
    private List<Double> actQuarterOfAnHourValues;  // aktuelle Werte innerhalb der Viertelstunde
    // ==> werden dann gemittelt

    public Sensor(String name, String unit) {
        this.name = name;
        this.unit = unit;
        actDate = new DateTime();
        actQuarterOfAnHour = getTimeSlotIndex(actDate);
        actQuarterOfAnHourValues = new LinkedList<>();
        values = new double[DAY_BUFFER_LENGTH][24 * 4];
    }

    /**
     * Aus der aktuellen Zeit wird der Index der Viertelstunde ermittelt
     *
     * @param time
     * @return
     */
    public static int getTimeSlotIndex(DateTime time) {
        return time.getMinuteOfDay() / 15;
    }

    /**
     * Füllt das Wertearray am aktuellen Tag im Bereich from - to (exkl)
     * mit dem Wert
     *
     * @param value
     * @param from
     * @param to
     */
    private void fillValues(double value, int from, int to) {
        for (int i = from + 1; i < to; i++) {
            values[0][i] = value;
        }

    }

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
     * Messwert der aktuellen Viertelstunde zurückliefern
     *
     * @return
     */
    public double getValue() {
        return getValue(actQuarterOfAnHour);
    }

    /**
     * Liefert den Messwert für die gegebene Viertelstunde
     *
     * @param index
     * @return
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
