package com.marcfarssac.foursquarejetpackapp.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * Adapter for the list of venues.
 */
class VenuesAdapter : PagedListAdapter<Venue, RecyclerView.ViewHolder>(VENUES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VenueViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val venueItem = getItem(position)
        if (venueItem != null) {
            (holder as VenueViewHolder).bind(venueItem)
        }
    }

    companion object {
        private val VENUES_COMPARATOR = object : DiffUtil.ItemCallback<Venue>() {
            override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean =
                    oldItem == newItem
        }
    }
}