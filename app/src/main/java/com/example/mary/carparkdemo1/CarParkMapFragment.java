package com.example.mary.carparkdemo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarParkMapFragment extends Fragment {

    List<CarPark> carParks = new ArrayList<>();
    MapView mMapView;
    GoogleMap googleMap;
    getData get;
    private Toolbar toolbar;


    // Middle of York
    LatLng DEFAULT_LOCATION = new LatLng(53.9583, -1.0803);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
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

    private void addMapContent() {

        /** Make sure that the map has been initialised **/
        if (null != googleMap) {

            get = new getData();
            get.execute("http://data.cyc.opendata.arcgis.com/datasets/601ef57b2c7449b19630a3e243fc5293_4.geojson");

            // add the CarPark markers
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

                    for (CarPark carPark : carParks) {
                        if (carPark.getMidPointLocation().equals(marker.getPosition())) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), CarParkInfo.class);

                            Bundle bundle = new Bundle();

                            bundle.putSerializable("spaces", carPark.getFreeSpacesNumber());
                            bundle.putSerializable("website", carPark.getWebPage());
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

        for (CarPark carPark : carParks) {
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


    private void addMapMarkers() {

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
                    .snippet("Free Spaces: " + spacesText + "/" + carPark.getTotalSpaces() + "\n\nAddress: " + carPark.getAddress())
                    .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                    .draggable(false));
        }
    }


    public void refreshSpaces(){
        googleMap.clear();
        // User chose the "Settings" item, show the app settings UI...
        for (CarPark carPark : carParks) {
            get.generateSpaces(carPark);
        }
        addMapMarkers();
    }

    private class getData extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        //Downloads the data from the URL requested
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            InputStream iStream = urlConnection.getInputStream();
            try {
                // Reading data from url
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();

            } catch (Exception e) {

            } finally {
                urlConnection.disconnect();
                if (iStream != null) {
                    iStream.close();
                }
            }
            return data;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(result);
                JSONArray features = jsonObject.getJSONArray("features");

                //Access every car park
                for (int i = 0; i < features.length(); i++) {
                    CarPark carPark = new CarPark();
                    ArrayList coordinates = new ArrayList();
                    JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                    carPark.setName(properties.getString("DESCRIPTIO"));

                    //Access all the co ordinates and add to arraylist
                    JSONArray geometry = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                    for (int j = 0; j < geometry.length(); j++) {
                        coordinates.add(new LatLng((double) geometry.getJSONArray(j).get(1), (double) geometry.getJSONArray(j).get(0)));
                    }

                    carPark.setCoordinates(coordinates);
                    carPark.setWebPage(properties.getString("WEBSITE2"));
                    carPark.setAddress(properties.getString("LV_DETAILS"));

                    //Generate whether number of spaces is known and number + total if known
                    generateSpaces(carPark);

                    carParks.add(carPark);
                }
            } catch (Exception e) {

            }

            drawCarParkLines();
            addMapMarkers();
        }


        private void generateSpaces(CarPark carPark) {
            Random r = new Random();
            int free = 50;
            if (r.nextInt(4) == 1) {
                carPark.setFreeSpacesKnown(false);
            } else {
                carPark.setFreeSpacesKnown(true);
                free = r.nextInt(100);
                carPark.setFreeSpacesNumber(free);

            }
            //Set total spaces to be round number greater than free spaces and rounded to 5
            carPark.setTotalSpaces(((r.nextInt(300) + free) + 4) / 5 * 5);

        }

    }
}
