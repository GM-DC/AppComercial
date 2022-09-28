package com.unosoft.ecomercialapp.db.cotizacion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDateTime

@Entity
data class EntityQuotationMaster(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val iD_COTIZACION: Int,
    @ColumnInfo
    val numerO_COTIZACION: String?,
    @ColumnInfo
    val codigO_VENDEDOR: String?,
    @ColumnInfo
    val codigO_VENDEDOR_ASIGNADO: String?,
    @ColumnInfo
    val codigO_CPAGO: String?,
    @ColumnInfo
    val codigO_MONEDA: String?,
    @ColumnInfo
    val fechA_COTIZACION: String?,
    @ColumnInfo
    val numerO_OCLIENTE: String?,
    @ColumnInfo
    val importE_STOT: Double,
    @ColumnInfo
    val importE_DESCUENTO: Double,
    @ColumnInfo
    val valoR_VENTA: Double,
    @ColumnInfo
    val importE_IGV: Double,
    @ColumnInfo
    val importE_TOTAL: Double,
    @ColumnInfo
    val porcentajE_DESCUENTO: Double,
    @ColumnInfo
    val porcentajE_IGV: Double,
    @ColumnInfo
    val observacion: String?,
    @ColumnInfo
    val estado: String?,
    @ColumnInfo
    val iD_CLIENTE: Int,
    @ColumnInfo
    val iD_CLIENTE_FACTURA: Int,
    @ColumnInfo
    val importE_ISC: Double,
    @ColumnInfo
    val contacto: String?,
    @ColumnInfo
    val emaiL_CONTACTO: String?,
    @ColumnInfo
    val usuariO_CREACION: String?,
    @ColumnInfo
    val fechA_CREACION: String?,
    @ColumnInfo
    val usuariO_MODIFICACION: String?,
    @ColumnInfo
    val fechA_MODIFICACION: String?,
    @ColumnInfo
    val codigO_EMPRESA: String?,
    @ColumnInfo
    val codigO_SUCURSAL: String?,
    @ColumnInfo
    val tipO_FECHA_ENTREGA: String?,
    @ColumnInfo
    val diaS_ENTREGA: Int,
    @ColumnInfo
    val fechA_ENTREGA: String?,
    @ColumnInfo
    val usuariO_AUTORIZA: String?,
    @ColumnInfo
    val fechA_AUTORIZACION: String?,
    @ColumnInfo
    val lugaR_ENTREGA: String?,
    @ColumnInfo
    val comision: Double,
    @ColumnInfo
    val redondeo: String?,
    @ColumnInfo
    val validez: String?,
    @ColumnInfo
    val motivo: String?,
    @ColumnInfo
    val correlativo: String?,
    @ColumnInfo
    val centrO_COSTO: String?,
    @ColumnInfo
    val tipO_CAMBIO: Double,
    @ColumnInfo
    val iD_COTIZACION_PARENT: String?,
    @ColumnInfo
    val telefonos: String?,
    @ColumnInfo
    val sucursal: String?,
    @ColumnInfo
    val tipO_ENTREGA: String?,
    @ColumnInfo
    val diaS_ENTREGA2: Int,
    @ColumnInfo
    val observacioN2: String?,
    @ColumnInfo
    val costo: String?,
    @ColumnInfo
    val iD_OPORTUNIDAD: String?,
    @ColumnInfo
    val motivO_PERDIDA: String?,
    @ColumnInfo
    val competencia: String?,
    @ColumnInfo
    val notA_PERDIDA: String?,
    @ColumnInfo
    val separaR_STOCK: String?,
    @ColumnInfo
    val tipO_DSCTO: String?,
    @ColumnInfo
    val iD_PROYECTO: String?,
    @ColumnInfo
    val swT_VISADO: String?,
    @ColumnInfo
    val usuario: String?,
    @ColumnInfo
    val noM_MONEDA: String?,
    @ColumnInfo
    val direccion: String?,
    @ColumnInfo
    val ruc: String?,
    @ColumnInfo
    val persona: String?,
    @ColumnInfo
    val tipO_CLIENTE: String?,
)