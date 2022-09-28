package com.unosoft.ecomercialapp.entity.Empresa

import java.io.Serializable

data class dcEmpresa(
    val nameEmpresa:String,
    val ruc:String,
    val nameUser:String,
    val usuario:String,
    val url:String,
    val Userkey:String, // User + Url
): Serializable
