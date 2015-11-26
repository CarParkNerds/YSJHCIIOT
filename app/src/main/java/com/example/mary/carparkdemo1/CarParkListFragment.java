package com.example.mary.carparkdemo1;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class CarParkListFragment extends Fragment implements AbsListView.OnItemClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ListViewAdapter mAdapter;
    // reference to MainActivity's car park list
    ArrayList<CarPark> carParks;

    private OnFragmentInteractionListener mListener;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public Location mCurrentLocation;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;


    public CarParkListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        carParks = ((MainActivity) getActivity()).getCarParks();
        mAdapter = new ListViewAdapter(getActivity(), carParks);

        // Set the adapter
        AbsListView mListView = (AbsListView) view.findViewById(R.id.carParkList);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
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

    @Override
    public void onConnected(Bundle bundle) {
        if (servicesAvailable()) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                carParksChanged();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("test", "LOCATION UPDATE");
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            carParksChanged();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), 0).show();
            return false;
        }

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

    public void carParksChanged() {
        mAdapter.notifyDataSetChanged();
    }


}



