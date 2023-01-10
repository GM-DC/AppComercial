package com.unosoft.ecomercialapp.ui.Cotizacion

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unosoft.ecomercialapp.Adapter.ProductListCot.productlistcotadarte
import com.unosoft.ecomercialapp.Adapter.ProductoComercial.productocomercialadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ProductoComercial
import com.unosoft.ecomercialapp.databinding.ActivityCardQuotationBinding
import com.unosoft.ecomercialapp.db.EntityListProct
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot
import com.unosoft.ecomercialapp.entity.ProductoComercial.productocomercial
import com.unosoft.ecomercialapp.helpers.utils
import com.unosoft.ecomercialapp.ui.pedidos.ActivityAddPedido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityCardQuotation : AppCompatActivity() {
    private lateinit var binding: ActivityCardQuotationBinding
    //********************************************************

    private lateinit var adapterProductoComercial: productocomercialadapter
    private lateinit var productlistcotadarte: productlistcotadarte

    private val listaProductoCotizacion = ArrayList<productlistcot>()
    private val listaProductoListados = ArrayList<productlistcot>()


    var apiInterface2: ProductoComercial? = null

    var montoTotal:Double = 0.0
    var igvTotal:Double = 0.0
    var subtotal:Double = 0.0

    var tipomoneda = ""
    var itemSelect: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardQuotationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tipomoneda = intent.getStringExtra("TIPOMONEDA").toString()

        apiInterface2 = APIClient.client?.create(ProductoComercial::class.java)

        //iniciarLista()
        getData()

        productosListado()
        enableSwipeToDelete()
        eventsHandlers()
    }

    private fun eventsHandlers() {
        binding.btnGuardarCartCotizacion.setOnClickListener { guardarDatos() }
        binding.btnCancelarCartCotizacion.setOnClickListener { cancelarCotizacion() }
        binding.iconAddMoreProductList.setOnClickListener{ abrirListProductos() }
    }
    private fun cancelarCotizacion() {
        CoroutineScope(Dispatchers.IO).launch{
            if(listaProductoListados.isNotEmpty()){
                runOnUiThread {
                    alerDialogueCard()
                }
            }else{
                runOnUiThread {
                    val intent = Intent(this@ActivityCardQuotation, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private fun alerDialogueCard(){
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE,)

        dialog.setTitleText("Cancelar")
        dialog.setContentText("Si retrocede, se perdera todo el cambios ¿Desea retroceder?")

        dialog.setConfirmText("SI").setConfirmButtonBackgroundColor(Color.parseColor("#013ADF"))
        dialog.setConfirmButtonTextColor(Color.parseColor("#ffffff"))

        dialog.setCancelText("NO").setCancelButtonBackgroundColor(Color.parseColor("#c8c8c8"))

        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
            sDialog.cancel()
        }

        dialog.setConfirmClickListener { sDialog ->
            sDialog.cancel()
            CoroutineScope(Dispatchers.IO).launch{
                database.daoTblBasica().deleteTableListProct()
                database.daoTblBasica().clearPrimaryKeyListProct()
                runOnUiThread {
                    val intent = Intent(this@ActivityCardQuotation, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        dialog.show()
    }
    private fun guardarDatos() {
        guardarListRoom()

        val intent = Intent(this, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
    }
    //********* INICIA DATOS  **************
    fun getData() {
        CoroutineScope(Dispatchers.IO).launch {

            if (database.daoTblBasica().isExistsEntityProductList()){

                database.daoTblBasica().getAllListProct().forEach {
                    listaProductoListados.add(
                        productlistcot(
                            it.id_Producto,it.codigo,it.codigo_Barra,it.nombre,tipomoneda,it.precio_Venta,it.factor_Conversion,
                            it.cdg_Unidad,it.unidad,it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal
                        )
                    )
                }

                database.daoTblBasica().deleteTableListProct()
                database.daoTblBasica().clearPrimaryKeyListProct()
            }

            CoroutineScope(Dispatchers.IO).launch {
                calcularMontoTotal()
            }
        }

    }

    //************  PRODUCTOS LISTADOS *************
    fun productosListado() {
        binding.rvListProdut.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productlistcotadarte = productlistcotadarte(
            datos = listaProductoListados,
            onItemPosition = { position -> onItemPosition(position) },
        )
        binding.rvListProdut.adapter = productlistcotadarte
    }

    private fun onItemPosition(position: Int) {
        val data = listaProductoListados[position]

        //***********  Alerta de Dialogo  ***********
        val builder = AlertDialog.Builder(this)
        val vista = layoutInflater.inflate(R.layout.item_add_product_list_edit, null)
        vista.setBackgroundResource(R.color.transparent)

        builder.setView(vista)

        val dialog = builder.create()
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()

        var tv_nameProducto  = vista.findViewById<TextView>(R.id.tv_nameProducto)
        val tv_codProducto  = vista.findViewById<TextView>(R.id.tv_codProducto)
        val tv_precioUnidad  = vista.findViewById<TextView>(R.id.tv_precioUnidad)
        val tv_precioUnidadDinero  = vista.findViewById<EditText>(R.id.tv_precioUnidadDinero)
        val tv_precioTotal  = vista.findViewById<TextView>(R.id.tv_precioTotal)
        val tv_precioTotalDinero  = vista.findViewById<TextView>(R.id.tv_precioTotalDinero)
        val iv_btnAutementar  = vista.findViewById<ImageView>(R.id.iv_btnAutementar)
        val tv_cantidad  = vista.findViewById<EditText>(R.id.tv_cantidad)
        val iv_btnDisminuir  = vista.findViewById<ImageView>(R.id.iv_btnDisminuir)
        val iv_btnGuardar  = vista.findViewById<Button>(R.id.iv_btnGuardar)
        val iv_btnCerrar  = vista.findViewById<ImageView>(R.id.iv_btnCerrar)
        val tv_cantidadTexto  = vista.findViewById<TextView>(R.id.tv_cantidadTexto)


        tv_nameProducto.text = data.nombre
        tv_codProducto.text = data.codigo
        tv_precioUnidad.text = "P. Unit: ${data.mon} "
        tv_precioUnidadDinero.setText(data.precioUnidad.toString())
        tv_precioTotal.text = "P. Total: ${data.mon} "
        tv_precioTotalDinero.text = data.precioTotal.toString()
        tv_cantidad.setText(data.cantidad.toString())
        tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"

        iv_btnAutementar.setOnClickListener {
            data.cantidad = data.cantidad + 1
            tv_cantidad.setText(data.cantidad.toString())
            val precioUnitario = tv_precioUnidadDinero.text.toString().toDouble()
            data.precioTotal = precioUnitario*data.cantidad
            tv_precioTotalDinero.text = (utils().pricetostringformat(data.precioTotal))
            tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
        }
        iv_btnDisminuir.setOnClickListener {
            if(data.cantidad>0){
                data.cantidad = data.cantidad - 1
                tv_cantidad.setText(data.cantidad.toString())
                val precioUnitario = tv_precioUnidadDinero.text.toString().toDouble()
                data.precioTotal = precioUnitario*data.cantidad
                tv_precioTotalDinero.text = (utils().pricetostringformat(data.precioTotal))
                tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
            }
        }
        tv_precioUnidadDinero.doAfterTextChanged{
            if(it.isNullOrBlank()){
                data.precioUnidad = 0.0
                data.cantidad = tv_cantidad.text.toString().toInt()
                data.precioTotal = data.cantidad*data.precioUnidad
                tv_precioTotalDinero.setText(utils().pricetostringformat((data.precioTotal)))
                tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
            }else{
                data.precioUnidad = it.toString().toDouble()
                data.cantidad = tv_cantidad.text.toString().toInt()
                data.precioTotal = data.cantidad*data.precioUnidad
                tv_precioTotalDinero.setText(utils().pricetostringformat((data.precioTotal)))
                tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
            }
        }
        tv_cantidad.doAfterTextChanged{
            if(it.isNullOrBlank()){
                data.cantidad = 0
                data.precioUnidad = tv_precioUnidadDinero.text.toString().toDouble()
                data.precioTotal = data.cantidad*data.precioUnidad
                tv_precioTotalDinero.setText(utils().pricetostringformat((data.precioTotal)))
                tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
            }else{
                data.cantidad = it.toString().toInt()
                data.precioUnidad = tv_precioUnidadDinero.text.toString().toDouble()
                data.precioTotal = data.cantidad*data.precioUnidad
                tv_precioTotalDinero.setText(utils().pricetostringformat((data.precioTotal)))
                tv_cantidadTexto.text = "Cantidad: ${data.cantidad} ${data.unidad}"
            }
        }
        iv_btnGuardar.setOnClickListener {
            listaProductoListados[position] = productlistcot(
                id_Producto= data.id_Producto,
                codigo= data.codigo,
                codigo_Barra= data.codigo_Barra,
                nombre= data.nombre,
                mon= data.mon,
                precio_Venta= data.precio_Venta,
                factor_Conversion= data.factor_Conversion,
                cdg_Unidad= data.cdg_Unidad,
                unidad= data.unidad,
                moneda_Lp= data.moneda_Lp,
                cantidad= data.cantidad,
                precioUnidad= data.precioUnidad,
                precioTotal= data.precioTotal)
            productlistcotadarte.notifyItemChanged(position)
            calcularMontoTotal()
            Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show()
            dialog.hide()
            dialog.cancel()
        }
        iv_btnCerrar.setOnClickListener {
            dialog.hide()
            dialog.cancel()
        }
    }

    fun enableSwipeToDelete(){
        val itemswipe = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder): Boolean { return false }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                println(viewHolder.bindingAdapterPosition)
                listaProductoListados.removeAt(viewHolder.bindingAdapterPosition)
                productlistcotadarte.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                calcularMontoTotal()
            }
        }
        val swap =  ItemTouchHelper(itemswipe)
        swap.attachToRecyclerView(binding.rvListProdut)
    }

    fun abrirListProductos() {
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
            val sp_filtroListaProducto = vista.findViewById<Spinner>(R.id.sp_filtroListaProducto)

            iv_cerrarListProducto.setOnClickListener {
                dialog.hide()
                dialog.cancel()
            }

            rv_productos.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            adapterProductoComercial = productocomercialadapter(
                datos = listaProductoCotizacion,
                onClickListener = { data -> onItemDatosProductos(data) },
                onClickItem = { posicion -> onClickItemAddProducto(posicion) }
            )
            rv_productos.adapter = adapterProductoComercial

            val listaFiltro = listOf("Nombre", "Codigo")
            val Adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaFiltro)

            sp_filtroListaProducto?.adapter = Adaptador

            sp_filtroListaProducto?.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    itemSelect = listaFiltro[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

            ll_cargando.isVisible = false

            getDataListProductos(ll_contenedor,ll_cargando)

            sv_consultasproductos?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(itemSelect!!,newText.toString())
                    return false
                }
            })
    }

    private fun getDataListProductos(ll_contenedor: LinearLayout,ll_cargando: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface2!!.getProductoComercial("${database.daoTblBasica().getAllDataCabezera()[0].codListPrecio}", "${database.daoTblBasica().getAllDataCabezera()[0].codMoneda}", "${prefs.getTipoCambio()}")
            runOnUiThread {
                if (response.isSuccessful) {
                    ll_contenedor.isVisible = true
                    ll_cargando.isVisible = false
                    listaProductoCotizacion.clear()
                    response.body()!!.forEach {
                        listaProductoCotizacion.add(
                            productlistcot(
                                id_Producto= it.id_Producto,
                                codigo= it.codigo,
                                codigo_Barra= it.codigo_Barra,
                                nombre= it.nombre,
                                mon= it.mon,
                                precio_Venta= it.precio_Venta!!,
                                factor_Conversion= it.factor_Conversion,
                                cdg_Unidad= it.cdg_Unidad,
                                unidad= it.unidad,
                                moneda_Lp= it.moneda_Lp,
                                cantidad = 0,
                                precioUnidad = 0.0,
                                precioTotal = 0.0
                            )
                        )
                    }
                    adapterProductoComercial.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onClickItemAddProducto(posicion: Int) {

    }

    fun filter(campo:String,text: String) {
        val filterdNameProducto: ArrayList<productlistcot> = ArrayList()

        println("campo : ${campo}")
        println("Cantidad : ${listaProductoCotizacion.size}")

        if (campo == "Nombre"){
            for (i in listaProductoCotizacion.indices) {
                if (listaProductoCotizacion[i].nombre.lowercase().contains(text.lowercase())) {

                    filterdNameProducto.add(listaProductoCotizacion[i])
                }
            }
        }
        if (campo == "Codigo"){
            for (i in listaProductoCotizacion.indices) {
                if (listaProductoCotizacion[i].codigo!!.lowercase().contains(text.lowercase())) {
                    filterdNameProducto.add(listaProductoCotizacion[i])
                }
            }
        }
        adapterProductoComercial.filterList(filterdNameProducto)
    }

    fun onItemDatosProductos(data: productlistcot) {
        Toast.makeText(this, "Se agrego el producto", Toast.LENGTH_SHORT).show()
        listaProductoListados.add(data.copy())
        productlistcotadarte.notifyItemChanged(listaProductoListados.size)
        calcularMontoTotal()
    }

    //************* FUNCIONES ADICIONALES  ****************
    fun calcularMontoTotal(){
        montoTotal = listaProductoListados.sumOf { it.precioTotal }
        igvTotal = utils().priceIGV(montoTotal)
        subtotal = utils().priceSubTotal(montoTotal)

        val tv_subtotalCot = binding.tvSubTotalAddCart
        val tv_igvCot = binding.tvIgvCotAddCart
        val tv_totalCot = binding.tvTotalCotAddCart

        tv_totalCot.text = "${tipomoneda} ${utils().pricetostringformat(montoTotal)}"
        tv_igvCot.text = "${tipomoneda} ${utils().pricetostringformat(igvTotal)}"
        tv_subtotalCot.text = "${tipomoneda} ${utils().pricetostringformat(subtotal)}"
    }
    //************* GUARDAR ROOM  ****************
    fun guardarListRoom(){

        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()

            if(listaProductoListados.size>0){
                listaProductoListados.forEach {
                    database.daoTblBasica().insertListProct(
                        EntityListProct(
                            0, it.id_Producto, it.codigo!!, it.codigo_Barra,
                            it.nombre,it.mon,it.precio_Venta,it.factor_Conversion,it.cdg_Unidad,it.unidad,
                            it.moneda_Lp,it.cantidad,it.precioUnidad,it.precioTotal,subtotal,igvTotal,montoTotal
                        )
                    )
                }
            }

            println(database.daoTblBasica().getAllListProct())
        }

    }
    override fun onBackPressed() {
        CoroutineScope(Dispatchers.IO).launch {
            if(listaProductoListados.isNotEmpty()){
                runOnUiThread {
                    cancelarCotizacion()
                }
            }else{
                runOnUiThread {
                    super.onBackPressed()
                }
            }
        }
    }
}