package com.example.busboardsnew.util.extensions

import android.content.res.Resources

fun Int.toDp(): Int {
    return (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
}


