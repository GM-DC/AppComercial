package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityDataCabezera(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo
    var idCliente: String?,
    @ColumnInfo
    var nombreCliente: String?,
    @ColumnInfo
    var rucCliente: String?,
    @ColumnInfo
    var tipoMoneda: String?,
    @ColumnInfo
    var codMoneda: String?,
    @ColumnInfo
    var listPrecio: String?,
    @ColumnInfo
    var codListPrecio: String?,
    @ColumnInfo
    var validesDias: String?,
    @ColumnInfo
    var codValidesDias: String?,
    @ColumnInfo
    var condicionPago: String?,
    @ColumnInfo
    var codCondicionPago: String?,
    @ColumnInfo
    var vendedor: String?,
    @ColumnInfo
    var codVendedor: String?
)
