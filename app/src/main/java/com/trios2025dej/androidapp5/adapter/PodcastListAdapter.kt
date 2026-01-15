package com.trios2025dej.androidapp5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.databinding.ItemPodcastBinding
import com.trios2025dej.androidapp5.models.PodcastResult

class PodcastListAdapter(
    private var items: List<PodcastResult>,
    private val onClick: (PodcastResult) -> Unit
) : RecyclerView.Adapter<PodcastListAdapter.VH>() {

    inner class VH(val binding: ItemPodcastBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPodcastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = items[position]
        holder.binding.txtTitle.text = p.collectionName ?: "Podcast"
        holder.binding.txtArtist.text = p.artistName ?: ""
        holder.binding.root.setOnClickListener { onClick(p) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<PodcastResult>) {
        items = newItems
        notifyDataSetChanged()
    }
}
