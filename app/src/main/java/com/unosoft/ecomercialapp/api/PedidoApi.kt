package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Pedidos.EnviarPedido
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface PedidoApi{

    @GET("api/PedidoComercial/{cdg_ven}")
    suspend fun getPedido(@Path("cdg_ven") cdg_ven:String): Response<List<pedidosDto>>

    @POST("/api/PedidoComercial/CreateOrder")
    suspend fun postCreatePedido(@Body enviarPedido: EnviarPedido) : Response<EnviarPedido>

}
