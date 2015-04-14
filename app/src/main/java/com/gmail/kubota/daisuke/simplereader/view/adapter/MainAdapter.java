package com.gmail.kubota.daisuke.simplereader.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.gmail.kubota.daisuke.simplereader.R;
import com.gmail.kubota.daisuke.simplereader.model.BitmapCache;
import com.gmail.kubota.daisuke.simplereader.model.RssObject;

import java.util.List;

/**
 *
 * Created by daisuke on 15/03/15.
 */
public class MainAdapter extends ArrayAdapter<RssObject> {

    RequestQueue mQueue;
    BitmapCache mCache;

    public MainAdapter(Context context, int resource, List<RssObject> objects) {
        super(context, resource, objects);
        mQueue = Volley.newRequestQueue(context);
        mCache = new BitmapCache();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.adapter_reader_row, parent, false);
//            convertView = super.getView(position, null, parent);
            holder = new Holder();
            holder.mTitleTextView =
                    (TextView) convertView.findViewById(R.id.reader_title_text_view);
            holder.mDescriptionTextView =
                    (TextView) convertView.findViewById(R.id.reader_description_text_view);
            holder.mImageView = (NetworkImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RssObject object = getItem(position);
        holder.mTitleTextView.setText(object.getTitle());
        holder.mDescriptionTextView.setText(object.getDescription());
        ImageLoader loader = new ImageLoader(mQueue, mCache);
        holder.mImageView.setImageUrl(object.getImageUrl(), loader);
        return convertView;
    }

    private class Holder {
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        NetworkImageView mImageView;
    }
}
