package com.ani.kulk.pullrequests.utils

import java.text.SimpleDateFormat
import java.util.*

object DateDisplayUtils {

    fun getDayMonthYearFormattedDate(date: String): String {
        val dateTime =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).parse(date)
        return if (dateTime != null) {
            return SimpleDateFormat("dd MMM yyyy", Locale.US).format(dateTime)
        } else {
            date
        }
    }
}