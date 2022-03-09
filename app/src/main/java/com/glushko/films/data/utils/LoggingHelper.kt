package com.glushko.films.data.utils

import android.util.Log
import com.glushko.films.BuildConfig

object LoggingHelper {
    private const val TAG = "APP"
    fun log(lvl: Int, message: String?, tag:String = TAG){
        if(BuildConfig.LOGGING){
            when(lvl) {
                Log.VERBOSE -> Log.v(tag, "$message")
                Log.DEBUG -> Log.d(tag, "$message")
                Log.INFO -> Log.i(tag, "$message")
                Log.ERROR -> Log.e(tag, "$message")
                Log.WARN -> Log.w(tag, "$message")
            }
        }
    }
}