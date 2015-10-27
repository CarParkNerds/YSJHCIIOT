package com.example.mary.carparkdemo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CarParkInfo extends AppCompatActivity {

    private WebView webView;
    private CarPark carPark;
    private TextView spacesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_info);

        Bundle bundle = this.getIntent().getExtras();
        carPark = (CarPark) bundle.getSerializable("carPark");

        // show number of free spaces
        spacesMessage = (TextView) findViewById(R.id.spacesMsg);
        if (carPark.isFreeSpacesKnown())
        {
            spacesMessage.setText("Free Spaces: " + carPark.getFreeSpacesNumber());
        }
        else
        {
            spacesMessage.setText("Free Spaces: Unknown");
        }

        // display council car park directory web page for this car park
        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());      // this forces it to open in the app not in a separate browser
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(carPark.getWebPage());
    }

}
