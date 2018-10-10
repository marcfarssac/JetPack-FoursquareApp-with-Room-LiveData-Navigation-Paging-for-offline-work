package com.marcfarssac.foursquarejetpackapp.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Immutable model class for a VenueDetails that holds the information required to find by name and
 * it and display it on a map.
 * Objects of this type are received from the Foursquare API, therefore all the fields are annotated
 * with the serialized name.
 * This class also defines the Room repos table, where the repo [id] is the primary key.
 */
//@Entity(tableName = "venues", indices = arrayOf(Index("id")))
@Entity(tableName = "venues")
data class Venue(
        @PrimaryKey @field:SerializedName("id") var id: String ="",
        @field:SerializedName("queried")  var queried: String ="",
        @field:SerializedName("name")  var name: String ="",
        @field:SerializedName("address") var address: String? ="",
        @field:SerializedName("distance") var distance: Int? = 0,
        @field:SerializedName("lng") var lng: Double? = 0.0,
        @field:SerializedName("lat") var lat: Double? =0.0,
        @Ignore var ignored: String? = null

)

//@Entity(tableName = "venues", indices = arrayOf(Index("query"), Index(value = *arrayOf("query"))))
//class Venue {
//
//    @PrimaryKey
//    @SerializedName("id")
//    var id: String? = null
//    @ColumnInfo(name = "query")
//    @SerializedName("name")
//    var name: String
//    @SerializedName("address")
//    var address: String? = null
//    @SerializedName("distance")
//    var distance: Int = 0
//    @SerializedName("lng")
//    var lng: Double = 0.toDouble()
//    @SerializedName("lat")
//    var lat: Double = 0.toDouble()
//    @Ignore
//    private val ignored: String? = null
//}
//
//

