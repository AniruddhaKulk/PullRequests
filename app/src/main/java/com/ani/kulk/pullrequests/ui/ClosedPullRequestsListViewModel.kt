package com.ani.kulk.pullrequests.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ani.kulk.pullrequests.model.PullRequest
import com.ani.kulk.pullrequests.usecase.ClosedPullRequestUseCase
import com.ani.kulk.pullrequests.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClosedPullRequestsListViewModel(
    private val closedPullRequestUseCase: ClosedPullRequestUseCase
) : ViewModel() {
    private val _closedPullRequestsViewState =
        MutableStateFlow<ClosedPullRequestsViewState>(ClosedPullRequestsViewState.Loading)
    val closedPullRequestsViewState = _closedPullRequestsViewState.asStateFlow()

    init {
        getClosedPullRequests()

        viewModelScope.launch {
            closedPullRequestUseCase.closedPullRequests.collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        _closedPullRequestsViewState.value = ClosedPullRequestsViewState.Loading
                    }
                    is NetworkResult.Success -> {
                        _closedPullRequestsViewState.value = ClosedPullRequestsViewState.Success(it.data)
                    }
                    is NetworkResult.Error -> {
                        _closedPullRequestsViewState.value = ClosedPullRequestsViewState.Error(it.message)
                    }
                }
            }
        }
    }

    fun getClosedPullRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            closedPullRequestUseCase.getClosedPullRequests()
        }
    }
}

sealed interface ClosedPullRequestsViewState {
    object Loading : ClosedPullRequestsViewState
    class Success(val list: List<PullRequest>?) : ClosedPullRequestsViewState
    class Error(val message: String?) : ClosedPullRequestsViewState
}

