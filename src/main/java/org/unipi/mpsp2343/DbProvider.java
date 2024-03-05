package org.unipi.mpsp2343;

import org.unipi.mpsp2343.dbResultModels.Average;
import org.unipi.mpsp2343.dbResultModels.Maximum;
import org.unipi.mpsp2343.dbResultModels.Minimum;

import java.sql.*;
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
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //function that gets the average of a column from the saved weather data
    //can get average across all cities or for a user entered city
    public Average getAverage(String column, String city){
        Average average = null;
        boolean includeCity = !city.isEmpty();
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT " + (includeCity ? "city, " : "") + "AVG(" + column + ") AS \"avg\" FROM WEATHERDATA "
                             + (includeCity ? "WHERE LOWER(city) = ? GROUP BY city" : ""));){

            if(includeCity){
                preparedStatement.setString(1, city.toLowerCase());
            }

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                average = new Average("", 0f);
                if(includeCity) {
                    average.setCity(rs.getString("city"));
                }
                average.setAverage(rs.getFloat("avg"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return average;
    }

    //function that gets the maximum of a column from the saved weather data
    //can get maximum across all cities or for a user entered city
    public Maximum getMaximum(String column, String city){
        Maximum maximum = null;
        boolean forCity = !city.isEmpty();
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT city," + column + ", timestamp FROM WEATHERDATA"
                             + (forCity ? " WHERE LOWER(city) = ?" : ""));){

            if(forCity){
                preparedStatement.setString(1, city.toLowerCase());
            }

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                maximum = new Maximum(null, 0, null, !forCity);
                int temp;
                do {
                    temp = rs.getInt(column);
                    if(temp >= maximum.getMaximum()){
                        maximum.setMaximum(temp);
                        maximum.setCity(rs.getString("city"));
                        maximum.setTimestamp(rs.getTimestamp("timestamp"));
                    }
                }while (rs.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return maximum;
    }

    //function that gets the minimum of a column from the saved weather data
    //can get minimum across all cities or for a user entered city
    public Minimum getMinimum(String column, String city){
        Minimum minimum = null;
        boolean forCity = !city.isEmpty();
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT city," + column + ", timestamp FROM WEATHERDATA"
                             + (forCity ? " WHERE LOWER(city) = ?" : ""));){

            if(forCity){
                preparedStatement.setString(1, city.toLowerCase());
            }

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                minimum = new Minimum(null, Integer.MAX_VALUE, null, !forCity);
                int temp;
                do {
                    temp = rs.getInt(column);
                    if(temp <= minimum.getMinimum()){
                        minimum.setMinimum(temp);
                        minimum.setCity(rs.getString("city"));
                        minimum.setTimestamp(rs.getTimestamp("timestamp"));
                    }
                }while (rs.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return minimum;
    }
}

