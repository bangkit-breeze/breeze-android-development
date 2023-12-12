package com.example.breeze.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breeze.databinding.FragmentOnBoardingBinding
import com.example.breeze.ui.adapter.frag.OnBoardingAdapter
import com.example.breeze.ui.fragments.onboarding.screen.FirstScreen
import com.example.breeze.ui.fragments.onboarding.screen.SecondScreen
import com.example.breeze.ui.fragments.onboarding.screen.ThirdScreen

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        binding.viewPager.adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.dotsIndicator.attachTo(binding.viewPager)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}