package com.marcfarssac.foursquarejetpackapp.locationService;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Class to process location results.
 */
public class LocationResultHelper {

    private final static String KEY_LOCATION_LAT = "location-lat";
    private final static String KEY_LOCATION_LON = "location-lon";

    private final static String YOUR_MD_LAT = "51.520677";
    private final static String YOUR_MD_LON = "-0.135897";

    private final Context mContext;
    private final List<Location> mLocations;

    LocationResultHelper(Context context, List<Location> locations) {
        mContext = context;
        mLocations = locations;
    }

    private String getLastLocationLat() {

        String mLastLocationLat = YOUR_MD_LAT;

        if (mLocations.isEmpty()) {
            return mLastLocationLat;
        }

        for (Location location : mLocations) {
            mLastLocationLat = String.valueOf(location.getLatitude());
        }
        return mLastLocationLat;
    }
    private String getLastLocationLon() {

        String mLastLocationLon = YOUR_MD_LON;

        if (mLocations.isEmpty()) {
            return mLastLocationLon;
        }

        for (Location location : mLocations) {
            mLastLocationLon = String.valueOf(location.getLongitude());
        }
        return mLastLocationLon;
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_LAT, getLastLocationLat())
                .putString(KEY_LOCATION_LON, getLastLocationLon())
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    private static String getSavedLon(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_LON, YOUR_MD_LON);
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    private static String getSavedLat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_LAT, YOUR_MD_LAT);
    }

    static String getLocationText(Context context) {
        return "("+getSavedLat(context)+"," +getSavedLon(context)+")";
    }
}
