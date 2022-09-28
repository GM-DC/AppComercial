package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Cotizacion.CotizacionCabDTO
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PDFApi {

    @GET("/api/Cotizacion/pdf/{id_Cotizacion}")
    suspend fun getpdfCotizacion(@Path("id_Cotizacion") id_Cotizacion:String): Response<ResponseBody>

    @GET
    suspend fun downloadPdfFile(@Url pdfUrl: String): Response<ResponseBody>
}