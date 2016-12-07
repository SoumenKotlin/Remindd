package com.example.soumendutta.mappsepnavigation.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.soumendutta.mappsepnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity
{
    SearchView search;
    ListView listView;
    String status= "YES";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    ArrayList<String> arrayList;
    public static String API_KEY ="AIzaSyAsE0edaQKl5wgqcfTibDmdUuHQgFEoldc";

    ArrayList<String> placeid;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search=(SearchView) findViewById(R.id.searchView1);
        listView =(ListView)findViewById(R.id.list_item);
        placeid = new ArrayList<>();
        search.setIconifiedByDefault(true);
        search.setIconified(false);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                PlaceParser placeParser = new PlaceParser();
                placeParser.getAddress(SearchActivity.this, placeid.get(position));
                Log.e("Place Id: ",""+placeid.toString());
                placeid.clear();
                finish();

            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                placeid.clear();

                if (newText.isEmpty()){

                    listView.setAdapter(null);
                }

                new ExecuteData().execute(newText);

                return false;
            }

        });

    }

    class ExecuteData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... input) {
            String result = "";
            try {
                HttpHandler httpHandler = new HttpHandler();
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("&input=" + URLEncoder.encode(input[0], "utf8"));

                result = httpHandler.makeServiceCall(sb.toString());

            }
             catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            autocompletedText(s);
        }
    }

    public  ArrayList<String> autocompletedText(String input){
        ArrayList<String> searchResults = null;

        try {

            // Create a JSON object hierarchy from the results

            JSONObject jsonObj = new JSONObject(input);

            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results

            searchResults = new ArrayList<>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Log.e("description",predsJsonArray.getJSONObject(i).getString("description"));

                searchResults.add(predsJsonArray.getJSONObject(i).getString("description"));
                placeid.add( predsJsonArray.getJSONObject(i).getString("place_id"));

            }

            arrayAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, searchResults);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.e("", "Cannot process JSON results", e);

        }

        return searchResults;
    }

}