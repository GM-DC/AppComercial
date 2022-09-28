package com.unosoft.ecomercialapp.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LogoApi {

    @GET("/api/Company/LogoEmpresa")
    suspend fun getImagen() : Response<ResponseBody>
}