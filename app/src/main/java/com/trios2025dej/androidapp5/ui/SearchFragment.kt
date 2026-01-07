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

        // ✅ Adapter + click
        adapter = PodcastAdapter(items) { selected: PodcastResult ->

            val title = selected.collectionName ?: "Unknown Podcast"
            val audioUrl = selected.previewUrl  // may be null (preview not available)

            if (audioUrl.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    "No preview available for:\n$title\n\nNext step: parse feedUrl RSS for episodes.",
                    Toast.LENGTH_LONG
                ).show()
                return@PodcastAdapter
            }

            // ✅ Set Now Playing (store the selected podcast in PlayerQueue)
            PlayerQueue.nowPlaying = selected


            // ✅ Switch to Player tab
            requireActivity()
                .findViewById<BottomNavigationView>(R.id.bottomNav)
                .selectedItemId = R.id.nav_player
        }

        binding.recyclerResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerResults.adapter = adapter

        // ✅ Search button
        binding.btnSearch.setOnClickListener {
            val term = binding.inputSearch.text.toString().trim()
            if (term.isBlank()) {
                Toast.makeText(requireContext(), "Enter a search term", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Advanced filter: regex (optional)
            val regexText = binding.inputRegex.text.toString().trim()
            val regex = regexText.takeIf { it.isNotBlank() }

            search(term, regex)
        }
    }

    private fun search(term: String, regex: String?) {
        binding.progress.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.searchPodcasts(term)
                }

                val results: List<PodcastResult> = response.results ?: emptyList()

                val filtered = if (!regex.isNullOrBlank()) {
                    val r = Regex(regex, RegexOption.IGNORE_CASE)
                    results.filter { (it.collectionName ?: "").contains(r) }
                } else {
                    results
                }

                items.clear()
                items.addAll(filtered)
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
