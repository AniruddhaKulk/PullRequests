package com.ani.kulk.pullrequests.model

import java.time.ZonedDateTime

data class PullRequest(
    val title:String,
    val created_at: String,
    val closed_at: String,
    val number: Int,
    val user: User
)
