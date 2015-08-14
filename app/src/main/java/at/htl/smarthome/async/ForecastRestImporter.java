package at.htl.smarthome.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import at.htl.smarthome.api.WeatherAPI;
import at.htl.smarthome.entity.DayForecast;
import at.htl.smarthome.repository.WeatherRepository;


/**
 * Die Vorhersagedaten von wetter.com werden per REST ausgelesen
 * und im Repository gespeichert
 */
public class ForecastRestImporter extends AsyncTask<Void, Void, String> {

    private Context context;

    public ForecastRestImporter(Context context) {
        this.context = context;
    }

    /**
     * Im Hintergrund JSON-String lesen
     *
     * @param params
     * @return JSON-String zum Parsen
     */
    @Override
    protected String doInBackground(Void... params) {
        return WeatherAPI.getRestForecast();
    }

    /**
     * JSON-String in die drei Tageswerte parsen und in Repository schreiben.
     *
     * @param jsonString
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
                parseDayIntoRepository(context, dayString, day);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WeatherRepository.getInstance().notifyObservers();
    }


    /**
     * Die Vorhersagedaten eines Tages parsen und im Repository abspeichern.
     *
     * @param context
     * @param dayString
     * @param jsonObject
     * @throws JSONException
     */
    public void parseDayIntoRepository(Context context, String dayString, JSONObject jsonObject) throws JSONException {
        DayForecast dayForecast = new DayForecast();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dayForecast.setDay(formatter.parse(dayString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayForecast.setMinTemperature(jsonObject.getInt("tn"));
        dayForecast.setMaxTemperature(jsonObject.getInt("tx"));
        dayForecast.setStatus(jsonObject.getString("w_txt"));
        dayForecast.setAirPressure(jsonObject.getDouble("pc"));
        dayForecast.setWindDirection(jsonObject.getString("wd_txt"));
        dayForecast.setWindSpeed(jsonObject.getDouble("ws"));
        dayForecast.setIcon(context.getResources().getIdentifier("d" + jsonObject.getString("w"), "drawable", context.getPackageName()));
        WeatherRepository.getInstance().addForecast(dayForecast);
    }
}
