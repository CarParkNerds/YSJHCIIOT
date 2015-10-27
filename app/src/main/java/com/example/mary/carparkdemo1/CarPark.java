package com.example.mary.carparkdemo1;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Created by Mary on 17/10/2015.
 */
public class CarPark implements Serializable {

    private String name;
    private String address;
    private String webPage;
    private ArrayList<LatLng> coordinates;
    private int freeSpacesNumber;
    private boolean freeSpacesKnown;
    private int totalSpaces;


    public CarPark() {

    }

    public int getTotalSpaces() {
        return totalSpaces;
    }

    public void setTotalSpaces(int totalSpaces) {
        this.totalSpaces = totalSpaces;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public ArrayList<LatLng> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<LatLng> coordinates) {
        this.coordinates = coordinates;
    }

    public int getFreeSpacesNumber() {
        return freeSpacesNumber;
    }

    public void setFreeSpacesNumber(int freeSpacesNumber) {
        this.freeSpacesNumber = freeSpacesNumber;
    }

    public boolean isFreeSpacesKnown() {
        return freeSpacesKnown;
    }

    public void setFreeSpacesKnown(boolean freeSpacesKnown) {
        this.freeSpacesKnown = freeSpacesKnown;
    }

    public LatLng getMidPointLocation() {
        double totalLat = 0;
        double totalLng = 0;

        for (LatLng c : coordinates) {
            totalLat += c.latitude;
            totalLng += c.longitude;
        }

        return new LatLng(totalLat / coordinates.size(), totalLng / coordinates.size());
    }
}

