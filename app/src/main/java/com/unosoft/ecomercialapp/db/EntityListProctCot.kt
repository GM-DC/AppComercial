package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityListProct (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val id_Producto: Int,
    @ColumnInfo
    val codigo: String,
    @ColumnInfo
    val codigo_Barra: String,
    @ColumnInfo
    val nombre: String,
    @ColumnInfo
    val mon: String,
    @ColumnInfo
    val precio_Venta: Double,
    @ColumnInfo
    val factor_Conversion: Double?,
    @ColumnInfo
    val cdg_Unidad: String,
    @ColumnInfo
    val unidad: String,
    @ColumnInfo
    val moneda_Lp: String,
    @ColumnInfo
    var cantidad: Int,
    @ColumnInfo
    var precioUnidad: Double,
    @ColumnInfo
    var precioTotal: Double,
    @ColumnInfo
    var montoSubTotal: Double,
    @ColumnInfo
    var montoTotalIGV: Double,
    @ColumnInfo
    var montoTotal: Double
)