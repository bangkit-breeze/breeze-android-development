package com.example.breeze.ui.fragments.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.user.DataUser
import com.example.breeze.databinding.FragmentLeaderBoardBinding
import com.example.breeze.ui.adapter.frag.EventPagerAdapter
import com.example.breeze.ui.factory.LeaderBoardViewModelFactory
import com.example.breeze.ui.fragments.leaderboard.screen.AllLeaderBoardFragment
import com.example.breeze.ui.fragments.leaderboard.screen.WeekLeaderBoardFragment
import com.example.breeze.ui.viewmodel.LeaderBoardViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.showToastString
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LeaderBoardFragment : Fragment() {
    private var _binding: FragmentLeaderBoardBinding? = null
    private val binding get() = _binding!!
    private val  viewModel: LeaderBoardViewModel by viewModels {
        LeaderBoardViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var dataUser: LoginResult
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentLeaderBoardBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        observeSession()
        observeProfile()
    }

    private fun observeSession() {
        viewModel.getSession().observe(viewLifecycleOwner) {
            dataUser = it
        }
    }

    private fun observeProfile() {
        viewModel.getProfile(dataUser.token).observe(this) { result ->
            when (result) {
                is Result.Loading -> return@observe
                is Result.Success -> updateUI(result.data.dataUser)
                is Result.Error -> showError(result.error)
            }
        }
    }

    private fun updateUI(user: DataUser?) {
        with(binding) {
            tvName.text = user?.fullName.orEmpty()
            tvPoints.text = "${user?.experiences ?: 0} Exp"
            Glide.with(this@LeaderBoardFragment)
                .load(user?.avatarUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(ivPicture)
        }
    }

    private fun showError(error: String) {
        showToastString(requireContext(), error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerAndTabLayout()
    }

    private fun setupViewPagerAndTabLayout() {
        val viewPager: ViewPager2 = binding?.viewPager ?: return
        val adapter = EventPagerAdapter(this)
        adapter.addFragment(AllLeaderBoardFragment())
        adapter.addFragment(WeekLeaderBoardFragment())
        viewPager.adapter = adapter

        val tabLayout: TabLayout = binding?.tabs ?: return
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All Time"
                1 -> "Weekly"
                else -> ""
            }
        }.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}