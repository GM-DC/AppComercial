package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class EntityPedidoMaster(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val iD_PEDIDO: Int,
    @ColumnInfo
    val numerO_PEDIDO: String,
    @ColumnInfo
    val noM_MON: String,
    @ColumnInfo
    val smB_MON: String,
    @ColumnInfo
    val conD_PAGO: String,
    @ColumnInfo
    val persona: String,
    @ColumnInfo
    val ruc: String,
    @ColumnInfo
    val freC_DIAS: String,
    @ColumnInfo
    val codigO_VENDEDOR: String,
    @ColumnInfo
    val codigO_CPAGO: String,
    @ColumnInfo
    val codigO_MONEDA: String,
    @ColumnInfo
    val fechA_PEDIDO: String,
    @ColumnInfo
    val numerO_OCLIENTE: String,
    @ColumnInfo
    val importE_STOT: Double,
    @ColumnInfo
    val importE_IGV: Double,
    @ColumnInfo
    val importE_DESCUENTO: Double,
    @ColumnInfo
    val importE_TOTAL: Double,
    @ColumnInfo
    val porcentajE_DESCUENTO: Int,
    @ColumnInfo
    val porcentajE_IGV: Int,
    @ColumnInfo
    val observacion: String,
    @ColumnInfo
    val serie: String,
    @ColumnInfo
    val estado: String,
    @ColumnInfo
    val iD_CLIENTE: Int,
    @ColumnInfo
    val importE_ISC: Int,
    @ColumnInfo
    val usuariO_CREACION: String,
    @ColumnInfo
    val usuariO_AUTORIZA: String,
    @ColumnInfo
    val fechA_CREACION: String,
    @ColumnInfo
    val fechA_MODIFICACION: String,
    @ColumnInfo
    val codigO_EMPRESA: String,
    @ColumnInfo
    val codigO_SUCURSAL: String,
    @ColumnInfo
    val valoR_VENTA: Double,
    @ColumnInfo
    val iD_CLIENTE_FACTURA: Int,
    @ColumnInfo
    val codigO_VENDEDOR_ASIGNADO: String,
    @ColumnInfo
    val fechA_PROGRAMADA: String,
    @ColumnInfo
    val facturA_ADELANTADA: String,
    @ColumnInfo
    val contacto: String,
    @ColumnInfo
    val emaiL_CONTACTO: String,
    @ColumnInfo
    val lugaR_ENTREGA: String,
    @ColumnInfo
    val iD_COTIZACION: Int,
    @ColumnInfo
    val comision: Int,
    @ColumnInfo
    val puntO_VENTA: String,
    @ColumnInfo
    val redondeo: String,
    @ColumnInfo
    val validez: String,
    @ColumnInfo
    val motivo: String,
    @ColumnInfo
    val correlativo: String,
    @ColumnInfo
    val centrO_COSTO: String,
    @ColumnInfo
    val tipO_CAMBIO: Double,
    @ColumnInfo
    val sucursal: String,
)



