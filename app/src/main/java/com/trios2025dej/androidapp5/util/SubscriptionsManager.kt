package com.trios2025dej.androidapp5.util

import android.content.Context
import com.trios2025dej.androidapp5.models.PodcastResult
import org.json.JSONArray
import org.json.JSONObject

object SubscriptionsManager {

    private const val PREFS = "podplay_prefs"
    private const val KEY_SUBS = "subscriptions"

    fun getSubscriptions(context: Context): MutableList<PodcastResult> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_SUBS, "[]") ?: "[]"

        val arr = try {
            JSONArray(raw)
        } catch (_: Exception) {
            JSONArray()
        }

        val list = mutableListOf<PodcastResult>()

        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue

            // Treat missing/0 as null so we don’t accidentally subscribe to id=0
            val id = o.optLong("collectionId", 0L).let { if (it <= 0L) null else it }

            // Skip invalid entries (no id)
            if (id == null) continue

            list.add(
                PodcastResult(
                    collectionId = id,
                    collectionName = o.optString("collectionName", null),
                    artistName = o.optString("artistName", null),
                    artworkUrl100 = o.optString("artworkUrl100", null),
                    feedUrl = o.optString("feedUrl", null),
                    previewUrl = o.optString("previewUrl", null)
                )
            )
        }

        return list
    }

    fun isSubscribed(context: Context, podcast: PodcastResult): Boolean {
        val id = podcast.collectionId ?: return false
        return getSubscriptions(context).any { it.collectionId == id }
    }

    fun addSubscription(context: Context, podcast: PodcastResult): Boolean {
        val id = podcast.collectionId ?: return false

        val list = getSubscriptions(context)
        if (list.any { it.collectionId == id }) return false

        list.add(podcast)
        save(context, list)
        return true
    }

    fun removeSubscription(context: Context, podcast: PodcastResult): Boolean {
        val id = podcast.collectionId ?: return false

        val list = getSubscriptions(context)
        val newList = list.filterNot { it.collectionId == id }

        val changed = newList.size != list.size
        if (changed) save(context, newList)

        return changed
    }

    private fun save(context: Context, list: List<PodcastResult>) {
        val arr = JSONArray()

        list.forEach { p ->
            val id = p.collectionId ?: return@forEach // don’t save invalid
            if (id <= 0L) return@forEach

            val o = JSONObject()
            o.put("collectionId", id)
            o.put("collectionName", p.collectionName)
            o.put("artistName", p.artistName)
            o.put("artworkUrl100", p.artworkUrl100)
            o.put("feedUrl", p.feedUrl)
            o.put("previewUrl", p.previewUrl)
            arr.put(o)
        }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SUBS, arr.toString())
            .apply()
    }
}
