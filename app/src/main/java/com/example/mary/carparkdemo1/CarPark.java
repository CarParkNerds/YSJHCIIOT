package com.example.mary.carparkdemo1;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Mary on 17/10/2015.
 */
public class CarPark implements Serializable {

    private String name;
    private String address;
    private String webPage;
    private double cpLat, cpLong;
    private int freeSpacesNumber;
    private boolean freeSpacesKnown;

    public CarPark(String inName, String inAddress, String inWebPage, double inCpLat,
                double inCpLong, int inFreeSpacesNumber, boolean inFreeSpacesknown) {
        name = inName;
        address = inAddress;
        webPage = inWebPage;
        cpLat = inCpLat;
        cpLong = inCpLong;
        freeSpacesNumber = inFreeSpacesNumber;
        freeSpacesKnown = inFreeSpacesknown;

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

    public double getCpLat() {
        return cpLat;
    }

    public void setCpLat(double cpLat) {
        this.cpLat = cpLat;
    }

    public double getCpLong() {
        return cpLong;
    }

    public void setCpLong(double cpLong) {
        this.cpLong = cpLong;
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

    public LatLng getLocation()
    {
        return new LatLng(cpLat, cpLong);
    }
}

