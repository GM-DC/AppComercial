package com.unosoft.ecomercialapp.entity.ProductoComercial

data class productocomercial(
    val id_Producto: Int,
    val codigo: String,
    val codigo_Barra: String,
    val nombre: String,
    val mon: String,
    val precio_Venta: Double?,
    val factor_Conversion: Double?,
    val cdg_Unidad: String,
    val unidad: String,
    val moneda_Lp: String
)