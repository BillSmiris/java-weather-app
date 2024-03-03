package org.unipi.mpsp2343;

import com.google.gson.*;
/*
|----------[JsonParsers]-----------------|
|This class keeps various functions to   |
|parse the json data retrieved from the  |
|wttr.in API.                            |
|----------------------------------------|
*/
public class JsonParsers {
    private static final Gson gson = new Gson();
    /*
    |----------[getWeatherData]--------------------|
    |This functions reads the retrieved json data  |
    |in order to collect the required fields to    |
    |build a WeatherData object. It throws an      |
    |exception if any of the expected fields is    |
    |not found, to indicate a problem with the     |
    |retrieved data.                               |
    |----------------------------------------------|
    */
    public static WeatherData getWeatherData(String json) throws JsonParseException {
        //the json data is turned into a jsonElement object
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        //variables to store the required data are intialized
        String city = null; //we also save city so the user can later export weather statistics per city
        Integer temperature = null;
        Integer humidity = null;
        Integer windSpeed = null;
        Integer uvIndex = null;
        String weatherDesc = null;
        //this variable indicates that all the required data were found successfully
        boolean allDataFound = false;
        //any failure to get a piece of data exits the if block
        //although the block breaks nesting rules due to many nested ifs, each failed data retrieval prevents further
        //expensive search operations on the json element
        if(jsonElement.isJsonObject()) {
            JsonArray currentConditionArray = jsonElement.getAsJsonObject().getAsJsonArray("current_condition");
            if (currentConditionArray != null && !currentConditionArray.isEmpty()) {
                JsonObject currentCondition = currentConditionArray.get(0).getAsJsonObject();
                if(currentCondition != null) {
                    temperature = currentCondition.get("temp_C").getAsInt();
                    if(temperature != null){
                        humidity = currentCondition.get("humidity").getAsInt();
                        if(humidity != null) {
                            windSpeed = currentCondition.get("windspeedKmph").getAsInt();
                            if(windSpeed != null) {
                                uvIndex = currentCondition.get("uvIndex").getAsInt();
                                if(uvIndex != null) {
                                    JsonArray weatherDescArray = currentCondition.getAsJsonArray("weatherDesc");
                                    if (weatherDescArray != null && !weatherDescArray.isEmpty()) {
                                        JsonObject weatherDescObject = weatherDescArray.get(0).getAsJsonObject();
                                        if (weatherDescObject != null) {
                                            weatherDesc = weatherDescObject.get("value").getAsString();
                                            if(weatherDesc != null){
                                                JsonArray nearestAreaArray = jsonElement.getAsJsonObject().getAsJsonArray("nearest_area");
                                                if (nearestAreaArray != null && !nearestAreaArray.isEmpty()) {
                                                    JsonObject nearestArea = nearestAreaArray.get(0).getAsJsonObject();
                                                    if(nearestArea != null) {
                                                        JsonArray areaNameArray = nearestArea.getAsJsonArray("areaName");
                                                        if (areaNameArray != null && !areaNameArray.isEmpty()) {
                                                            JsonObject areaNameObject = areaNameArray.get(0).getAsJsonObject();
                                                            if (areaNameObject != null) {
                                                                city = areaNameObject.get("value").getAsString();
                                                                if (city != null) {
                                                                    //indicates that all required data was found
                                                                    allDataFound = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(allDataFound){ //if all data was found, the function returns an object with the collected data
            return new WeatherData(city, temperature, humidity, windSpeed, uvIndex, weatherDesc);
        }
        else { //else it throws an exception to indicate an error with the format or contents of the retrieved json data
            throw new JsonParseException("Invalid response data format!");
        }
    }
}
