/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marcfarssac.foursquarejetpackapp.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TAG = "Foursquare"
private const val client_id = "WFW1MAYPFSDLAOKIKZFXDBFVUU0YO4DQYJFP0W5E1DCHWZ44"
private const val client_secret = "ZF1ALSEJ1XDRU3QZJ3ICPXXNG0BBXIKPI5PU3KYGO1ORLX4P"
private const val version = "20181004"

/**
 * Search venueDetails based on a query.
 *
 * Developer documentation at: https://developer.foursquare.com/docs/api/venues/search
 *
 * Trigger a request to the Foursquare Places API with the following params:
 * @param query A search term to be applied against venue names.
 * @param ll Latitude and longitude of the user’s location. Optional if using intent=global
 * @param limit Number of results to return, up to 50.
 * @param intentParam One of the values below, indicating your intent in performing the search.
 *        If no value is specified, defaults to checkin.

 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of venueDetails received
 * @param onError function that defines how to handle request failure
 */
fun searchVenue(
        api: FoursquareService,
        query: String,
        ll: String,
        limit: Int,
        intentParam: String,
        onSuccess: (venueDetailsList: List<VenueDetails>) -> Unit,
        onError: (error: String) -> Unit) {

    Log.d(TAG, "query: $query, ll: $ll, intent $intentParam, limit: $limit")

    api.searchVenues(query, ll, limit, intentParam).enqueue(
            object : Callback<FourSquareApiPlacesResponse> {
                override fun onFailure(call: Call<FourSquareApiPlacesResponse>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<FourSquareApiPlacesResponse>?,
                        response: Response<FourSquareApiPlacesResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val venueList = response.body()?.venues?.venueDetailsList ?: emptyList()
                        onSuccess(venueList)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

/**
 * Foursquare API communication setup via Retrofit.
 */
interface FoursquareService {

    /**
     * Get repos ordered by stars.
     */
    @GET("search?$API_KEY")
    fun searchVenues(@Query("query") query: String,
                     @Query( "ll") ll: String,
                     @Query( "limit") limit: Int,
                     @Query("intent") intent: String): Call<FourSquareApiPlacesResponse>

    companion object {
        private const val API_KEY = "client_id=$client_id&client_secret=$client_secret&v=$version"
        private const val BASE_URL = "https://api.foursquare.com/v2/venues/"

        fun create(): FoursquareService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
//                    .addConverterFactory(MoshiConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(FoursquareService::class.java)
        }
    }
}