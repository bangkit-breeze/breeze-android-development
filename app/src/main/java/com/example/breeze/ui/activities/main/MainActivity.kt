package com.example.breeze.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.breeze.R
import com.example.breeze.databinding.ActivityMainBinding
import com.example.breeze.ui.fragments.event.EventFragment
import com.example.breeze.ui.fragments.home.HomeFragment
import com.example.breeze.ui.fragments.leaderboard.LeaderBoardFragment
import com.example.breeze.ui.fragments.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_event -> {
                    replaceFragment(EventFragment())
                    true
                }
                R.id.bottom_add ->{
                    Toast.makeText(this, "feature not yet", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.bottom_leaderboard -> {
                    replaceFragment(LeaderBoardFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.navFragment, fragment).commit()
    }

}