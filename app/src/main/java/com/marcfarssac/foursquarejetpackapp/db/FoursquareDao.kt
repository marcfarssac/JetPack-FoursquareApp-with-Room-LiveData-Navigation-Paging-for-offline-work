package com.marcfarssac.foursquarejetpackapp.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * Room data access object for accessing the [Venue] table.
 */
@Dao
interface FoursquareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(venues: List<Venue>)

    @Query("SELECT * FROM venues WHERE (name LIKE :queryString)")
    fun venueByName(queryString: String): DataSource.Factory<Int, Venue>

}