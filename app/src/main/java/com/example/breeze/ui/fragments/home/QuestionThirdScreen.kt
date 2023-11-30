package com.example.breeze.ui.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.R
import com.example.breeze.databinding.FragmentQuestionScreenBinding
import com.example.breeze.databinding.FragmentQuestionThirdScreenBinding


class QuestionThirdScreen : Fragment() {
    private var _binding: FragmentQuestionThirdScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionThirdScreenBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}