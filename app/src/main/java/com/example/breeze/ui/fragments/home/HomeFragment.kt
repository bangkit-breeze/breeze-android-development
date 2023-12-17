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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.breeze.R
import com.example.breeze.data.model.response.article.ArticleResponse
import com.example.breeze.data.model.response.user.UserProfileResponse
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.event.EventResponse
import com.example.breeze.data.model.response.user.DataUser
import com.example.breeze.databinding.BottomDialogInfoCarbonBinding
import com.example.breeze.databinding.BottomDialogInfoEventBinding
import com.example.breeze.databinding.BottomDialogInfoFoodBinding
import com.example.breeze.databinding.BottomDialogInfoVechileBinding
import com.example.breeze.databinding.FragmentHomeBinding
import com.example.breeze.ui.activities.camera.CameraFoodCarbonActivity
import com.example.breeze.ui.activities.carbon.DetailCarbonActivity
import com.example.breeze.ui.activities.main.MainActivity
import com.example.breeze.ui.activities.project.ProjectActivity
import com.example.breeze.ui.activities.vehicle.AddVehicleCarbonActivity
import com.example.breeze.ui.adapter.rv.ArticlesAdapter
import com.example.breeze.ui.adapter.frag.QuestionAdapter
import com.example.breeze.ui.adapter.rv.EventPopularAdapter
import com.example.breeze.ui.factory.HomeViewModelFactory
import com.example.breeze.ui.fragments.event.EventFragment
import com.example.breeze.ui.fragments.home.screen.QuestionScreen
import com.example.breeze.ui.fragments.home.screen.QuestionSecondScreen
import com.example.breeze.ui.fragments.home.screen.QuestionThirdScreen
import com.example.breeze.ui.viewmodel.HomeViewModel
import com.example.breeze.utils.animation.AnimationUtils
import com.example.breeze.utils.number.NumberUtils
import com.example.breeze.utils.constans.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.dialog.ProgressDialogUtils
import com.example.breeze.utils.showToast
import com.example.breeze.utils.showToastString

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var alertDialog: AlertDialog.Builder
    private val  viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var tokenUser: LoginResult
    private val adapterArticle = ArticlesAdapter()
    private val adapterEvent = EventPopularAdapter()
    private var valueProgress: Int = 0
    private var totalRemoved: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireContext())
        setupInfoListeners()
        setupViews()
        setupRecyclerViewArticle(adapterArticle)
        setupRecyclerViewEvent(adapterEvent)

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        effectShimmer()
        refreshData()
    }

    private fun effectShimmer(){
        binding.dataMainLayout.visibility = View.GONE
        binding.shimmerView.visibility = View.VISIBLE
        binding.shimmerView.startShimmerAnimation()

    }
    private fun setupViews() {
        viewModel.getToken().observe(viewLifecycleOwner) {
            tokenUser = it
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = true

        val token = tokenUser.token
        viewModel.getArticles(token).observe(viewLifecycleOwner) {
            handleArticleResult(it, adapterArticle)
            stopRefreshing()
        }

        viewModel.getEventsPopular(token).observe(viewLifecycleOwner) {
            handleEventResult(it, adapterEvent)
            stopRefreshing()
        }

        viewModel.getProfile(token).observe(viewLifecycleOwner) {
            handleProfile(it)
            stopRefreshing()
        }
    }

    private fun stopRefreshing() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun handleProfile(result: Result<UserProfileResponse>){
        when(result){
            is Result.Loading -> {
                binding.shimmerView.startShimmerAnimation()
            }
            is Result.Success -> {
                binding.dataMainLayout.visibility = View.VISIBLE
                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE
                loadProfileSuccess(result.data.dataUser)
            }
            is Result.Error -> {
                binding.dataMainLayout.visibility = View.VISIBLE
                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE
                showToastString(requireContext(),result.error)
            }
        }
    }
    private fun loadProfileSuccess(userProfile: DataUser?) {
        userProfile?.let {
            loadAvatar(it.avatarUrl)
            updateUI(it)
            var exp = userProfile.experiences
            exp?.let {
                val level = NumberUtils.calculateLevel(it.toFloat())
                val (expNow, expMaxNow) = if (it < 100) {
                    it to 100
                } else {
                    NumberUtils.calculateExpNow(
                        it.toFloat(),
                        level
                    ) to NumberUtils.calculateExpMaxNow(level)
                }
                updateProfileExpAndProgress(
                    level + 1,
                    it.toString(),
                    expMaxNow.toString(),
                    expNow.toInt()
                )
            }
        }
    }
    private fun updateUI(user: DataUser) {
        with(binding) {
            tvPoint.text = user.points.toString()
            tvName.text = "Hi, ${user.fullName}"
            tvTotalFood.text = user.foodEmissionCount.toString()
            tvTotalVehicle.text = user.vehicleEmissionCount.toString()
            tvTotalCarbon.text = NumberUtils.formatTotalCarbon(user.totalCo2Removed?.toFloat())
            tvTotalEvent.text = user.totalEvent.toString()
            valueProgress = NumberUtils.calculateProgress(user.totalCo2Removed?.toFloat())
            tvProgress.text = "$valueProgress%"
            totalRemoved =  user.totalCo2Removed!!
            progressBarCircular.progress = totalRemoved
            tvCo2Removed.text = "0 kg"
        }
    }

    private fun loadAvatar(avatarUrl: String?) {
        avatarUrl?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fallback(R.drawable.ic_launcher_background)
                .into(binding.ivPictureProfile)
        }
    }
    private fun updateProfileExpAndProgress(level: Int, exp: String, expMax: String, progress: Int) {
        with(binding) {
            tvLevel.text = "Level $level"
            tvExp.text = exp
            tvExpMax.text = expMax
            progressBarHorizontal.max = 100
            progressBarHorizontal.progress = progress
        }
    }
    private fun handleArticleResult(result: Result<ArticleResponse>, adapter: ArticlesAdapter) {
        when (result) {
            is Result.Loading ->  {
                showLoadingArticle(true)
            }
            is Result.Success -> {
                showLoadingArticle(false)
                val articles = result.data.data
                adapter.submitList(articles)
            }
            is Result.Error -> {
                showLoadingArticle(false)
                val message = result.error
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleEventResult(result: Result<EventResponse>, adapter: EventPopularAdapter) {
        when (result) {
            is Result.Loading ->  showLoadingArticle(true)
            is Result.Success -> {
                showLoadingArticle(false)
                val events = result.data.dataEvent
                adapter.submitList(events)
            }
            is Result.Error -> {
                showLoadingArticle(false)
                val message = result.error
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViewArticle(adapter: ArticlesAdapter) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = adapter
    }
    private fun showLoadingArticle(isLoading: Boolean) {
        binding.progressBarArticle.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setupRecyclerViewEvent(adapter: EventPopularAdapter) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEvent.layoutManager = layoutManager
        binding.rvEvent.adapter = adapter
    }


    private fun setupInfoListeners() {
        binding?.apply {
            tvSeeEventPopuler.setOnClickListener {
                val eventFragment = EventFragment()
                (activity as MainActivity).replaceFragment(eventFragment)
            }
            btnInfoFood.setOnClickListener {
                animateButtonClick(btnInfoFood)
                showFoodBottomSheet()
            }
            btnInfoVechile.setOnClickListener {
                animateButtonClick(btnInfoVechile)
                showVehicleBottomSheet()
            }

            btnInfoEvent.setOnClickListener {
                animateButtonClick(btnInfoEvent)
                showEventBottomSheet()
            }

            btnInfoCarbon.setOnClickListener {
                animateButtonClick(btnInfoCarbon)
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
            btnExplore.setOnClickListener {
                val intent = Intent(activity, ProjectActivity::class.java)
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
        binding.konfettiView.start(AnimationUtils.party)
    }

    private fun showProgressBarDialog() {
        ProgressDialogUtils.showProgressBarDialog(
            requireContext(),
            totalRemoved = totalRemoved,
            valueProgress = valueProgress
        )
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
            sheetDialog.dismiss()
            val intent = Intent(activity, CameraFoodCarbonActivity::class.java)
            startActivity(intent)
        }
        sheetDialog.show()
    }

    private fun showVehicleBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoVechileBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)
        sheetBinding.btnAddTrackVechile.setOnClickListener {
            sheetDialog.dismiss()
            val intent = Intent(activity, AddVehicleCarbonActivity::class.java)
            startActivity(intent)
        }
        sheetDialog.show()
    }

    private fun showEventBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoEventBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)
        sheetBinding.btnAddEvent.setOnClickListener {
            sheetDialog.dismiss()
            val eventFragment = EventFragment()
            (activity as MainActivity).replaceFragment(eventFragment)
        }
        sheetDialog.show()
    }

    private fun showCarbonBottomSheet() {
        val sheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomDialogInfoCarbonBinding.inflate(layoutInflater)
        sheetDialog.setContentView(sheetBinding.root)
        sheetBinding.btnAddRemoveCarbon.setOnClickListener {
            showToast(requireContext(), R.string.feature_not_yet)
        }
        sheetDialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}