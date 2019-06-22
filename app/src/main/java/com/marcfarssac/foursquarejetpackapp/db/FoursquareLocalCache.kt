package com.marcfarssac.foursquarejetpackapp.db

import android.util.Log
import androidx.paging.DataSource
import com.marcfarssac.foursquarejetpackapp.model.Venue
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source.
 */
class FoursquareLocalCache(
        private val foursquareDao: FoursquareDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of venues in the database, on a background thread.
     */
    fun insert(venues: List<Venue>, insertFinished: ()-> Unit) {
        ioExecutor.execute {
            Log.d("FoursquareLocalCache", "inserting ${venues.size} venues")
            foursquareDao.insert(venues)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<FourSquareResponse>> from the Dao, based on a venue name.
     * @param name venue name
     */
    fun venuesByName(name: String): DataSource.Factory<Int, Venue> {
        return foursquareDao.venueByName(name)
    }
}