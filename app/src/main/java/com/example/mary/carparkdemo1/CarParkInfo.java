package com.example.mary.carparkdemo1;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CarParkInfo extends AppCompatActivity {

    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_info);

        Bundle bundle = this.getIntent().getExtras();
        int spaces = bundle.getInt("spaces");
        boolean known = bundle.getBoolean("known");
        String website = bundle.getString("website");
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.setTitleTextColor(Color.WHITE);
        // show number of free spaces (show "unknown" if not known)
        if (known) {
            toolbar.setTitle("Empty Spaces: " + spaces);
        } else {
            toolbar.setTitle("Empty Spaces: Unknown");
        }
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // display council car park directory web page for this car park
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());      // this forces it to open in the app not in a separate browser
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(website);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.action_navigate:
                    //start google directions going from current location to car park
                    String url = "http://maps.google.com/maps?mode=driving&saddr=&daddr=" + lat + "," + lng;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                    return true;
                }
            return super.onOptionsItemSelected(item);
        }
}






