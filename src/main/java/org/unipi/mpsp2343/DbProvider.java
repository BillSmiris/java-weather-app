package org.unipi.mpsp2343;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
|----------[DbProvider]-----------------------|
|This class keeps all required functions      |
|to connect to a derby database and           |
|perform queries on it. It contains functions |
|to save weather data in the db and export    |
|statistics from the save data. The class     |
|conforms to the "Singleton" design pattern,  |
|so there is a single shared instance in all  |
|parts of the program and not open many db    |
|connections.                                 |
|---------------------------------------------|
*/
public class DbProvider implements AutoCloseable{ //implements AutoCloseable so it be autoclosed in try-catch blocks

    private final Connection connection;

    private static final DbProvider instance  = new DbProvider();
    private DbProvider(){
        if(instance != null){
            throw new IllegalStateException("Instance already created!");
        }
        System.out.println("\nDbProvider: Initializing db provider...");
        connection = connect(); //connect to the database
        createTableAndData(); //create table in db if it does not already exist
        System.out.println("DbProvider: Db provider initialized!\n");
    }
    public static DbProvider getInstance(){
        return instance;
    }

    //this function opens a connection to the derby database
    private static Connection connect(){
        String connectionString = "jdbc:derby:weatherapp;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    //this functions creates the WEATHERDATA table if it does not exist
    //its purpose is to auto-create the table on app startup, similar to JPA
    private void createTableAndData(){
        try (Connection connection = connect();
             Statement statement = connection.createStatement()){
            System.out.println("DbProvider: Creating database tables!");
            DatabaseMetaData metaData = connection.getMetaData();
            //the functions first tries to retrieve the WEATHERDATA table from the db
            ResultSet tables = metaData.getTables(null, null, "WEATHERDATA", null);

            if(!tables.next()){ //if it does not find the table, it creates it
                String createTableSQL = "CREATE TABLE WEATHERDATA ("
                        + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," //incremental id
                        + "city VARCHAR(255),"
                        + "temperature INT,"
                        + "humidity INT,"
                        + "windSpeed INT,"
                        + "uvIndex INT,"
                        + "description VARCHAR(255),"
                        + "timestamp TIMESTAMP"
                        + ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("DbProvider: WeatherData table created successfully!");
            }
            else{
                System.out.println("DbProvider: WeatherData table already exists! Skipping creation...");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //required when implementing AutoClosable. closes any open resources
    @Override
    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Queries
    //insertWeatherData: insert a new weather data object into the db
    public void insertWeatherData(WeatherData newWeatherData) {
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO WEATHERDATA (city, " +
                     "temperature, humidity, windSpeed, uvIndex, description, timestamp) VALUES (?,?,?,?,?,?,?)");){

            preparedStatement.setString(1, newWeatherData.getCity());
            preparedStatement.setInt(2, newWeatherData.getTemperature());
            preparedStatement.setInt(3, newWeatherData.getHumidity());
            preparedStatement.setInt(4, newWeatherData.getWindSpeed());
            preparedStatement.setInt(5, newWeatherData.getUvIndex());
            preparedStatement.setString(6, newWeatherData.getWeatherDesc());
            preparedStatement.setTimestamp(7, newWeatherData.getTimestamp());
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count + " record updated");
            }

            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

