package at.htl.smarthome;

import java.util.Locale;

public class Utils {
    /***
     * Doublewert mit entsprechenden Nachkommastellen gerundet
     * als Text zurückgeben
     *
     * @param value         double-Wert
     * @param decimalPlaces Anzahl Stellen
     * @return Textdarstellung
     */
    public static String getDoubleString(double value, int decimalPlaces) {
        if (decimalPlaces == 0) {
            int number = (int) Math.round(value);
            return String.format(Locale.GERMAN, "%d", number);
        } else {
            String format = "%." + decimalPlaces + "f";
            return String.format(Locale.GERMAN, format, value);
        }
    }

}
