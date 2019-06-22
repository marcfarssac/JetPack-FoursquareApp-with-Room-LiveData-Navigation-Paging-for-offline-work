package com.marcfarssac.foursquarejetpackapp.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FoursquareCallParams extends BaseObservable {

    private String mQuery;
    private String mLatLng;
    private int mLimit;
    private String mIntent;

    public FoursquareCallParams(String query, String latLng, int limit, String intent ) {
        mQuery = query;
        mLatLng = latLng;
        mLimit = limit;
        mIntent = intent;
    }

    @Bindable
    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        this.mQuery = query;
    }

    public String getLatLng() {
        return mLatLng;
    }

    public void setLatLng(String latLng) {
        this.mLatLng = latLng;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int limit) {
        this.mLimit = limit;
    }

    public String getIntent() {
        return mIntent;
    }

    public void setIntent(String intent) {
        this.mIntent = intent;
    }
}
