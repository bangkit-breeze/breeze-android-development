package com.example.breeze.ui.fragments.onboarding.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.breeze.R
import com.example.breeze.databinding.FragmentScreenFirstBinding
import com.example.breeze.databinding.FragmentScreenThirdBinding


class ThirdScreen : Fragment() {
    private var _binding: FragmentScreenThirdBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreenThirdBinding.inflate(inflater, container, false)
        binding.btnFinish.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginActivity)
            onBoardingIsFinished()
        }

        return binding.root
    }
    private fun onBoardingIsFinished(){
        val sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}