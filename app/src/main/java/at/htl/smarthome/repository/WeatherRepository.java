package at.htl.smarthome.repository;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import at.htl.smarthome.entity.Brightness;
import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.entity.HomematicSensor;
import at.htl.smarthome.entity.RainCounterAll;
import at.htl.smarthome.entity.RainToday;
import at.htl.smarthome.entity.Sensor;
import at.htl.smarthome.entity.SunshineDuration;
import at.htl.smarthome.services.CsvFileManager;


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
    private Map<String, Sensor> sensors;


    private WeatherRepository() {
        CsvFileManager.getInstance().traceLineToFile("Repository angelegt!");
        CsvFileManager.getInstance().traceLineToFile("Zweite Zeile");

        sensors = new HashMap<>();
        temperatureOut = new Sensor(1, "tag_temperatureOut", 1, "°");
        airPressure = new Sensor(2, "tag_airPressure", 1, " hPa");
        hmTemperatureOut = new HomematicSensor(3, "tag_hmTemperatureOut", 1, "°", "LEQ0214590:1", "TEMPERATURE");
        humidityOut = new HomematicSensor(4, "tag_humidityOut", 0, "%", "LEQ0214590:1", "HUMIDITY");
        rainToday = new RainToday(6, "tag_rainToday", 1, " mm");
        rainCounterAll = new RainCounterAll(5, "tag_rainCounterAll", 1, " mm", "LEQ0214590:1", "RAIN_COUNTER", rainToday);
        windSpeed = new HomematicSensor(7, "tag_windSpeed", 0, " km/h", "LEQ0214590:1", "WIND_SPEED");
        windDirection = new HomematicSensor(8, "tag_windDirection", 0, "°", "LEQ0214590:1", "WIND_DIRECTION");
        brightness = new Brightness(9, "tag_brightness", 0, "%", "LEQ0214590:1", "BRIGHTNESS");
        sunshineDuration = new SunshineDuration(10, "tag_sunshineDuration", 1, " h", "LEQ0214590:1", "SUNSHINEDURATION");
        temperatureLivingRoom = new HomematicSensor(11, "tag_temperatureLivingRoom", 1, "°", "KEQ0850330:1", "TEMPERATURE");
        humidityLivingRoom = new HomematicSensor(12, "tag_humidityLivingRoom", 0, "%", "KEQ0850330:1", "HUMIDITY");
        temperatureCellar = new HomematicSensor(13, "tag_temperatureCellar", 1, "°", "LEQ0228587:1", "TEMPERATURE");
        humidityCellar = new HomematicSensor(14, "tag_humidityCellar", 0, "%", "LEQ0228587:1", "HUMIDITY");
        sensors.put("tag_temperatureOut", temperatureOut);
        sensors.put("tag_airPressure", airPressure);
        sensors.put("tag_hmTemperatureOut", hmTemperatureOut);
        sensors.put("tag_humidityOut", humidityOut);
        sensors.put("tag_rainCounterAll", rainCounterAll);
        sensors.put("tag_rainToday", rainToday);
        sensors.put("tag_windSpeed", windSpeed);
        sensors.put("tag_windDirection", windDirection);
        sensors.put("tag_brightness", brightness);
        sensors.put("tag_sunshineDuration", sunshineDuration);
        sensors.put("tag_temperatureLivingRoom", temperatureLivingRoom);
        sensors.put("tag_humidityLivingRoom", humidityLivingRoom);
        sensors.put("tag_temperatureCellar", temperatureCellar);
        sensors.put("tag_humidityCellar", humidityCellar);
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
        readMeasurementsFromCsvFile();
    }

    public static Date getMeYesterday() {
        return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    public static synchronized WeatherRepository getInstance() {
        if (repository == null)
            repository = new WeatherRepository();
        return repository;
    }

    private void readMeasurementsFromCsvFile() {
        List<String> lines = CsvFileManager.readMeasurementsFromFile();
        if (lines == null) {
            Log.e(LOG_TAG, "readMeasurementsFromCsvFile(), lines waren null");
            return;
        }
        for (String line : lines) {
            String[] elements = line.split(";");
            if (elements.length < 98) continue;
            Sensor sensor = sensors.get(elements[1]);
            if (sensor != null) {
                String[] dateElements = elements[0].split("-");
                if (dateElements.length != 3) continue;
                int dayIndex = Integer.parseInt(dateElements[2]) - 1;
                boolean isErrorInLine = false;
                for (int i = 2; i < 98 && !isErrorInLine; i++) {
                    String valueString = elements[i].replace(',', '.');
                    try {
                        sensor.setValue(dayIndex, i - 2, Double.parseDouble(valueString));
                    } catch (NumberFormatException ex) {
                        Log.e(LOG_TAG, "initializeSensorsByMeasurements() Sensor: " + sensor.getViewTag() + ", Index: " + (i - 2) + "ValueString: " + valueString);
                        isErrorInLine = true;
                    }
                }
            }
        }
    }

    /***
     * Messwerte des Vortages werden auf die SD-Karte geschrieben
     */
    public void appendMeasurementsToCsvFile(Date day) {
        CsvFileManager.getInstance().appendYesterdaysMeasurements(getSensors(), day);
    }

    public Sensor getSensor(String tagName) {
        return sensors.get(tagName);
    }

    public Sensor[] getSensors() {
        return sensors.values().toArray(new Sensor[sensors.values().size()]);
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
        notifyObservers();
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
     * @param sensor    Sensor
     * @param quarter   Nummer der Viertelstunde (0-95)
     * @param value     Messwert
     */
    public void persistQuarterOfAnHour(Sensor sensor, int quarter, double value) {
        String line = sensor.getViewTag().substring(4);
        line = line + ";" + quarter + ";" + CsvFileManager.convertDoubleToGermanString(value);
        CsvFileManager.getInstance().traceLineToFile(line);
    }

}
