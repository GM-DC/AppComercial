package com.unosoft.ecomercialapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityGuardarCotizacion (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,

    )