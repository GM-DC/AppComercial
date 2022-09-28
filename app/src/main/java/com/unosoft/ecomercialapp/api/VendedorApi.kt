package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.Vendedor.VendedorResponse
import retrofit2.Response
import retrofit2.http.GET


interface VendedorApi {
    @GET("api/TablasBasicas/Detail?filter=codigo eq 'VENDEDOR'&select=codigo,nombre,numero")
    suspend fun getListaVendedor(): Response<ArrayList<VendedorResponse>>
}