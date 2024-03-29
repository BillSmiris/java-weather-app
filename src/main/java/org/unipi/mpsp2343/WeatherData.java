package org.unipi.mpsp2343;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/*
|----------[WeatherData]-----------------|
|This class represents the weather data  |
|retrieved for the wttr.in API. It has   |
|fields for the project required info,   |
|for the timestamp and the city that the |
|user searched the data for.             |
|----------------------------------------|
*/
public class WeatherData {
    private String city;
    private int temperature;
    private int humidity;
    private int windSpeed;
    private int uvIndex;
    private String weatherDesc;
    private Timestamp timestamp;
    //no args constructor
    public WeatherData() {
    }
    //constructor for the data read form the json response
    public WeatherData(String city, int temperature, int humidity, int windSpeed, int uvIndex, String weatherDesc) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.uvIndex = uvIndex;
        this.weatherDesc = weatherDesc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    //formats the search timestamp to a useful format for printing
    public String getFormattedTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.timestamp);
    }

    //prints weather data in structured human-readable format
    public void print(){
        System.out.println("\nCurrent weather for " + this.city);
        System.out.println("\n----------------------------");
        System.out.println("\nTemperature: " + this.temperature + "C");
        System.out.println("\nHumidity: " + this.humidity + "%");
        System.out.println("\nWind speed: " + this.windSpeed + "Km/h");
        System.out.println("\nUV Index: " + this.uvIndex);
        System.out.println("\nDescription: " + this.weatherDesc);
        System.out.println("\n----------------------------");
        System.out.println("\nSearch timestamp: " + this.getFormattedTimestamp());
        System.out.println("\n----------------------------");
    }
}