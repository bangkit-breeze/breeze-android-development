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
import com.example.breeze.databinding.FragmentLeaderBoardBinding
import com.example.breeze.ui.adapter.frag.EventPagerAdapter
import com.example.breeze.ui.factory.LeaderBoardViewModelFactory
import com.example.breeze.ui.fragments.leaderboard.screen.AllLeaderBoardFragment
import com.example.breeze.ui.fragments.leaderboard.screen.WeekLeaderBoardFragment
import com.example.breeze.utils.Result
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
        viewModel.getUserLogin().observe(viewLifecycleOwner) {
            dataUser = it
        }
        viewModel.getProfile(dataUser.token).observe(this) {
            when (it) {
                is Result.Loading -> return@observe
                is Result.Success -> {
                    binding.tvName.text = it.data.dataUser?.fullName ?: ""
                    binding.tvPoints.text = "${it.data.dataUser?.experiences ?: 0} Exp"
                    Glide.with(this)
                        .load(it.data.dataUser?.avatarUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .fallback(R.drawable.ic_launcher_background)
                        .into(binding.ivPicture)
                }

                is Result.Error -> {
                    val message = it.error
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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