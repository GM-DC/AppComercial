package com.unosoft.ecomercialapp.Adapter.ProductListCot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ItemAddProductBinding
import com.unosoft.ecomercialapp.databinding.ItemAddProductListBinding
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.helpers.utils

class productlistcotadarte (
    private var datos: ArrayList<productlistcot>,
    private val onItemPosition: (Int) -> Unit,
    ) : RecyclerView.Adapter<productlistcotadarte.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_add_product_list,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onItemPosition)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val binding = ItemAddProductListBinding.bind(view)
        fun render (
            datos: productlistcot,
            onItemPosition: (Int) -> Unit,
        ){
            binding.tvNameProducto.text = datos.nombre
            binding.tvCodProducto.text = "COD "+datos.codigo
            binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
            binding.tvCantidad.text = ""
            binding.tvPrecioUnidad.text = "Precio Unit: ${datos.mon} "
            binding.tvPrecioUnidadDinero.text = utils().pricetostringformat(datos.precioUnidad)
            binding.tvPrecioTotal.text = "Precio Total: ${datos.mon} "
            binding.tvPrecioTotalDinero.text = utils().pricetostringformat(datos.precioTotal)

            itemView.setOnClickListener {
                onItemPosition(bindingAdapterPosition)
            }

        }


    }


}