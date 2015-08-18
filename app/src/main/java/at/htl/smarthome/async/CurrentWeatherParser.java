package at.htl.smarthome.async;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import at.htl.smarthome.api.WeatherAPI;
import at.htl.smarthome.repository.WeatherRepository;


/**
 * Liest die aktuellen Wetterdaten von wetter.com ein,
 * parst die HTML-Seite und speichert das Ergebnis im Repository
 */
public class CurrentWeatherParser extends AsyncTask<Void, String, String> {
    private static final String LOG_TAG = CurrentWeatherParser.class.getSimpleName();

    private static double parseDouble(String text, int lengthPostfix) {
        text = text.substring(0, text.length() - lengthPostfix);
        String result = "0";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != '.') {  // Tausendertrennzeichen werden überlesen
                if (text.charAt(i) == ',') {  // , als Dezimaltrennzeichen in . umwandeln
                    result += '.';
                } else {
                    result += text.charAt(i);
                }
            }
        }
        return Double.parseDouble(result);
    }

    @Override
    protected String doInBackground(Void... voids) {
        return WeatherAPI.getCurrentWeather();
    }

    @Override
    protected void onPostExecute(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        String text;
        text = doc.getElementsByClass("degree-in").first().text();
        double temperature = parseDouble(text, 3);
        //String status = doc.getElementsByClass("status").first().text();
        String leftBox = doc.getElementsByClass("leftBox").html();
        //Log.d(LOG_TAG, "onPostExecute(), leftBox: " + leftBox);
        int startPos = leftBox.indexOf("wstate_large");
        if (startPos > 0) {
            String weatherIconFileName = "" + leftBox.charAt(startPos + 13);  // wstat... überlesen  ==> d oder n
            startPos += 15;   // hier beginnt Nummer
            while (startPos < leftBox.length() && Character.isDigit(leftBox.charAt(startPos))) {  // Ziffern anhängen
                weatherIconFileName += leftBox.charAt(startPos);
                startPos++;
            }
            //Log.d(LOG_TAG, "onPostExecute(), IconFileName: " + weatherIconFileName);
            WeatherRepository.getInstance().setActualWeatherIconFileName(weatherIconFileName);
        }
        Element details = doc.getElementsByClass("currentValuesTable").first();
        text = details.child(0).child(1).child(3).text();
        double airPressure = parseDouble(text, 4);
        WeatherRepository.getInstance().addTemperatureOut(temperature);
        WeatherRepository.getInstance().addAirPressure(airPressure);
        WeatherRepository.getInstance().notifyUI();
    }
}
