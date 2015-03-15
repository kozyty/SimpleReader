package com.gmail.kubota.daisuke.simplereader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.kubota.daisuke.simplereader.model.RssObject;
import com.gmail.kubota.daisuke.simplereader.view.adapter.MainAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    RequestQueue mQueue;

    MainAdapter mAdapter;

    ArrayList<RssObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        mAdapter = new MainAdapter(this, R.layout.adapter_reader_row, mList);

        Button button = (Button) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRss();
            }
        });

        View emptyView = findViewById(R.id.main_empty_layout);
        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(emptyView);
    }

    private void requestRss() {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject responseData = response.getJSONObject("responseData");
                    JSONObject feed = responseData.getJSONObject("feed");
                    JSONArray entries = feed.getJSONArray("entries");
                    ArrayList<RssObject> list = new ArrayList<>();
                    for (int i = 0; i < entries.length(); i++) {
                        JSONObject object = entries.getJSONObject(i);
                        RssObject rss = RssObject.getInstance(object);
                        if (rss != null) {
                            list.add(rss);
                        }
                    }
                    if (list.size() > 0) {
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception ignore) {
                    Log.e("rss", "requestRss()", ignore);
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.main_response_parse_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.main_response_error),
                        Toast.LENGTH_LONG);
                toast.show();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(
                "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nanapi.jp/feed&num=20",
                null,
                listener,
                errorListener);
        mQueue.add(request);
    }

}
