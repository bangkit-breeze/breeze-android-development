package com.example.breeze.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.breeze.R
import com.example.breeze.databinding.ActivityMainBinding
import com.example.breeze.databinding.BottomDialogTrackedBinding
import com.example.breeze.ui.fragments.event.EventFragment
import com.example.breeze.ui.fragments.home.HomeFragment
import com.example.breeze.ui.fragments.leaderboard.LeaderBoardFragment
import com.example.breeze.ui.fragments.profile.ProfileFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private var prevSelectedItemId: Int = R.id.bottom_home
    private  val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    prevSelectedItemId = R.id.bottom_home
                    true
                }
                R.id.bottom_event -> {
                    replaceFragment(EventFragment())
                    prevSelectedItemId = R.id.bottom_event
                    true
                }
                R.id.bottom_add ->{
                    showBottomSheet()
                    true
                }
                R.id.bottom_leaderboard -> {
                    replaceFragment(LeaderBoardFragment())
                    prevSelectedItemId = R.id.bottom_leaderboard
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    prevSelectedItemId = R.id.bottom_profile
                    true
                }
                else -> false
            }
        }

        replaceFragment(HomeFragment())
    }

    private fun showBottomSheet(){
        val sheetdialog = BottomSheetDialog(this)
        val sheetBinding = BottomDialogTrackedBinding.inflate(layoutInflater)
        sheetdialog.setContentView(sheetBinding.root)
        sheetdialog.apply {
            setContentView(sheetBinding.root)
            setOnDismissListener {
                binding.bottomNavigation.selectedItemId = prevSelectedItemId
            }
            show()
        }

        sheetBinding.btnFood.setOnClickListener {
            Toast.makeText(this, "Feature not yed", Toast.LENGTH_SHORT).show()
        }
        sheetBinding.btnVechile.setOnClickListener {
            Toast.makeText(this, "Feature not yed", Toast.LENGTH_SHORT).show()
        }
        sheetBinding.btnCancel.setOnClickListener{
            binding.bottomNavigation.selectedItemId = prevSelectedItemId
            sheetdialog.dismiss()
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.navFragment, fragment).commit()
    }

}