package at.htl.smarthome.repository;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import at.htl.smarthome.api.CsvFileManager;
import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.entity.HomematicSensor;
import at.htl.smarthome.entity.RainToday;
import at.htl.smarthome.entity.Sensor;
import at.htl.smarthome.entity.SunshineDuration;


/**
 * Repository mit den Wetterdaten von wetter.com (REST und HTTP) und
 * von der Homematic
 */
public class WeatherRepository extends Observable {
    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();
    private static WeatherRepository repository;
    private String actualWeatherIconFileName;
    private Sensor temperatureOut;
    private Sensor airPressure;
    private HomematicSensor hmTemperatureOut;
    private HomematicSensor humidityOut;
    private HomematicSensor rainCounterAll;
    private RainToday rainToday;
    private HomematicSensor windSpeed;
    private HomematicSensor windDirection;
    private HomematicSensor brightness;
    private HomematicSensor sunshineDuration;
    private HomematicSensor temperatureLivingRoom;
    private HomematicSensor humidityLivingRoom;
    private HomematicSensor temperatureCellar;
    private HomematicSensor humidityCellar;

    private List<DayForecast> dayForecasts;
    private Map<String, HomematicSensor> homematicSensors;
    private List<Sensor> sensors;


    private WeatherRepository() {
        CsvFileManager.getInstance().traceLineToFile("Repository angelegt!");
        CsvFileManager.getInstance().traceLineToFile("Zweite Zeile");

        sensors = new ArrayList<>();
        temperatureOut = new Sensor(1, "tag_temperatureOut", 1, "°");
        airPressure = new Sensor(2, "tag_airPressure", 1, " hPa");
        hmTemperatureOut = new HomematicSensor(3, "tag_hmTemperatureOut", 1, "°", "LEQ0214590:1", "TEMPERATURE");
        humidityOut = new HomematicSensor(4, "tag_humidityOut", 0, "%", "LEQ0214590:1", "HUMIDITY");
        rainCounterAll = new HomematicSensor(5, "tag_rainCounterAll", 1, " mm", "LEQ0214590:1", "RAIN_COUNTER");
        rainToday = new RainToday(6, "tag_rainToday", 1, " mm");
        windSpeed = new HomematicSensor(7, "tag_windSpeed", 0, " km/h", "LEQ0214590:1", "WIND_SPEED");
        windDirection = new HomematicSensor(8, "tag_windDirection", 0, "°", "LEQ0214590:1", "WIND_DIRECTION");
        brightness = new HomematicSensor(9, "tag_brightness", 0, "%", "LEQ0214590:1", "BRIGHTNESS");
        sunshineDuration = new SunshineDuration(10, "tag_sunshineDuration", 1, " h", "LEQ0214590:1", "SUNSHINEDURATION");
        temperatureLivingRoom = new HomematicSensor(11, "tag_temperatureLivingRoom", 1, "°", "KEQ0850330:1", "TEMPERATURE");
        humidityLivingRoom = new HomematicSensor(12, "tag_humidityLivingRoom", 0, "%", "KEQ0850330:1", "HUMIDITY");
        temperatureCellar = new HomematicSensor(13, "tag_temperatureCellar", 1, "°", "LEQ0228587:1", "TEMPERATURE");
        humidityCellar = new HomematicSensor(14, "tag_humidityCellar", 0, "%", "LEQ0228587:1", "HUMIDITY");
        sensors.add(temperatureOut);
        sensors.add(airPressure);
        sensors.add(hmTemperatureOut);
        sensors.add(humidityOut);
        sensors.add(rainCounterAll);
        sensors.add(rainToday);
        sensors.add(windSpeed);
        sensors.add(windDirection);
        sensors.add(brightness);
        sensors.add(sunshineDuration);
        sensors.add(temperatureLivingRoom);
        sensors.add(humidityLivingRoom);
        sensors.add(temperatureCellar);
        sensors.add(humidityCellar);
        dayForecasts = new ArrayList<>();
        homematicSensors = new HashMap<>();
        homematicSensors.put(hmTemperatureOut.getMapKey(), hmTemperatureOut);
        homematicSensors.put(humidityOut.getMapKey(), humidityOut);
        homematicSensors.put(rainCounterAll.getMapKey(), rainCounterAll);
        homematicSensors.put(windSpeed.getMapKey(), windSpeed);
        homematicSensors.put(windDirection.getMapKey(), windDirection);
        homematicSensors.put(brightness.getMapKey(), brightness);
        homematicSensors.put(sunshineDuration.getMapKey(), sunshineDuration);
        homematicSensors.put(temperatureLivingRoom.getMapKey(), temperatureLivingRoom);
        homematicSensors.put(humidityLivingRoom.getMapKey(), humidityLivingRoom);
        homematicSensors.put(temperatureCellar.getMapKey(), temperatureCellar);
        homematicSensors.put(humidityCellar.getMapKey(), humidityCellar);
    }

    public static synchronized WeatherRepository getInstance() {
        if (repository == null)
            repository = new WeatherRepository();
        return repository;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public List<DayForecast> getDayForecasts() {
        return dayForecasts;
    }

    public void addForecast(DayForecast dayForecast) {
        dayForecasts.add(dayForecast);
        setChanged();
    }

    public void clearForecasts() {
        dayForecasts.clear();
    }

    public String getActualWeatherIconFileName() {
        return actualWeatherIconFileName;
    }

    public void setActualWeatherIconFileName(String actualWeatherIconFileName) {
        this.actualWeatherIconFileName = actualWeatherIconFileName;
    }

    public void addTemperatureOut(double value) {
        temperatureOut.addValue(value);
        setChanged();
    }

    public void addAirPressure(double value) {
        airPressure.addValue(value);
        setChanged();
    }


    public void addHomematicSensorValue(String address, String valueKey, String stringValue) {
        Log.d(LOG_TAG, "addHomematicSensorValue: " + address + "," + valueKey + ":" + stringValue);
        String key = address + "-" + valueKey;
        if (homematicSensors.containsKey(key)) {
            HomematicSensor sensor = homematicSensors.get(key);
            double value = Double.parseDouble(stringValue);
            sensor.addValue(value);
            Log.d(LOG_TAG, "addHomematicSensorValue() Value in Messwerte eingefuegt");
        }
    }

    /**
     * UI soll nicht bei jeder Änderung verständigt werden, falls mehrere
     * Sensordaten gemeinsam eingelesen werden.
     */
    public void notifyUI() {
        notifyObservers();
    }



    /***
     * Schreibt einen Viertelstundenwert auf die SD-Karte
     *
     * @param sensor
     * @param quarter
     * @param value
     */
    public void persistQuarterOfAnHour(Sensor sensor, int quarter, double value) {
        String line = sensor.getViewTag().substring(4);
        line = line + ";" + quarter + ";" + value;
        CsvFileManager.getInstance().traceLineToFile(line);
    }

}
