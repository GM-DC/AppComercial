package com.unosoft.ecomercialapp.artifact

import java.math.BigDecimal
import java.time.LocalDateTime

class detalle_coti {
    var Codigo_Barra: String? = null
    var ID_COTIZACION = 0
    var ID_PRODUCTO = 0
    var Cantidad: BigDecimal = BigDecimal(0)
    var Precio: BigDecimal = BigDecimal(0)
    var Descuento: BigDecimal = BigDecimal(0)
    var Igv: BigDecimal = BigDecimal(0)
    var Importe: BigDecimal = BigDecimal(0)
    var Observacion: String? = null
    var Secuencia = 0
    var PreciO_ORIGINAL: BigDecimal = BigDecimal(0)
    var Tipo: String? = null
    var importE_DSCTO: BigDecimal = BigDecimal(0)
    var AfectO_IGV: String? = null
    var comision = 0
    var iD_PRESUPUESTO = 0
    var CdG_SERV: String? = null
    var ImageN_REF: String? = null
    var ObservacioN2: String? = null
    var Unidad: String? = null
    var PoR_PERCEPCION: BigDecimal = BigDecimal(0)
    var Percepcion: BigDecimal = BigDecimal(0)
    var ValoR_VEN: BigDecimal = BigDecimal(0)
    var UniD_VEN: String? = null
    var FechA_VEN: LocalDateTime? = null
    var CdG_KIT: String? = null
    var SwT_PIGV: String? = null
    var PoR_DETRACCION = 0
    var Detraccion = 0
    var FactoR_CONVERSION = 0
    var Margen = 0
    var ImportE_MARGEN = 0
    var CostO_ADIC = 0
    var CanT_KIT = 0

}