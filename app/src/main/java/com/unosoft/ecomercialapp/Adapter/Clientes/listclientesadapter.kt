package com.unosoft.ecomercialapp.Adapter.Clientes

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import kotlin.collections.ArrayList

class listclientesadapter(var clientes: ArrayList<ClientListResponse>,private val onClickListener: (ClientListResponse) -> Unit) : RecyclerView.Adapter<listclientesadapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(layoutInflater.inflate(R.layout.cardview_clientes,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(clientes[position],onClickListener)
    }

    override fun getItemCount(): Int = clientes.size

    class ViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun render (clientes: ClientListResponse,onClickListener: (ClientListResponse) -> Unit){

            val lblnrazonsocial = view.findViewById<TextView>(R.id.name)
            val lblnrruc = view.findViewById<TextView>(R.id.document)

            lblnrazonsocial.text = clientes.nombre

            lblnrazonsocial.setTypeface(null, Typeface.BOLD)

            lblnrruc.text = StringBuilder().append("Doc N#: ").append(clientes.ruc)

            itemView.setOnClickListener{onClickListener(clientes)}
        }


    }

    fun filterCliente(nameCliente: ArrayList<ClientListResponse>) {
        clientes = nameCliente
        notifyDataSetChanged()
    }




}