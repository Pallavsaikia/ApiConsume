package com.pallavsaikia.task.network

import com.pallavsaikia.task.pojo.ApiPojo
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface{
    @GET("getInterviewData")
    fun getApiCall():Single<ApiPojo>
}