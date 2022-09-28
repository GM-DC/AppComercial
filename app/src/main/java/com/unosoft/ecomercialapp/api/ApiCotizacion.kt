package com.unosoft.ecomercialapp.api
import com.unosoft.ecomercialapp.entity.Cotizacion.CotizacionCabDTO
import com.unosoft.ecomercialapp.entity.Cotizacion.EnviarCotizacion
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiCotizacion {

    @GET("/api/Cotizacion/{cdg_ven}")
    suspend fun fetchAllCotizaciones(@Path("cdg_ven") cdg_ven:String) : Response<List<cotizacionesDto>>


    @GET("/api/Cotizacion/MS/{ID_COTIZACION}")
    suspend fun findbyidCotizacion(@Path("ID_COTIZACION") ID_COTIZACION:String) : Response<List<CotizacionCabDTO>>


    @POST("/api/Cotizacion/CreateCotizacion")
    suspend fun postCreateCotizacion(@Body enviarCotizacion: EnviarCotizacion) : Response<EnviarCotizacion>


}