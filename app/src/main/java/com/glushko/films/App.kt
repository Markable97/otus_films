package com.glushko.films

import android.app.Application
import com.glushko.films.data_layer.repository.MainDatabase
import com.glushko.films.di.AppComponent
import com.glushko.films.di.DaggerAppComponent
import com.glushko.films.di.module.AppModule
import javax.inject.Inject

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
        lateinit var appComponent: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}