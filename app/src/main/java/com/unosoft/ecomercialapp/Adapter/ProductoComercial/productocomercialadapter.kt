package com.unosoft.ecomercialapp.Adapter.ProductoComercial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ItemAddProductBinding
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.helpers.utils

class productocomercialadapter (
    var datos: ArrayList<productlistcot>,
    private val onClickListener: (productlistcot) -> Unit,
    private val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<productocomercialadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_add_product,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(datos[position],onClickListener,onClickItem)
    }

    override fun getItemCount(): Int = datos.size

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val binding = ItemAddProductBinding.bind(view)
        fun render (
            datos: productlistcot,
            onClickListener: (productlistcot) -> Unit,
            onClickItem: (Int) -> Unit
        ){
            binding.tvNameProducto.text = datos.nombre
            binding.tvCodProducto.text = datos.codigo
            binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
            binding.tvCantidad.setText("${datos.cantidad}")
            binding.tvPrecioUnidad.text = "Precio Unit: ${datos.mon} "
            binding.tvPrecioUnidadDinero.setText(utils().pricetostringformat(datos.precio_Venta))
            binding.tvPrecioTotal.text = "Precio Total: ${datos.mon} "
            binding.tvPrecioTotalDinero.text = (utils().pricetostringformat((datos.precioUnidad*datos.cantidad)))

            binding.ivBtnAutementar.setOnClickListener {
                datos.cantidad = datos.cantidad + 1
                binding.tvCantidad.setText(datos.cantidad.toString())
                val precioUnitario = datos.precioUnidad
                datos.precioTotal = precioUnitario*datos.cantidad
                binding.tvPrecioTotalDinero.text = (utils().pricetostringformat(datos.precioTotal))
                binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
            }
            binding.ivBtnDisminuir.setOnClickListener {
                if(datos.cantidad>0){
                    datos.cantidad = datos.cantidad - 1
                    binding.tvCantidad.setText(datos.cantidad.toString())
                    val precioUnitario = datos.precioUnidad
                    datos.precioTotal = precioUnitario*datos.cantidad
                    binding.tvPrecioTotalDinero.text = (utils().pricetostringformat(datos.precioTotal))
                    binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
                }
            }
            binding.tvPrecioUnidadDinero.doAfterTextChanged {
                if(it.isNullOrBlank()){
                    datos.precioUnidad = 0.0
                    datos.cantidad = binding.tvCantidad.text.toString().toInt()
                    datos.precioTotal = datos.cantidad*datos.precioUnidad
                    binding.tvPrecioTotalDinero.setText(utils().pricetostringformat((datos.precioTotal)))
                    binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
                }else{
                    datos.precioUnidad = it.toString().toDouble()
                    datos.cantidad = binding.tvCantidad.text.toString().toInt()
                    datos.precioTotal = it.toString().toDouble()*datos.cantidad
                    binding.tvPrecioTotalDinero.setText(utils().pricetostringformat((datos.precioTotal)))
                    binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
                }
            }
            binding.tvCantidad.doAfterTextChanged {
                if(it.isNullOrBlank()){
                    datos.cantidad = 0
                    datos.precioUnidad = binding.tvPrecioUnidadDinero.text.toString().toDouble()
                    datos.precioTotal = datos.cantidad*datos.precioUnidad
                    binding.tvPrecioTotalDinero.setText(utils().pricetostringformat((datos.precioTotal)))
                    binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
                }else{
                    datos.cantidad = it.toString().toInt()
                    datos.precioUnidad = binding.tvPrecioUnidadDinero.text.toString().toDouble()
                    datos.precioTotal = datos.cantidad.toDouble()*datos.precioUnidad
                    binding.tvPrecioTotalDinero.setText(utils().pricetostringformat((datos.precioTotal)))
                    binding.tvCantidadTexto.text = "Cantidad: ${datos.cantidad} ${datos.unidad}"
                }
            }

            binding.ivAgregarProducto.setOnClickListener {
                if (datos.cantidad>0) {
                    onClickListener(datos)
                }
            }

        }
    }

    fun filterList(nameProductoComercial: ArrayList<productlistcot>) {
        datos = nameProductoComercial
        notifyDataSetChanged()
    }

}