package at.htl.smarthome.api;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.htl.smarthome.entity.Sensor;

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
        traceFile = new File(externalStorageDir + "/" + APP_FOLDER_NAME, "trace.csv");
        if (!traceFile.exists()) {
            try {
                traceFile.createNewFile();
            } catch (IOException e) {
                Log.e(LOG_TAG, "CsvFileManager, createNewFile: " + e.getMessage());
            }
        }
    }

    public static synchronized CsvFileManager getInstance() {
        if (instance == null)
            instance = new CsvFileManager();
        return instance;
    }

    /**
     * Für jedes Monat wird eine Datei mit allen Viertelstundenwerten aller
     * Sensoren angelegt.
     * Existiert die Datei noch nicht, wird sie angelegt.
     * Gespeichert werden jeweils die Werte des Vortages
     * Eine Zeile enthält durch Semikolon getrennt Das Datum YYYY-MM-DD,
     * den Sensornamen und die 96 Viertelstundenwerte.
     *
     * @param sensor
     * @param values
     */
    public void persistYesterdaysMeasurements(Sensor sensor, String dateString, double[] values) {
        Date yesterday = getMeYesterday();  //
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        dateFormat = new SimpleDateFormat("yyyy-MM", Locale.GERMANY);
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
        writeLineToFile(measurementsFile, stringBuilder.toString());
    }

    private String convertDoubleToGermanString(double value) {
        String text = "" + value;
        text = text.replace('.', ',');
        return text;
    }

    private Date getMeYesterday() {
        return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    /**
     * Schreibt die Zeile auf die Datei. Am Beginn der Zeile
     * wird Datum und Uhrzeit eingefügt.
     *
     * @param line
     */
    public void traceLineToFile(String line) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
        String dateTime = dateFormat.format(new java.util.Date());
        line = dateTime + ";" + line + "\n";
        writeLineToFile(traceFile, line);
    }

    /**
     * Schreibt die Zeile auf die Datei. Am Beginn der Zeile
     * wird Datum und Uhrzeit eingefügt.
     *
     * @param file
     * @param line
     */
    public void writeLineToFile(File file, String line) {
        if (file == null) return;
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);  // append
            OutputStreamWriter writer = new OutputStreamWriter(fOut);
            writer.append(line);
            writer.flush();
            writer.close();
            fOut.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "writeLineToFile(): " + e.getMessage());
        }
    }


}
