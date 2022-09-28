package com.unosoft.ecomercialapp.entity.DatosCabezeraCotizacion

import java.io.Serializable


data class datosCabezera(
    var idCliente: String? = "",
    var nombreCliente: String?= "",
    var rucCliente: String?= "",
    var tipoMoneda: String?= "",
    var codMoneda: String?= "",
    var listPrecio: String?= "",
    var codListPrecio: String?= "",
    var validesDias: String?= "",
    var codValidesDias: String?= "",
    var condicionPago: String?= "",
    var codCondicionPago: String?= "",
    var vendedor: String?= "",
    var codVendedor: String?= ""
)
