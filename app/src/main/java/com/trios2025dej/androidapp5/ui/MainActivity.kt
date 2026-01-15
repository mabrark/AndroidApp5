package com.trios2025dej.androidapp5.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trios2025dej.androidapp5.R
import com.trios2025dej.androidapp5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val searchFragmentTag = "SEARCH"
    private val subsFragmentTag = "SUBS"
    private val playerFragmentTag = "PLAYER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Create fragments ONCE
            val search = SearchFragment()
            val subs = SubscriptionsFragment()
            val player = PlayerFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, search, searchFragmentTag)
                .add(R.id.fragmentContainer, subs, subsFragmentTag).hide(subs)
                .add(R.id.fragmentContainer, player, playerFragmentTag).hide(player)
                .commit()

            binding.bottomNav.selectedItemId = R.id.nav_search
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            showFragment(
                when (item.itemId) {
                    R.id.nav_search -> searchFragmentTag
                    R.id.nav_subs -> subsFragmentTag
                    R.id.nav_player -> playerFragmentTag
                    else -> searchFragmentTag
                }
            )
            true
        }
    }

    private fun showFragment(tagToShow: String) {
        val search = supportFragmentManager.findFragmentByTag(searchFragmentTag)
        val subs = supportFragmentManager.findFragmentByTag(subsFragmentTag)
        val player = supportFragmentManager.findFragmentByTag(playerFragmentTag)

        supportFragmentManager.beginTransaction().apply {
            if (search != null) hide(search)
            if (subs != null) hide(subs)
            if (player != null) hide(player)

            val target = supportFragmentManager.findFragmentByTag(tagToShow)
            if (target != null) show(target)
        }.commit()
    }
}
