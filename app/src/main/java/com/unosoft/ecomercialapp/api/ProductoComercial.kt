package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Pedidos.pedidocabDTO
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductoComercial{

    @GET("/api/Producto/Comercial/{cdg_lista}/{CDG_MONEDA}/{TIPO_CAMBIO}")

    suspend fun getProductoComercial(@Path("cdg_lista") cdg_lista:String,
                                     @Path("CDG_MONEDA") CDG_MONEDA:String,
                                     @Path("TIPO_CAMBIO") TIPO_CAMBIO:String): Response<List<productocomercial>>
}