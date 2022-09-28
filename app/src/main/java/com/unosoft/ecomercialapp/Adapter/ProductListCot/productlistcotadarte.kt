package com.unosoft.ecomercialapp.Adapter.ProductListCot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import java.lang.StringBuilder

class productlistcotadarte (var datos: ArrayList<productlistcot>, private val onClickListener: (productlistcot) -> Unit) : RecyclerView.Adapter<productlistcotadarte.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_productocomericaldetalladocot,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onClickListener)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun render (datos: productlistcot, onClickListener: (productlistcot) -> Unit){

            val tv_nameProducto = view.findViewById<TextView>(R.id.tv_nameProducto)
            val tv_codProducto = view.findViewById<TextView>(R.id.tv_codProducto)
            val tv_precioUnidad = view.findViewById<TextView>(R.id.tv_precioUnidad)
            val tv_cantidad = view.findViewById<TextView>(R.id.tv_cantidad)
            val tv_preciototal = view.findViewById<TextView>(R.id.tv_precioTotal)

            tv_nameProducto.text = datos.nombre
            tv_codProducto.text = "COD: "+datos.codigo
            tv_precioUnidad.text = "${datos.mon} ${utils().pricetostringformat(datos.precio_Venta)}"
            tv_cantidad.text = "${datos.cantidad} ${datos.unidad}"
            tv_preciototal.text = StringBuilder().append(datos.mon).append(utils().pricetostringformat(datos.precio_Venta*datos.cantidad))
            itemView.setOnClickListener { onClickListener(datos) }
        }
    }
}