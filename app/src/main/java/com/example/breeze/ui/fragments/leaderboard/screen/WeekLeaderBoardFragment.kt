package com.example.breeze.ui.fragments.leaderboard.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breeze.data.model.response.leaderboard.LeaderBoardResponse
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.databinding.FragmentWeekLeaderBoardBinding
import com.example.breeze.ui.adapter.rv.LeaderBoardAdapter
import com.example.breeze.ui.factory.LeaderBoardViewModelFactory
import com.example.breeze.ui.viewmodel.LeaderBoardViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.showLoading


class WeekLeaderBoardFragment : Fragment() {

    private var _binding: FragmentWeekLeaderBoardBinding? = null
    private val binding get() = _binding!!

    private val  viewModel: LeaderBoardViewModel by viewModels {
        LeaderBoardViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var dataUser: LoginResult
    private val adapter = LeaderBoardAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentWeekLeaderBoardBinding.inflate(inflater, container, false)
        setupRecyclerView(adapter)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupViews()
        viewModel.getLeaderBordWeekly(dataUser.token).observe(this) { result ->
            handleEventResult(result, adapter)
        }
    }
    private fun setupViews() {
        viewModel.getSession().observe(viewLifecycleOwner) {
            dataUser = it
        }
    }
    private fun setupRecyclerView(adapter: LeaderBoardAdapter) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderboard.layoutManager = layoutManager
        binding.rvLeaderboard.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.let { showLoading(it, isLoading) }
    }
    private fun handleEventResult(result: Result<LeaderBoardResponse>, adapter: LeaderBoardAdapter) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                val leaderboard = result.data.dataLeaderboard
                if (leaderboard?.leaderboards.isNullOrEmpty()) {
                    binding.tvEmptyData.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyData.visibility = View.GONE
                    adapter.submitList(leaderboard?.leaderboards)
                }
            }
            is Result.Error -> {
                showLoading(false)
                val message = result.error
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}