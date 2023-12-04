package com.example.breeze.ui.activities.main

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.breeze.R
import com.example.breeze.databinding.ActivityMainBinding
import com.example.breeze.databinding.BottomDialogTrackedBinding
import com.example.breeze.ui.activities.register.RegisterActivity
import com.example.breeze.ui.activities.vehicle.AddVehicleCarbonActivity
import com.example.breeze.ui.fragments.event.EventFragment
import com.example.breeze.ui.fragments.home.HomeFragment
import com.example.breeze.ui.fragments.leaderboard.LeaderBoardFragment
import com.example.breeze.ui.fragments.profile.ProfileFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.breeze.ui.activities.camera.CameraFoodCarbonActivity

class MainActivity : AppCompatActivity() {
    private var prevSelectedItemId: Int = R.id.bottom_home
    private  val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
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

        if (intent.getBooleanExtra("return_to_home", false)) {
            returnToHomeFragment()
        } else if (intent.getBooleanExtra("return_to_profile", false)) {
            returnToProfileFragment()
        } else {
            replaceFragment(HomeFragment())
        }

        replaceFragment(HomeFragment())
    }


    private fun returnToHomeFragment() {
        binding.bottomNavigation.selectedItemId = R.id.bottom_home
        replaceFragment(HomeFragment())
    }
    private fun returnToProfileFragment() {
        binding.bottomNavigation.selectedItemId = R.id.bottom_profile
        replaceFragment(ProfileFragment())
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
            startActivity(Intent(this@MainActivity, CameraFoodCarbonActivity::class.java))
        }
        sheetBinding.btnVechile.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddVehicleCarbonActivity::class.java))
        }
        sheetBinding.btnCancel.setOnClickListener{
            binding.bottomNavigation.selectedItemId = prevSelectedItemId
            sheetdialog.dismiss()
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }

}