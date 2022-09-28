package com.unosoft.ecomercialapp.api

import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Headers
import kotlin.collections.ArrayList

interface ClientApi {
    @GET("api/Persona?select=idPersona,nombre,ruc")
    suspend fun getAllClients(): Response<ArrayList<ClientListResponse>>
}