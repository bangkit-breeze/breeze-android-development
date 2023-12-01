package com.example.breeze.ui.fragments.home

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.breeze.R
import com.example.breeze.databinding.BottomDialogInfoCarbonBinding
import com.example.breeze.databinding.BottomDialogInfoEventBinding
import com.example.breeze.databinding.BottomDialogInfoFoodBinding
import com.example.breeze.databinding.BottomDialogInfoVechileBinding
import com.example.breeze.databinding.FragmentHomeBinding
import com.example.breeze.ui.activities.details.carbon.DetailCarbonActivity
import com.example.breeze.ui.adapter.OnBoardingAdapter
import com.example.breeze.ui.adapter.QuestionAdapter
import com.example.breeze.ui.fragments.onboarding.screen.FirstScreen
import com.example.breeze.ui.fragments.onboarding.screen.SecondScreen
import com.example.breeze.ui.fragments.onboarding.screen.ThirdScreen
import com.example.breeze.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var alertDialog: AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireContext())
        setupInfoListeners()

        return binding.root
    }
    private fun setupInfoListeners() {
        binding?.apply {
            btnInfoFood.setOnClickListener {
                animateButtonClick(btnInfoFood)
                showFoodBottomSheet()
            }
            btnInfoVechile.setOnClickListener {
                animateButtonClick(btnInfoVechile)
                showVehicleBottomSheet()
            }

            btnInfoEvent.setOnClickListener {
                animateButtonClick( btnInfoEvent)
                showEventBottomSheet()
            }

            btnInfoCarbon.setOnClickListener {
                animateButtonClick( btnInfoCarbon)
                showCarbonBottomSheet()
            }
            progressBarCircular.setOnClickListener {
                showProgressBarDialog()
            }
            progressBarCircular.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX", Constants.INITIAL_SCALE_FACTOR),
                            PropertyValuesHolder.ofFloat("scaleY", Constants.INITIAL_SCALE_FACTOR)
                        )
                        scaleDown.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleDown.start()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX", Constants.FINAL_SCALE_FACTOR),
                            PropertyValuesHolder.ofFloat("scaleY", Constants.FINAL_SCALE_FACTOR)
                        )
                        scaleUp.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleUp.start()
                    }
                }
                false
            }
            tvDetails.setOnClickListener {
                val intent = Intent(activity, DetailCarbonActivity::class.java)
                startActivity(intent)
            }

            tvDetails.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX",0.9f),
                            PropertyValuesHolder.ofFloat("scaleY", 0.9f)
                        )
                        scaleDown.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleDown.start()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX", Constants.FINAL_SCALE_FACTOR),
                            PropertyValuesHolder.ofFloat("scaleY", Constants.FINAL_SCALE_FACTOR)
                        )
                        scaleUp.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleUp.start()
                    }
                }
                false
            }

            imgMark.setOnClickListener {
                showQuestionDialog()
            }

            imgMark.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX", Constants.INITIAL_SCALE_FACTOR),
                            PropertyValuesHolder.ofFloat("scaleY", Constants.INITIAL_SCALE_FACTOR)
                        )
                        scaleDown.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleDown.start()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
                            view,
                            PropertyValuesHolder.ofFloat("scaleX", Constants.FINAL_SCALE_FACTOR),
                            PropertyValuesHolder.ofFloat("scaleY", Constants.FINAL_SCALE_FACTOR)
                        )
                        scaleUp.duration = Constants.DURATION_ANIMATION_CLICKED_DELAY
                        scaleUp.start()
                    }
                }
                false
            }

        }
    }


    private fun showQuestionDialog() {
        val customDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_question, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(customDialogView)
            .create()
        val viewPager = customDialogView.findViewById<ViewPager2>(R.id.view_pager_question)
        val dotsIndicator = customDialogView.findViewById<DotsIndicator>(R.id.dots_indicator_question)
        val fragmentList = arrayListOf<Fragment>(
            QuestionScreen(),
            QuestionSecondScreen(),
            QuestionThirdScreen()
        )
        viewPager.adapter = QuestionAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        dotsIndicator.attachTo(viewPager)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun showProgressBarDialog() {
        val customDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_carbon, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(customDialogView)
            .create()

        val ivClose = customDialogView.findViewById<ImageView>(R.id.iv_close)
        val btnOkay = customDialogView.findViewById<MaterialButton>(R.id.btn_okay)

        ivClose.setOnClickListener {
            alertDialog.dismiss()
        }
        btnOkay.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }


    private fun animateButtonClick(view: View) {
        view.animate()
            .scaleX(Constants.INITIAL_SCALE_FACTOR)
            .scaleY(Constants.INITIAL_SCALE_FACTOR)
            .setDuration(Constants.DURATION_ANIMATION_CLICKED_DELAY)
            .withEndAction {
                view.animate()
                    .scaleX(Constants.FINAL_SCALE_FACTOR)
                    .scaleY(Constants.FINAL_SCALE_FACTOR)
                    .setDuration(Constants.DURATION_ANIMATION_CLICKED_DELAY)
                    .start()
            }
            .start()
    }

    private fun showFoodBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoFoodBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)

        sheetBinding.btnAddTrackFood.setOnClickListener {
            displayToast(R.string.feature_not_yet)
        }

        sheetDialog.show()
    }

    private fun showVehicleBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoVechileBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)

        sheetBinding.btnAddTrackVechile.setOnClickListener {
            displayToast(R.string.feature_not_yet)
        }

        sheetDialog.show()
    }

    private fun showEventBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoEventBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)

        sheetBinding.btnAddEvent.setOnClickListener {
            displayToast(R.string.feature_not_yet)
        }

        sheetDialog.show()
    }

    private fun showCarbonBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoCarbonBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)

        sheetBinding.btnAddRemoveCarbon.setOnClickListener {
            displayToast(R.string.feature_not_yet)
        }

        sheetDialog.show()
    }

    private fun displayToast(message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}