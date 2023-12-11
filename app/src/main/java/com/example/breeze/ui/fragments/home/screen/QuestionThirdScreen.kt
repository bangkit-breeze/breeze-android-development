package com.example.breeze.ui.fragments.home.screen

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breeze.R
import com.example.breeze.databinding.FragmentQuestionThirdScreenBinding


class QuestionThirdScreen : Fragment() {
    private var _binding: FragmentQuestionThirdScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionThirdScreenBinding.inflate(inflater, container, false)
        val fullText = resources.getString(R.string.text_questione_tree)
        val spannableString = SpannableString(fullText)

        val carbonStartIndex = fullText.indexOf(resources.getString(R.string.text_carbon))
        val carbonEndIndex = carbonStartIndex + resources.getString(R.string.text_carbon).length
        val carbonColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(carbonColorSpan, carbonStartIndex, carbonEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val calorieStartIndex = fullText.indexOf(resources.getString(R.string.text_calorie))
        val calorieEndIndex = calorieStartIndex + resources.getString(R.string.text_calorie).length
        val calorieColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(calorieColorSpan, calorieStartIndex, calorieEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        binding.textView.text = spannableString

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}