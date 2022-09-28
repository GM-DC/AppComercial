package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.Pedidos.pedidocabDTO
import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PedidoMaster {

    @GET("/api/PedidoComercial/MS/{Id_pedido}")
    suspend fun getbyIdPedidoCab(@Path("Id_pedido") Id_pedido:String): Response<pedidocabDTO>
    @GET("/api/PedidoComercial/DT/{Id_pedido}")
    suspend fun getbyIdPedidoDetail(@Path("Id_pedido") Id_pedido:String): Response<List<EntityEditPedidoDetail>>
}

