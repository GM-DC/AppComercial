package com.unosoft.ecomercialapp.ui.pedidos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.Adapter.ProductListCot.productlistcotadarte
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.databinding.ActivityCartPedidoBinding
import com.unosoft.ecomercialapp.db.EntityListProct
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityCartPedido : AppCompatActivity() {
    private lateinit var binding: ActivityCartPedidoBinding
    //********************************************************

    private val listaProductoListados = ArrayList<productlistcot>()
    private val listaProductoPedido = ArrayList<productocomercial>()

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    var apiInterface2: ProductoComercial? = null

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    var tipomoneda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //*********************************************************
        tipomoneda = intent.getStringExtra("TIPOMONEDA").toString()

        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)

        getData()
        productosListado()
        abrirListProductos()
        eventsHandlers()
    }

    private fun eventsHandlers() {
        binding.btnGuardarCartPedido.setOnClickListener { guardarDatos() }
        binding.btnCancelarCartPedido.setOnClickListener { cancelarPedido() }
    }

    private fun cancelarPedido() {
        CoroutineScope(Dispatchers.IO).launch{
            if(listaProductoListados.isNotEmpty()){
                runOnUiThread {
                    alerDialogueCard()
                }
            }else{
                runOnUiThread {
                    val intent = Intent(this@ActivityCartPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun alerDialogueCard() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE,)

        dialog.titleText = "Cancelar"
        dialog.contentText = "Si retrocede, se perdera todo el cambios ¿Desea retroceder?"

        dialog.confirmText = "SI"
        dialog.confirmButtonBackgroundColor = Color.parseColor("#013ADF")
        dialog.confirmButtonTextColor = Color.parseColor("#ffffff")

        dialog.cancelText = "NO"
        dialog.cancelButtonBackgroundColor = Color.parseColor("#c8c8c8")

        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
            sDialog.cancel()
        }

        dialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            CoroutineScope(Dispatchers.IO).launch{
                DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()
                runOnUiThread {
                    val intent = Intent(this@ActivityCartPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        dialog.show()
    }

    private fun guardarDatos() {
        guardarListRoom()

        val intent = Intent(this, ActivityAddPedido::class.java)
        startActivity(intent)
        finish()
    }

    fun getData() {

        CoroutineScope(Dispatchers.IO).launch {

            if (DATAGLOBAL.database.daoTblBasica().isExistsEntityProductList()){

                DATAGLOBAL.database.daoTblBasica().getAllListProct().forEach {
                    listaProductoListados.add(
                        productlistcot(
                            it.id_Producto,it.codigo,it.codigo_Barra,it.nombre,tipomoneda,it.precio_Venta,it.factor_Conversion,
                            it.cdg_Unidad,it.unidad,it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal
                        )
                    )
                }

                DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()
            }

            CoroutineScope(Dispatchers.IO).launch {
                calcularMontoTotal()
            }
        }

    }



    //************  PRODUCTOS LISTADOS *************
    fun productosListado() {
        val rv_listproductpedido = binding.rvListProdut
        rv_listproductpedido.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(listaProductoListados) { data -> onItemDatosProductList(data) }
        rv_listproductpedido.adapter = productlistcotadarte

        //**************** Implementacion de Swiped ********************
        val itemswipe = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listaProductoListados.removeAt(viewHolder.bindingAdapterPosition)
                calcularMontoTotal()
                rv_listproductpedido?.adapter?.notifyDataSetChanged()
            }
        }
        val swap =  ItemTouchHelper(itemswipe)
        swap.attachToRecyclerView(rv_listproductpedido)

    }
    fun onItemDatosProductList(data: productlistcot) {
        //***********  Alerta de Dialogo  ***********
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_productocomericaldetallado, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************

        //********************    DECLARA LOS COMPONENTES   ******************
        val tv_nameProducto = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioTotal = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_cantidad = vista.findViewById<TextView>(R.id.tv_cantidad)
        val iv_btnAutementar = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val iv_btnDisminuir = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)
        val iv_cerrarProducto = vista.findViewById<ImageView>(R.id.iv_cerrarProducto)


        //********   SETEA VALORES INICIALES
        val evalua = buscaCoincidencia(data.codigo!!)
        val action = evalua[0]
        val pos = evalua[1]

        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "${data.mon} ${utils().pricetostringformat(data.precio_Venta)}"
        tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"

        if (action == 0) {
            tv_cantidad.text = "0"
            tv_precioTotal.text = "0"
        } else {
            tv_cantidad.text = listaProductoListados[pos].cantidad.toString()
            tv_precioTotal.text = "${data.mon} ${
                utils().pricetostringformat(
                    calculatepricebyqty(
                        tv_cantidad.text.toString().toInt(), data.precio_Venta
                    )
                )
            }"
        }

        iv_cerrarProducto.setOnClickListener {
            dialog.hide()
            dialog.cancel()
        }

        //********   AUMENTA PRODUCTOS O AGREGA    *************
        iv_btnAutementar.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]
            var cantidad = 0

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action == 0) {
                cantidad += 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                listaProductoListados.add(
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                Toast.makeText(this, "Se agrego ${data.nombre}", Toast.LENGTH_SHORT).show()
                rv_listproductcot.adapter?.notifyDataSetChanged()
                rv_listproductcot?.scrollToPosition(listaProductoListados.size - 1)
            } else {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad + 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal
                )
                rv_listproductcot?.adapter?.notifyDataSetChanged()
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()

        }
        //********   DISMINUYE PRODUCTOS O ELIMINA    *************
        iv_btnDisminuir.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action != 0) {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad - 1
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal)
                tv_cantidad.text = cantidad.toString()
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                rv_listproductcot?.adapter?.notifyDataSetChanged()
                if (cantidad == 0) {
                    listaProductoListados.removeAt(pos)
                    rv_listproductcot?.adapter?.notifyDataSetChanged()
                }
            } else {
                println("no hay productos que conincidan")
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()
        }
    }
    fun abrirListProductos() {

        binding.iconAddMoreProductList.setOnClickListener {

            //***********  Alerta de Dialogo  ***********
            val builder = AlertDialog.Builder(this)
            val vista = layoutInflater.inflate(R.layout.dialogue_detalle_cotizacion_productos, null)
            vista.setBackgroundResource(R.color.transparent)

            builder.setView(vista)

            val dialog = builder.create()
            dialog.window!!.setGravity(Gravity.TOP)
            dialog.show()

            val rv_productos = vista.findViewById<RecyclerView>(R.id.rv_productos)
            val sv_consultasproductos = vista.findViewById<SearchView>(R.id.sv_consultasproductos)
            val iv_cerrarListProducto = vista.findViewById<ImageView>(R.id.iv_cerrarListProducto)
            val ll_contenedor = vista.findViewById<LinearLayout>(R.id.ll_contenedor)
            val ll_cargando = vista.findViewById<LinearLayout>(R.id.ll_cargando)

            iv_cerrarListProducto.setOnClickListener {
                dialog.hide()
                dialog.cancel()
            }

            rv_productos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapterProductoComercial = productocomercialadapter(listaProductoPedido) { data ->
                onItemDatosProductos(data)
            }

            rv_productos.adapter = adapterProductoComercial

            CoroutineScope(Dispatchers.IO).launch {
                val response = apiInterface2!!.getProductoComercial("${DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].codListPrecio}", "${DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].codMoneda}", "${DATAGLOBAL.prefs.getTipoCambio()}")
                runOnUiThread {
                    if (response.isSuccessful) {

                        ll_contenedor.isVisible = true
                        ll_cargando.isVisible = false

                        listaProductoPedido.clear()
                        listaProductoPedido.addAll(response.body()!!)
                        adapterProductoComercial.notifyDataSetChanged()
                    }
                }
            }

            sv_consultasproductos?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    println("$newText")
                    filter(newText.toString())
                    return false
                }
            })

        }
    }
    fun filter(text: String) {
        val filterdNameProducto: ArrayList<productocomercial> = ArrayList()
        for (i in listaProductoPedido.indices) {
            if (listaProductoPedido[i].nombre.lowercase().contains(text.lowercase())) {
                filterdNameProducto.add(listaProductoPedido[i])
            }
        }
        adapterProductoComercial.filterList(filterdNameProducto)
    }
    fun onItemDatosProductos(data: productocomercial) {
        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_productocomericaldetallado, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.TOP)
        dialog.show()
        //*********************************************

        //********************    DECLARA LOS COMPONENTES   ******************
        val tv_nameProducto = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioTotal = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_cantidad = vista.findViewById<TextView>(R.id.tv_cantidad)
        val iv_btnAutementar = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val iv_btnDisminuir = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)
        val iv_cerrarProducto = vista.findViewById<ImageView>(R.id.iv_cerrarProducto)


        //********   SETEA VALORES INICIALES
        val evalua = buscaCoincidencia(data.codigo)
        val action = evalua[0]
        val pos = evalua[1]

        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "${data.mon} ${utils().pricetostringformat(data.precio_Venta!!)}"
        tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"

        if (action == 0) {
            tv_cantidad.text = "0"
            tv_precioTotal.text = "0"
        } else {
            tv_cantidad.text = listaProductoListados[pos].cantidad.toString()
            tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(calculatepricebyqty(tv_cantidad.text.toString().toInt(), data.precio_Venta))}"
        }

        iv_cerrarProducto.setOnClickListener {
            dialog.hide()
            dialog.cancel()
        }

        //********   AUMENTA PRODUCTOS O AGREGA    *************
        iv_btnAutementar.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]
            var cantidad = 0

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action == 0) {
                cantidad += 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                listaProductoListados.add(
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                rv_listproductcot.adapter?.notifyDataSetChanged()
                rv_listproductcot?.scrollToPosition(listaProductoListados.size - 1)
            } else {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad + 1
                tv_cantidad.text = cantidad.toString()
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                listaProductoListados.set(
                    pos,
                    productlistcot(
                        data.id_Producto,
                        data.codigo,
                        data.codigo_Barra,
                        data.nombre,
                        data.mon,
                        data.precio_Venta,
                        data.factor_Conversion,
                        data.cdg_Unidad,
                        data.unidad,
                        data.moneda_Lp,
                        cantidad,
                        data.precio_Venta,
                        precioTotal
                    )
                )
                rv_listproductcot?.adapter?.notifyDataSetChanged()
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()

        }

        //********   DISMINUYE PRODUCTOS O ELIMINA    *************
        iv_btnDisminuir.setOnClickListener {
            val rv_listproductcot = binding.rvListProdut

            val buscador = buscaCoincidencia(data.codigo)
            val action = buscador[0]
            val pos = buscador[1]

            //----------------  AGREGA O AUMENTA LA CANTIDAD -------------------
            if (action != 0) {
                val lt = listaProductoListados[pos]
                var cantidad = lt.cantidad - 1
                val precioTotal: Double = calculatepricebyqty(cantidad, data.precio_Venta)
                listaProductoListados[pos] = productlistcot(
                    data.id_Producto,
                    data.codigo,
                    data.codigo_Barra,
                    data.nombre,
                    data.mon,
                    data.precio_Venta,
                    data.factor_Conversion,
                    data.cdg_Unidad,
                    data.unidad,
                    data.moneda_Lp,
                    cantidad,
                    data.precio_Venta,
                    precioTotal
                )
                tv_cantidad.text = cantidad.toString()
                tv_precioTotal.text = "${data.mon} ${utils().pricetostringformat(precioTotal)}"
                rv_listproductcot?.adapter?.notifyDataSetChanged()
                if (cantidad == 0) {
                    listaProductoListados.removeAt(pos)
                    rv_listproductcot?.adapter?.notifyDataSetChanged()
                }
            } else {
                println("no hay productos que conincidan")
            }
            //-------------------------------------------------------------------
            calcularMontoTotal()
            productlistcotadarte.notifyDataSetChanged()
        }
    }

    //*************** FUNCIONES UTILITARIAS ********
    private fun buscaCoincidencia(dataCodigo:String): List<Int> {
        //-------------Evalua POSICION Y ACCION DE AGREGAR-------------------
        //println("------- Evalua POSICION Y ACCION DE AGREGAR-------------")
        var action = 0
        var pos = -1

        for (i in listaProductoListados.indices) {
            println("Cod: ${listaProductoListados[i].codigo}")
            if (listaProductoListados[i].codigo == dataCodigo) {
                action += 1
            }
            if (action == 1) {
                pos = i
                println("posicion: $pos")
                break
            }
        }

        return listOf(action, pos)
    }
    private fun calculatepricebyqty(Qty: Int, Price: Double): Double {
        return (Price * Qty.toDouble())
    }
    fun calcularMontoTotal(){

        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = utils().priceIGV(montoTotal)
        subtotal = utils().priceSubTotal(montoTotal)

        val tvSubTotalAddCart = binding.tvSubTotalAddCart
        val tvIgvAddCart = binding.tvIgvAddCart
        val tvTotalAddCart = binding.tvTotalAddCart

        tvTotalAddCart.text = "${tipomoneda} ${utils().pricetostringformat(montoTotal)}"
        tvIgvAddCart.text = "${tipomoneda} ${utils().pricetostringformat(igvTotal)}"
        tvSubTotalAddCart.text ="${tipomoneda} ${utils().pricetostringformat(subtotal)}"
    }

    //************* GUARDAR ROOM  ****************
    fun guardarListRoom(){

        CoroutineScope(Dispatchers.IO).launch {

            DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()

            if(listaProductoListados.size>0){
                listaProductoListados.forEach {
                    DATAGLOBAL.database.daoTblBasica().insertListProct(
                        EntityListProct(
                            0, it.id_Producto, it.codigo!!, it.codigo_Barra,
                            it.nombre,it.mon,it.precio_Venta,it.factor_Conversion,it.cdg_Unidad,it.unidad,
                            it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal,subtotal,igvTotal,montoTotal
                        )
                    )
                }
            }
            println(DATAGLOBAL.database.daoTblBasica().getAllListProct())
        }

    }
    override fun onBackPressed() {
        CoroutineScope(Dispatchers.IO).launch {
            if(listaProductoListados.isNotEmpty()){
                runOnUiThread {
                    cancelarPedido()
                }
            }else{
                runOnUiThread {
                    super.onBackPressed()
                }
            }
        }
    }
}