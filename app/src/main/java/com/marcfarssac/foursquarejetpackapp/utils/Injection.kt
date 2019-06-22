
package com.marcfarssac.foursquarejetpackapp.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.marcfarssac.foursquarejetpackapp.api.FoursquareService
import com.marcfarssac.foursquarejetpackapp.data.FoursquareRepository
import com.marcfarssac.foursquarejetpackapp.db.FoursquareDatabase
import com.marcfarssac.foursquarejetpackapp.db.FoursquareLocalCache
import com.marcfarssac.foursquarejetpackapp.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 */
object Injection {

    /**
     * Creates an instance of [FoursquareLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): FoursquareLocalCache {
        val database = FoursquareDatabase.getInstance(context)
        return FoursquareLocalCache(database.foursquareDao(), Executors.newWorkStealingPool())
    }

    /**
     * Creates an instance of [FoursquareRepository] based on the [FoursquareService] and a
     * [FoursquareLocalCache]
     */
    private fun provideFoursquareRepository(context: Context): FoursquareRepository {
        return FoursquareRepository(FoursquareService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * ViewModel objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideFoursquareRepository(context))
    }

}