package com.unosoft.ecomercialapp.Adapter.Cotizaciones

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto

import kotlin.collections.ArrayList

class listcotizacionesadapter(var cotizaciones: ArrayList<cotizacionesDto>, private val onClickListener: (cotizacionesDto) -> Unit) : RecyclerView.Adapter<listcotizacionesadapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(layoutInflater.inflate(R.layout.cardview_cotizaciones,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(cotizaciones[position],onClickListener)
    }

    override fun getItemCount(): Int = cotizaciones.size

    class ViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun render (cotizaciones: cotizacionesDto,onClickListener: (cotizacionesDto) -> Unit){
            val lblnrocotizacion = view.findViewById<TextView>(R.id.numerocotizacion)
            val lblnrazonsocial = view.findViewById<TextView>(R.id.razonsocial)
            val lblnrruc = view.findViewById<TextView>(R.id.ruc)
            val lblntotal = view.findViewById<TextView>(R.id.preciocotizacion)
            val lblfecha = view.findViewById<TextView>(R.id.fechacotizacion)

            lblnrocotizacion.text = cotizaciones.numero_Cotizacion
            lblnrazonsocial.text = cotizaciones.persona

            val originalString = StringBuffer(cotizaciones.fecha_Cotizacion)
            val neworiginalString = originalString.replace(10, 22, "").toString()

            lblfecha.text = "FECHA: "+neworiginalString

            lblnrazonsocial.setTypeface(null, Typeface.BOLD)
            lblntotal.setTypeface(null, Typeface.BOLD)
            lblntotal.text = StringBuilder().append("IMPORTE TOTAL ").append(cotizaciones.mon+". ").append(String.format("%,.2f", cotizaciones.importe_total))
            lblnrruc.text = StringBuilder().append(cotizaciones.documento+": ").append(cotizaciones.ruc)

            itemView.setOnClickListener {
                onClickListener(cotizaciones)
            }
        }
    }

    fun filterList(nameCotizacion: ArrayList<cotizacionesDto>) {
        cotizaciones = nameCotizacion
        notifyDataSetChanged()
    }

}