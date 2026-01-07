package com.trios2025dej.androidapp5.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.models.PodcastResult

class SubscriptionsAdapter(
    private val items: MutableList<PodcastResult>,
    private val onRemove: (PodcastResult, Int) -> Unit,
    private val onPlay: (PodcastResult) -> Unit
) : RecyclerView.Adapter<SubscriptionsAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtArtist: TextView = itemView.findViewById(R.id.txtArtist)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
        val btnPlay: Button = itemView.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscription, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.collectionName ?: "(No title)"
        holder.txtArtist.text = item.artistName ?: ""

        holder.btnRemove.setOnClickListener {
            onRemove(item, position)
        }

        holder.btnPlay.setOnClickListener {
            onPlay(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
