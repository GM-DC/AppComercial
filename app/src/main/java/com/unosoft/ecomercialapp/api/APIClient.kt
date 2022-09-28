package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {
    private var retrofit: Retrofit? = null
    @JvmStatic
    val client: Retrofit?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.MINUTES) // AGREGRE
                .writeTimeout(5, TimeUnit.MINUTES) // AGREGE
                .readTimeout(5, TimeUnit.MINUTES).build()
            retrofit = Retrofit.Builder()
                .baseUrl("${prefs.getURLBase()}")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}