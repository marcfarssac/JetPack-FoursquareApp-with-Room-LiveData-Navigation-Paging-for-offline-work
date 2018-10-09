package com.marcfarssac.foursquarejetpackapp.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList

import com.marcfarssac.foursquarejetpackapp.api.FoursquareService
import com.marcfarssac.foursquarejetpackapp.api.searchVenue
import com.marcfarssac.foursquarejetpackapp.db.FoursquareLocalCache
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * The Foursquare API does not allow at this point to page requests.
 * This is to save resources on the local device in case that more
 * than the max page size number of Venues where stored locally.
 */

class FoursquareBoundaryCallback(
        private val fourSquareQueryParams: FoursquareCallParams,
        private val service: FoursquareService,
        private val cache: FoursquareLocalCache
) : PagedList.BoundaryCallback<Venue>() {

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: MutableLiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Venue) {
        requestAndSaveData()
    }
    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchVenue(service, fourSquareQueryParams ,{ venues ->

            cache.insert(venues) {
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


}