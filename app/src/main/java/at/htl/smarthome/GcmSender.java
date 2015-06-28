/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.htl.smarthome;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GcmSender {
    public static final String API_KEY = "AIzaSyAiSzrViMbKwaVpLLmrII8g8Yp_bF92_wk";
    private static final String LOG_TAG = GcmSender.class.getSimpleName();

    public static void sendMessage(String message) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jsonGcmData = new JSONObject();
            JSONObject jsonData = new JSONObject();
            jsonData.put("message", message.trim());
            // What to send in GCM message.
            jsonGcmData.put("data", jsonData);
            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jsonGcmData.toString().getBytes());
            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();

            String response = stringBuilder.toString();
            System.out.println(response);
            Log.d(LOG_TAG, "sendMessage() " + message);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "sendMessage() JsonException:  " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "sendMessage() Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
