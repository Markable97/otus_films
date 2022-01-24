package com.glushko.films

import android.app.Application
import com.glushko.films.data_layer.repository.MainDatabase
import com.glushko.films.di.AppComponent
import com.glushko.films.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var appComponent: AppComponent
            private set
    }

    lateinit var db: MainDatabase

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        db = MainDatabase.getDatabase(applicationContext)
    }
}