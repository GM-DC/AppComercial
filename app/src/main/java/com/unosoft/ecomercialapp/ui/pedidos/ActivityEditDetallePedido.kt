package com.unosoft.ecomercialapp.ui.pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Pedidos.productlisteditorderadapter
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.databinding.ActivityDetallePedidoBinding
import com.unosoft.ecomercialapp.databinding.ActivityPedidoEditarBinding
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityEditDetallePedido : AppCompatActivity() {
    private lateinit var binding: ActivityDetallePedidoBinding
    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var adapterdetailpedido: productlisteditorderadapter

    private val listaProductoPEDIDO = ArrayList<EntityEditPedidoDetail>()
    private val listaProductoListados = ArrayList<productlistcot>()

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    var tipoMoneda = ""

    var apiInterface2: ProductoComercial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)
        tipoMoneda = intent.getStringExtra("TIPOMONEDA").toString()

        InitRecyclerview()
        getData()
    }

    fun InitRecyclerview() {
        binding.rvLtsorder.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapterdetailpedido = productlisteditorderadapter(listaProductoListados)
        binding.rvLtsorder.adapter = adapterdetailpedido
    }

    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (database.daoTblBasica().isExistsEntityListEditPedido()) {

                println("*** DATOS DE DETALLE LISTA PEDIDO ***")
                println(database.daoTblBasica().getAllDetail())
                println("*** TIPO DE MONEDA ***")
                println(tipoMoneda)

                database.daoTblBasica().getAllDetail().forEach {
                    listaProductoListados.add(
                        productlistcot(
                        id_Producto = it.iD_PRODUCTO!!,
                        codigo = it.iD_PRODUCTO.toString(),
                        codigo_Barra = it.codigO_BARRA!!,
                        nombre =it.nombre.toString(),
                        mon = tipoMoneda,
                        precio_Venta = it.precio!!,
                        factor_Conversion = it.factoR_CONVERSION!!,
                        cdg_Unidad = it.unidad!!,
                        unidad = it.noM_UNIDAD!!,
                        moneda_Lp = "",
                        cantidad = it.cantidad!!.toInt(),
                        precioUnidad = it.precio,
                        precioTotal = it.precio*it.cantidad
                        )
                    )
                }

            }
            adapterdetailpedido.notifyDataSetChanged()
            calcularMontoTotal()
        }
    }

    //************* FUNCIONES ADICIONALES  ****************
    fun calcularMontoTotal(){
        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = utils().priceIGV(montoTotal)
        subtotal = utils().priceSubTotal(montoTotal)

        val tv_subtotalCot = findViewById<TextView>(R.id.tv_subtotalPedido)
        val tv_igvCot = findViewById<TextView>(R.id.tv_igvPedido)
        val tv_totalCot = findViewById<TextView>(R.id.tv_totalPedido)

        tv_totalCot.text = "${tipoMoneda} ${utils().pricetostringformat(montoTotal)}"
        tv_igvCot.text = "${tipoMoneda} ${utils().pricetostringformat(igvTotal)}"
        tv_subtotalCot.text = "${tipoMoneda} ${utils().pricetostringformat(subtotal)}"
    }

}

