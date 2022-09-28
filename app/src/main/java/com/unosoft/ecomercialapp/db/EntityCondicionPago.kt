package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityCondicionPago(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val Codigo: String,
    @ColumnInfo
    val Nombre: String,
    @ColumnInfo
    val Numero: String,
    @ColumnInfo
    val Referencia1: String = ""
)