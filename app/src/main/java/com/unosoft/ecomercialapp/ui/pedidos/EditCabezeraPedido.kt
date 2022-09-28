package com.unosoft.ecomercialapp.ui.pedidos

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraPedidoBinding
import com.unosoft.ecomercialapp.db.EntityDataCabezera
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.DatosCabezeraCotizacion.datosCabezera
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCabezeraPedido : AppCompatActivity() {
    private lateinit var binding: ActivityEditCabezeraPedidoBinding
    //***************************************************************

    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    private val listaClient = ArrayList<ClientListResponse>()

    private var DatosCabezeraPedido : datosCabezera = datosCabezera()

    private lateinit var adapterCliente : listclientesadapter
    var apiInterface2: ClientApi? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCabezeraPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //*******************************************************************
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        iniciarDatos()
    }

    private fun iniciarDatos() {
        CoroutineScope(Dispatchers.IO).launch{

            println(" *********** EVALUANDO DATOS ***************  ")
            println(DATAGLOBAL.database.daoTblBasica().isExistsEntityDataCabezera())

            if (DATAGLOBAL.database.daoTblBasica().isExistsEntityDataCabezera()){
                DATAGLOBAL.database.daoTblBasica().getAllDataCabezera().forEach {
                    DatosCabezeraPedido.idCliente = it.idCliente
                    DatosCabezeraPedido.nombreCliente= it.nombreCliente
                    DatosCabezeraPedido.rucCliente= it.rucCliente
                    DatosCabezeraPedido.tipoMoneda= it.tipoMoneda
                    DatosCabezeraPedido.codMoneda= it.codMoneda
                    DatosCabezeraPedido.listPrecio= it.listPrecio
                    DatosCabezeraPedido.codListPrecio= it.codListPrecio
                    DatosCabezeraPedido.validesDias= it.validesDias
                    DatosCabezeraPedido.codValidesDias= it.codValidesDias
                    DatosCabezeraPedido.condicionPago= it.condicionPago
                    DatosCabezeraPedido.codCondicionPago= it.codCondicionPago
                    DatosCabezeraPedido.vendedor= it.vendedor
                    DatosCabezeraPedido.codVendedor= it.codVendedor
                }

                runOnUiThread{
                    binding.tvCliente.text = "Nombre: ${DatosCabezeraPedido.nombreCliente}"
                    binding.tvRuc.text = "RUC: ${DatosCabezeraPedido.rucCliente}"
                    eventsHanlder()
                }

            }else{
                runOnUiThread{
                    eventsHanlder()
                }
            }
        }
    }

    private fun eventsHanlder() {
        binding.ivDatosClientCabezera.setOnClickListener { listarClientes() }
        iniciarSpinnerMoneda()
        iniciarSpinnerListPrecio()
        iniciarSpinnerValidez()
        iniciarSpinnerCondicionPago()
        iniciarSpinnerVendedor()

        binding.btnCancelCabezeraPedido.setOnClickListener { cancelar() }
        binding.btnGuardarCabeceraPedido.setOnClickListener { guardarInfo() }

    }

    private fun cancelar() {
        CoroutineScope(Dispatchers.IO).launch{
            if(DATAGLOBAL.database.daoTblBasica().isExistsEntityDataCabezera()){
                val datos = DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0]
                if (DatosCabezeraPedido.idCliente == datos.idCliente &&
                    DatosCabezeraPedido.nombreCliente == datos.nombreCliente &&
                    DatosCabezeraPedido.rucCliente == datos.rucCliente &&
                    DatosCabezeraPedido.tipoMoneda == datos.tipoMoneda &&
                    DatosCabezeraPedido.codMoneda == datos.codMoneda &&
                    DatosCabezeraPedido.listPrecio == datos.listPrecio &&
                    DatosCabezeraPedido.codListPrecio == datos.codListPrecio &&
                    DatosCabezeraPedido.validesDias == datos.validesDias &&
                    DatosCabezeraPedido.codValidesDias == datos.codValidesDias &&
                    DatosCabezeraPedido.condicionPago == datos.condicionPago &&
                    DatosCabezeraPedido.codCondicionPago == datos.codCondicionPago &&
                    DatosCabezeraPedido.vendedor == datos.vendedor &&
                    DatosCabezeraPedido.codVendedor == datos.codVendedor){
                    val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    runOnUiThread {
                        alerDialogue()
                    }
                }
            }else{
                runOnUiThread {
                    val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                    super.onBackPressed()
                }
            }
        }
    }

    private fun alerDialogue() {
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
                DATAGLOBAL.database.daoTblBasica().deleteTableDataCabezera()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDataCabezera()
                DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()
                runOnUiThread {
                    val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        dialog.show()
    }

    private fun guardarInfo() {
        CoroutineScope(Dispatchers.IO).launch{

            if (DATAGLOBAL.database.daoTblBasica().isExistsEntityDataCabezera()){
                if(DATAGLOBAL.database.daoTblBasica().getAllDataCabezera()[0].codMoneda != DatosCabezeraPedido.codMoneda){
                    //************ LIMPIA DATOS LISTA PRODUCTO  *************
                    if (DATAGLOBAL.database.daoTblBasica().isExistsEntityProductList()){
                        DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
                        DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()
                        println("Limpio la tabla")
                    }
                }
            }

            DATAGLOBAL.database.daoTblBasica().deleteTableDataCabezera()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDataCabezera()

            DATAGLOBAL.database.daoTblBasica().insertDataCabezera(
                EntityDataCabezera(0,
                    DatosCabezeraPedido.idCliente,
                    DatosCabezeraPedido.nombreCliente,
                    DatosCabezeraPedido.rucCliente,
                    DatosCabezeraPedido.tipoMoneda,
                    DatosCabezeraPedido.codMoneda,
                    DatosCabezeraPedido.listPrecio,
                    DatosCabezeraPedido.codListPrecio,
                    DatosCabezeraPedido.validesDias,
                    DatosCabezeraPedido.codValidesDias,
                    DatosCabezeraPedido.condicionPago,
                    DatosCabezeraPedido.codCondicionPago,
                    DatosCabezeraPedido.vendedor,
                    DatosCabezeraPedido.codVendedor
                )
            )

            println("***********   DATA CABEZERA   ***************")
            println(DATAGLOBAL.database.daoTblBasica().getAllDataCabezera())

            runOnUiThread{
                if (DatosCabezeraPedido.nombreCliente == "" ||
                    DatosCabezeraPedido.rucCliente == "" ||
                    DatosCabezeraPedido.idCliente == ""){
                    Toast.makeText(this@EditCabezeraPedido, "Falta ingresar datos cliente", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


    //****************  SPINNER  ***********************
    private fun iniciarSpinnerVendedor() {
        val listVendedor = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosVendedor = DATAGLOBAL.database.daoTblBasica().getAllVendedor()
            runOnUiThread{
                datosVendedor.forEach {
                    listVendedor.add(it.Nombre)
                }
                val spVendedorasignadoCabezera = binding.spVendedorasignadoCabezera
                val AdaptadorVendedor = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listVendedor)
                spVendedorasignadoCabezera.adapter = AdaptadorVendedor

                if(DatosCabezeraPedido.vendedor != ""){
                    for (i in listVendedor.indices){
                        if (DatosCabezeraPedido.vendedor == listVendedor[i]){
                            spVendedorasignadoCabezera.setSelection(i)
                        }
                    }
                }

                spVendedorasignadoCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.vendedor = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosVendedor.forEach { if (it.Nombre == item){DatosCabezeraPedido.codVendedor = it.Numero} }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }
    private fun iniciarSpinnerCondicionPago() {
        val listCondicionPago = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosCondicionPago = DATAGLOBAL.database.daoTblBasica().getAllCondicionPago()
            runOnUiThread{
                datosCondicionPago.forEach {
                    listCondicionPago.add("${it.Nombre}")
                }
                val sp_ListCondicionPago = binding.spCondicionpagoCabezera
                val AdaptadorCondicionPago = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listCondicionPago)
                sp_ListCondicionPago.adapter = AdaptadorCondicionPago


                if(DatosCabezeraPedido.condicionPago != ""){
                    for (i in listCondicionPago.indices){
                        if (DatosCabezeraPedido.condicionPago == listCondicionPago[i]){
                            sp_ListCondicionPago.setSelection(i)
                        }
                    }
                }


                sp_ListCondicionPago.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.condicionPago = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosCondicionPago.forEach { if (it.Nombre == item){DatosCabezeraPedido.codCondicionPago = it.Numero} }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }


    }
    private fun iniciarSpinnerValidez() {
        val listFrecuenciaDia = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosFrecuenciaDia = DATAGLOBAL.database.daoTblBasica().getAllFrecuenciaDias()

            runOnUiThread{
                datosFrecuenciaDia.forEach {
                    listFrecuenciaDia.add("Dias habiles ${it.Nombre}")
                }

                val sp_ListFrecuenciaDia = binding.spValidezCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listFrecuenciaDia)
                sp_ListFrecuenciaDia.adapter = AdaptadorListPrecio


                if(DatosCabezeraPedido.validesDias != ""){
                    for (i in listFrecuenciaDia.indices){
                        if ("Dias habiles ${DatosCabezeraPedido.validesDias}" == listFrecuenciaDia[i]){
                            sp_ListFrecuenciaDia.setSelection(i)
                        }
                    }
                }


                sp_ListFrecuenciaDia.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            datosFrecuenciaDia.forEach {if ("Dias habiles ${it.Nombre}" == item){
                                DatosCabezeraPedido.codValidesDias = it.Codigo
                                DatosCabezeraPedido.validesDias = it.Nombre
                            } }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }
    private fun iniciarSpinnerListPrecio() {
        val listspListPrecio = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            val datosListaPrecio = DATAGLOBAL.database.daoTblBasica().getAllListaPrecio()
            runOnUiThread{
                datosListaPrecio.forEach {
                    listspListPrecio.add(it.nombre)
                }
                val sp_ListPrecioCabezera = binding.spListPrecioCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listspListPrecio)
                sp_ListPrecioCabezera.adapter = AdaptadorListPrecio

                if(DatosCabezeraPedido.listPrecio != ""){
                    for (i in listspListPrecio.indices){
                        if (DatosCabezeraPedido.listPrecio == listspListPrecio[i]){
                            sp_ListPrecioCabezera.setSelection(i)
                        }
                    }
                }

                sp_ListPrecioCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraPedido.listPrecio = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosListaPrecio.forEach { if (it.nombre == item){DatosCabezeraPedido.codListPrecio = it.codigo} }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

            }
        }



    }
    private fun iniciarSpinnerMoneda() {
        val listspMoneda = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {

            DATAGLOBAL.database.daoTblBasica().getAllMoneda().forEach {
                listaTipoMoneda.add(
                    MonedaResponse(it.Nombre,it.Numero,it.Referencia1)
                )
            }

            runOnUiThread{
                listaTipoMoneda.forEach { listspMoneda.add(it.Nombre) }

                val sp_filtroMonedaCabezera = binding.spMonedaCabezera
                val AdaptadorMoneda = ArrayAdapter(this@EditCabezeraPedido, android.R.layout.simple_spinner_item, listspMoneda)
                sp_filtroMonedaCabezera.adapter = AdaptadorMoneda

                if(DatosCabezeraPedido.tipoMoneda != ""){
                    for (i in listaTipoMoneda.indices){
                        if (DatosCabezeraPedido.codMoneda == listaTipoMoneda[i].Numero){
                        sp_filtroMonedaCabezera.setSelection(i)
                        }
                    }
                }

                sp_filtroMonedaCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                        val item: String = parent!!.getItemAtPosition(position).toString()

                        listaTipoMoneda.forEach { if(it.Nombre == item) {
                            DatosCabezeraPedido.codMoneda = it.Numero
                            DatosCabezeraPedido.tipoMoneda = it.Referencia1
                        } }

                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            }
        }
    }

    //************ CLIENTE ******************
    private fun listarClientes() {
        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this@EditCabezeraPedido)
        val vista = layoutInflater.inflate(R.layout.dialogue_cliente, null)
        vista.setBackgroundResource(R.color.transparent)

        //Pasar vista al buielder
        builder.setView(vista)
        //*********************************************
        val sv_buscadorCliente = vista.findViewById<SearchView>(R.id.sv_buscadorCliente)
        val iv_cerrarCliente = vista.findViewById<ImageView>(R.id.iv_cerrarCliente)
        val ll_cargando = vista.findViewById<LinearLayout>(R.id.ll_cargando)
        val ll_contenedor = vista.findViewById<LinearLayout>(R.id.ll_contenedor)

        val dialog = builder.create()

        dialog.window!!.setGravity(Gravity.TOP)
        //*********************************************


        fun onItemDatosClientes(data: ClientListResponse) {
            binding.tvCliente.text = "Nombre: ${data.nombre}"
            binding.tvRuc.text = "RUC: ${data.ruc}"

            DatosCabezeraPedido.nombreCliente = data.nombre
            DatosCabezeraPedido.rucCliente = data.ruc
            DatosCabezeraPedido.idCliente = data.idpersona

            dialog.hide()
            dialog.dismiss()
        }
        if (!isFinishing)
            dialog.show()

        val rv_buscarCliente = vista.findViewById<RecyclerView>(R.id.rv_buscarCliente)
        rv_buscarCliente?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        adapterCliente = listclientesadapter(listaClient) { data -> onItemDatosClientes(data) }
        rv_buscarCliente?.adapter = adapterCliente

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface2!!.getAllClients()
            runOnUiThread {
                if(response.isSuccessful){

                    ll_cargando.isVisible = false
                    ll_contenedor.isVisible = true

                    listaClient.clear()
                    listaClient.addAll(response.body()!!)
                    adapterCliente.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@EditCabezeraPedido, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        sv_buscadorCliente?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                println("$newText")
                filterCliente(newText.toString())
                return false
            }
        })
        iv_cerrarCliente.setOnClickListener {
            dialog.hide()
            dialog.cancel()
        }
    }
    fun filterCliente(text: String) {
        val filterdNamePlato: ArrayList<ClientListResponse> = ArrayList()
        for (i in listaClient.indices) {
            if (listaClient[i].nombre.lowercase().contains(text.lowercase())) {
                filterdNamePlato.add(listaClient[i])
            }
        }
        adapterCliente.filterCliente(filterdNamePlato)
    }
    //******** FIN DE CLIENTE ******************

    override fun onBackPressed() {
        val intent = Intent(this@EditCabezeraPedido, ActivityAddPedido::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}