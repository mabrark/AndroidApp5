package com.trios2025dej.androidapp5.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trios2025dej.androidapp5.models.PodcastResult

object SubscriptionsManager {

    private const val PREFS_NAME = "subscriptions_prefs"
    private const val KEY_SUBS = "subs"

    fun getSubscriptions(context: Context): MutableList<PodcastResult> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SUBS, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<PodcastResult>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun addSubscription(context: Context, podcast: PodcastResult) {
        val subs = getSubscriptions(context)
        if (subs.none { it.collectionId == podcast.collectionId }) {
            subs.add(podcast)
            save(context, subs)
        }
    }

    fun removeSubscription(context: Context, podcast: PodcastResult) {
        val subs = getSubscriptions(context)
        subs.removeAll { it.collectionId == podcast.collectionId }
        save(context, subs)
    }

    private fun save(context: Context, subs: List<PodcastResult>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_SUBS, Gson().toJson(subs))
            .apply()
    }
}
