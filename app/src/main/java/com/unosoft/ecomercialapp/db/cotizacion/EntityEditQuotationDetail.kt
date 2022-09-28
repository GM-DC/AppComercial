package com.unosoft.ecomercialapp.db.cotizacion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityEditQuotationDetail(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val iD_COTIZACION: Int,
    @ColumnInfo
    val iD_PRODUCTO: Int,
    @ColumnInfo
    val codigo: String?,
    @ColumnInfo
    val codigO_BARRA: String,
    @ColumnInfo
    val nombre: String,
    @ColumnInfo
    val cantidad: Int,
    @ColumnInfo
    val iD_UNIDAD: String?,
    @ColumnInfo
    val unidad: String,
    @ColumnInfo
    val precio: Double,
    @ColumnInfo
    val descuento: Int,
    @ColumnInfo
    val importe: Double,
    @ColumnInfo
    val secuencia: Int,
    @ColumnInfo
    val igv: Double,
    @ColumnInfo
    val preciO_ORIGINAL: Double,
    @ColumnInfo
    val tipo: String,
    @ColumnInfo
    val afectO_IGV: String,
    @ColumnInfo
    val importE_DSCTO: Int,
    @ColumnInfo
    val comision: Int,
    @ColumnInfo
    val swT_PIGV: String?,
    @ColumnInfo
    val noM_UNIDAD: String,
)