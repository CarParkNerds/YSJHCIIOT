package com.example.mary.carparkdemo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

public class CarParkMapFragment extends Fragment {

    MapView mMapView;
    GoogleMap googleMap;

    // Middle of York
    LatLng DEFAULT_LOCATION = new LatLng(53.9583, -1.0803);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate and return the layout

        View v = inflater.inflate(R.layout.fragment_map, container,false);

        mMapView = (MapView) v.findViewById(R.id.myMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create the google map
        googleMap = mMapView.getMap();

        // add map content
        addMapContent();

        // Perform any camera updates here
        return v;
    }

    // put the CarPark markers onto the map
    // centre on default location
    // set up customised info window
    // listen for info window clicks




    public void addMapContent() {

        /** Make sure that the map has been initialised **/
        if (null != googleMap) {

            // add the CarPark markers
            drawCarParkLines();

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

                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);


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

                    for (CarPark carPark : ((MainActivity)getActivity()).getCarParks()) {
                        if (carPark.getMidPointLocation().equals(marker.getPosition())) {
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
                    }
                }
            });

        }
    }

    // add markers to the map
    // markers show the number of free spaces, and are coloured according to the number of free spaces

    private void drawCarParkLines() {

        for (CarPark carPark : ((MainActivity)getActivity()).getCarParks()) {
            LatLng previousPos = null;
            for (LatLng coordinates : carPark.getCoordinates()) {
                if (previousPos == null) {
                    previousPos = new LatLng(coordinates.latitude, coordinates.longitude);
                } else {
                    LatLng currentPos = new LatLng(coordinates.latitude, coordinates.longitude);
                    googleMap.addPolyline(new PolylineOptions().add((previousPos), (currentPos)).width(5).color(Color.BLUE));
                    previousPos = currentPos;
                }
            }
        }
    }


    public void addMapMarkers(ArrayList<CarPark> carParks) {

        String iconText;
        String spacesText;
        IconGenerator ig = new IconGenerator(getActivity());

        for (CarPark carPark : carParks) {

            // set icon text and colour depending on number of free spaces
            if (carPark.isFreeSpacesKnown()) {

                iconText = Integer.toString(carPark.getFreeSpacesNumber());
                spacesText = Integer.toString(carPark.getFreeSpacesNumber());
                if (carPark.getFreeSpacesNumber() < 10) {
                    ig.setStyle(IconGenerator.STYLE_RED);
                }
                if (carPark.getFreeSpacesNumber() >= 10 &&
                        carPark.getFreeSpacesNumber() < 30) {
                    ig.setStyle(IconGenerator.STYLE_ORANGE);
                }
                if (carPark.getFreeSpacesNumber() >= 30) {
                    ig.setStyle(IconGenerator.STYLE_GREEN);
                }

            } else {
                iconText = "?";
                spacesText = "Unknown";
                ig.setStyle(IconGenerator.STYLE_BLUE);
            }

            // create a bitmap from the icon generator
            Bitmap iconBitmap = ig.makeIcon(iconText);

            // add marker
            googleMap.addMarker(new MarkerOptions()
                    .position(carPark.getMidPointLocation())
                    .title(carPark.getName())
                    .snippet("Empty Spaces: " + spacesText + "/" + carPark.getTotalSpaces() + "\n\nAddress: " + carPark.getAddress())
                    .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                    .draggable(false));
        }
    }



}
