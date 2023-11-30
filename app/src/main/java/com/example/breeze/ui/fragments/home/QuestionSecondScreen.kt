package com.example.breeze.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.R
import com.example.breeze.databinding.FragmentQuestionScreenBinding
import com.example.breeze.databinding.FragmentQuestionSecondScreenBinding


class QuestionSecondScreen  : Fragment() {
    private var _binding: FragmentQuestionSecondScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionSecondScreenBinding.inflate(inflater, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_question)
        binding.btnNext.setOnClickListener {
            viewPager?.currentItem = 2
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}