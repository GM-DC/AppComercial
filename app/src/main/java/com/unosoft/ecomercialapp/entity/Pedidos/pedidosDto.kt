package com.unosoft.ecomercialapp.entity.Pedidos

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class pedidosDto(
    val id_pedido: Int,
    var numero_Pedido: String,
    val fecha_pedido: String,
    val persona: String,
    val ruc: String,
    val documento: String,
    val direccion: String,
    val telefono: String,
    val lugar_entrega: String,
    val mon: String,
    val importe_descuento: Int,
    val valor_venta: Int,
    val nom_moneda: String,
    val importe_igv: Double,
    val importe_Total: Double,
    val estado: String
) : Serializable