package at.htl.smarthome.entity;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import at.htl.smarthome.repository.WeatherRepository;

/**
 * Verwaltet die Messwerte eines Sensors. Quelle kann die Homematic oder auch Werte aus dem Netz
 * sein.
 * Als Messwerte werden Viertelstundenwerte abgespeichert. Die Viertelstundenwerte ergeben
 * sich aus dem Durchschnitt der, in der letzten Viertelstunde eingelangten Messwerte.
 */
public class Sensor {
    private static final String LOG_TAG = Sensor.class.getSimpleName();

    private static int DAY_BUFFER_LENGTH = 31;  // ein Monat wird im Hauptspeicher gepuffert
    double[][] values;
    int id;
    private String viewTag;        // Name des Sensors
    private int decimalPlaces;
    private String unit;        // Einheit
    private Date actDate;   // Aktuelles Datum
    private int actDateIndex;
    private int actQuarterOfAnHour; // Aktuelle Viertelstunde
    private List<Double> actQuarterOfAnHourValues;  // aktuelle Werte innerhalb der Viertelstunde
    // ==> werden dann gemittelt

    public Sensor(int id, String viewTag, int decimalPlaces, String unit) {
        this.id = id;
        this.viewTag = viewTag;
        this.decimalPlaces = decimalPlaces;
        this.unit = unit;
        DateTime dateTime = new DateTime();
        actDate = dateTime.toDate();
        actDateIndex = getDayIndex(actDate);
        actQuarterOfAnHour = getTimeSlotIndex(dateTime);
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

    public static int getDayIndex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH) - 1;
    }

    public int getId() {
        return id;
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
    private void fillValues(int dayIndex, double value, int from, int to) {
        for (int i = from + 1; i < to; i++) {
            values[dayIndex][i] = value;
        }

    }

    /**
     * Messwert zu Sensor hinzufügen
     *
     * @param newValue neuer Messwert
     */
    public void addValue(double newValue) {
        DateTime newDateTime = new DateTime();
        Date newDate = newDateTime.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDateString = sdf.format(actDate);
        String newDateString = sdf.format(newDate);
        int newQuarterOfAnHour = getTimeSlotIndex(newDateTime);
        if (!lastDateString.equals(newDateString)) {  // Tageswechsel
            // Werte des letzten Tages bis Mitternacht auffüllen
            //CsvFileManager.getInstance().traceLineToFile("Tageswechsel von: " + lastDateString + " auf: " + newDateString);
            //fillValues(actDateIndex, getValue(), actQuarterOfAnHour, 24 * 4);
            // letzten Tag persisitieren
            //CsvFileManager.getInstance().persistYesterdaysMeasurements(this, lastDateString, values[actDateIndex]);
            actDateIndex = getDayIndex(newDate);
            double initialValue = getDayStartValue(getValue());  // bis zur aktuellen Viertelstunde füllen
            // bis zur aktuellen Viertelstunde füllen
            fillValues(actDateIndex, initialValue, 0, newQuarterOfAnHour);
            actQuarterOfAnHourValues.clear();
            actQuarterOfAnHourValues.add(newValue);
            actQuarterOfAnHour = newQuarterOfAnHour;
            actDate = newDate;
        } else {
            // Viertelstunde im aktuellen Tag
            if (newQuarterOfAnHour == actQuarterOfAnHour) {  // neuer Wert für aktuelle Viertelstunde
                actQuarterOfAnHourValues.add(newValue);  // eintragen und Mittelwert neu berechnen
                double sumOfValues = 0;
                for (double v : actQuarterOfAnHourValues) {
                    sumOfValues += v;
                }
                values[actDateIndex][actQuarterOfAnHour] = sumOfValues / actQuarterOfAnHourValues.size();
            } else {  // Viertelstunde hat sich geändert
                // Viertelstundenwerte, die zwischen der letzten Änderung und jetzt liegen
                // werden mit dem letzten Wert befüllt
                // neue Mittelwertbildung beginnen
                fillValues(actDateIndex, getValue(), actQuarterOfAnHour, newQuarterOfAnHour);
                actQuarterOfAnHourValues.clear();
                actQuarterOfAnHourValues.add(newValue);
                actQuarterOfAnHour = newQuarterOfAnHour;
                values[actDateIndex][newQuarterOfAnHour] = newValue;
                WeatherRepository.getInstance().persistQuarterOfAnHour(this, newQuarterOfAnHour, newValue);
            }
        }
    }

    public void setValue(int dayIndex, int quarterHour, double value) {
        values[dayIndex][quarterHour] = value;
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
        return values[actDateIndex][index];
    }

}
