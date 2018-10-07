package com.marcfarssac.foursquarejetpackapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Class to process location results.
 */
public class PreferencesHelper {

    private final static String SHOW_VENUES_LOCATION = "show venues location";
    private final static String SHOW_VENUES_LOCATION_CLOSE = "global";
    public final static String SHOW_VENUES_LOCATION_ALL = "checkin";

    private final static String SHOW_VENUES_FORMAT = "show venues format";
    private final static String SHOW_VENUES_FORMAT_MAP = "map";
    public final static String SHOW_VENUES_FORMAT_AS_LIST = "list";

    PreferencesHelper() {}

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public static void savePreferredVenuesLocation(Context context, boolean showVenuesCloser) {

        String venuesLocation = SHOW_VENUES_LOCATION_ALL; // Default

        if (showVenuesCloser) venuesLocation = SHOW_VENUES_LOCATION_CLOSE;

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SHOW_VENUES_LOCATION, venuesLocation)
                .apply();
    }

    public static void savePreferredVenuesDisplayType(Context context, boolean showVenuesOnScreenAsMap) {

        String venuesDisplayType = SHOW_VENUES_FORMAT_AS_LIST;

        if (showVenuesOnScreenAsMap) venuesDisplayType = SHOW_VENUES_FORMAT_MAP;

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SHOW_VENUES_FORMAT, venuesDisplayType)
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     *
     * We get the saved value or we set a default one
     */
    public static String getPreferredVenuesDisplayType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SHOW_VENUES_FORMAT, SHOW_VENUES_FORMAT_AS_LIST);
    }

    public static String getPreferredVenuesLocation(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SHOW_VENUES_LOCATION, SHOW_VENUES_LOCATION_ALL);
    }
}
