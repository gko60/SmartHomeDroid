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
    private static final int PAGE_DATE = 0;
    private static final int PAGE_TIME = 1;
    private static final int PAGE_TEMPERATURE = 2;
    private static LedWallService instance = null;
    private String commandType;  // Nächstes zu versendendes Kommando
    private String text;
    private int pageNumber = 0;

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

    public void setLightCommand(int brightness) {
        commandType = "LIGHT";
        brightness = Math.min(90, brightness); // 100% schafft die Stromversorgung nicht
        this.text = "255 255 255 " + brightness;
        Log.d(LOG_TAG, " setLightCommand: " + text);
    }

    public void setColorPickerCommand(int r, int g, int b) {
        commandType = "COLORPICKER";
        this.text = r + " " + g + " " + b + " 30";
        Log.d(LOG_TAG, " setColorPickerCommand: " + text);
    }


    public void setTextCommand(String text) {
        commandType = "FADETEXT";
        this.text = text;
    }

    public String getCommand() {
        String command = "";
        SimpleDateFormat sdf;
        switch (commandType) {
            case "INFO":
                switch (pageNumber) {
                    case 0:
                        sdf = new SimpleDateFormat("dd.MM.yyyy");
                        command = "T" + sdf.format(new Date());
                        break;
                    case 1:
                        sdf = new SimpleDateFormat("HH:mm:ss");
                        command = "T" + sdf.format(new Date());
                        break;
                    case 2:
                        Sensor temperatureOut = WeatherRepository.getInstance().getSensor("tag_hmTemperatureOut");
                        command = "T" + Utils.getDoubleString(temperatureOut.getValue(), 1) + " Grad";
                        break;
                    case 3:
                        Sensor powerMeter = WeatherRepository.getInstance().getSensor("tag_hmPowerMeter");
                        command = "T" + Utils.getDoubleString(powerMeter.getValue(), 0) + " Watt";
                        break;
                }
                pageNumber++;
                if (pageNumber >= 4) {
                    pageNumber = 0;
                }
                Log.d(LOG_TAG, "getCommand() pageNumber: " + pageNumber);
                break;
            case "FADETEXT":
                command = "S" + text;
                break;
            case "COLORPICKER":
                command = "C " + text;
                break;
            case "LIGHT":
                command = "C " + text;
                break;
        }
        return command;
    }
}
