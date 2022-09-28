package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityListaPrecio(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val codigo: String,
    @ColumnInfo
    val nombre: String,
    @ColumnInfo
    val moneda: String
)