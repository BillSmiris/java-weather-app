package org.unipi.mpsp2343;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static final String EXIT_PROMPT = "exit";
    private static final Scanner scanner = new Scanner(System.in);
    private static String userInput = "";

    private static DbProvider dbProvider;

    public static void main(String[] args) {
        System.out.println("Welcome to \"Java Weather App\"!\n");

        boolean exit = false;

        dbProvider = DbProvider.getInstance();
        while(!exit){
            System.out.println("\nMenu\n------\n1. Get weather data\n2. Get averages\n3. Get maximums\n4. Get minimums\n\nType \"exit\" to exit the app.\n\nEnter option: ");
            userInput = scanner.nextLine().trim();
            switch (userInput) {
                case "1":
                    getWeatherData();
                    break;
                case "2":
                    getAverages();
                    break;
                case "3":
                    getMaximums();
                    break;
                case "4":
                    getMinimums();
                    break;
                default:
                    if(userInput.equalsIgnoreCase(EXIT_PROMPT)){
                        exit = true;
                    }
                    else {
                        System.out.println("\nInvalid option!");
                    }
            }
        }
        dbProvider.close();
    }

    public static void getWeatherData() {
        System.out.println("\nEnter city: ");
        userInput = scanner.nextLine().trim();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        try {
            String json = APICalls.getCurrentWeatherByCity(userInput);
            WeatherData weatherData = JsonParsers.getWeatherData(json);
            weatherData.setTimestamp(timestamp);
            dbProvider.insertWeatherData(weatherData);
            weatherData.print();
        }
        catch (IOException | JsonParseException e) {
            System.out.println("\n" + e.getMessage());
        }

        pressEnterToContinue();
    }

    private static void getAverages() {
        pressEnterToContinue();
    }
     private static void getMaximums() {
         pressEnterToContinue();
     }

     private static void getMinimums() {
         pressEnterToContinue();
     }

     private static void pressEnterToContinue() {
         System.out.println("\nPress enter to continue...");
         try
         {
             System.in.read();
         }
         catch(Exception e)
         {

         }
     }
}