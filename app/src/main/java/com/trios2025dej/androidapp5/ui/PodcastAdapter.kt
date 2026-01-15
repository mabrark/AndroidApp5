package com.trios2025dej.androidapp5.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.models.PodcastResult
import com.trios2025dej.androidapp5.util.SubscriptionsManager

class PodcastAdapter(
    private val items: MutableList<PodcastResult>,
    private val onPlay: (PodcastResult) -> Unit,
    private val onSubscribeToggle: (PodcastResult) -> Unit
) : RecyclerView.Adapter<PodcastAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtArtist: TextView = itemView.findViewById(R.id.txtArtist)
        val btnPlay: Button = itemView.findViewById(R.id.btnPlay)
        val btnSubscribe: Button = itemView.findViewById(R.id.btnSubscribe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_podcast_search, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]

        holder.txtTitle.text = item.collectionName ?: "Untitled Podcast"
        holder.txtArtist.text = item.artistName ?: ""

        // ✅ FIX: only ONE argument
        holder.btnPlay.setOnClickListener { onPlay(item) }

        val isSubbed = SubscriptionsManager.isSubscribed(holder.itemView.context, item)
        holder.btnSubscribe.text = if (isSubbed) "Unsubscribe" else "Subscribe"

        // ✅ FIX: only ONE argument
        holder.btnSubscribe.setOnClickListener {
            onSubscribeToggle(item)
            notifyItemChanged(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = items.size
}
