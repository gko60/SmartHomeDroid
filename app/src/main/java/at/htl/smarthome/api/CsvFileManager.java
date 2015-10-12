package at.htl.smarthome.api;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import at.htl.smarthome.entity.Sensor;
import at.htl.smarthome.repository.WeatherRepository;

/**
 * Stellt die Routinen zum Lesen und Schreiben der Textdateien (Tracing, Persistenz der Sensordaten, ...)
 * zur Verfügung
 */
public class CsvFileManager {
    private static final String LOG_TAG = CsvFileManager.class.getSimpleName();
    private static final String APP_FOLDER_NAME = "SMARTHOME";
    private static final String TRACE_FILE_NAME = "trace.csv";
    private static CsvFileManager instance = null;
    private File traceFile;


    private CsvFileManager() {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        // create directory if doesnt exist
        File smarthomeDirectory = new File(externalStorageDir + "/" + APP_FOLDER_NAME);
        if (!smarthomeDirectory.exists()) {
            if (smarthomeDirectory.mkdirs()) {
                traceFile = new File(smarthomeDirectory, "trace.csv");
                if (!traceFile.exists()) {
                    try {
                        if (!traceFile.createNewFile()) {
                            Log.e(LOG_TAG, "CsvFileManager, createNewFile ging schief");
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "CsvFileManager, createNewFile: " + e.getMessage());
                    }
                }
            } else {
                Log.e(LOG_TAG, "CsvFileManager, mkdirs() ging schief: ");
            }

        }
    }

    public static synchronized CsvFileManager getInstance() {
        if (instance == null)
            instance = new CsvFileManager();
        return instance;
    }

    /**
     * Wandelt die US-Codierung des Dezimaltrennzeichens in
     * einen Beistrich um.
     *
     * @param value umzuwandelnder Wert
     * @return Doublestring mit Komma
     */
    public static String convertDoubleToGermanString(double value) {
        String text = "" + value;
        text = text.replace('.', ',');
        return text;
    }

    /**
     * Messwerte des aktuellen Monats einlesen
     *
     * @return Aktuelle Messwerte als Text (zeilenweise)
     */
    public static List<String> readMeasurementsFromFile() {
        LinkedList<String> lines = new LinkedList<>();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.GERMANY);
        String yearMonthFileName = dateFormat.format(date) + ".csv";
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File measurementsFile = new File(externalStorageDir + "/" + APP_FOLDER_NAME, yearMonthFileName);
        try {
            FileInputStream fIn = new FileInputStream(measurementsFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            fIn.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "readMeasurementsFromFile(): " + e.getMessage());
        }
        return lines;
    }

    /**
     * Für jedes Monat wird eine Datei mit allen Viertelstundenwerten aller
     * Sensoren angelegt.
     * Existiert die Datei noch nicht, wird sie angelegt.
     * Gespeichert werden jeweils die Werte des Vortages
     * Eine Zeile enthält durch Semikolon getrennt Das Datum YYYY-MM-DD,
     * den Sensornamen und die 96 Viertelstundenwerte.
     *
     * @param sensor  Sensor
     * @param values  Messwerte des Sensors
     */
    public void persistYesterdaysMeasurements(Sensor sensor, String dateString, double[] values) {
        Date yesterday = WeatherRepository.getMeYesterday();  //
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.GERMANY);
        String yearMonthFileName = dateFormat.format(yesterday) + ".csv";
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File measurementsFile = new File(externalStorageDir + "/" + APP_FOLDER_NAME, yearMonthFileName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dateString);
        stringBuilder.append(';');
        stringBuilder.append(sensor.getViewTag());
        stringBuilder.append(';');
        for (int i = 0; i < 96; i++) {
            stringBuilder.append(convertDoubleToGermanString(sensor.getValue(i)));
            if (i + 1 < 96) {
                stringBuilder.append(';');
            }
        }
        writeToFile(measurementsFile, stringBuilder.toString());
    }

    /**
     * Für jedes Monat wird eine Datei mit allen Viertelstundenwerten aller
     * Sensoren angelegt.
     * Existiert die Datei noch nicht, wird sie angelegt.
     * Gespeichert werden jeweils die Werte des Vortages
     * Eine Zeile enthält durch Semikolon getrennt Das Datum YYYY-MM-DD,
     * den Sensornamen und die 96 Viertelstundenwerte.
     *
     * @param sensors Sensoren, die zu persistieren sind
     */
    public void appendYesterdaysMeasurements(Sensor[] sensors, Date day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.GERMANY);
        String yearMonthFileName = dateFormat.format(day) + ".csv";
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File measurementsFile = new File(externalStorageDir + "/" + APP_FOLDER_NAME, yearMonthFileName);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        String dateString = dateFormat.format(day);
        StringBuilder stringBuilder = new StringBuilder();
        for (Sensor sensor : sensors) {
            stringBuilder.append(dateString);
            stringBuilder.append(';');
            stringBuilder.append(sensor.getViewTag());
            stringBuilder.append(';');
            for (int j = 0; j < 96; j++) {
                stringBuilder.append(convertDoubleToGermanString(sensor.getValue(j)));
                if (j + 1 < 96) {
                    stringBuilder.append(';');
                }
            }
            stringBuilder.append("\n");
        }
        writeToFile(measurementsFile, stringBuilder.toString());
    }

    /**
     * Schreibt die Zeile auf die Datei. Am Beginn der Zeile
     * wird Datum und Uhrzeit eingefügt.
     *
     * @param line zu tracende Zeile
     */
    public void traceLineToFile(String line) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
        String dateTime = dateFormat.format(new java.util.Date());
        line = dateTime + ";" + line;
        writeToFile(traceFile, line);
    }

    /**
     * Schreibt die Zeile auf die Datei. Am Beginn der Zeile
     * wird Datum und Uhrzeit eingefügt.
     *
     * @param file  Datei, auf die geschrieben wird
     * @param line  zu schreibende Zeile
     */
    public void writeToFile(File file, String line) {
        if (file == null) return;
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);  // append
            OutputStreamWriter writer = new OutputStreamWriter(fOut);
            writer.append(line);
            writer.flush();
            writer.close();
            fOut.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "writeToFile(): " + e.getMessage());
        }
    }


}
