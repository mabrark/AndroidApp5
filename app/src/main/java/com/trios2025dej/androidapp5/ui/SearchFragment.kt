package com.trios2025dej.androidapp5.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.databinding.FragmentSearchBinding
import com.trios2025dej.androidapp5.models.PodcastResult
import com.trios2025dej.androidapp5.network.ApiClient
import com.trios2025dej.androidapp5.util.PlayerQueue
import com.trios2025dej.androidapp5.util.SubscriptionsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PodcastAdapter
    private val items = mutableListOf<PodcastResult>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PodcastAdapter(
            items = items,
            onPlay = { selected -> playPodcast(selected) },
            onSubscribeToggle = { selected -> toggleSubscribe(selected) }
        )

        binding.recyclerResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerResults.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val term = binding.inputSearch.text?.toString()?.trim().orEmpty()
            if (term.isBlank()) {
                Toast.makeText(requireContext(), "Enter a search term", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            search(term)
        }
    }

    private fun playPodcast(selected: PodcastResult) {
        val title = selected.collectionName ?: "Unknown Podcast"
        val previewUrl = selected.previewUrl

        if (previewUrl.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "No preview available for:\n$title",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        PlayerQueue.nowPlaying = selected

        // Switch to Player tab
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_player
    }

    private fun toggleSubscribe(selected: PodcastResult) {
        val id = selected.collectionId
        if (id == null) {
            Toast.makeText(requireContext(), "Cannot subscribe: missing collectionId", Toast.LENGTH_SHORT).show()
            return
        }

        val isSubbed = SubscriptionsManager.isSubscribed(requireContext(), selected)
        if (isSubbed) {
            SubscriptionsManager.removeSubscription(requireContext(), selected)
            Toast.makeText(requireContext(), "Unsubscribed", Toast.LENGTH_SHORT).show()
        } else {
            SubscriptionsManager.addSubscription(requireContext(), selected)
            Toast.makeText(requireContext(), "Subscribed", Toast.LENGTH_SHORT).show()
        }

        // refresh subscribe button label
        adapter.notifyDataSetChanged()
    }

    private fun search(term: String) {
        binding.progress.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.searchPodcasts(term)
                }

                val results: List<PodcastResult> = response.results ?: emptyList()

                items.clear()
                items.addAll(results)
                adapter.notifyDataSetChanged()

                if (items.isEmpty()) {
                    Toast.makeText(requireContext(), "No results found", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Search failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progress.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
