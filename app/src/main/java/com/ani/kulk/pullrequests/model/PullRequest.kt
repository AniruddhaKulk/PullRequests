package com.ani.kulk.pullrequests.model

data class PullRequest(
    val title:String,
    val closed_at: String,
    val number: Int,
    val user: User
)
