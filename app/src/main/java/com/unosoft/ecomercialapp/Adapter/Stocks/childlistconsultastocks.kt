package com.unosoft.ecomercialapp.Adapter.Stocks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Stocks.Almacen
import java.util.ArrayList

class childlistconsultastocks (var data: List<Almacen>): RecyclerView.Adapter<childlistconsultastocks.holderAlmacenItem>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAlmacenItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        return holderAlmacenItem(layoutInflater.inflate(R.layout.cardview_stocks_warehouses,parent,false))
    }

    override fun onBindViewHolder(holder: holderAlmacenItem, position: Int) {
        holder.render(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class holderAlmacenItem(private val view: View): RecyclerView.ViewHolder(view){
        fun render (datos: Almacen){
            val tv_almacen = view.findViewById<TextView>(R.id.tv_almacen)
            val tv_strock = view.findViewById<TextView>(R.id.tv_strock)
            val tv_precio = view.findViewById<TextView>(R.id.tv_precio)
            val tv_listaprecio = view.findViewById<TextView>(R.id.tv_listaprecio)

            tv_almacen.text = "ALMACEN: " +datos.almacen
            tv_strock.text = "STOCK ACTUAL: "+datos.stockActual.toString()+" "+datos.unidad.toString()
            tv_precio.text = "PRECIO: "+datos.mon+" "+datos.precioVenta.toString()
            tv_listaprecio.text = datos.listaPrecio

        }
    }
}