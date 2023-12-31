package com.example.breeze.ui.fragments.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.databinding.FragmentEventBinding
import com.example.breeze.ui.adapter.frag.EventPagerAdapter
import com.example.breeze.ui.fragments.event.screen.ActiveEventFragment
import com.example.breeze.ui.fragments.event.screen.ExploreEventFragment
import com.example.breeze.ui.fragments.event.screen.FinishedEventFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerAndTabLayout()
    }

    private fun setupViewPagerAndTabLayout() {
        val viewPager: ViewPager2 = binding?.viewPager ?: return
        val adapter = EventPagerAdapter(this)
        adapter.addFragment(ExploreEventFragment())
        adapter.addFragment(ActiveEventFragment())
        adapter.addFragment(FinishedEventFragment())
        viewPager.adapter = adapter

        val tabLayout: TabLayout = binding?.tabs ?: return
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Explore"
                1 -> "Active"
                2 -> "Finished"
                else -> ""
            }
        }.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}