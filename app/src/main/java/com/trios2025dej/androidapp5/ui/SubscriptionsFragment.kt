package com.trios2025dej.androidapp5.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.models.PodcastResult
import com.trios2025dej.androidapp5.util.PlayerQueue
import com.trios2025dej.androidapp5.util.SubscriptionsManager

class SubscriptionsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: SubscriptionsAdapter

    private val subs: MutableList<PodcastResult> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        recycler = view.findViewById(R.id.recyclerSubs)
        txtEmpty = view.findViewById(R.id.txtEmptySubs)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Initial load
        subs.clear()
        subs.addAll(SubscriptionsManager.getSubscriptions(requireContext()))

        adapter = SubscriptionsAdapter(
            subs,
            onRemove = { podcast, position ->
                SubscriptionsManager.removeSubscription(requireContext(), podcast)

                if (position in subs.indices) {
                    subs.removeAt(position)
                    adapter.notifyItemRemoved(position)
                } else {
                    // Fallback
                    subs.clear()
                    subs.addAll(SubscriptionsManager.getSubscriptions(requireContext()))
                    adapter.notifyDataSetChanged()
                }

                updateEmptyState()
            },
            onPlay = { podcast ->
                // ✅ Set Now Playing
                PlayerQueue.nowPlaying = podcast

                // ✅ Switch to Player tab
                val bottomNav = requireActivity().findViewById<BottomNavigationView?>(R.id.bottomNav)
                bottomNav?.selectedItemId = R.id.nav_player
            }
        )

        recycler.adapter = adapter
        updateEmptyState()

        return view
    }

    override fun onResume() {
        super.onResume()

        // ✅ Refresh list when returning from Search screen
        subs.clear()
        subs.addAll(SubscriptionsManager.getSubscriptions(requireContext()))
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        val empty = subs.isEmpty()
        txtEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        recycler.visibility = if (empty) View.GONE else View.VISIBLE
    }
}
