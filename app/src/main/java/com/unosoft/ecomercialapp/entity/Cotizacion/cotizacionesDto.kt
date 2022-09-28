package com.unosoft.ecomercialapp.entity.Cotizacion

import java.io.Serializable

data class cotizacionesDto (val id_cotizacion: Int,
                            val numero_Cotizacion: String,
                            val fecha_Cotizacion: String,
                            val persona: String,
                            val ruc: String,
                            val documento: String,
                            val mon: String,
                            val importe_descuento: Double,
                            val importe_igv: Double,
                            val importe_total: Double,
                            val eSTADO: String) : Serializable