@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package com.marcfarssac.foursquarejetpackapp

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.marcfarssac.foursquarejetpackapp.data.CustomMutableLiveData
import com.marcfarssac.foursquarejetpackapp.data.FoursquareCallParams
import com.marcfarssac.foursquarejetpackapp.data.FoursquareRepository
import com.marcfarssac.foursquarejetpackapp.model.Venue
import com.marcfarssac.foursquarejetpackapp.model.VenueSearchResult

/**
 * ViewModel for the MainActivity screen.
 * The ViewModel works with the [FoursquareRepository] to get the data.
 */
class MainActivityViewModel(private val repository: FoursquareRepository) : ViewModel() {

    private var newQuery = CustomMutableLiveData<FoursquareCallParams>()

    private val venueResult: LiveData<VenueSearchResult> = Transformations.map(newQuery) {
        repository.search(it)
    }

    val venues: LiveData<PagedList<Venue>> = Transformations.switchMap(venueResult) { it ->
        it.data }

    val networkErrors: LiveData<String> = Transformations.switchMap(venueResult) { it ->
        it.networkErrors
    }

    /**
     * Search a repository based on a query string.
     */
    fun searchVenue(queryString: String, latLng: String, limit: Int, intent: String) {
        newQuery.postValue(FoursquareCallParams(queryString, latLng, limit, intent ))

    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = newQuery.value?.query
}