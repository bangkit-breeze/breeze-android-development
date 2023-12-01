package com.example.breeze.ui.fragments.home

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        val fullText = resources.getString(R.string.text_questione_two)
        val spannableString = SpannableString(fullText)
        val foodStartIndex = fullText.indexOf(resources.getString(R.string.text_food))
        val foodEndIndex = foodStartIndex + resources.getString(R.string.text_food).length
        val foodColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(foodColorSpan, foodStartIndex, foodEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val vehicleStartIndex = fullText.indexOf(resources.getString(R.string.text_vehicle))
        val vehicleEndIndex = vehicleStartIndex + resources.getString(R.string.text_vehicle).length
        val vehicleColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(vehicleColorSpan, vehicleStartIndex, vehicleEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val carbonStartIndex = fullText.indexOf(resources.getString(R.string.text_carbon))
        val carbonEndIndex = carbonStartIndex + resources.getString(R.string.text_carbon).length
        val carbonColorSpan = ForegroundColorSpan(resources.getColor(R.color.light_primary))
        spannableString.setSpan(carbonColorSpan, carbonStartIndex, carbonEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textView.text = spannableString
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}