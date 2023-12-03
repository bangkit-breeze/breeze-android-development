package com.example.breeze.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.breeze.R
import com.example.breeze.databinding.FragmentHomeBinding
import com.example.breeze.databinding.FragmentProfileBinding
import com.example.breeze.ui.activities.details.carbon.DetailCarbonActivity
import com.example.breeze.ui.activities.profile.DetailProfileActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupInfoListeners()
        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupInfoListeners() {
        binding?.apply {

            btnProfile.setOnClickListener {
                val intent = Intent(activity, DetailProfileActivity::class.java)
                startActivity(intent)
            }
            val features = listOf(
               btnPoints, btnSubscription,
                btnSharetofriend, btnPrivacy, btnHelpcenter,
                btnLanguage, btnDarkmode, btnLogout
            )

            features.forEach { btn ->
                btn.setOnClickListener {
                    showToast(resources.getString(R.string.feature_not_yet))
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}