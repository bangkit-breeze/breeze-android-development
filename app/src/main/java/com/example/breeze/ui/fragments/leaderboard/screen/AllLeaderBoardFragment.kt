package com.example.breeze.ui.fragments.leaderboard.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breeze.R
import com.example.breeze.databinding.FragmentActiveEventBinding
import com.example.breeze.databinding.FragmentAllLeaderBoardBinding


class AllLeaderBoardFragment : Fragment() {

    private var _binding: FragmentAllLeaderBoardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentAllLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
      }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
