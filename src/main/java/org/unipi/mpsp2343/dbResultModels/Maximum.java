package org.unipi.mpsp2343.dbResultModels;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Maximum {
    private String city;
    private int maximum;
    private Timestamp timestamp;

    private boolean allCities;

    public Maximum() {
    }

    public Maximum(String city, int maximum, Timestamp timestamp, boolean allCities) {
        this.city = city;
        this.maximum = maximum;
        this.timestamp = timestamp;
        this.allCities = allCities;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
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
            System.out.println("\nMaximum " + column + " of all cities:");
            System.out.println("\n" + this.city + ": " + this.maximum  + symbol + " at " + this.getFormattedTimestamp());
        }
        else {
            System.out.println("\nMaximum " + column + " in " + this.city + ": " + this.maximum  + symbol + " at " + this.getFormattedTimestamp());
        }
    }
}
