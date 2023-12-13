package com.example.breeze.ui.fragments.event.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breeze.utils.showLoading
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.event.EventResponse
import com.example.breeze.databinding.FragmentExploreEventBinding
import com.example.breeze.ui.adapter.rv.EventAdapter
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.ui.viewmodel.EventViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.showToastString

class ExploreEventFragment : Fragment() {
    private var _binding: FragmentExploreEventBinding? = null
    private val binding get() = _binding!!
    private val  viewModel: EventViewModel by viewModels {
        EventViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var dataUser: LoginResult
    private val adapter = EventAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =   FragmentExploreEventBinding.inflate(inflater, container, false)
        setupRecyclerView(adapter)
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        setupViews()
        viewModel.getEventExplore(dataUser.token).observe(this) { result ->
            handleEventResult(result, adapter)
        }
    }
    private fun setupViews() {
        viewModel.getSession().observe(viewLifecycleOwner) {
            dataUser = it
        }
    }
    private fun setupRecyclerView(adapter: EventAdapter) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.let { showLoading(it, isLoading) }
    }
    private fun handleEventResult(result: Result<EventResponse>, adapter: EventAdapter) {
        when (result) {
            is Result.Loading ->  showLoading(true)
            is Result.Success -> {
                showLoading(false)
                with(binding) {
                    dataEmpty.visibility = if (result.data.dataEvent.isNullOrEmpty()) View.VISIBLE else View.GONE
                    if (!result.data.dataEvent.isNullOrEmpty()) {
                        adapter.submitList(result.data.dataEvent)
                    }
                }
            }
            is Result.Error -> {
                showLoading(false)
                showToastString(requireContext(), result.error)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}