package com.example.mary.carparkdemo1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by michael.carr on 17/11/15.
 */
public class CarParkListFragment extends Fragment implements AbsListView.OnItemClickListener {

    ListViewAdapter mAdapter;
    // reference to MainActivity's car park list
    ArrayList<CarPark> carParks;
    private AbsListView mListView;
    private OnFragmentInteractionListener mListener;


    public CarParkListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        carParks = ((MainActivity) getActivity()).getCarParks();
        mAdapter = new ListViewAdapter(getActivity(), carParks);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.carParkList);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CarPark carPark = this.carParks.get((position));

        Intent intent = new Intent();
        intent.setClass(getActivity(), CarParkInfo.class);

        Bundle bundle = new Bundle();

        bundle.putInt("spaces", carPark.getFreeSpacesNumber());
        bundle.putBoolean("known", carPark.isFreeSpacesKnown());
        bundle.putString("website", carPark.getWebPage());
        LatLng latlng = carPark.getMidPointLocation();
        bundle.putDouble("lat", latlng.latitude);
        bundle.putDouble("lng", latlng.longitude);
        intent.putExtras(bundle);

        startActivity(intent);

    }
    public void carParksChanged()
    {
        mAdapter.notifyDataSetChanged();
    }


}



