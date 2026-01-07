package com.trios2025dej.androidapp5.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.models.PodcastResult
import com.trios2025dej.androidapp5.util.SubscriptionsManager
import com.trios2025dej.androidapp5.util.PlayerQueue // optional if you have it

class SubscriptionsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: SubscriptionsAdapter

    private lateinit var subs: MutableList<PodcastResult>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        recycler = view.findViewById(R.id.recyclerSubs)
        txtEmpty = view.findViewById(R.id.txtEmptySubs)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Load subscriptions from SharedPreferences
        subs = SubscriptionsManager.getSubscriptions(requireContext())

        adapter = SubscriptionsAdapter(
            subs,
            onRemove = { podcast, position ->
                // remove from prefs
                SubscriptionsManager.removeSubscription(requireContext(), podcast)
                // remove from list + update UI
                subs.removeAt(position)
                adapter.notifyItemRemoved(position)
                updateEmptyState()
            },
            onPlay = { podcast ->
                // If you have a queue system, set it here:
                // Example: PlayerQueue.nowPlaying = podcast
                // Then switch to Player tab.
               // ONLY if your PlayerQueue supports this

                // If you DON'T have this yet, comment out line above and we’ll wire it next.
            }
        )

        recycler.adapter = adapter

        updateEmptyState()
        return view
    }

    override fun onResume() {
        super.onResume()
        // Refresh list when coming back from Search
        subs.clear()
        subs.addAll(SubscriptionsManager.getSubscriptions(requireContext()))
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        txtEmpty.visibility = if (subs.isEmpty()) View.VISIBLE else View.GONE
    }
}
