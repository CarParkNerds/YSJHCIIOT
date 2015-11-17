package com.example.mary.carparkdemo1;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by michael.carr on 17/11/15.
 */
public class CarParkListFragment extends ListFragment {

    ListViewAdapter mAdapter;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mAdapter = new ListViewAdapter(getActivity(),((MainActivity)getActivity()).getCarParks());

    }

public void onItemClick(AdapterView<?> parent, View view, int position, long id){



}

}



