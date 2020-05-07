package com.pallavsaikia.task.viewmodel

import android.view.View
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
import kotlinx.android.synthetic.main.unit_recycle_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class ApiViewModel(private val apiService: ApiInterface) : ViewModel() {

    val mutableList = mutableListOf<Data>()
    val disposible = CompositeDisposable()
    var isTrending=false
    var ascending=-1
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

    fun addDateTime(ascending:Boolean){
        for(i in mutableList){
            if (i.expiryDate != null) {
                val dateArray = i.expiryDate.split("T", ignoreCase = true)
                val dateFormat = SimpleDateFormat("dd-mm-yyyy HH:mm:ss")
                val date = dateFormat.parse(dateArray[0] + " " + dateArray[1])
                if (date.time > 0) {
                    i.time=date.time
                } else {
                    val dateFormat1 = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
                    val date1 = dateFormat1.parse(dateArray[0] + " " + dateArray[1])
                    i.time=date1.time

                }
            }else{
                if(ascending){
                    i.time=999999999999999999
                }else{
                    i.time=0
                }

            }
        }
    }


}