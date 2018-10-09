package com.marcfarssac.foursquarejetpackapp.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.marcfarssac.foursquarejetpackapp.R
import com.marcfarssac.foursquarejetpackapp.model.Venue

/**
 * View Holder for a [Venue] RecyclerView list item.
 */
class VenueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.venue_name)
    private val address: TextView = view.findViewById(R.id.venue_address)
    private val distance: TextView = view.findViewById(R.id.venue_distance)

    private var venue: Venue? = null

    init {
        view.setOnClickListener {
            //            venue?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
        }
    }

    fun bind(venue: Venue?) {
        if (venue == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            address.visibility = View.GONE
        } else {
            showRepoData(venue)
        }
    }

    private fun showRepoData(venue: Venue) {
        this.venue = venue
        name.text = venue.name

        // if the description is missing, hide the TextView
        var addressVisibility = View.GONE
        if (venue.address != null) {
            address.text = venue.address
            addressVisibility = View.VISIBLE
        }
        address.visibility = addressVisibility

        if (venue?.distance!=0) {
            val resources = this.itemView.context.resources
            distance.text = resources.getString(R.string.distance, venue?.distance.toString())
            distance.visibility = View.VISIBLE
        } else distance.visibility= View.GONE

    }

    companion object {
        fun create(parent: ViewGroup): VenueViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.venue_view_item, parent, false)
            return VenueViewHolder(view)
        }
    }
}
