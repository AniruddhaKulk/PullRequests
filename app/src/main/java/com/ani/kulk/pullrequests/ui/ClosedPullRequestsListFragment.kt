package com.ani.kulk.pullrequests.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.kulk.pullrequests.databinding.FragmentClosedPullRequestsListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClosedPullRequestsListFragment : Fragment() {

    private var _binding: FragmentClosedPullRequestsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClosedPullRequestsListViewModel by viewModel()
    private lateinit var adapter: ClosedPullRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClosedPullRequestsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.fragmentClosedPullRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ClosedPullRequestAdapter(mutableListOf())
        binding.fragmentClosedPullRequestRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.closedPullRequestsViewState.collect {
                when (it) {
                    is ClosedPullRequestsViewState.Loading -> {
                        Log.d("TAG", "Show loader")
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.VISIBLE
                    }
                    is ClosedPullRequestsViewState.Success -> {
                        Log.d("TAG", "SUCCESS ${it.list}")
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.GONE
                        if (it.list.isNullOrEmpty()) {
                            binding.fragmentClosedPullRequestRecyclerView.visibility = View.GONE
                        } else {
                            binding.fragmentClosedPullRequestRecyclerView.visibility = View.VISIBLE
                            adapter.setItems(it.list)
                        }
                    }
                    is ClosedPullRequestsViewState.Error -> {
                        Log.d("TAG", "ERROR ${it.message}")
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}