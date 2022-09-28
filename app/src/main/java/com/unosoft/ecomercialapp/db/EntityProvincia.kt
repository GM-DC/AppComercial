package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityProvincia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val Codigo: String,
    @ColumnInfo
    val Nombre: String,
    @ColumnInfo
    val Numero: String,
    @ColumnInfo
    val Referencia2: String
)