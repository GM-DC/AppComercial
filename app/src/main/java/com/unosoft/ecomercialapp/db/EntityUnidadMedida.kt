package com.unosoft.ecomercialapp.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityUnidadMedida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val Codigo: String,

    @ColumnInfo
    val Nombre: String,

    @ColumnInfo
    val Numero: String,

    @ColumnInfo
    val Referencia1: String
)