package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityDataLogin (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo
    val nombreusuario: String,
    @ColumnInfo
    val codigO_EMPRESA: String,
    @ColumnInfo
    val iD_CLIENTE: Int,
    @ColumnInfo
    val poR_IGV: String,
    @ColumnInfo
    val cdgmoneda: String,
    @ColumnInfo
    val validez: String,
    @ColumnInfo
    val cdgpago: String,
    @ColumnInfo
    val sucursal: String,
    //@ColumnInfo
    //val nombremozo: String,
    @ColumnInfo
    val usuarioautoriza: String,
    @ColumnInfo
    val usuariocreacion: String,
    @ColumnInfo
    val descuento: String,
    @ColumnInfo
    val seriepedido: String,
    @ColumnInfo
    val estadopedido: String,
    @ColumnInfo
    val tipocambio: Double,
    @ColumnInfo
    val jwtToken: String,
    @ColumnInfo
    val facturA_ADELANTADA: String,
    @ColumnInfo
    val iD_COTIZACION: String,
    @ColumnInfo
    val puntO_VENTA: String,
    @ColumnInfo
    val redondeo: String,
    //@ColumnInfo
    //val motivo: Any,
    //@ColumnInfo
    //val correlativo: Any,
    //@ColumnInfo
    //val refreshToken: String,
    @ColumnInfo
    val cdG_VENDEDOR: String
    )