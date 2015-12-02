package at.htl.smarthome.async;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.repository.WeatherRepository;
import at.htl.smarthome.services.WeatherAPI;


/**
 * Die Vorhersagedaten von wetter.com werden per REST ausgelesen
 * und im Repository gespeichert
 */
public class ForecastRestImporter extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = ForecastRestImporter.class.getSimpleName();

    /**
     * Im Hintergrund JSON-String lesen
     *
     * @param params keine
     * @return JSON-String zum Parsen
     */
    @Override
    protected String doInBackground(Void... params) {
        return WeatherAPI.getRestForecast();
    }

    /**
     * JSON-String in die drei Tageswerte parsen und in Repository schreiben.
     *
     * @param jsonString  JsonString
     */
    @Override
    protected void onPostExecute(String jsonString) {
        WeatherRepository.getInstance().clearForecasts();
        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("city").getJSONObject("forecast");
            Iterator<String> days = json.keys();
            while (days.hasNext()) {
                String dayString = days.next();
                JSONObject day = json.getJSONObject(dayString);
                parseDayIntoRepository(dayString, day);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WeatherRepository.getInstance().notifyObservers();
    }


    /**
     * Die Vorhersagedaten eines Tages parsen und im Repository abspeichern.
     *
     * @param dayString     Zu behandelnder Tag
     * @param jsonObject    zugehörige Wetterdaten
     * @throws JSONException
     */
    public void parseDayIntoRepository(String dayString, JSONObject jsonObject) throws JSONException {
        DayForecast dayForecast = new DayForecast();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dayForecast.setDay(formatter.parse(dayString));
            dayForecast.setMinTemperature(jsonObject.getInt("tn"));
            dayForecast.setMaxTemperature(jsonObject.getInt("tx"));
            dayForecast.setStatus(jsonObject.getString("w_txt"));
            dayForecast.setAirPressure(jsonObject.getDouble("pc"));
            dayForecast.setWindDirection(jsonObject.getString("wd_txt"));
            dayForecast.setWindSpeed(jsonObject.getDouble("ws"));
            String fileName = "d" + jsonObject.getString("w");
            //Log.d(LOG_TAG, "parseDayIntoRepository(), IconFileName: " + fileName);
            //dayForecast.setIcon(context.getResources().getIdentifier(fileName, "drawable", context.getPackageName()));
            dayForecast.setIconFileName(fileName);
            WeatherRepository.getInstance().addForecast(dayForecast);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "parseDayIntoRepository(): " + e.getMessage());
        }
    }
}
