package com.marcfarssac.foursquarejetpackapp.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.marcfarssac.foursquarejetpackapp.api.FoursquareService
import com.marcfarssac.foursquarejetpackapp.db.FoursquareLocalCache
import com.marcfarssac.foursquarejetpackapp.model.FoursquareSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class FoursquareRepository(
        private val service: FoursquareService,
        private val cache: FoursquareLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String, ll: String, limit: Int, intent: String): FoursquareSearchResult {
        Log.d("FoursquareRepository", "New query: $query")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.venuesByName(query)

        // Construct the boundary callback
        val boundaryCallback = FoursquareBoundaryCallback(query, ll, limit, intent, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return FoursquareSearchResult(data, networkErrors)
    }

//    fun requestMore(query: String) {
//        requestAndSaveData(query)
//    }

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}