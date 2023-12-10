package com.example.breeze.ui.fragments.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.R
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.databinding.FragmentEventBinding
import com.example.breeze.databinding.FragmentLeaderBoardBinding
import com.example.breeze.ui.adapter.EventPagerAdapter
import com.example.breeze.ui.factory.LeaderBoardViewModelFactory
import com.example.breeze.ui.factory.ViewModelFactory
import com.example.breeze.ui.fragments.event.screen.ActiveEventFragment
import com.example.breeze.ui.fragments.event.screen.ExploreEventFragment
import com.example.breeze.ui.fragments.event.screen.FinishedEventFragment
import com.example.breeze.ui.fragments.home.HomeViewModel
import com.example.breeze.ui.fragments.leaderboard.screen.AllLeaderBoardFragment
import com.example.breeze.ui.fragments.leaderboard.screen.WeekLeaderBoardFragment
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