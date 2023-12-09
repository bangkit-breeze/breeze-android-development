package com.example.breeze.ui.fragments.event.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breeze.R
import com.example.breeze.data.model.auth.LoginResult
import com.example.breeze.data.model.event.EventResponse
import com.example.breeze.databinding.FragmentExploreEventBinding
import com.example.breeze.databinding.FragmentFinishedEventBinding
import com.example.breeze.ui.adapter.EventAdapter
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.ui.fragments.event.EventViewModel
import com.example.breeze.utils.Result

class FinishedEventFragment : Fragment() {
    private var _binding: FragmentFinishedEventBinding? = null
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
        _binding =    FragmentFinishedEventBinding.inflate(inflater, container, false)
        setupRecyclerView(adapter)
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        setupViews()
        viewModel.getEventFinished(dataUser.token).observe(this) { result ->
            handleEventResult(result, adapter)
        }
    }
    private fun setupViews() {
        viewModel.getUserLogin().observe(viewLifecycleOwner) {
            dataUser = it
        }
    }
    private fun setupRecyclerView(adapter: EventAdapter) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = adapter
    }
    private fun handleEventResult(result: Result<EventResponse>, adapter: EventAdapter) {
        when (result) {
            is Result.Loading ->  return
            is Result.Success -> {
                val events = result.data.dataEvent
                if (events.isNullOrEmpty()) {
                    binding.tvEmptyData.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyData.visibility = View.GONE
                    adapter.submitList(events)
                }
            }
            is Result.Error -> {
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