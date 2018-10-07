package com.marcfarssac.foursquarejetpackapp.locationService;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.util.List;

/**
 * Class to process location results.
 */
public class LocationHelper {

    private final static String DEFAULT_LOCATION_IS_NONE = "41.428154,2.165668";
    private final static String KEY_DEFAULT_LOCATION = "key default location";

    LocationHelper() {
    }

    public static String getLastLocation(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_DEFAULT_LOCATION, DEFAULT_LOCATION_IS_NONE);
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public void saveLastLocation(Context context, List<Location> mLocations) {

        String lastLocation = DEFAULT_LOCATION_IS_NONE;

        if (mLocations.size()>0)
            lastLocation = mLocations.get(mLocations.size()-1).getLongitude() + "," + mLocations.get(mLocations.size()-1).getLatitude();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_DEFAULT_LOCATION, lastLocation)
                .apply();
    }
}
