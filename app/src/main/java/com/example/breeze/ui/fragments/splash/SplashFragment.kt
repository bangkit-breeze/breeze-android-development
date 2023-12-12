package com.example.breeze.ui.fragments.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.breeze.R
import com.example.breeze.ui.viewmodel.LoginViewModel
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.factory.AuthViewModelFactory
import com.example.breeze.ui.viewmodel.OnBoardingViewModel
import com.example.breeze.ui.viewmodel.OnBoardingViewModelFactory
import com.example.breeze.utils.Constants
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: OnBoardingViewModel by viewModels {
        OnBoardingViewModelFactory(requireContext())
    }

    private val loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel.getUserLogin().observe(viewLifecycleOwner) { user ->
            val delayIntent = Intent(
                requireActivity(),
                if (user.token.isNullOrEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        lifecycleScope.launch {
                            findNavController().navigate(getDestination())
                        }
                    }, Constants.SPLASH_SCREEN_DELAY)
                    return@observe
                } else {
                    MainActivity::class.java
                }
            )

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(delayIntent)
                requireActivity().finish()
            }, Constants.SPLASH_SCREEN_DELAY)
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    private suspend fun getDestination(): Int {
        return if (viewModel.isOnBoardingFinished()) {
            R.id.action_splashFragment_to_loginActivity
        } else {
            R.id.action_splashFragment_to_onBoardingFragment
        }
    }
}