package com.trios2025dej.androidapp5.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.databinding.ItemPodcastBinding
import com.trios2025dej.androidapp5.models.PodcastResult

class PodcastAdapter(
    private val items: List<PodcastResult>,
    private val onClick: (PodcastResult) -> Unit
) : RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder>() {

    inner class PodcastViewHolder(val binding: ItemPodcastBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val binding = ItemPodcastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PodcastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val item = items[position]

        holder.binding.txtTitle.text = item.collectionName ?: "Unknown Podcast"
        holder.binding.txtArtist.text = item.artistName ?: "Unknown Artist"

        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
