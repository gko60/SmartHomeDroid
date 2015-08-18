package at.htl.smarthome.repository;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.entity.HomematicSensor;
import at.htl.smarthome.entity.Sensor;


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
    private HomematicSensor rainCounter;
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


    private WeatherRepository() {
        temperatureOut = new Sensor("Aussentemperatur", "°");
        airPressure = new Sensor("Luftdruck", "hPa");
        hmTemperatureOut = new HomematicSensor("HmAussentemperaur", "°C", "LEQ0214590:1", "TEMPERATURE");
        humidityOut = new HomematicSensor("Luftfeuchte", "%", "LEQ0214590:1", "HUMIDITY");
        rainCounter = new HomematicSensor("Regenmenge", "mm", "LEQ0214590:1", "RAIN_COUNTER");
        windSpeed = new HomematicSensor("Windgeschwindigkeit", "km/h", "LEQ0214590:1", "WIND_SPEED");
        windDirection = new HomematicSensor("Windrichtung", "°", "LEQ0214590:1", "WIND_DIRECTION");
        brightness = new HomematicSensor("Helligkeit", "%", "LEQ0214590:1", "BRIGHTNESS");
        sunshineDuration = new HomematicSensor("Sonnenscheindauer", "min", "LEQ0214590:1", "SUNSHINEDURATION");
        temperatureLivingRoom = new HomematicSensor("Innentemperatur", "°C", "KEQ0850330:1", "TEMPERATURE");
        humidityLivingRoom = new HomematicSensor("Luftfeuchte Wohnzimmer", "°C", "KEQ0850330:1", "HUMIDITY");
        temperatureCellar = new HomematicSensor("Kellertemperatur", "°C", "LEQ0228587:1", "TEMPERATURE");
        humidityCellar = new HomematicSensor("Luftfeuchte Keller", "°C", "LEQ0228587:1", "HUMIDITY");
        dayForecasts = new ArrayList<>();
        homematicSensors = new HashMap<>();
        homematicSensors.put(hmTemperatureOut.getMapKey(), hmTemperatureOut);
        homematicSensors.put(humidityOut.getMapKey(), humidityOut);
        homematicSensors.put(rainCounter.getMapKey(), rainCounter);
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

    public double getTemperatureOut() {
        return temperatureOut.getValue();
    }

    public double getHmTemperatureOut() {
        return hmTemperatureOut.getValue();
    }

    public double getTemperatureLivingRoom() {
        return temperatureLivingRoom.getValue();
    }

    public double getHumidityLivingRoom() {
        return humidityLivingRoom.getValue();
    }
    public double getAirPressure() {
        return airPressure.getValue();
    }

    public double getHumidityOut() {
        return humidityOut.getValue();
    }

    public double getRainCounter() {
        return rainCounter.getValue();
    }

    public double getWindSpeed() {
        return windSpeed.getValue();
    }

    public double getWindDirection() {
        return windDirection.getValue();
    }

    public double getBrightness() {
        return brightness.getValue();
    }

    public double getSunshineDuration() {
        return sunshineDuration.getValue();
    }

    public double getTemperatureCellar() {
        return temperatureCellar.getValue();
    }

    public double getHumidityCellar() {
        return humidityCellar.getValue();
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
}
