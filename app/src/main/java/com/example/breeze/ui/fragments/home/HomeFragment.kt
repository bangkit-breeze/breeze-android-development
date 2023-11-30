package com.example.breeze.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.breeze.R
import com.example.breeze.databinding.BottomDialogInfoCarbonBinding
import com.example.breeze.databinding.BottomDialogInfoEventBinding
import com.example.breeze.databinding.BottomDialogInfoFoodBinding
import com.example.breeze.databinding.BottomDialogInfoVechileBinding
import com.example.breeze.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupInfoListeners()

        return binding.root
    }
    private fun setupInfoListeners() {
        binding?.apply {
            btnInfoFood.setOnClickListener {
                animateButtonClick(btnInfoFood)
                showFoodBottomSheet()
            }
            btnInfoVechile.setOnClickListener {
                animateButtonClick(btnInfoVechile)
                showVehicleBottomSheet()
            }

            btnInfoEvent.setOnClickListener {
                animateButtonClick( btnInfoEvent)
                showEventBottomSheet()
            }

            btnInfoCarbon.setOnClickListener {
                animateButtonClick( btnInfoCarbon)
                showCarbonBottomSheet()
            }
        }
    }

   


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}