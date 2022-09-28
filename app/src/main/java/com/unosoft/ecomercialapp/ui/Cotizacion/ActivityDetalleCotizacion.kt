package com.unosoft.ecomercialapp.ui.Cotizacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.ProductListCot.productlistcotadarte
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityDetalleCotizacion : AppCompatActivity() {

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    private val listaProductoCotizacion = ArrayList<productocomercial>()
    private val listaProductoListados = ArrayList<productlistcot>()

    var apiInterface2: ProductoComercial? = null

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    var tipoMoneda = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cotizacion)
        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)
        tipoMoneda = intent.getStringExtra("TIPOMONEDA").toString()

        getData()
        productosListado()
    }

    fun productosListado() {
        val rv_listproductcot = findViewById<RecyclerView>(R.id.rv_listproductcot)
        rv_listproductcot?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(listaProductoListados) { data -> onItemDatosProductList(data) }
        rv_listproductcot?.adapter = productlistcotadarte
    }
    private fun onItemDatosProductList(data: productlistcot) {

    }

    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {

            println("/////////************************************////////")
            println("/////////************  Inicio  **************////////")
            println(database.daoTblBasica().getAllQuotationDetail())
            println("/////////*************  fin  **************////////")

            if (database.daoTblBasica().isExistsEntityListEditQuotation()) {
                    database.daoTblBasica().getAllQuotationDetail().forEach {
                        listaProductoListados.add(
                            productlistcot(
                                id_Producto = it.iD_PRODUCTO,
                                codigo =it.iD_PRODUCTO.toString(),
                                codigo_Barra = it.codigO_BARRA,
                                nombre = it.nombre,
                                mon = tipoMoneda,
                                precio_Venta = it.precio,
                                factor_Conversion = 0.0,
                                cdg_Unidad = it.unidad,
                                unidad = it.noM_UNIDAD,
                                moneda_Lp = "",
                                cantidad = it.cantidad,
                                precioUnidad = it.precio,
                                precioTotal = it.precio*it.cantidad
                            )
                        )
                    }
            }
            productlistcotadarte.notifyDataSetChanged()
            calcularMontoTotal()
        }
    }


    //************* FUNCIONES ADICIONALES  ****************
    fun calcularMontoTotal(){
        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = utils().priceIGV(montoTotal)
        subtotal = utils().priceSubTotal(montoTotal)

        val tv_subtotalCot = findViewById<TextView>(R.id.tv_subtotalCotDetailQuotation)
        val tv_igvCot = findViewById<TextView>(R.id.tv_igvCotDetailQuotation)
        val tv_totalCot = findViewById<TextView>(R.id.tv_totalCotDetailQuotation)

        tv_totalCot.text = "$tipoMoneda ${utils().pricetostringformat(montoTotal)}"
        tv_igvCot.text = "$tipoMoneda ${utils().pricetostringformat(igvTotal)}"
        tv_subtotalCot.text = "$tipoMoneda  ${utils().pricetostringformat(subtotal)}"
    }

}