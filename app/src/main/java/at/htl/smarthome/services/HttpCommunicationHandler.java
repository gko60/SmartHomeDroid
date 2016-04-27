package at.htl.smarthome.services;

import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.htl.smarthome.repository.WeatherRepository;

public class HttpCommunicationHandler {
    private static final String LOG_TAG = HttpCommunicationHandler.class.getSimpleName();
    static final int PORT = 5679;

    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();

    public void stopServer(){
        server.stop();
    }


    public void startServer() {
        Log.d(LOG_TAG, "startServer(), listening on Port "+PORT);
        server.get("/", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                Log.d(LOG_TAG, "onRequest()");
                String responseString;
                String component = request.getQuery().getString("Component");
                String sensor = request.getQuery().getString("Sensor");
                String valueString = request.getQuery().getString("Value");
                if (component == null || component.isEmpty()){
                    responseString = "Component is null or empty";
                    Log.e(LOG_TAG, "httpMeasurement() "+responseString);
                }
                else if (sensor == null || sensor.isEmpty()){
                    responseString = "Sensor is null or empty";
                    Log.e(LOG_TAG, "httpMeasurement() "+responseString);
                }
                else if (valueString == null || valueString.isEmpty()){
                    responseString = "Value is null or empty";
                    Log.e(LOG_TAG, "httpMeasurement() "+responseString);
                }
                else{  // Daten sind korrekt
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
                    Date now = new Date();
                    String strDate = sdfDate.format(now);
                    responseString = "OK, "+ String.format("%s,%s,%s,%s", strDate, component, sensor, valueString);
                    Log.d(LOG_TAG, "IncomingMeasurement: " + responseString);
                    WeatherRepository.getInstance().addHomematicSensorValue(component, sensor, valueString);
                }
                response.send(responseString);
            }
        });
        server.listen(mAsyncServer, PORT);
    }

}
