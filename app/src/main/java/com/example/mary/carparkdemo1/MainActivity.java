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

        loadCarParkData();

       // create map and add content
        createMapView();
        addMapContent();

    }

    // load bus CarPark information from the database
    private void loadCarParkData() {

        carParks.add( new CarPark("Rowntree Park","Rowntree Park car park, York, YO23 1JQ","http://www.york.gov.uk/directory_record/511/rowntrees_park_car_park",53.950620, -1.079530,2, true));
        carParks.add( new CarPark("East Parade","East Parade car park, York, YO31 0XH","https://www.york.gov.uk/directory_record/510/east_parade_car_park",53.964356, -1.065754,11, true));
        carParks.add( new CarPark("Moor Lane","Moor Lane car park, Moor Lane, York, YO24 1LW","https://www.york.gov.uk/directory_record/514/moor_lane_car_park",53.933820, -1.113617,95, true));
        carParks.add( new CarPark("Union Terrace","Clarence Street, York, YO31 7ES","http://www.york.gov.uk/directory_record/508/union_terrace_car_park",53.967632, -1.082782,0,false));
        carParks.add( new CarPark("Castle Mills","Piccadilly, York, YO1 9NX","http://www.york.gov.uk/directory_record/512/castle_mills_car_park",53.956533, -1.077828,0, true));
        carParks.add( new CarPark("Foss Bank","Jewbury, York, YO31 7PL","http://www.york.gov.uk/directory_record/501/foss_bank_car_park",53.962338, -1.076124,31, true));
        carParks.add( new CarPark("Monk Bar","St John’s Street, York, YO31 7QR","http://www.york.gov.uk/directory_record/504/monk_bar_car_park",53.964794, -1.078715,0, false));
        carParks.add( new CarPark("Bootham Row","Bootham Row, York, YO30 7BP","http://www.york.gov.uk/directory_record/498/bootham_row_car_park",53.964542, -1.084789,7,true));
        carParks.add( new CarPark("Bishopthorpe Road","Bishopthorpe Road, York, YO23 1NA","http://www.york.gov.uk/directory_record/497/bishopthorpe_road_car_park",53.951860, -1.085047,43, true));
        carParks.add( new CarPark("Marygate","Frederick Street, York, YO30 7DT","http://www.york.gov.uk/directory_record/503/marygate_car_park",53.962350, -1.090873,29, true));
        carParks.add( new CarPark("Piccadilly","Piccadilly, York, YO1 9NX","http://www.york.gov.uk/directory_record/506/piccadilly_car_park",53.956546, -1.077796,37, true));
        carParks.add( new CarPark("Nunnery Lane","Nunnery Lane, York, YO23 1AA","http://www.york.gov.uk/directory_record/505/nunnery_lane_car_park",53.954371, -1.087429,19,true));
        carParks.add( new CarPark("Castle","Tower Street, York, YO1 9SA","http://www.york.gov.uk/directory_record/499/castle_car_park",53.955769, -1.080923,10, true));
        carParks.add( new CarPark("St Georges Field","St Georges Field, Tower Street, York","http://www.york.gov.uk/directory_record/507/st_georges_field_car_park",53.954074, -1.079892,33, true));
        carParks.add( new CarPark("Esplanade","West Esplanade, York, YO1 6FZ","http://www.york.gov.uk/directory_record/500/esplanade_car_park",53.960014, -1.088640,0, false));

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
                        if (carPark.getLocation().equals(marker.getPosition())) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, CarParkInfo.class);

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("carPark", carPark);

                            intent.putExtras(bundle);

                            startActivity(intent);

                        }
                    }


//                    Toast.makeText(getApplicationContext(), "clicked me", Toast.LENGTH_SHORT).show();

      //              Log.d("", marker.getTitle());
                }
            });
            // when a marker is clicked, start the Timetable screen, passing information
            // on the selected bus CarPark
            /*
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    //              marker.showInfoWindow();
                    for (CarPark CarPark : CarParks) {
                        if (CarPark.getCarParkLocation().equals(marker.getPosition())) {
                            Intent intent = new Intent();
                            intent.setClass(CarParkMap.this, Timetable.class);

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("CarPark", CarPark);

                            intent.putExtras(bundle);

                            startActivity(intent);

                        }
                    }
                    return true;
                }
            });
            */

        }
    }

    // add markers to the map
    private void addMapMarkers() {

        for (CarPark carPark : carParks) {

            if (carPark.isFreeSpacesKnown()) {

                if (carPark.getFreeSpacesNumber() < 10)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(carPark.getLocation())
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
                            .position(carPark.getLocation())
                            .title(carPark.getName())
                                    // this needs fixing as ass text shown on one line
                            .snippet("Free Spaces: " + carPark.getFreeSpacesNumber() + "\n\nAddress: " + carPark.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .draggable(false));
                }
                if (carPark.getFreeSpacesNumber() >= 30)
                {
                    googleMap.addMarker(new MarkerOptions()
                            .position(carPark.getLocation())
                            .title(carPark.getName())
                                    // this needs fixing as ass text shown on one line
                            .snippet("Free Spaces: " + carPark.getFreeSpacesNumber() + "\n\nAddress: " + carPark.getAddress())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .draggable(false));
                }

            } else
            {
                googleMap.addMarker(new MarkerOptions()
                        .position(carPark.getLocation())
                        .title(carPark.getName())
                                // this needs fixing as ass text shown on one line
                        .snippet("Free Spaces: unknown\n\nAddress: " + carPark.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .draggable(false));
            }

        }

    }

}
