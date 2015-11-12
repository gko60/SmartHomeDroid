package at.htl.smarthome.commandservice;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.htl.smarthome.Utils;
import at.htl.smarthome.entity.Sensor;
import at.htl.smarthome.repository.WeatherRepository;

/**
 * Implementiert die Nutzroutinen, die im Zusammenhang mit
 * der Kommunikation mit der LED-Wall stehen.
 */
public class LedWallService {
    private static final String LOG_TAG = LedWallService.class.getSimpleName();
    private static LedWallService instance = null;

    private String commandType;  // Nächstes zu versendendes Kommando
    private String text;
    private boolean isInfoDateTime = true;

    private LedWallService() {

    }

    public static synchronized LedWallService getInstance() {
        if (instance == null)
            instance = new LedWallService();
        return instance;
    }

    public void setInfoCommand() {
        this.commandType = "INFO";
    }

    public void setTextCommand(String text) {
        commandType = "TEXT";
        this.text = text;
    }

    public String getCommand() {
        String command = "";
        switch (commandType) {
            case "INFO":
                if (isInfoDateTime) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM. HH:mm:ss");
                    command = "T" + sdf.format(new Date());
                } else {
                    Sensor temperatureOut = WeatherRepository.getInstance().getSensor("tag_hmTemperatureOut");
                    command = "T" + Utils.getDoubleString(temperatureOut.getValue(), 1) + " Grad";
                }
                isInfoDateTime = !isInfoDateTime;
                Log.d(LOG_TAG, "getCommand() isInfoDateTime: " + isInfoDateTime);
                break;
            case "TEXT":
                command = "T" + text;
                break;
        }
        return command;
    }
}
