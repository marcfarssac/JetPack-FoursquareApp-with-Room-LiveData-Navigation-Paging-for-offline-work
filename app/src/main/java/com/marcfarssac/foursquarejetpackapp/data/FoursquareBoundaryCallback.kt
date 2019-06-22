package com.marcfarssac.foursquarejetpackapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
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
        private val backendCallParams: FoursquareCallParams,
        private val service: FoursquareService,
        private val cache: FoursquareLocalCache
) : PagedList.BoundaryCallback<Venue>() {

    // keep the last requested page.
// When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Venue) {
        requestAndSaveData()
    }

    override fun onItemAtFrontLoaded(itemAtFront: Venue) {
        requestAndSaveData()
    }
    private fun requestAndSaveData() {
        if ((isRequestInProgress) || (lastRequestedPage>1))return

        isRequestInProgress = true
        searchVenue(service, backendCallParams ,{ venues ->

            cache.insert(venues) {
                isRequestInProgress = false
                lastRequestedPage++
            }
        }, { error ->
//            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


}