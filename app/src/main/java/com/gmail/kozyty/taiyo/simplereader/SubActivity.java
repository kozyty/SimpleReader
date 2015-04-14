package com.gmail.kozyty.taiyo.simplereader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.gmail.kozyty.taiyo.simplereader.model.BitmapCache;
import com.gmail.kozyty.taiyo.simplereader.model.RssObject;

/**
 * Created by kozyty on 15/03/17.
 */
public class SubActivity extends ActionBarActivity {

    RequestQueue mQueue;
    BitmapCache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("詳細");

        setContentView(R.layout.activity_sub);
        mQueue = Volley.newRequestQueue(this);
        mCache = new BitmapCache();

        final RssObject rss = (RssObject) getIntent().getSerializableExtra("rss");

        ImageLoader loader = new ImageLoader(mQueue, mCache);
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.background_image_view);
        imageView.setImageUrl(rss.getImageUrl(), loader);

        TextView title = (TextView) findViewById(R.id.title_view);
        title.setText(rss.getTitle().toString());

        final TextView link = (TextView) findViewById(R.id.link_view);
        link.setText(rss.getLink().toString());

        final TextView publishDate = (TextView) findViewById(R.id.publish_date);
        publishDate.setText(rss.getPublishedDate().toString());

        TextView description = (TextView) findViewById(R.id.description_view);
        description.setText(rss.getDescription().toString());

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(rss.getLink().toString()));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
