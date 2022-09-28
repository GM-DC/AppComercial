package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DepartamentoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DistritoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DocIdentidadResponse
import com.unosoft.ecomercialapp.entity.TableBasic.FrecuenciaDiasResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.ProvinciaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.UnidadMedidaResponse
import retrofit2.Response
import retrofit2.http.GET

interface TablaBasicaApi {
    @GET("api/TablasBasicas/Detail? filter=codigo eq 'COND_PAGO' and estado eq  '1'& select=codigo,nombre,numero, referencia1")
    suspend fun getCondicionPago(): Response<List<CondicionPagoResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'DEPARTAMENTO' and estado eq  '1'&select=codigo,nombre,numero&orderby=nombre asc")
    suspend fun getDepartamento(): Response<List<DepartamentoResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'UBIGEO' and estado eq  '1'&select=codigo,nombre,numero,referencia2,referencia3&orderby=nombre asc")
    suspend fun getDistrito(): Response<List<DistritoResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'TIPO_IDEN' and estado eq  '1'&select=codigo,nombre,numero, referencia1")
    suspend fun getDocIdentidad(): Response<List<DocIdentidadResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'FREC_DIAS' and estado eq  '1'&select=codigo,nombre,numero")
    suspend fun getFrecuenciaDias(): Response<List<FrecuenciaDiasResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'MONEDA'&select=nombre,numero,referencia1")
    suspend fun getMoneda(): Response<List<MonedaResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'PROVINCIA' and estado eq '1'&select=codigo,nombre,numero,referencia2&orderby=nombre asc")
    suspend fun getProvincia(): Response<List<ProvinciaResponse>>

    @GET("api/TablasBasicas/Detail?filter=codigo eq 'UNID_MEDI' and estado eq '1'&select=codigo,nombre,numero, referencia1")
    suspend fun getUnidadMedida(): Response<List<UnidadMedidaResponse>>


}