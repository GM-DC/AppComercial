package com.unosoft.ecomercialapp.entity.Cliente

import com.google.gson.annotations.SerializedName

data class ClientListResponse(
    @SerializedName("IdPersona") val idpersona: String,
    @SerializedName("Nombre")    val nombre: String,
    @SerializedName("Ruc")       val ruc: String
)