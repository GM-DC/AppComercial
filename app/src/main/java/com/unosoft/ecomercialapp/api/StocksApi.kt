package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StocksApi{
    @GET("api/Stocks/GetAllWarehouse")
    suspend fun getStockForName(@Query("Nombre") Nombre:String): Response<List<ConsultaStocksResponseItem>>

    @GET("api/Stocks/GetAllWarehouse")
    suspend fun getStockForCdgRef(@Query("CdgRef") CdgRef:String): Response<List<ConsultaStocksResponseItem>>

    @GET("api/Stocks/GetAllWarehouse")
    suspend fun getStockForCdgBarra(@Query("CodigoBarra") CodigoBarra:String): Response<List<ConsultaStocksResponseItem>>

}

