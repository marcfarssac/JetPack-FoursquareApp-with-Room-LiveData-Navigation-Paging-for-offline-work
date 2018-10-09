package com.marcfarssac.foursquarejetpackapp.data

import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.marcfarssac.foursquarejetpackapp.api.FoursquareService
import com.marcfarssac.foursquarejetpackapp.db.FoursquareLocalCache
import com.marcfarssac.foursquarejetpackapp.model.VenueSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class FoursquareRepository(
        private val service: FoursquareService,
        private val cache: FoursquareLocalCache
) {

    /**
     * Search repositories whose names match the query.
     */
    fun search(fourSquareQuery: FoursquareCallParams): VenueSearchResult {
        Log.d("FoursquareRepository", "New query: ${fourSquareQuery.query}")

        // Get data source factory from the local cache
        val query = '%'+fourSquareQuery.query+'%'
        val dataSourceFactory = cache.venuesByName(query)
        // Construct the boundary callback
        val boundaryCallback = FoursquareBoundaryCallback(fourSquareQuery, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return VenueSearchResult(data, networkErrors)
    }

    companion object {
        const val DATABASE_PAGE_SIZE = 20
    }
}