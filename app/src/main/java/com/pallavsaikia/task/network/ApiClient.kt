package com.pallavsaikia.task.network
import android.content.Context
import com.pallavsaikia.task.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val baseURL = "https://us-central1-webpush-151905.cloudfunctions.net/"
    private var retrofit: Retrofit? = null

    private var logging = HttpLoggingInterceptor()
    private fun getHttpLogClient(context: Context): OkHttpClient {
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY


        }
        val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)

        return clientBuilder.build()
    }

    fun getApi(context: Context): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpLogClient(context))
                    .build()
        }
        return retrofit
    }


}