package org.unipi.mpsp2343;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
|----------[APICalls]-----------------|
|This class keeps various functions to|
|make calls to the wttr.in API        |
|wttr.in API.                         |
|-------------------------------------|
*/
public class APICalls {
    /*
    |----------[getCurrentWeatherByCity]------------|
    |This functions gets the current weather for    |
    |a user entered city in json format.            |
    |It throws IO exceptions to indicate errors     |
    |in the format of the recieved response or that |
    |no data where found for the user entered city. |
    |-----------------------------------------------|
    */
    public static String getCurrentWeatherByCity(String city) throws IOException{
        //spaces in the user entered city name are replaced by + signs, so the name can be sent in the url
        city = String.join("+", city.split(" "));
        //format=j2 requests the weather data for the current day in json format
        URL url = new URL("https://wttr.in/" + city + "?format=j2");
        //the data is retrieved and read from the api endpoint and is returned as a json string
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
