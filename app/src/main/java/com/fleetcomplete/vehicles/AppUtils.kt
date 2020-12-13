package com.fleetcomplete.vehicles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.util.*

val MINUTE_MILLIS = 60000
val HOUR_MILLIS = 3600000
val DAY_MILLIS = 86400000

fun inflate(context: Context, viewId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(viewId, parent, attachToRoot)
}

fun showToast(context: Context, strError: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, strError, length).show()
}

fun Date.getTimeAgo(): String? {
    var time = getTime()
    if (time < 1000000000000L) {
        // if timestamp given in seconds, convert to millis
        time *= 1000
    }
    val now: Long = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return "in the future"
    }
    val diff = now - time

    return when{
            diff < 60000 ->  (diff/1000).toString() + "s ago"
            diff < 2 * MINUTE_MILLIS -> "a minute ago"
            diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + "m ago"
            diff < 90 * MINUTE_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + "h ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            else ->  (diff / DAY_MILLIS).toString() + "d ago"
    }
}
