package com.unosoft.ecomercialapp.helpers

import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class utils {

    fun pricetostringformat(valuenumeric: Double): String {
        return String.format("%,.2f", valuenumeric)
    }

    fun priceSubTotal(price: Double): Double {
        val igv = prefs.getIGV().toDouble()
        return (price.div(1 + (igv.div(100))))
    }

    fun priceIGV(price: Double): Double {
        val igv = prefs.getIGV().toDouble()
        return price.minus(price.div(1 + (igv.div(100))))
    }

    fun getFecha(): String {
        return SimpleDateFormat("dd/MM/yyyy").format(LocalDateTime.now())
    }

    fun fechaActual(): LocalDateTime {
        val fechaActual = LocalDateTime.now()
        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val fechaSalida = fechaActual.format(dtf)

        return LocalDateTime.parse(fechaSalida)
    }

    fun formatearFecha(fecha:String):String{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val output = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val d: Date = sdf.parse(fecha)
        val formattedTime = output.format(d)

        return formattedTime
    }



}