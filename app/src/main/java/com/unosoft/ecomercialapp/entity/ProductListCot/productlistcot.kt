package com.unosoft.ecomercialapp.entity.ProductListCot

data class productlistcot(
    val id_Producto: Int,
    val codigo: String?,
    val codigo_Barra: String,
    val nombre: String,
    val mon: String,
    val precio_Venta: Double,
    val factor_Conversion: Double?,
    val cdg_Unidad: String,
    val unidad: String,
    val moneda_Lp: String,
    var cantidad: Int,
    var precioUnidad: Double,
    var precioTotal: Double,
)