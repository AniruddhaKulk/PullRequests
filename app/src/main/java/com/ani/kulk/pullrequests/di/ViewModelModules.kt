package com.ani.kulk.pullrequests.di

import com.ani.kulk.pullrequests.ui.ClosedPullRequestsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel {
        ClosedPullRequestsListViewModel(closedPullRequestUseCase = get())
    }
}