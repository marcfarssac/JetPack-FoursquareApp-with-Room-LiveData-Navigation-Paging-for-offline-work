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

data class Base(val meta: Meta?, val response: Response)

data class Categories(val id: String?, val name: String?, val pluralName: String?, val shortName: String?, val icon: Icon?, val primary: Boolean?)

data class Icon(val prefix: String?, val suffix: String?)

data class LabeledLatLngs614145358(val label: String?, val lat: Number?, val lng: Number?)

data class Location(val address: String?, val crossStreet: String?, val lat: Number?, val lng: Number?, val labeledLatLngs: List<LabeledLatLngs614145358>?, val distance: Number?, val postalCode: String?, val cc: String?, val city: String?, val state: String?, val country: String?, val formattedAddress: List<String>?)

data class Meta(val code: Number?, val requestId: String?)

data class Response(val venues: List<Venues>?)

data class VenuePage(val id: String?)

data class Venues(val id: String?, val name: String?, val location: Location?, val categories: List<Categories>?, val venuePage: VenuePage?)

//data class FourSquareApiPlacesResponse(val meta: Meta?, val foursquareVenues: Venues)
//
//data class BeenHere(val count: Number?, val lastCheckinExpiredAt: Number?, val marked: Boolean?, val unconfirmedCount: Number?)
//
//data class Categories(val id: String?, val name: String?, val pluralName: String?, val shortName: String?, val icon: Icon?, val primary: Boolean?)
//
//data class Contact(val contact: Any?)
//
//data class Icon(val prefix: String?, val suffix: String?)
//
//data class LabeledLatLngs(val label: String?, val lat: Number?, val lng: Number?)
//
//data class Location(val address: String?, val crossStreet: String?, val lat: Double, val lng: Double, val labeledLatLngs: List<LabeledLatLngs>?, val distance: Int?, val postalCode: String?, val cc: String?, val neighborhood: String?, val city: String?, val state: String?, val country: String?, val formattedAddress: List<String>?)
//
//data class Meta(val code: Number?, val requestId: String?)
//
//data class Venues(val venueDetailsList: List<VenueDetails>)
//
//data class Stats(val tipCount: Number?, val usersCount: Number?, val checkinsCount: Number?, val visitsCount: Number?)
//
//data class VenuePage(val id: String?)
//
//data class VenueDetails(val id: String, val name: String?, val contact: Contact?,
//                        val location: Location?, val categories: List<Categories>?,
//                        val verified: Boolean?, val stats: Stats?, val beenHere: BeenHere?, val venuePage: VenuePage?, val referralId: String?, val venueChains: List<Any>?, val hasPerk: Boolean?)