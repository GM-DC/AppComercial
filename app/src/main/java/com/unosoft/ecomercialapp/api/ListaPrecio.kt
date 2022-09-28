package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.ListaPrecio.ListaPrecioRespuesta
import retrofit2.Response
import retrofit2.http.GET

interface ListaPrecio {
    @GET("api/TablasBasicas/Listasdeprecio")
    suspend fun getListaPrecio(): Response<List<ListaPrecioRespuesta>>
}