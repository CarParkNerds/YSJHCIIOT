package com.example.mary.carparkdemo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<CarPark> carParks = new ArrayList<>();

//    List<CarPark> CarParksOnMap = new ArrayList<CarPark>();

    GoogleMap googleMap;

    // somewhere in teh middle of York
    LatLng DEFAULT_LOCATION = new LatLng(53.9583, -1.0803);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // create map and add content
        createMapView();
        addMapContent();

    }

    // create the google map
    private void createMapView() {

        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    // put the bus CarPark markers onto the map
    // centre on default location
    // listne for marker clicks and map moves

    private void addMapContent() {

        /** Make sure that the map has been initialised **/
        if (null != googleMap) {

            // add the bus CarPark markers
            addMapMarkers();

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

             // move map to default Location in centre of York
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 14));

            // add the google location button, allowing user to return map to current location
            googleMap.setMyLocationEnabled(true);

            // customise Info Window as standard one does not allow line breaks
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.info_window, null);


                    TextView winTitle = (TextView) v.findViewById(R.id.winTitle);
                    winTitle.setText(marker.getTitle());

                    TextView winSnippet = (TextView) v.findViewById(R.id.winSnippet);
                    winSnippet.setText(marker.getSnippet());

                    return v;
                }
            });

            // when info window clicked, go to car park information activity
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {

                    for (CarPark carPark : carParks) {
                        if (carPark.getMidPointLocation().equals(marker.getPosition())) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, CarParkInfo.class);

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("carPark", carPark);

                            intent.putExtras(bundle);

                            startActivity(intent);

                        }
                    }


                }
            });


        }
    }

    // add markers to the map
    private void addMapMarkers() {

        for (CarPark carPark : carParks) {

            if (carPark.isFreeSpacesKnown()) {

                if (carPark.getFreeSpacesNumber() < 10)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(carPark.getMidPointLocation())
                            .title(carPark.getName())
                                    // this needs fixing as ass text shown on one line
                            .snippet("Free Spaces: " + carPark.getFreeSpacesNumber() + "\n\nAddress: " + carPark.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .draggable(false));
                }
                if (carPark.getFreeSpacesNumber() >= 10 &&
                        carPark.getFreeSpacesNumber() < 30)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(carPark.getMidPointLocation())
                            .title(carPark.getName())
                                    // this needs fixing as ass text shown on one line
                            .snippet("Free Spaces: " + carPark.getFreeSpacesNumber() + "\n\nAddress: " + carPark.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .draggable(false));
                }
                if (carPark.getFreeSpacesNumber() >= 30)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(carPark.getMidPointLocation())
                            .title(carPark.getName())
                                    // this needs fixing as ass text shown on one line
                            .snippet("Free Spaces: " + carPark.getFreeSpacesNumber() + "\n\nAddress: " + carPark.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .draggable(false));
                }

            } else
            {
                googleMap.addMarker(new MarkerOptions()
                        .position(carPark.getMidPointLocation())
                        .title(carPark.getName())
                                // this needs fixing as ass text shown on one line
                        .snippet("Free Spaces: unknown\n\nAddress: " + carPark.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .draggable(false));
            }

        }

    }

}
