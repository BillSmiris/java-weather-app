package org.unipi.mpsp2343.dbResultModels;

public class Average {
    private String city;
    private float average;

    public Average() {
    }

    public Average(String city, float average) {
        this.city = city;
        this.average = average;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public void print(String column, String symbol) {
        if(city.isEmpty()){
            System.out.println("\nAverage " + column + " among all cities: " + this.average + symbol);
        }
        else {
            System.out.println("\nAverage " + column + " in " + this.city + ": " + this.average + symbol);
        }
    }
}
