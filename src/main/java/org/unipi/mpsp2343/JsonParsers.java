package org.unipi.mpsp2343;

import com.google.gson.*;

public class JsonParsers {
    private static final Gson gson = new Gson();
    public static WeatherData getWeatherData(String json) throws JsonParseException {
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);

        String city = null;
        Integer temperature = null;
        Integer humidity = null;
        Integer windSpeed = null;
        Integer uvIndex = null;
        String weatherDesc = null;

        boolean allDataFound = false;

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

        if(allDataFound){
            return new WeatherData(city, temperature, humidity, windSpeed, uvIndex, weatherDesc);
        }
        else {
            throw new JsonParseException("Invalid response data format!");
        }
    }
}
