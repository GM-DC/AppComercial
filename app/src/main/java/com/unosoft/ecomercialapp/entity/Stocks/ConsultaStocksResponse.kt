package com.unosoft.ecomercialapp.entity.Stocks

data class ConsultaStocksResponseItem(
    val producto: String?,
    val codigoBarra: String?,
    val codRef: String?,
    val codigo: String?,
    val almacen: List<Almacen>
)

data class Almacen(
    val almacen: String?,
    val stockActual: Double?,
    val unidad: String?,
    val listaPrecio: String?,
    val mon: String?,
    val precioVenta: Double?
)