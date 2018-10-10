package com.marcfarssac.foursquarejetpackapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * Database schema that holds the list of venues.
 */
@Database(
        entities = [Venue::class],
        version = 2,
        exportSchema = false
)
abstract class FoursquareDatabase : RoomDatabase() {

    abstract fun foursquareDao(): FoursquareDao

    companion object {

        @Volatile
        private var INSTANCE: FoursquareDatabase? = null

        fun getInstance(context: Context): FoursquareDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        FoursquareDatabase::class.java, "SearchedVenues.db")
                        .build()
    }
}