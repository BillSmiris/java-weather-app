package org.unipi.mpsp2343;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICalls {
    public static String getCurrentWeatherByCity(String city) throws IOException{
        city = String.join("+", city.split(" "));
        URL url = new URL("https://wttr.in/" + city + "?format=j2");
        StringBuilder json = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            int c;
            while ((c = bufferedReader.read()) != -1) {
                json.append((char) c);
            }
        } catch (IOException e) {
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("City was not found!");
            }
            throw e;
        } finally {
            connection.disconnect();
        }
        return json.toString();
    }
}
