package com.unosoft.ecomercialapp.Adapter.Stocks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Stocks.Almacen
import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import java.lang.StringBuilder

class listconsultastocks (var data:ArrayList<ConsultaStocksResponseItem>): RecyclerView.Adapter<listconsultastocks.holderConsultaStock>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderConsultaStock {
        val layoutInflater = LayoutInflater.from(parent.context)
        return holderConsultaStock(layoutInflater.inflate(R.layout.cardview_stocks,parent,false))
    }

    override fun onBindViewHolder(holder: holderConsultaStock, position: Int) {
        holder.holderConsultaStock(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class holderConsultaStock(private val view:View): RecyclerView.ViewHolder(view){

        fun holderConsultaStock (datos: ConsultaStocksResponseItem){
            val tv_nameProducto = view.findViewById<TextView>(R.id.nombreproductostocks)
            val tv_codProducto = view.findViewById<TextView>(R.id.tv_codProducto)
            val tv_CodBarra = view.findViewById<TextView>(R.id.tv_CodBarra)
            val tv_codReferente = view.findViewById<TextView>(R.id.tv_codReferente)
            val rv_cunsultaStocks = view.findViewById<RecyclerView>(R.id.rv_recyclerstocksalmacenes)

            val childlistconsultastocks = childlistconsultastocks(datos.almacen)
            val newname = StringBuilder().append(datos.producto!!.replace("""[$,.,/]""".toRegex(), " "))
            tv_nameProducto.text = datos.producto
            tv_codProducto.text = datos.codigo

            if (datos.codigoBarra!!.isNotEmpty()){
                tv_CodBarra.text = datos.codigoBarra
            }
            if (datos.codRef!!.isNotEmpty()){
                tv_codReferente.text = datos.codRef
            }


            rv_cunsultaStocks?.layoutManager = LinearLayoutManager(this.itemView.context, RecyclerView.VERTICAL,false)

            val childRecyclerAdapter = childlistconsultastocks(datos.almacen)
            rv_cunsultaStocks.adapter = childRecyclerAdapter
            //rv_cunsultaStocks.adapter = childlistconsultastocks(datos.almacen)
            //childlistconsultastocks.notifyDataSetChanged()

        }





    }

    fun filterList(nameProducto: ArrayList<ConsultaStocksResponseItem>) {
        data = nameProducto
        notifyDataSetChanged()
    }

}
