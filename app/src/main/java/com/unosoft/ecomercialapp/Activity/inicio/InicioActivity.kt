package com.unosoft.ecomercialapp.Activity.inicio

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import com.unosoft.ecomercialapp.*
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.api.*
import com.unosoft.ecomercialapp.databinding.ActivityInicioBinding
import com.unosoft.ecomercialapp.db.*
import com.unosoft.ecomercialapp.entity.ListaPrecio.ListaPrecioRespuesta
import com.unosoft.ecomercialapp.entity.TableBasic.*
import com.unosoft.ecomercialapp.entity.Vendedor.VendedorResponse
import kotlinx.coroutines.*
import java.net.URI
import java.net.URL

class InicioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityInicioBinding

    var apiInterface: LogoApi? = null

    var apiInterface2: TablaBasicaApi? = null
    var apiInterface3: ListaPrecio? = null
    var apiInterface4: VendedorApi? = null

    private val listCondicionPago = ArrayList<CondicionPagoResponse>()
    private val listDepartamento = ArrayList<DepartamentoResponse>()
    private val listDistrito = ArrayList<DistritoResponse>()
    private val listDocIdentidad = ArrayList<DocIdentidadResponse>()
    private val listFrecuenciaDias = ArrayList<FrecuenciaDiasResponse>()
    private val listMoneda = ArrayList<MonedaResponse>()
    private val listProvincia = ArrayList<ProvinciaResponse>()
    private val listUnidadMedida = ArrayList<UnidadMedidaResponse>()
    private val listPrecio = ArrayList<ListaPrecioRespuesta>()
    private val listVendedores = ArrayList<VendedorResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiInterface = APIClient.client?.create(LogoApi::class.java) as LogoApi

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarInicio.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        iniciarDrawerLayout()

        cargarTablaBasica()

        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        appBarConfiguration = AppBarConfiguration(setOf( R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    private fun cargarTablaBasica() {

        val pd = ProgressDialog(this)
        pd.show()
        pd.setContentView(R.layout.dialogue_progress)
        pd.setCancelable(false)
        pd.window!!.setBackgroundDrawableResource(R.color.transparent)

        apiInterface2 = APIClient.client!!.create(TablaBasicaApi::class.java)
        apiInterface3 = APIClient.client!!.create(ListaPrecio::class.java)
        apiInterface4 = APIClient.client!!.create(VendedorApi::class.java)

        fun inyectarDataRoom(
            listaCondicionPagoResponse: ArrayList<CondicionPagoResponse>,
            listaDepartamentoResponse: ArrayList<DepartamentoResponse>,
            listaDistritoResponse: ArrayList<DistritoResponse>,
            listaDocIdentidadResponse: ArrayList<DocIdentidadResponse>,
            listaFrecuenciaDiasResponse: ArrayList<FrecuenciaDiasResponse>,
            listaMonedaResponse: ArrayList<MonedaResponse>,
            listaProvinciaResponse: ArrayList<ProvinciaResponse>,
            listaUnidadMedidaResponse: ArrayList<UnidadMedidaResponse>,
            ListaPrecioRespuesta : ArrayList<ListaPrecioRespuesta>,
            listVendedores: ArrayList<VendedorResponse>
        ) {
            GlobalScope.launch(Dispatchers.Default) {
                DATAGLOBAL.database.daoTblBasica().deleteTableCondicionPago()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyCondicionPago()
                DATAGLOBAL.database.daoTblBasica().deleteTableDepartamento()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDepartamento()
                DATAGLOBAL.database.daoTblBasica().deleteTableDistrito()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDistrito()
                DATAGLOBAL.database.daoTblBasica().deleteTableDocIdentidad()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDocIdentidad()
                DATAGLOBAL.database.daoTblBasica().deleteTableFrecuenciaDias()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyFrecuenciaDias()
                DATAGLOBAL.database.daoTblBasica().deleteTableMoneda()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyMoneda()
                DATAGLOBAL.database.daoTblBasica().deleteTableProvincia()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyProvincia()
                DATAGLOBAL.database.daoTblBasica().deleteTableUnidadMedida()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyUnidadMedida()
                DATAGLOBAL.database.daoTblBasica().deleteTableListaPrecio()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListaPrecio()
                DATAGLOBAL.database.daoTblBasica().deleteTableVendedor()
                DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyVendedor()
                withContext(Dispatchers.IO)
                {
                    listaCondicionPagoResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertCondicionPago(
                            EntityCondicionPago(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaDepartamentoResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertDepartamento(
                            EntityDepartamento(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                    listaDistritoResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertDistrito(
                            EntityDistrito(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2,it.Referencia3)
                        )
                    }
                    listaDocIdentidadResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertDocIdentidad(
                            EntityDocIdentidad(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaFrecuenciaDiasResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertFrecuenciaDias(
                            EntityFrecuenciaDias(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                    listaMonedaResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertMoneda(
                            EntityMoneda(0,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaProvinciaResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertProvincia(
                            EntityProvincia(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2)
                        )
                    }
                    listaUnidadMedidaResponse.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertUnidadMedida(
                            EntityUnidadMedida(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    ListaPrecioRespuesta.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertListaPrecio(
                            EntityListaPrecio(0,it.codigo,it.nombre,it.moneda)
                        )
                    }
                    listVendedores.forEach {
                        DATAGLOBAL.database.daoTblBasica().insertVendedor(
                            EntityVendedor(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                }

                runOnUiThread {
                    pd.cancel()
                }
            }
        }

        fun getDataRoom(){
            CoroutineScope(Dispatchers.IO).launch {

                val response1 = apiInterface2!!.getCondicionPago()
                val response2 = apiInterface2!!.getDepartamento()
                val response3 = apiInterface2!!.getDistrito()
                val response4 = apiInterface2!!.getDocIdentidad()
                val response5 = apiInterface2!!.getFrecuenciaDias()
                val response6 = apiInterface2!!.getMoneda()
                val response7 = apiInterface2!!.getProvincia()
                val response8 = apiInterface2!!.getUnidadMedida()
                val response9 = apiInterface3!!.getListaPrecio()
                val response10 = apiInterface4!!.getListaVendedor()

                runOnUiThread {
                    if(response1.isSuccessful){
                        listCondicionPago.addAll(response1.body()!!)
                    }
                    if(response2.isSuccessful){
                        listDepartamento.addAll(response2.body()!!)
                    }
                    if(response3.isSuccessful){
                        listDistrito.addAll(response3.body()!!)
                    }
                    if(response4.isSuccessful){
                        listDocIdentidad.addAll(response4.body()!!)
                    }
                    if(response5.isSuccessful){
                        listFrecuenciaDias.addAll(response5.body()!!)
                    }
                    if(response6.isSuccessful){
                        listMoneda.addAll(response6.body()!!)
                    }
                    if(response7.isSuccessful){
                        listProvincia.addAll(response7.body()!!)
                    }
                    if(response8.isSuccessful){
                        listUnidadMedida.addAll(response8.body()!!)
                    }
                    if(response9.isSuccessful){
                        listPrecio.addAll(response9.body()!!)
                    }
                    if(response10.isSuccessful){
                        listVendedores.addAll(response10.body()!!)
                    }


                    inyectarDataRoom(
                        listCondicionPago,
                        listDepartamento,
                        listDistrito,
                        listDocIdentidad,
                        listFrecuenciaDias,
                        listMoneda,
                        listProvincia,
                        listUnidadMedida,
                        listPrecio,
                        listVendedores
                    )
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            println("Valor ${!DATAGLOBAL.database.daoTblBasica().isExists()}")
            if(!DATAGLOBAL.database.daoTblBasica().isExists()) {
                runOnUiThread {
                    getDataRoom()
                    println("paso aqui")
                }
            }else{
                pd.cancel()
            }
        }
    }


    private fun iniciarDrawerLayout() {
        val navView: NavigationView = binding.navView

        val headerView = navView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tv_usuarioDra)
        val tvNameEmpresa = headerView.findViewById<TextView>(R.id.tv_nameEmpresaDra)
        val ivlogo = headerView.findViewById<ImageView>(R.id.iv_logoDra)

        CoroutineScope(Dispatchers.IO).launch {

            tvUserName.text = prefs.getUser()
            tvNameEmpresa.text = prefs.getCompany()


            runOnUiThread {

                    tvUserName.text = prefs.getUser()
                    tvNameEmpresa.text = prefs.getCompany()

                    val str = "${prefs.getURLBase()}api/Company/LogoEmpresa"
                    Picasso.get().load(str)
                        .resize(80, 80)
                        .error(R.drawable.image_not_found)
                        .into(ivlogo)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.inicio, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_inicio)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}