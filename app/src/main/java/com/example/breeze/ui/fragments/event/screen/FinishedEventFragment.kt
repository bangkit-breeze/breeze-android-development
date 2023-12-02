package com.example.breeze.ui.fragments.event.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breeze.R
import com.example.breeze.databinding.FragmentExploreEventBinding
import com.example.breeze.databinding.FragmentFinishedEventBinding

class FinishedEventFragment : Fragment() {
    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =    FragmentFinishedEventBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}