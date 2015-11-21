package com.example.mary.carparkdemo1;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mary.carparkdemo1.SlidingLayout.SlidingTabLayout;
import com.example.mary.carparkdemo1.SortingClasses.DistanceSorter;
import com.example.mary.carparkdemo1.SortingClasses.NameSorter;
import com.example.mary.carparkdemo1.SortingClasses.SpacesSorter;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements CarParkListFragment.OnFragmentInteractionListener {

    // Declaring Your View and Variables
    getData get;
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence titles[] = {"Map", "List"};
    int numbOfTabs = 2;
    ArrayList<CarPark> carParks = new ArrayList<>();
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showOverflowMenu(false);

        //Error occurring
        if (savedInstanceState == null) {
            //   getSupportFragmentManager().beginTransaction().add(R.id.myMapView, new CarParkMapFragment()).commit();
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles for the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numbOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        get = new getData();
        get.execute("http://data.cyc.opendata.arcgis.com/datasets/601ef57b2c7449b19630a3e243fc5293_4.geojson");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        CarParkMapFragment carParkMapFragment = adapter.getMap();

        switch (item.getItemId()) {
            case R.id.action_refresh:
                carParkMapFragment.googleMap.clear();
                // At this point the data would be called again as this is not live we just regenerate the spaces
                for (CarPark carPark : carParks) {
                    get.generateSpaces(carPark);
                }

                carParkMapFragment.addMapMarkers(carParks);
                adapter.getList().carParksChanged();
                return true;

            // show the Help screen
            case R.id.help_item:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, HelpActivity.class);
                startActivity(intent2);
                return true;
            // show the About screen
            case R.id.about_item:
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menuSortAlphabetical:
                Collections.sort(carParks, new NameSorter());
                adapter.getList().mAdapter.notifyDataSetChanged();
                Log.e("Sorted by", "name");
                return true;
            case R.id.menuSortDistance:
                Location myLocation = carParkMapFragment.googleMap.getMyLocation();
                float results[] = new float[1];
                for (CarPark c : carParks) {
                    LatLng carParkLocation = c.getMidPointLocation();
                    Location.distanceBetween(carParkLocation.latitude,carParkLocation.longitude,myLocation.getLatitude(),myLocation.getLongitude(),results);
                    c.setDistanceFromLocation(results[0]);
               }
                Collections.sort(carParks, new DistanceSorter());
                adapter.getList().mAdapter.notifyDataSetChanged();
                Log.e("Sorted by", "distance");
                return true;
            case R.id.menuSortSpaces:
                Collections.sort(carParks, new SpacesSorter());
                adapter.getList().mAdapter.notifyDataSetChanged();
                Log.e("Sorted by", "free spaces");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void showOverflowMenu(boolean showMenu){
        if(menu == null)
            return;
        menu.setGroupVisible(R.id.sortGroup, showMenu);
    }


    public void onFragmentInteraction(String id){}

    public ArrayList<CarPark> getCarParks(){
        return carParks;
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
                //Sort car park list alphabetically
                Collections.sort(carParks, new NameSorter());
                adapter.getMap().addMapMarkers(carParks);
                adapter.getList().carParksChanged();
            } catch (Exception e) {

            }


        }

        public void generateSpaces(CarPark carPark) {
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