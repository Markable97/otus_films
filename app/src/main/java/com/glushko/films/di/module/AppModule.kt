package com.glushko.films.di.module

import android.app.Application
import android.content.Context
import com.glushko.films.data_layer.repository.FilmsDao
import com.glushko.films.data_layer.repository.MainDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(val application: Application) {

    @Singleton
    @Provides
    fun getDao(database: MainDatabase): FilmsDao{
        return database.filmsDao()
    }

    @Singleton
    @Provides
    fun getRoomDbInstance(): MainDatabase{
        return MainDatabase.getDatabase(provideAppContext())
    }
    @Singleton
    @Provides
    fun provideAppContext(): Context{
        return application.applicationContext
    }
}