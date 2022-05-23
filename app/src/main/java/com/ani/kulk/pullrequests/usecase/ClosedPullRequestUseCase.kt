package com.ani.kulk.pullrequests.usecase

import com.ani.kulk.pullrequests.model.PullRequest
import com.ani.kulk.pullrequests.utils.NetworkResult
import com.ani.kulk.pullrequests.utils.NetworkService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClosedPullRequestUseCase(private val networkService: NetworkService) {
    private val _closedPullRequests = MutableStateFlow<NetworkResult<List<PullRequest>>>(NetworkResult.Loading())
    val closedPullRequests = _closedPullRequests.asStateFlow()

    suspend fun getClosedPullRequests() {
        _closedPullRequests.value = NetworkResult.Loading()
        try {
            val result = networkService.getClosedPullRequests()
            _closedPullRequests.value = NetworkResult.Success(result)
        } catch (e: Exception) {
            _closedPullRequests.value = NetworkResult.Error(message = e.message)
        }

    }
}