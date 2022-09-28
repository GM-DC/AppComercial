package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.db.cotizacion.EntityEditQuotationDetail
import com.unosoft.ecomercialapp.db.cotizacion.EntityQuotationMaster
import com.unosoft.ecomercialapp.entity.Cotizacion.CotizacionCabDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CotizacionMaster {
    @GET("/api/Cotizacion/MS/{ID_COTIZACION}")
    suspend fun getbyIdQuotationCab(@Path("ID_COTIZACION") ID_COTIZACION:String): Response<List<CotizacionCabDTO>>

    @GET("/api/Cotizacion/DT/{ID_COTIZACION}")
    suspend fun getbyIdQuotationDetail(@Path("ID_COTIZACION") ID_COTIZACION:String): Response<List<EntityEditQuotationDetail>>
}