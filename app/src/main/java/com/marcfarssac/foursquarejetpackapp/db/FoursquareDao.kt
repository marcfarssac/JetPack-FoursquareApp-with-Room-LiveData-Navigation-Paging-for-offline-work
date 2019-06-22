package com.marcfarssac.foursquarejetpackapp.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * Room data access object for accessing the [Venue] table.
 */
@Dao
interface FoursquareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(venues: List<Venue>)

    @Query("SELECT * FROM venues WHERE queried LIKE :queryString")
    fun venueByName(queryString: String): DataSource.Factory<Int, Venue>
}