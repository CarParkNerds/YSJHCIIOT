package com.example.mary.carparkdemo1;

import java.util.Comparator;

/**
 * Created by maikcaru on 21/11/15.
 */
public class DistanceSorter implements Comparator<CarPark> {
    @Override
    public int compare(CarPark lhs, CarPark rhs) {
        int returnValue = 0;

        if (lhs.getDistanceFromLocation() < rhs.getDistanceFromLocation()) {
            returnValue = -1;
        } else if (lhs.getDistanceFromLocation() > rhs.getDistanceFromLocation()) {
            returnValue = 1;
        } else if (lhs.getDistanceFromLocation() == rhs.getDistanceFromLocation()){
            returnValue = 0;
    }
        return returnValue;
    }
}
