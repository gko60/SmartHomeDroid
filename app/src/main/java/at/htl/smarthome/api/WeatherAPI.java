package at.htl.smarthome.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    public static final String PROJECT_NAME = "weatherviewerfleimgruber";
    public static final String API_KEY = "496db1fcea45119b54e1674aedaa7c1e";

    public static final String LEONDING = "ATAT30331";
    //md5(PROJECT_NAME+API_KEY+LEONDING)
    public static final String CHECKSUM = "639510166cacfc4b386bc3890e31e7f4";

    public static final String REST_URL = "http://api.wetter.com/forecast/weather/city/" + LEONDING + "/project/" +
            PROJECT_NAME + "/cs/" + CHECKSUM + "/?output=json";

    public static final String CURRENT_WEATHER = "http://at.wetter.com/wetter_aktuell/aktuelles_wetter/oesterreich/leonding/untergaumberg/ATAT30331020.html";

    /**
     * Liest per GET-Request Daten von einem Webserver aus.
     * Wird für REST und HTTP verwendet
     *
     * @param url
     * @return // Response des Servers
     */
    private static String httpGet(String url) {
        StringBuilder builder = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return builder.toString();
    }

    /**
     * Liest die Vorhersagedaten per REST aus
     *
     * @return // JSON-String mit den Vorhersagedaten
     */
    public static String getRestForecast() {
        return httpGet(REST_URL);
    }

    /**
     * Parst das aktuelle Wetter von der Webseite wetter.com
     *
     * @return
     */
    public static String getCurrentWeather() {
        return httpGet(CURRENT_WEATHER);
    }
}
