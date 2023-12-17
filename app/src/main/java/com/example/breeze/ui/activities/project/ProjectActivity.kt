package com.example.breeze.ui.activities.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breeze.R
import com.example.breeze.data.model.response.auth.LoginResult
import com.example.breeze.data.model.response.event.EventResponse
import com.example.breeze.data.model.response.project.ProjectResponse
import com.example.breeze.databinding.ActivityDetailArticleBinding
import com.example.breeze.databinding.ActivityProjectBinding
import com.example.breeze.ui.adapter.rv.EventAdapter
import com.example.breeze.ui.adapter.rv.ProjectAdapter
import com.example.breeze.ui.factory.EventViewModelFactory
import com.example.breeze.ui.factory.ProjectViewModelFactory
import com.example.breeze.ui.viewmodel.EventViewModel
import com.example.breeze.ui.viewmodel.ProjectViewModel
import com.example.breeze.utils.constans.Result
import com.example.breeze.utils.showToastString

class ProjectActivity : AppCompatActivity() {
    private val  viewModel: ProjectViewModel by viewModels {
        ProjectViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private val adapter = ProjectAdapter()
    private lateinit var binding: ActivityProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        setupRecyclerView(adapter)
    }
    override fun onResume() {
        super.onResume()
        setupViews()
        viewModel.getProject(dataUser.token).observe(this) { result ->
            handleEventResult(result, adapter)
        }
    }
    private fun setupViews() {
        viewModel.getSession().observe(this) {
            dataUser = it
        }
    }
    private fun setupRecyclerView(adapter: ProjectAdapter) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvArticle.layoutManager = layoutManager
        binding.rvArticle.adapter = adapter
    }
    private fun handleEventResult(result: Result<ProjectResponse>, adapter: ProjectAdapter) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                with(binding) {
                    dataEmpty.visibility = if (result.data.dataProject.isNullOrEmpty()) View.VISIBLE else View.GONE
                    if (!result.data.dataProject.isNullOrEmpty()) {
                        adapter.submitList(result.data.dataProject)
                    }
                }
            }
            is Result.Error -> {
                showLoading(false)
                showToastString(this, result.error)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.let { com.example.breeze.utils.showLoading(it, isLoading) }
    }
}