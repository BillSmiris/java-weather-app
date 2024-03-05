package org.unipi.mpsp2343;

import com.google.gson.JsonParseException;
import org.unipi.mpsp2343.dbResultModels.Average;
import org.unipi.mpsp2343.dbResultModels.Maximum;
import org.unipi.mpsp2343.dbResultModels.Minimum;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static final String EXIT_PROMPT = "exit";//prompt the user has to type to exit the program
    private static final Scanner scanner = new Scanner(System.in);
    private static String userInput = ""; //keeps user input
    private static boolean userInputIsValid = false;

    private static DbProvider dbProvider;

    public static void main(String[] args) {
        System.out.println("Welcome to \"Java Weather App\"!\n");

        boolean exit = false;

        dbProvider = DbProvider.getInstance();
        while(!exit){ //main menu loop
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

    //function that handles retrieving and saving weather data
    public static void getWeatherData() {
        System.out.println("\nEnter city: ");
        userInput = scanner.nextLine().trim(); //get a city as user input
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());//get timestamp of search

        try {
            String json = APICalls.getCurrentWeatherByCity(userInput); //retrieve the data in json format
            WeatherData weatherData = JsonParsers.getWeatherData(json); //parse json weather data
            weatherData.setTimestamp(timestamp); //set timestamp to parsed weather data object
            dbProvider.insertWeatherData(weatherData); //save data to db
            weatherData.print(); //print retrieved data
        }
        catch (IOException | JsonParseException e) {
            System.out.println("\n" + e.getMessage());
        }

        pressEnterToContinue();
    }

    //handles the menu for getting averages
    private static void getAverages() {
        userInputIsValid = false;
        while(!userInputIsValid) {
            System.out.println("\nAverages\n--------\n1. Temperature\n2. Humidity\n3. Wind speed\n4. UV Index\n5. Back\n\nEnter option: ");
            userInput = scanner.nextLine().trim();
            switch (userInput) {
                case "1":
                    if(enterCity("Temperature")) { //prompt user to enter a city. pass relevant header
                        Average average = dbProvider.getAverage("temperature", userInput); //query the db for the required data
                        if(average != null){ //if data is found in the db
                            average.print("temperature", "C"); //print data. pass a human-readable name that indicates what column the data is from
                        }
                        else {
                            System.out.println("\nNo data found!"); //print message if data is not found
                        }
                    }
                    userInputIsValid = true;
                    break;
                case "2":
                    if(enterCity("Humidity")) {
                        Average average = dbProvider.getAverage("humidity", userInput);
                        if(average != null){
                            average.print("humidity", "%");
                        }
                        else {
                            System.out.println("\nNo data found!");
                        }
                    }
                    userInputIsValid = true;
                    break;
                case "3":
                    if(enterCity("Wind speed")) {
                        Average average = dbProvider.getAverage("windSpeed", userInput);
                        if(average != null){
                            average.print("wind speed", "Km/h");
                        }
                        else {
                            System.out.println("\nNo data found!");
                        }
                    }
                    userInputIsValid = true;
                    break;
                case "4":
                    if(enterCity("UV Index")) {
                        Average average = dbProvider.getAverage("uvIndex", userInput);
                        if(average != null){
                            average.print("UV index", "");
                        }
                        else {
                            System.out.println("\nNo data found!");
                        }
                    }
                    userInputIsValid = true;
                    break;
                case "5":
                    userInputIsValid = true;
                    break;
                default:
                    System.out.println("\nInvalid option!");
                    userInputIsValid = false;
            }
        }

        pressEnterToContinue();
    }

    //handles the menu for getting maximums
     private static void getMaximums() {
         userInputIsValid = false;
         while(!userInputIsValid) {
             System.out.println("\nMaximums\n--------\n1. Temperature\n2. Humidity\n3. Wind speed\n4. UV Index\n5. Back\n\nEnter option: ");
             userInput = scanner.nextLine().trim();
             switch (userInput) {
                 case "1":
                     if(enterCity("Temperature")) {
                         Maximum maximum = dbProvider.getMaximum("temperature", userInput);
                         if(maximum != null){
                             maximum.print("temperature", "C");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "2":
                     if(enterCity("Humidity")) {
                         Maximum maximum = dbProvider.getMaximum("humidity", userInput);
                         if(maximum != null){
                             maximum.print("humidity", "%");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "3":
                     if(enterCity("Wind speed")) {
                         Maximum maximum = dbProvider.getMaximum("windSpeed", userInput);
                         if(maximum != null){
                             maximum.print("wind speed", "Km/h");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "4":
                     if(enterCity("UV Index")) {
                         Maximum maximum = dbProvider.getMaximum("uvIndex", userInput);
                         if(maximum != null){
                             maximum.print("UV index", "");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "5":
                     userInputIsValid = true;
                     break;
                 default:
                     System.out.println("\nInvalid option!");
                     userInputIsValid = false;
             }
         }

         pressEnterToContinue();
     }

    //handles the menu for getting minimums
     private static void getMinimums() {
         userInputIsValid = false;
         while(!userInputIsValid) {
             System.out.println("\nMinimums\n--------\n1. Temperature\n2. Humidity\n3. Wind speed\n4. UV Index\n5. Back\n\nEnter option: ");
             userInput = scanner.nextLine().trim();
             switch (userInput) {
                 case "1":
                     if(enterCity("Temperature")) {
                         Minimum minimum = dbProvider.getMinimum("temperature", userInput);
                         if(minimum != null){
                             minimum.print("temperature", "C");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "2":
                     if(enterCity("Humidity")) {
                         Minimum minimum = dbProvider.getMinimum("humidity", userInput);
                         if(minimum != null){
                             minimum.print("humidity", "%");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "3":
                     if(enterCity("Wind speed")) {
                         Minimum minimum = dbProvider.getMinimum("windSpeed", userInput);
                         if(minimum != null){
                             minimum.print("wind speed", "Km/h");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "4":
                     if(enterCity("UV Index")) {
                         Minimum minimum = dbProvider.getMinimum("uvIndex", userInput);
                         if(minimum != null){
                             minimum.print("UV index", "");
                         }
                         else {
                             System.out.println("\nNo data found!");
                         }
                     }
                     userInputIsValid = true;
                     break;
                 case "5":
                     userInputIsValid = true;
                     break;
                 default:
                     System.out.println("\nInvalid option!");
                     userInputIsValid = false;
             }
         }

         pressEnterToContinue();
     }

     //handles city entry loop
     //returns true if user typed a city or chose to not type a city
     //returns false if user chose back
     //header argument is the header of the menu the function displays
     private static boolean enterCity(String header) {
        userInputIsValid = false;
         while(!userInputIsValid) {
             System.out.println("\n"+ header +"\n--------\n1. Per city\n2. Across all cities\n3. Back\n\nEnter option: ");
             userInput = scanner.nextLine().trim();
             switch (userInput) {
                 case "1":
                     System.out.println("\nEnter city name: ");
                     userInput = scanner.nextLine().trim();
                     if (userInput.isEmpty()) {
                         System.out.println("\nCity name should not me empty!");
                         userInputIsValid = false;
                     } else {
                         userInputIsValid = true;
                         return true;
                     }
                     break;
                 case "2":
                     userInput = "";
                     userInputIsValid = true;
                     return true;
                 case "3":
                     userInputIsValid = true;
                     return false;
                 default:
                     System.out.println("\nInvalid option!");
                     userInputIsValid = false;
             }
         }

         return false;
     }

     //function to freeze program execution until user presses enter
     //useful to keep last printed data in place and prevent moving them upwards to display menu
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