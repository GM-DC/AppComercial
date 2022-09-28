package com.unosoft.ecomercialapp.ui.Cotizacion

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.ActivityEditCabezeraBinding
import com.unosoft.ecomercialapp.db.EntityDataCabezera
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import com.unosoft.ecomercialapp.entity.DatosCabezeraCotizacion.datosCabezera
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.ui.pedidos.ActivityAddPedido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class EditCabezera : AppCompatActivity() {
    private lateinit var binding: ActivityEditCabezeraBinding
    //********************************************************************

    private val listaTipoMoneda = ArrayList<MonedaResponse>()
    private val listaClient = ArrayList<ClientListResponse>()

    private var DatosCabezeraCotizacion : datosCabezera = datosCabezera()

    private lateinit var adapterCliente : listclientesadapter
    var apiInterface2: ClientApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCabezeraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //***********************************
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        iniciarDatos()
    }

    private fun iniciarDatos() {
        CoroutineScope(Dispatchers.IO).launch{

            println(" *********** EVALUANDO DATOS ***************  ")
            println(database.daoTblBasica().isExistsEntityDataCabezera())

            if (database.daoTblBasica().isExistsEntityDataCabezera()){
                database.daoTblBasica().getAllDataCabezera().forEach {
                    DatosCabezeraCotizacion.idCliente = it.idCliente
                    DatosCabezeraCotizacion.nombreCliente= it.nombreCliente
                    DatosCabezeraCotizacion.rucCliente= it.rucCliente
                    DatosCabezeraCotizacion.tipoMoneda= it.tipoMoneda
                    DatosCabezeraCotizacion.codMoneda= it.codMoneda
                    DatosCabezeraCotizacion.listPrecio= it.listPrecio
                    DatosCabezeraCotizacion.codListPrecio= it.codListPrecio
                    DatosCabezeraCotizacion.validesDias= it.validesDias
                    DatosCabezeraCotizacion.codValidesDias= it.codValidesDias
                    DatosCabezeraCotizacion.condicionPago= it.condicionPago
                    DatosCabezeraCotizacion.codCondicionPago= it.codCondicionPago
                    DatosCabezeraCotizacion.vendedor= it.vendedor
                    DatosCabezeraCotizacion.codVendedor= it.codVendedor
                }

                runOnUiThread{
                    binding.tvCliente.text = "Nombre: ${DatosCabezeraCotizacion.nombreCliente}"
                    binding.tvRuc.text = "RUC: ${DatosCabezeraCotizacion.rucCliente}"
                    eventsHanlder()
                }

            }else{
                runOnUiThread{
                    eventsHanlder()
                }
            }
        }

    }


    //********** EVENTO DE CLICK ************
    fun eventsHanlder() {

        binding.ivDatosClientCabezera.setOnClickListener { listarClientes() }

        iniciarSpinnerMoneda()
        iniciarSpinnerListPrecio()
        iniciarSpinnerValidez()
        iniciarSpinnerCondicionPago()
        iniciarSpinnerVendedor()

        binding.btnGuardarCabeceraCot.setOnClickListener { guardarInfo() }
        binding.btnCancelCabezeraCot.setOnClickListener { cancelar() }
    }

    private fun cancelar() {
        CoroutineScope(Dispatchers.IO).launch{
            if(database.daoTblBasica().isExistsEntityDataCabezera()){
                val datos = database.daoTblBasica().getAllDataCabezera()[0]
                if (DatosCabezeraCotizacion.idCliente == datos.idCliente &&
                    DatosCabezeraCotizacion.nombreCliente == datos.nombreCliente &&
                    DatosCabezeraCotizacion.rucCliente == datos.rucCliente &&
                    DatosCabezeraCotizacion.tipoMoneda == datos.tipoMoneda &&
                    DatosCabezeraCotizacion.codMoneda == datos.codMoneda &&
                    DatosCabezeraCotizacion.listPrecio == datos.listPrecio &&
                    DatosCabezeraCotizacion.codListPrecio == datos.codListPrecio &&
                    DatosCabezeraCotizacion.validesDias == datos.validesDias &&
                    DatosCabezeraCotizacion.codValidesDias == datos.codValidesDias &&
                    DatosCabezeraCotizacion.condicionPago == datos.condicionPago &&
                    DatosCabezeraCotizacion.codCondicionPago == datos.codCondicionPago &&
                    DatosCabezeraCotizacion.vendedor == datos.vendedor &&
                    DatosCabezeraCotizacion.codVendedor == datos.codVendedor){
                    val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    runOnUiThread {
                        alerDialogue()
                    }
                }
            }else{
                runOnUiThread {
                    val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun alerDialogue(){
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
                database.daoTblBasica().deleteTableDataCabezera()
                database.daoTblBasica().clearPrimaryKeyDataCabezera()
                database.daoTblBasica().deleteTableListProct()
                database.daoTblBasica().clearPrimaryKeyListProct()
                runOnUiThread {
                    val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        dialog.show()
    }

    private fun guardarInfo() {

        CoroutineScope(Dispatchers.IO).launch{

            if (database.daoTblBasica().isExistsEntityDataCabezera()){
                if(database.daoTblBasica().getAllDataCabezera()[0].codMoneda != DatosCabezeraCotizacion.codMoneda){
                    //************ LIMPIA DATOS LISTA PRODUCTO  *************
                    if (database.daoTblBasica().isExistsEntityProductList()){
                            database.daoTblBasica().deleteTableListProct()
                            database.daoTblBasica().clearPrimaryKeyListProct()
                            println("Limpio la tabla")
                    }
                }
            }

            database.daoTblBasica().deleteTableDataCabezera()
            database.daoTblBasica().clearPrimaryKeyDataCabezera()

            database.daoTblBasica().insertDataCabezera(
                EntityDataCabezera(0,
                    DatosCabezeraCotizacion.idCliente,
                    DatosCabezeraCotizacion.nombreCliente,
                    DatosCabezeraCotizacion.rucCliente,
                    DatosCabezeraCotizacion.tipoMoneda,
                    DatosCabezeraCotizacion.codMoneda,
                    DatosCabezeraCotizacion.listPrecio,
                    DatosCabezeraCotizacion.codListPrecio,
                    DatosCabezeraCotizacion.validesDias,
                    DatosCabezeraCotizacion.codValidesDias,
                    DatosCabezeraCotizacion.condicionPago,
                    DatosCabezeraCotizacion.codCondicionPago,
                    DatosCabezeraCotizacion.vendedor,
                    DatosCabezeraCotizacion.codVendedor
                )
            )

            println("***********   DATA CABEZERA   ***************")
            println(database.daoTblBasica().getAllDataCabezera())

            runOnUiThread{
                if (DatosCabezeraCotizacion.nombreCliente == "" ||
                    DatosCabezeraCotizacion.rucCliente == "" ||
                    DatosCabezeraCotizacion.idCliente == ""){
                    Toast.makeText(this@EditCabezera, "Falta ingresar datos cliente", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    //****************  SPINNER  ***********************
    private fun iniciarSpinnerMoneda() {
        val listspMoneda = ArrayList<String>()

        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().getAllMoneda().forEach {
                listaTipoMoneda.add(
                    MonedaResponse(it.Nombre,it.Numero,it.Referencia1)
                )
            }

            runOnUiThread{
                listaTipoMoneda.forEach { listspMoneda.add(it.Nombre) }
                val sp_filtroMonedaCabezera = binding.spMonedaCabezera
                val AdaptadorMoneda = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listspMoneda)
                sp_filtroMonedaCabezera.adapter = AdaptadorMoneda


                if(DatosCabezeraCotizacion.tipoMoneda != ""){
                    for (i in listaTipoMoneda.indices){
                        if (DatosCabezeraCotizacion.codMoneda == listaTipoMoneda[i].Numero){
                            sp_filtroMonedaCabezera.setSelection(i)
                        }
                    }
                }


                sp_filtroMonedaCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                        val item: String = parent!!.getItemAtPosition(position).toString()

                        listaTipoMoneda.forEach { if(it.Nombre == item) {
                            DatosCabezeraCotizacion.codMoneda = it.Numero
                            DatosCabezeraCotizacion.tipoMoneda = it.Referencia1
                        } }

                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    private fun iniciarSpinnerVendedor() {
        val listVendedor = ArrayList<String>()
        CoroutineScope(Dispatchers.IO).launch {
            val datosVendedor = database.daoTblBasica().getAllVendedor()
            runOnUiThread{
                datosVendedor.forEach {
                    listVendedor.add("${it.Nombre}")
                }

                val spVendedorasignadoCabezera = binding.spVendedorasignadoCabezera
                val AdaptadorVendedor = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listVendedor)
                spVendedorasignadoCabezera.adapter = AdaptadorVendedor



                if(DatosCabezeraCotizacion.vendedor != ""){
                    for (i in listVendedor.indices){
                        if (DatosCabezeraCotizacion.vendedor == listVendedor[i]){
                            spVendedorasignadoCabezera.setSelection(i)
                        }
                    }
                }

                spVendedorasignadoCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.vendedor = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosVendedor.forEach { if (it.Nombre == item){DatosCabezeraCotizacion.codVendedor = it.Numero} }
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
                val AdaptadorCondicionPago = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listCondicionPago)
                sp_ListCondicionPago.adapter = AdaptadorCondicionPago


                if(DatosCabezeraCotizacion.condicionPago != ""){
                    for (i in listCondicionPago.indices){
                        if (DatosCabezeraCotizacion.condicionPago == listCondicionPago[i]){
                            sp_ListCondicionPago.setSelection(i)
                        }
                    }
                }


                sp_ListCondicionPago.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.condicionPago = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosCondicionPago.forEach { if (it.Nombre == item){DatosCabezeraCotizacion.codCondicionPago = it.Numero} }
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
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listFrecuenciaDia)
                sp_ListFrecuenciaDia.adapter = AdaptadorListPrecio


                if(DatosCabezeraCotizacion.validesDias != ""){
                    for (i in listFrecuenciaDia.indices){
                        if ("Dias habiles ${DatosCabezeraCotizacion.validesDias}" == listFrecuenciaDia[i]){
                            sp_ListFrecuenciaDia.setSelection(i)
                        }
                    }
                }


                sp_ListFrecuenciaDia.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        CoroutineScope(Dispatchers.IO).launch {
                            datosFrecuenciaDia.forEach {if ("Dias habiles ${it.Nombre}" == item){
                                DatosCabezeraCotizacion.codValidesDias = it.Codigo
                                DatosCabezeraCotizacion.validesDias = it.Nombre
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
            val datosListaPrecio = database.daoTblBasica().getAllListaPrecio()
            runOnUiThread{
                datosListaPrecio.forEach {listspListPrecio.add(it.nombre)}
                val sp_ListPrecioCabezera = binding.spListPrecioCabezera
                val AdaptadorListPrecio = ArrayAdapter(this@EditCabezera, android.R.layout.simple_spinner_item, listspListPrecio)
                sp_ListPrecioCabezera.adapter = AdaptadorListPrecio


                if(DatosCabezeraCotizacion.listPrecio != ""){
                    for (i in listspListPrecio.indices){
                        if (DatosCabezeraCotizacion.listPrecio == listspListPrecio[i]){
                            sp_ListPrecioCabezera.setSelection(i)
                        }
                    }
                }


                sp_ListPrecioCabezera.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        val item: String = parent!!.getItemAtPosition(position).toString()
                        DatosCabezeraCotizacion.listPrecio = item
                        CoroutineScope(Dispatchers.IO).launch {
                            datosListaPrecio.forEach { if (it.nombre == item){DatosCabezeraCotizacion.codListPrecio = it.codigo} }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }

            }
        }



    }
    //************ CLIENTE ******************
    private fun listarClientes() {

        //***********  Alerta de Dialogo  ************
        val builder = AlertDialog.Builder(this@EditCabezera)
        val vista = layoutInflater.inflate(R.layout.dialogue_cliente, null)
        vista.setBackgroundResource(R.color.transparent)

        //pasar vista al buielder
        builder.setView(vista)
        //*********************************************
        val sv_buscadorCliente = vista.findViewById<SearchView>(R.id.sv_buscadorCliente)
        val iv_cerrarCliente = vista.findViewById<ImageView>(R.id.iv_cerrarCliente)
        val ll_cargando = vista.findViewById<LinearLayout>(R.id.ll_cargando)
        val ll_contenedor = vista.findViewById<LinearLayout>(R.id.ll_contenedor)


        //Creamos dialogue
        val dialog = builder.create()

        dialog.window!!.setGravity(Gravity.TOP)
        //*********************************************


        fun onItemDatosClientes(data: ClientListResponse) {
            binding.tvCliente.text = "Nombre: ${data.nombre}"
            binding.tvRuc.text = "RUC: ${data.ruc}"

            DatosCabezeraCotizacion.nombreCliente = data.nombre
            DatosCabezeraCotizacion.rucCliente = data.ruc
            DatosCabezeraCotizacion.idCliente = data.idpersona

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
            runOnUiThread{
                if(response.isSuccessful){

                    ll_cargando.isVisible = false
                    ll_contenedor.isVisible = true

                    listaClient.clear()
                    listaClient.addAll(response.body()!!)
                    adapterCliente.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@EditCabezera, "Error", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(this@EditCabezera, ActivityAddCotizacion::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}


