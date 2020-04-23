package com.pallavsaikia.task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pallavsaikia.task.network.ApiInterface
import com.pallavsaikia.task.network.ApiResponse
import com.pallavsaikia.task.pojo.ApiPojo
import com.pallavsaikia.task.pojo.Data
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ApiViewModel(private val apiService: ApiInterface) : ViewModel() {

    val mutableList = mutableListOf<Data>()
    val disposible = CompositeDisposable()

    fun apiCall(isScrolling: Boolean):LiveData<ApiResponse> {
        val mld=MutableLiveData<ApiResponse>()
        if (mutableList.isEmpty() || isScrolling) {
            disposible.add(
                apiService.getApiCall()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ApiPojo>() {

                        override fun onSuccess(t: ApiPojo) {
                            mutableList.addAll(t.data)
                            mld.postValue(ApiResponse(t))
                        }

                        override fun onError(e: Throwable) {
                            mld.postValue(ApiResponse(e))
                        }

                    })
            )
        }
        return mld
    }

    fun getList():List<Data>{
        return mutableList
    }
}