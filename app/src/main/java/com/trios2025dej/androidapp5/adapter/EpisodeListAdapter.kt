package com.trios2025dej.androidapp5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.databinding.EpisodeItemBinding
import com.trios2025dej.androidapp5.models.Episode
import com.trios2025dej.androidapp5.util.DateUtils

class EpisodeListAdapter(
    private var items: List<Episode>,
    private val onClick: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeListAdapter.VH>() {

    inner class VH(val binding: EpisodeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val ep = items[position]

        holder.binding.titleView.text = ep.title ?: "Episode"
        holder.binding.descView.text = ep.description ?: ""

        val durMs = ep.durationMillis ?: 0L
        holder.binding.durationView.text = formatMillis(durMs)

        holder.binding.releaseDateView.text = DateUtils.dateToShortDate(ep.releaseDate)

        holder.binding.root.setOnClickListener { onClick(ep) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Episode>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun formatMillis(ms: Long): String {
        val totalSec = ms / 1000
        val m = totalSec / 60
        val s = totalSec % 60
        return "%02d:%02d".format(m, s)
    }
}
