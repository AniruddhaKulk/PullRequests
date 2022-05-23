package com.ani.kulk.pullrequests.utils

import com.ani.kulk.pullrequests.model.PullRequest
import retrofit2.http.GET

interface NetworkService {

    @GET("/repos/AniruddhaKulk/NewsApp/pulls?state=closed")
    suspend fun getClosedPullRequests(): List<PullRequest>

}