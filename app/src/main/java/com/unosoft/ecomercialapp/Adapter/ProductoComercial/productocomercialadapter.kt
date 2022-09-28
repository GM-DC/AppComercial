package com.unosoft.ecomercialapp.Adapter.ProductoComercial

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils

class productocomercialadapter (var datos: ArrayList<productocomercial>, private val onClickListener: (productocomercial) -> Unit) : RecyclerView.Adapter<productocomercialadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_productocomercial,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onClickListener)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun render (datos: productocomercial, onClickListener: (productocomercial) -> Unit){

            val tv_nameProductoComercial = view.findViewById<TextView>(R.id.tv_nameProductoComercial)
            val tv_codProductoComercial = view.findViewById<TextView>(R.id.tv_codProductoComercial)
            val tv_precioUnit = view.findViewById<TextView>(R.id.tv_precioUnit)
     //       val tv_precioTotal = view.findViewById<TextView>(R.id.tv_precioTotal)

            tv_nameProductoComercial.text = datos.nombre
            tv_codProductoComercial.text = datos.codigo
            tv_precioUnit.text = "${datos.mon} ${utils().pricetostringformat(datos.precio_Venta!!)}"
          //  tv_precioTotal.text = "${datos.mon} ${datos.precio_Venta}"

            itemView.setOnClickListener { onClickListener(datos) }

        }
    }

    fun filterList(nameProductoComercial: ArrayList<productocomercial>) {
        datos = nameProductoComercial
        notifyDataSetChanged()
    }

}