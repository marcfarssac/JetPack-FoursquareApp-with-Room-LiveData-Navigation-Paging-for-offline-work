package com.marcfarssac.foursquarejetpackapp

import android.Manifest
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo

import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.marcfarssac.foursquarejetpackapp.data.FoursquareCallParams
import com.marcfarssac.foursquarejetpackapp.locationService.LocationHelper
import com.marcfarssac.foursquarejetpackapp.locationService.LocationUpdatesBroadcastReceiver
import com.marcfarssac.foursquarejetpackapp.model.Venue
import com.marcfarssac.foursquarejetpackapp.ui.VenuesAdapter
import com.marcfarssac.foursquarejetpackapp.utils.Injection
import com.marcfarssac.foursquarejetpackapp.utils.PreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

@Suppress("SpellCheckingInspection")
class MainActivity : AppCompatActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener {

    companion object Constants {
        private const val TAG = "MainActivity"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE: Int = 34
        // We don't really wait for continuous updates, still we want the location fast
        private const val UPDATE_INTERVAL: Long = (10 * 1000).toLong()
        private const val FASTEST_UPDATE_INTERVAL: Long = UPDATE_INTERVAL / 2
        private const val MAX_WAIT_TIME: Long = UPDATE_INTERVAL * 3
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Hard Rock Cafe"
        private const val DEFAULT_QUERY_LIMIT = 50
        private const val DEFAULT_LAT_LONG = "41.428154,2.165668"
        private const val DEFAULT_INTENT = "global"
    }

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null

    /**
     * The entry point to Google Play Services.
     */
    private var mGoogleApiClient: GoogleApiClient? = null

    private lateinit var viewModel: MainActivityViewModel
    private val adapter = VenuesAdapter()

    private var updatingLocation = false

    private var showVenuesCloser = false
    private var showVenuesOnScreenAsMap = false

    private var backendCallParams: FoursquareCallParams = FoursquareCallParams(DEFAULT_QUERY, DEFAULT_LAT_LONG, DEFAULT_QUERY_LIMIT, DEFAULT_INTENT)

    private var mShowVenuesAsListOrMap: String = PreferencesHelper.SHOW_VENUES_FORMAT_AS_LIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        backendCallParams.latLng = LocationHelper.getLastLocation(this)
        backendCallParams.intent = PreferencesHelper.getPreferredVenuesLocation(this)

