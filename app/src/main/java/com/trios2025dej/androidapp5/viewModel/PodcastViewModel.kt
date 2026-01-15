package com.trios2025dej.androidapp5.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trios2025dej.androidapp5.models.PodcastResult
import com.trios2025dej.androidapp5.network.ApiClient
import com.trios2025dej.androidapp5.util.PlayerQueue
import com.trios2025dej.androidapp5.util.SubscriptionsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PodcastViewModel : ViewModel() {

    // ✅ No viewModelScope needed (avoids lifecycle-viewmodel-ktx requirement)
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    // -------------------------
    // Search Results
    // -------------------------
    private val _searchResults = MutableLiveData<List<PodcastResult>>(emptyList())
    val searchResults: LiveData<List<PodcastResult>> = _searchResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun search(term: String) {
        val q = term.trim()
        if (q.isBlank()) {
            _error.value = "Enter a search term"
            return
        }

        _isLoading.value = true
        _error.value = null

        scope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.api.searchPodcasts(q)
                }
                _searchResults.value = response.results ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message ?: "Search failed"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // -------------------------
    // Player
    // -------------------------
    private val _nowPlaying = MutableLiveData<PodcastResult?>(PlayerQueue.nowPlaying)
    val nowPlaying: LiveData<PodcastResult?> = _nowPlaying

    fun play(podcast: PodcastResult) {
        PlayerQueue.nowPlaying = podcast
        _nowPlaying.value = podcast
    }

    fun refreshNowPlaying() {
        _nowPlaying.value = PlayerQueue.nowPlaying
    }

    // -------------------------
    // Subscriptions
    // -------------------------
    private val _subscriptions = MutableLiveData<List<PodcastResult>>(emptyList())
    val subscriptions: LiveData<List<PodcastResult>> = _subscriptions

    fun loadSubscriptions(context: Context) {
        _subscriptions.value = SubscriptionsManager.getSubscriptions(context)
    }

    fun toggleSubscription(context: Context, podcast: PodcastResult): Boolean {
        // returns true if subscribed after toggle
        val isSubbed = SubscriptionsManager.isSubscribed(context, podcast)
        if (isSubbed) {
            SubscriptionsManager.removeSubscription(context, podcast)
        } else {
            SubscriptionsManager.addSubscription(context, podcast)
        }
        loadSubscriptions(context)
        return !isSubbed
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
