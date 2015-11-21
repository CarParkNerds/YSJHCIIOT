package com.example.mary.carparkdemo1.SortingClasses;

import com.example.mary.carparkdemo1.CarPark;

import java.util.Comparator;

/**
 * Created by maikcaru on 21/11/15.
 */
public class NameSorter implements Comparator<CarPark> {
    @Override
    public int compare(CarPark lhs, CarPark rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}
