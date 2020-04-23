package com.pallavsaikia.task.di

import com.pallavsaikia.task.network.ApiClient
import com.pallavsaikia.task.network.ApiInterface
import com.pallavsaikia.task.viewmodel.ApiViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelApp = module {
    single {
        ApiClient().getApi(get())!!.create(ApiInterface::class.java)
    }
    viewModel {ApiViewModel(get())}
}