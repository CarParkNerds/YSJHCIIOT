package com.example.mary.carparkdemo1.SortingClasses;

import com.example.mary.carparkdemo1.CarPark;

import java.util.Comparator;

/**
 * Created by maikcaru on 21/11/15.
 */
public class SpacesSorter implements Comparator<CarPark> {
    @Override
    public int compare(CarPark lhs, CarPark rhs) {
        int returnValue = 0;
            if (lhs.getFreeSpacesNumber() > rhs.getFreeSpacesNumber()) {
                returnValue = -1;
            } else if (lhs.getFreeSpacesNumber() < rhs.getFreeSpacesNumber()) {
                returnValue = 1;
            } else if (lhs.getFreeSpacesNumber() == rhs.getFreeSpacesNumber()) {
                returnValue = 0;
            }
        return returnValue;
    }
}
