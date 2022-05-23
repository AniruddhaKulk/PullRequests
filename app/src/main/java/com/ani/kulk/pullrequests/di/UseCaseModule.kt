package com.ani.kulk.pullrequests.di

import com.ani.kulk.pullrequests.usecase.ClosedPullRequestUseCase
import org.koin.dsl.module

val useCaseModules = module {
    single { ClosedPullRequestUseCase(networkService = get()) }
}