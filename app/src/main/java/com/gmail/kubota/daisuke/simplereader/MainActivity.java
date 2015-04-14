package com.gmail.kubota.daisuke.simplereader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    RequestQueue mQueue;
    MainAdapter mAdapter;

    ArrayList<RssObject> mList = new ArrayList<>();

    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("list")) {
                mList = (ArrayList<RssObject>) savedInstanceState.getSerializable("list");
            }
        } else {
            mList = loadRss();
        }

        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        mAdapter = new MainAdapter(this, R.layout.adapter_reader_row, mList);

        View emptyView = findViewById(R.id.main_empty_layout);
        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RssObject rss = mList.get(position);

                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("rss", rss);
                startActivity(intent);
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        // ローディングの色
        mRefreshLayout.setColorSchemeResources(
                R.color.common_green,
                R.color.common_purple,
                R.color.common_red,
                R.color.common_blue
        );

        mRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.common_yellow);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRss();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("list", mList);
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

                        saveRss(mList);

                        mAdapter.notifyDataSetChanged();
                    }

                    // 更新が終了したら、インジケータ非表示
                    mRefreshLayout.setRefreshing(false);

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

                // 更新が失敗したら、インジケータ非表示
                mRefreshLayout.setRefreshing(false);
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(
                "https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://nanapi.jp/feed&num=20",
                null,
                listener,
                errorListener);
        mQueue.add(request);
    }

    private void saveRss(ArrayList<RssObject> list) {
        FileOutputStream fos;
        try {
            fos = openFileOutput("rss", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
        } catch (Exception e) {
        }
    }

    private ArrayList<RssObject> loadRss() {
        FileInputStream fis;
        try {
            fis = openFileInput("rss");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<RssObject> list = (ArrayList<RssObject>) ois.readObject();
            ois.close();
            return list;
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
}
