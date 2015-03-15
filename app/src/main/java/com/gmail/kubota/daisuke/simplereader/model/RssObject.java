package com.gmail.kubota.daisuke.simplereader.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * Created by daisuke on 15/03/15.
 */
public class RssObject implements Serializable {

    private static final long serialVersionUID = -5591025683974631060L;

    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mPublishedDate;
    public static RssObject getInstance(JSONObject json) {
        try {
            RssObject object = new RssObject();
            object.mTitle = json.getString("title").trim();
            object.mDescription = json.getString("contentSnippet").trim();
            object.mLink = json.getString("link");
            object.mPublishedDate = json.getString("publishedDate");
            return object;
        } catch (Exception ignore) {
            Log.e("rss", "getInstance", ignore);
        }
        return null;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }
}
