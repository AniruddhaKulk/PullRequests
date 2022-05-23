package com.ani.kulk.pullrequests.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ani.kulk.pullrequests.databinding.FragmentClosedPullRequestsListBinding
import com.ani.kulk.pullrequests.utils.NetworkListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClosedPullRequestsListFragment : Fragment() {

    private var _binding: FragmentClosedPullRequestsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClosedPullRequestsListViewModel by viewModel()
    private lateinit var adapter: ClosedPullRequestAdapter
    private lateinit var networkListener: NetworkListener

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
        binding.fragmentClosedPullRequestSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getClosedPullRequests()
            binding.fragmentClosedPullRequestSwipeRefreshLayout.isRefreshing = false
        }
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
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.VISIBLE
                    }
                    is ClosedPullRequestsViewState.Success -> {
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.GONE
                        if (it.list.isNullOrEmpty()) {
                            handleErrorAndListViewVisibility(isError = true)
                        } else {
                            handleErrorAndListViewVisibility(isError = false)
                            adapter.setItems(it.list)
                        }
                    }
                    is ClosedPullRequestsViewState.Error -> {
                        binding.fragmentClosedPullRequestProgressBar.visibility = View.GONE
                        handleErrorAndListViewVisibility(isError = true)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { isConnectionAvailable ->
                if (!isConnectionAvailable) {
                    binding.fragmentClosedPullRequestConnectionStatusTextView.visibility = View.VISIBLE
                } else if (isConnectionAvailable) {
                    binding.fragmentClosedPullRequestConnectionStatusTextView.visibility = View.GONE
                }
            }
        }
    }

    private fun handleErrorAndListViewVisibility(isError: Boolean) {
        binding.fragmentClosedPullRequestErrorTextView.visibility = if (isError) View.VISIBLE else View.GONE
        binding.fragmentClosedPullRequestRecyclerView.visibility = if (isError) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}