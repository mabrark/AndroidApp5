package com.trios2025dej.androidapp5.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trios2025dej.androidapp5.databinding.FragmentPlayerBinding
import com.trios2025dej.androidapp5.util.PlayerQueue

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val now = PlayerQueue.nowPlaying
        binding.txtNowPlaying.text = now?.collectionName ?: "Nothing playing"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
