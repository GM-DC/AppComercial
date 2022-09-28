package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Login.LoginComercialResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers

interface LoginApi {
    @POST("api/Users/LoginComercial")
    fun login(@Body loginMozo: DCLoginUser): Call<LoginComercialResponse>

    @POST("api/Users/LoginComercial")
    suspend fun checkLoginComanda(@Body loginMozo: DCLoginUser) : Response<LoginComercialResponse>
}