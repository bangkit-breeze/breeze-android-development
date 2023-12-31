package com.example.breeze.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.user.DataUser
import com.example.breeze.data.model.response.user.UserProfileResponse
import com.example.breeze.databinding.FragmentProfileBinding
import com.example.breeze.ui.activities.login.LoginActivity
import com.example.breeze.ui.activities.profile.DetailProfileActivity
import com.example.breeze.ui.activities.profile.exchange.ExchangeTokenActivity
import com.example.breeze.ui.activities.profile.help.HelpCenterActivity
import com.example.breeze.ui.factory.ProfileViewModelFactory
import com.example.breeze.ui.viewmodel.ProfileViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.number.NumberUtils

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val  viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var tokenUser: LoginResult
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupInfoListeners()
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        viewModel.getToken().observe(viewLifecycleOwner) {
            tokenUser = it
            tokenUser.token?.let { token -> viewModel.getProfile(token).observe(viewLifecycleOwner, ::handleProfile) }
        }
    }

    private fun handleProfile(result: Result<UserProfileResponse>) {
        when (result) {
            is Result.Loading -> return
            is Result.Success -> result.data.dataUser?.let { showUserProfile(it) }
            is Result.Error -> showToast(result.error)
        }
    }

    private fun showUserProfile(userProfile: DataUser) {
        with(binding) {
            tvName.text = userProfile.fullName.toString()
            tvEmail.text = userProfile.email.toString()
            userProfile.experiences?.let {
                tvLevel.text = NumberUtils.calculateLevelProfile(it).toString()
            }
            tvPoint.text = userProfile.points.toString()
            Glide.with(this@ProfileFragment)
                .load(userProfile.avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(ivPicture)
            tvRank.text = "NaN"
        }
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
            btnHelpcenter.setOnClickListener {
                val intent = Intent(activity, HelpCenterActivity::class.java)
                startActivity(intent)
            }
           btnPoints.setOnClickListener {
                val intent = Intent(activity, ExchangeTokenActivity::class.java)
                startActivity(intent)
            }
            btnLogout.setOnClickListener {
                showLogoutDialog()
            }
            val features = listOf(
               btnSubscription,
                btnSharetofriend, btnPrivacy,
                btnLanguage, btnDarkmode
            )

            features.forEach { btn ->
                btn.setOnClickListener {
                    showToast(resources.getString(R.string.feature_not_yet))
                }
            }
        }
    }

    private fun showLogoutDialog() {
        val customDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_logout, null)
        val alertDialog = buildAlertDialog(customDialogView)
        val yesButton = customDialogView.findViewById<Button>(R.id.btnyes)
        val noButton = customDialogView.findViewById<Button>(R.id.btnno)
        yesButton.setOnClickListener {
            handleYesButtonClick()
        }
        noButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    private fun buildAlertDialog(customDialogView: View): AlertDialog {
        return AlertDialog.Builder(requireContext())
            .setView(customDialogView)
            .create()
    }
    private fun handleYesButtonClick() {
        viewModel.deleteUserLogin()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}