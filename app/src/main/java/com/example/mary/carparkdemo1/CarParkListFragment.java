package com.example.mary.carparkdemo1;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by michael.carr on 17/11/15.
 */
public class CarParkListFragment extends Fragment implements AbsListView.OnItemClickListener {

    ListViewAdapter mAdapter;
    ArrayList<CarPark> carParks;
    private AbsListView mListView;
    private OnFragmentInteractionListener mListener;


    public CarParkListFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carParks = ((MainActivity) getActivity()).getCarParks();

        if (carParks == null)
        {
            Log.e("Car park","is null");
        }
        else{
            Log.e("Car park", "is not null");
        }
        mAdapter = new ListViewAdapter(getActivity(), carParks);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_entry, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

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

        bundle.putSerializable("spaces", carPark.getFreeSpacesNumber());
        bundle.putSerializable("website", carPark.getWebPage());
        intent.putExtras(bundle);

        startActivity(intent);

    }

}



