package com.trios2025dej.androidapp5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trios2025dej.androidapp5.databinding.ActivityMainBinding
import com.trios2025dej.androidapp5.ui.PlayerFragment
import com.trios2025dej.androidapp5.ui.SearchFragment
import com.trios2025dej.androidapp5.ui.SubscriptionsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment())
                .commit()
        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SearchFragment())
                        .commit()
                    true
                }

                R.id.nav_subs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SubscriptionsFragment())
                        .commit()
                    true
                }

                R.id.nav_player -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, PlayerFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}
