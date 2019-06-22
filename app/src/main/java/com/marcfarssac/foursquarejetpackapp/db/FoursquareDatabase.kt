package com.marcfarssac.foursquarejetpackapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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