        progressBar.visibility = View.VISIBLE

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(MainActivityViewModel::class.java)

//        Switch button not working with Kotlin? //To Do review it
//
//        switchForActionBar.setOnClickListener({ v -> showProgressBar()})
//        switchForActionBar.setOnClickListener { _ -> showProgressBar()}
//        switch_button.setOnClickListener { v->
//            if (v.isEnabled ) doSomething()
//            else doSomethingElse() }


        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)

        initAdapter()
        val query = savedInstanceState?.getString(MainActivity.LAST_SEARCH_QUERY)
                ?: MainActivity.DEFAULT_QUERY
        viewModel.searchVenue(backendCallParams)
        initSearch(query)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        buildGoogleApiClient()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MainActivity.LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initAdapter() {

        list.adapter = adapter
        viewModel.venues.observe(this, Observer<PagedList<Venue>> {
            Log.d("Activity", "list: ${it?.size}")
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch(query: String) {
        edit_text_search_venue.setText(query)

        edit_text_search_venue.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateVenueListFromInput()
                true
            } else {
                false
            }
        }
        edit_text_search_venue.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateVenueListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateVenueListFromInput() {
        edit_text_search_venue.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                backendCallParams.query = it.toString()
                viewModel.searchVenue(backendCallParams)
                adapter.submitList(null)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun showListOfVenues(){

    }

    fun showMapOfVenues(){

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val menuVenuesLocation: MenuItem = menu.findItem(R.id.option_show_venues_location)
        var menuVenuesLocatoinText = getString(R.string.option_show_venues_all)
        val preferredVenuesLocation = PreferencesHelper.getPreferredVenuesLocation(this)

        if (preferredVenuesLocation != PreferencesHelper.SHOW_VENUES_LOCATION_ALL)
            menuVenuesLocatoinText = getString(R.string.option_show_venues_closest)

        menuVenuesLocation.title = menuVenuesLocatoinText

        val menuVenuesDisplayFormat: MenuItem = menu.findItem(R.id.option_show_venues_format)
        var menuVenuesDisplayFormatText = getString(R.string.option_show_venues_on_screen_as_list)
        val preferredVenuesDisplayFormat = PreferencesHelper.getPreferredVenuesDisplayType(this)

        if (preferredVenuesDisplayFormat != PreferencesHelper.SHOW_VENUES_FORMAT_AS_LIST)
            menuVenuesDisplayFormatText = getString(R.string.option_show_venues_on_screen_as_map)

        menuVenuesDisplayFormat.title = menuVenuesDisplayFormatText

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        showVenuesCloser = PreferencesHelper.getPreferredVenuesLocation(this)==PreferencesHelper.SHOW_VENUES_LOCATION_CLOSE
        showVenuesOnScreenAsMap = PreferencesHelper.getPreferredVenuesDisplayType(this) == PreferencesHelper.SHOW_VENUES_FORMAT_MAP

        when (item.itemId) {
            R.id.option_show_venues_location -> {
                if (showVenuesCloser) {
                    item.title = getString(R.string.option_show_venues_all)
                    // Check if the user revoked runtime permissions.
                    if (!checkPermissions()) {
                        requestPermissions()
                    }
                    else requestLocationUpdate()

                } else {
                    item.title = getString(R.string.option_show_venues_closest)
                }
                showVenuesCloser = !showVenuesCloser
                PreferencesHelper.savePreferredVenuesLocation(this, showVenuesCloser)
                return true
            }
            R.id.option_show_venues_format -> {

                if (showVenuesOnScreenAsMap) {
                    item.title = getString(R.string.option_show_venues_on_screen_as_list)
                } else {
                    item.title = getString(R.string.option_show_venues_on_screen_as_map)
                }
                showVenuesOnScreenAsMap = !showVenuesOnScreenAsMap
                PreferencesHelper.savePreferredVenuesDisplayType(this, showVenuesOnScreenAsMap)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        invalidateOptionsMenu()
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.option_show_venues_on_screen_as_list -> {
                PreferencesHelper.savePreferredVenuesDisplayType(this, false)
                showListOfVenues()
            }
            R.id.option_show_venues_on_screen_as_map -> {
                PreferencesHelper.savePreferredVenuesDisplayType(this, true)
                showMapOfVenues()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        invalidateOptionsMenu()
        return true
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
        if (mGoogleApiClient!=null) if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
        removeLocationUpdate()
        super.onStop()
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest?.interval = UPDATE_INTERVAL

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest?.maxWaitTime = MAX_WAIT_TIME
    }

    /**
     * Builds [GoogleApiClient], enabling automatic lifecycle management using
     * [GoogleApiClient.Builder.enableAutoManage]. I.e., GoogleApiClient connects in
     * [AppCompatActivity.onStart], or if onStart() has already happened, it connects
     * immediately, and disconnects automatically in [AppCompatActivity.onStop].
     */
    private fun buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onConnected(bundle: Bundle?) {
        Log.i(TAG, "GoogleApiClient connected")
    }

    /**
     * Gets a pending Intent with the BroadcastReceiver that upon reception
     * will update the SharedPreferences, and trigger the OnSharedPreferencesChange listener
     **/

    private fun getPendingIntent(): PendingIntent {

        val intent = Intent(this, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onConnectionSuspended(i: Int) {
        val text = "Connection suspended"
        Log.w(TAG, "$text: Error code: $i")
        showSnackbar("Connection suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val text = "Exception while connecting to Google Play services"
        Log.w(TAG, text + ": " + connectionResult.errorMessage)
        showSnackbar(text)
    }

    /**
     * Shows a [Snackbar] using `text`.
     *
     * @param text The Snackbar text.
     */
    private fun showSnackbar(text: String) {
        Snackbar.make(drawer_layout, text, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            Snackbar.make(
                    drawer_layout,
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) {
                        // Request permission
                        ActivityCompat.requestPermissions(this@MainActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                REQUEST_PERMISSIONS_REQUEST_CODE)
                    }
                    .show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission was granted. Kick off the process of building and connecting
                    // GoogleApiClient.
                    getCurrentLocation()
                else -> {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.

                    Snackbar.make(
                            drawer_layout,
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings) {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null)
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            .show()
                }
            }
        }
    }

    private fun getCurrentLocation() {
        createLocationRequest()
        requestLocationUpdate()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        backendCallParams.latLng = LocationHelper.getLastLocation(this)
        backendCallParams.intent = PreferencesHelper.getPreferredVenuesLocation(this)
        mShowVenuesAsListOrMap = PreferencesHelper.getPreferredVenuesDisplayType(this)
        Toast.makeText(this, "Current Location: ${backendCallParams.latLng}", Toast.LENGTH_LONG).show()

    }

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    private fun requestLocationUpdate() {

        if (mGoogleApiClient!=null) {

            updatingLocation = true

            try {
                Log.i(TAG, "Starting location updates")

                // ToDo substitute deprecated FusedLocationApi
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, getPendingIntent())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    private fun removeLocationUpdate() {
        Log.i(TAG, "Removing location updates")

        if (updatingLocation) {

            updatingLocation = false
            // ToDo substitute deprecated FuseLocationApi
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    getPendingIntent())
        }
    }

    private fun navigateToMap() {
        // ToDo show Map and venueDetails close by
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdate()
    }


}
