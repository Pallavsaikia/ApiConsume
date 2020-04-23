package com.pallavsaikia.task

import android.app.Application
import com.pallavsaikia.task.di.viewModelApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    viewModelApp
                )
            )
        }
    }
}