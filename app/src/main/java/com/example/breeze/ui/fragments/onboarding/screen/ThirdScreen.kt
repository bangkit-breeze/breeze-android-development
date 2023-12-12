package com.example.breeze.ui.fragments.onboarding.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.breeze.R
import com.example.breeze.databinding.FragmentScreenThirdBinding
import com.example.breeze.ui.viewmodel.OnBoardingViewModel
import com.example.breeze.ui.viewmodel.OnBoardingViewModelFactory
import kotlinx.coroutines.launch


class ThirdScreen : Fragment() {
    private var _binding: FragmentScreenThirdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnBoardingViewModel by viewModels {
        OnBoardingViewModelFactory(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreenThirdBinding.inflate(inflater, container, false)

        binding.btnFinish.setOnClickListener {
            lifecycleScope.launch {
                viewModel.saveOnBoardingFinished()
                findNavController().navigate(R.id.action_onBoardingFragment_to_loginActivity)
            }
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}