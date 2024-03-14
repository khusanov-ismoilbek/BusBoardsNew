package com.archay.busboards.util.extensions

import android.content.res.Resources
import android.os.Environment
import android.os.StatFs
import androidx.fragment.app.Fragment

fun Int.toDp(): Int {
    return (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
}


