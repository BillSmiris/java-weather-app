package org.unipi.mpsp2343;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static final String EXIT_PROMPT = "exit";
    private static final Scanner scanner = new Scanner(System.in);
    private static String userInput = "";

    public static void main(String[] args) {
        System.out.println("Welcome to \"Java Weather App\"!\n");

        boolean exit = false;

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
    }

    public static void getWeatherData() {
        System.out.println("\nEnter city: ");
        userInput = scanner.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String timestamp = LocalDateTime.now().format(formatter);

        try {
            String json = APICalls.getCurrentWeatherByCity(userInput);

            try {
                WeatherData weatherData = JsonParsers.getWeatherData(json);
                weatherData.setTimestamp(timestamp);

                weatherData.print();
            }
            catch (JsonParseException e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        catch (IOException e) {
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