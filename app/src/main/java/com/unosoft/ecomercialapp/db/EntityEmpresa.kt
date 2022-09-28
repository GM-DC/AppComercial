package com.unosoft.ecomercialapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityEmpresa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    @ColumnInfo
    val nameEmpresa: String,
    @ColumnInfo
    val ruc: String,
    @ColumnInfo
    val nameUser:String,
    @ColumnInfo
    val usuario: String,
    @ColumnInfo
    val url:String,
    @ColumnInfo
    val Userkey:String, // User + Url

)