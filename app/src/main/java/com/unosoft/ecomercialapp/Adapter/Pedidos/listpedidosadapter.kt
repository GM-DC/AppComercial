package com.unosoft.ecomercialapp.Adapter.Pedidos

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto

import kotlin.collections.ArrayList

class listpedidosadapter(var datos: ArrayList<pedidosDto>, private val onClickListener: (pedidosDto) -> Unit) : RecyclerView.Adapter<listpedidosadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.cardview_pedido,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onClickListener)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun render (datos: pedidosDto,onClickListener: (pedidosDto) -> Unit){
            val tv_cliente = view.findViewById<TextView>(R.id.tv_cliente)
            val tv_numeropedido = view.findViewById<TextView>(R.id.tv_numeropedido)
            val ruc = view.findViewById<TextView>(R.id.ruc)
            val preciocotizacion = view.findViewById<TextView>(R.id.preciopedido)
            val fechapedido = view.findViewById<TextView>(R.id.fechapedido)

            tv_cliente.text = datos.persona
            if (datos.numero_Pedido==null){
                tv_numeropedido.text = "PED-SINVALOR"
                datos.numero_Pedido = "PED-SINVALOR"
            }
            else {
                tv_numeropedido.text = datos.numero_Pedido
            }
            ruc.text = "${datos.documento}: ${datos.ruc}"
            preciocotizacion.text =  StringBuilder().append("IMPORTE TOTAL ").append(datos.mon+". ").append(String.format("%,.2f", datos.importe_Total))


            val originalString = StringBuffer(datos.fecha_pedido)
            val neworiginalString = originalString.replace(10, 22, "").toString()


            fechapedido.text = "FECHA: ${neworiginalString}"

            itemView.setOnClickListener {
                onClickListener(datos)
            }
        }
    }

    fun filterList(namePedido: ArrayList<pedidosDto>) {
        datos = namePedido
        notifyDataSetChanged()
    }

}