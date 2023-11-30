package com.example.breeze.ui.fragments.home

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.R
import com.example.breeze.databinding.FragmentQuestionScreenBinding
import com.example.breeze.databinding.FragmentScreenFirstBinding


class QuestionScreen  : Fragment() {
    private var _binding: FragmentQuestionScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionScreenBinding.inflate(inflater, container, false)

        val fullText = "You’re on a good way! Your day is going amazing"
        val spannableString = SpannableString(fullText)
        val goodWayStartIndex = fullText.indexOf("good way")
        val goodWayEndIndex = goodWayStartIndex + "good way".length
        val goodWayColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(goodWayColorSpan, goodWayStartIndex, goodWayEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val amazingStartIndex = fullText.indexOf("amazing")
        val amazingEndIndex = amazingStartIndex + "amazing".length
        val amazingColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary)) 
        spannableString.setSpan(amazingColorSpan, amazingStartIndex, amazingEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView.text = spannableString

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    interface OnNextClickListener {
        fun onNextClicked()
    }

}