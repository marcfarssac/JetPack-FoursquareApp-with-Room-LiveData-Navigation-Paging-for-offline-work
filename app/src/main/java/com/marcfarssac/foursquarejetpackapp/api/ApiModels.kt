@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package com.marcfarssac.foursquarejetpackapp.api

/**
 *  Api model created using the Kotlin Data class generator to convert a raw http request json result
 *
 *  https://http4k-data-class-gen.herokuapp.com/json
 *
 *  Created by: Marc Farssac
 *  Date: 05.10.2018
 */

// result generated from /json

data class Categories(val id: String?, val name: String?, val pluralName: String?, val shortName: String?, val icon: Icon?, val primary: Boolean?)

data class Icon(val prefix: String?, val suffix: String?)

data class LabeledLatLngs(val label: String?, val lat: Number?, val lng: Number?)

data class Location(val address: String?, val crossStreet: String?, val lat: Double?, val lng: Double?, val labeledLatLngs: List<LabeledLatLngs>?, val distance: Int?, val postalCode: String?, val cc: String?, val city: String?, val state: String?, val country: String?, val formattedAddress: List<String>?)

data class Meta(val code: Number?, val requestId: String?)

data class Response(val venues: List<Venues>?)

data class VenuePage(val id: String?)

data class Venues(val id: String, val name: String?, val location: Location?, val categories: List<Categories>?, val venuePage: VenuePage?)

