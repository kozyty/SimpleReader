package com.gmail.kubota.daisuke.simplereader.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gmail.kubota.daisuke.simplereader.R;
import com.gmail.kubota.daisuke.simplereader.model.RssObject;

import java.util.List;

/**
 *
 * Created by daisuke on 15/03/15.
 */
public class MainAdapter extends ArrayAdapter<RssObject> {
    public MainAdapter(Context context, int resource, List<RssObject> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(R.layout.adapter_reader_row, parent, false);
            holder = new Holder();
            holder.mTitleTextView =
                    (TextView) convertView.findViewById(R.id.reader_title_text_view);
            holder.mDescriptionTextView =
                    (TextView) convertView.findViewById(R.id.reader_description_text_view);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RssObject object = getItem(position);
        holder.mTitleTextView.setText(object.getTitle());
        holder.mDescriptionTextView.setText(object.getDescription());
        return convertView;
    }

    private class Holder {
        TextView mTitleTextView;
        TextView mDescriptionTextView;
    }
}
