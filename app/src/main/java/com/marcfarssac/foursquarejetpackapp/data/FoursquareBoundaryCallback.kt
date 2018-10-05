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
        private val query: String,
        private val ll: String,
        private val limit: Int,
        private val intent: String,
        private val service: FoursquareService,
        private val cache: FoursquareLocalCache
) : PagedList.BoundaryCallback<Venue>() {

    companion object {
        // A page size is not used by the Venues API which returns 50 items at maximum
        // even if requesting a value greater than this
        //private const val NETWORK_PAGE_SIZE = 50
    }

    // The Foursquare Places Api doesn't allow to query pages
    // Still we keep it prepared for future uses
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: MutableLiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    override fun onZeroItemsLoaded() {
        requestAndSaveData(query, ll, limit, intent)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Venue) {
        requestAndSaveData(query, ll, limit, intent)
    }
    private fun requestAndSaveData(query: String, ll: String, limit: Int, intent: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchVenue(service, query, ll, limit, intent, { venues ->

        val localVenues: ArrayList<Venue> = arrayListOf()
        // ToDo Adapt backend response to only needed room database data
        for(venue in venues)
            localVenues.add(Venue(venue.id, venue.name, venue.location?.address, venue.location?.distance, venue.location?.lat, venue.location?.lng))

            cache.insert(localVenues) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }


}