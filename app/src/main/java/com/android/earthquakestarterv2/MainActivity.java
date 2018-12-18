package com.android.earthquakestarterv2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<EarthquakeData> earthquakes;
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetEarthquakeData getEarthquakeData = new GetEarthquakeData();
        getEarthquakeData.execute(SAMPLE_JSON_RESPONSE);

    }

    private class GetEarthquakeData extends AsyncTask<String, Void, EarthquakeData> {


        @Override
        protected EarthquakeData doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            EarthquakeData earthquakeData = QueryUtils.fetchEarthquakeDate(urls[0]);
            return earthquakeData;
        }

        @Override
        protected void onPostExecute(EarthquakeData earthquakeData) {
            if (earthquakeData == null) {
                return;
            }
            updateUi(earthquakeData);
        }

        private void updateUi(EarthquakeData earthquakeData) {

            // Find a reference to the {@link ListView} in the layout
            ListView earthquakeListView = (ListView) findViewById(R.id.list);

            // Create a new adapter that takes the list of earthquakes as input
            final EarthquakeAdapter adapter = new EarthquakeAdapter(MainActivity.this, earthquakeData);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);

            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected earthquake.
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Find the current earthquake that was clicked on
                    EarthquakeData currentEarthquake = adapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            });
        }
    }
}

