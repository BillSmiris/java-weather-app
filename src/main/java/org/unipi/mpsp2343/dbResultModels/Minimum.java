package org.unipi.mpsp2343.dbResultModels;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Minimum {
    private String city;
    private int minimum;
    private Timestamp timestamp;
    private boolean allCities;

    public Minimum() {
    }

    public Minimum(String city, int minimum, Timestamp timestamp, boolean allCities) {
        this.city = city;
        this.minimum = minimum;
        this.timestamp = timestamp;
        this.allCities = allCities;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAllCities() {
        return allCities;
    }

    public void setAllCities(boolean allCities) {
        this.allCities = allCities;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(this.timestamp);
    }

    public void print(String column, String symbol) {
        if(allCities){
            System.out.println("\nMinimum " + column + " of all cities:");
            System.out.println("\n" + this.city + ": " + this.minimum  + symbol + " at " + this.getFormattedTimestamp());
        }
        else {
            System.out.println("\nMinimum " + column + " in " + this.city + ": " + this.minimum + symbol + " at " + this.getFormattedTimestamp());
        }
    }
}
