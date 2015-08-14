package at.htl.smarthome.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.entity.Sensor;


/**
 * Repository mit den Wetterdaten von wetter.com (REST und HTTP) und
 * von der Homematic
 */
public class WeatherRepository extends Observable {

    private static WeatherRepository repository;

    private Sensor temperatureOut;
    private Sensor airPressure;

    private List<DayForecast> dayForecasts;


    private WeatherRepository() {
        temperatureOut = new Sensor("Aussentemperatur", "°");
        airPressure = new Sensor("Luftdruck", "hPa");
        dayForecasts = new ArrayList<>();
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

    public double getAirPressure() {
        return airPressure.getValue();
    }


    /**
     * UI soll nicht bei jeder Änderung verständigt werden, falls mehrere
     * Sensordaten gemeinsam eingelesen werden.
     */
    public void notifyUI() {
        notifyObservers();
    }
}
