package com.glushko.films

import android.app.Application
import com.glushko.films.data_layer.repository.MainDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    lateinit var db: MainDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        db = MainDatabase.getDatabase(applicationContext)
    }
}