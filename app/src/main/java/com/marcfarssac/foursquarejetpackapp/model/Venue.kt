package com.marcfarssac.foursquarejetpackapp.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a VenueDetails that holds the information required to find by name and
 * it and display it on a map.
 * Objects of this type are received from the Foursquare API, therefore all the fields are annotated
 * with the serialized name.
 * This class also defines the Room repos table, where the repo [id] is the primary key.
 */
@Entity(tableName = "venues")
data class Venue(
        @PrimaryKey @field:SerializedName("id") val id: String,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("address") val address: String?,
        @field:SerializedName("distance") val distance: Int?,
        @field:SerializedName("lng") val lng: Double?,
        @field:SerializedName("lat") val lat: Double?

)
