package com.unosoft.ecomercialapp.ui.LoginPasscode

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.unosoft.ecomercialapp.Activity.inicio.InicioActivity
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ListaPrecio
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.TablaBasicaApi
import com.unosoft.ecomercialapp.api.VendedorApi
import com.unosoft.ecomercialapp.databinding.ActivityLoginPasscodeBinding
import com.unosoft.ecomercialapp.db.EntityCondicionPago
import com.unosoft.ecomercialapp.db.EntityDataLogin
import com.unosoft.ecomercialapp.db.EntityDepartamento
import com.unosoft.ecomercialapp.db.EntityDistrito
import com.unosoft.ecomercialapp.db.EntityDocIdentidad
import com.unosoft.ecomercialapp.db.EntityFrecuenciaDias
import com.unosoft.ecomercialapp.db.EntityListaPrecio
import com.unosoft.ecomercialapp.db.EntityMoneda
import com.unosoft.ecomercialapp.db.EntityProvincia
import com.unosoft.ecomercialapp.db.EntityUnidadMedida
import com.unosoft.ecomercialapp.db.EntityVendedor
import com.unosoft.ecomercialapp.entity.ListaPrecio.ListaPrecioRespuesta
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Login.LoginComercialResponse
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DepartamentoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DistritoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DocIdentidadResponse
import com.unosoft.ecomercialapp.entity.TableBasic.FrecuenciaDiasResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.ProvinciaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.UnidadMedidaResponse
import com.unosoft.ecomercialapp.entity.Vendedor.VendedorResponse
import com.unosoft.ecomercialapp.helpers.ErrorType
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActyLoginPasscode : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPasscodeBinding
    var apiInterface: LoginApi? = null
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
        binding = ActivityLoginPasscodeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val error = ErrorType.CONNECTIVITY_ERROR.message
        println(error)


        procesoDeLogueo()


    }

    private fun procesoDeLogueo() {
        apiInterface = APIClient.client!!.create(LoginApi::class.java)

        binding.tvUsurio.text = prefs.getUser().uppercase()

        val pd = ProgressDialog(this)
        pd.setMessage("Validando usuario....")
        pd.setCancelable(false)
        pd.create()

        binding.btnIngresar.setOnClickListener(View.OnClickListener {

            if (binding.etPass.text.toString().isEmpty()) {
                AlertMessage("Datos inválidos")
                return@OnClickListener
            } else {

                println(prefs.getUser())
                println(prefs.getURLBase())

                pd.show()

                //***********************  MANTENER   ****************************
                val _user = DCLoginUser(prefs.getUser(), binding.etPass.text.toString())
                val call1 = apiInterface!!.login(_user)
                call1.enqueue(object : Callback<LoginComercialResponse> {
                    override fun onResponse(call: Call<LoginComercialResponse>, response: Response<LoginComercialResponse>) {
                        if (response.code() == 400) {
                            AlertMessage("Usuario y/o Contraseña incorrecta")
                            pd.cancel()
                        } else {


                            val user1 = response.body()!!

                            CoroutineScope(Dispatchers.IO).launch {
                                database.daoTblBasica().deleteTableDataLogin()
                                database.daoTblBasica().clearPrimaryKeyDataLogin()

                                database.daoTblBasica().insertDataLogin(
                                    EntityDataLogin(
                                        0,
                                        user1.nombreusuario,
                                        user1.codigO_EMPRESA,
                                        user1.iD_CLIENTE,
                                        user1.poR_IGV,
                                        user1.cdgmoneda,
                                        user1.validez,
                                        user1.cdgpago,
                                        user1.sucursal,
                                        user1.usuarioautoriza,
                                        user1.usuariocreacion,
                                        user1.descuento,
                                        user1.seriepedido,
                                        user1.estadopedido,
                                        user1.tipocambio,
                                        user1.jwtToken,
                                        user1.facturA_ADELANTADA,
                                        user1.iD_COTIZACION,
                                        user1.puntO_VENTA,
                                        user1.redondeo,
                                        user1.cdG_VENDEDOR
                                    )
                                )

                                println("***************  IMPRIMIENDO DATOS USUARIO  *****************")
                                println(database.daoTblBasica().getAllDataLogin())

                                println("***************  LIMPIAR DATOS LISTA PRODUCTO Y CABEZERA  *****************")
                                database.daoTblBasica().deleteTableListProct()
                                database.daoTblBasica().clearPrimaryKeyListProct()
                                database.daoTblBasica().deleteTableDataCabezera()
                                database.daoTblBasica().clearPrimaryKeyDataCabezera()

                                runOnUiThread {

                                    println(user1.cdG_VENDEDOR)
                                    println(user1.tipocambio)

                                    prefs.save_CdgVendedor(user1.cdG_VENDEDOR)
                                    prefs.save_TipoCambio(user1.tipocambio.toString())
                                    prefs.save_IGV(user1.poR_IGV)
                                    prefs.save_Company(user1.nombre)

                                    val i = Intent(applicationContext, InicioActivity::class.java)
                                    startActivity(i)
                                    finish()

                                    pd.cancel()

                                    binding.etPass.setText("")

                                    // Toast.makeText(getApplicationContext(), user1.nombreusuario + " " + user1.jwtToken + " " + user1.poR_IGV + " " + user1.refreshToken, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginComercialResponse>, t: Throwable) {
                        AlertMessage("Error de Conexión: " + t.message)
                        call.cancel()
                    }
                })
                //*******  MANTENER ********
            }
        })
    }

    private fun cargarTablaBasica() {

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
                database.daoTblBasica().deleteTableCondicionPago()
                database.daoTblBasica().clearPrimaryKeyCondicionPago()
                database.daoTblBasica().deleteTableDepartamento()
                database.daoTblBasica().clearPrimaryKeyDepartamento()
                database.daoTblBasica().deleteTableDistrito()
                database.daoTblBasica().clearPrimaryKeyDistrito()
                database.daoTblBasica().deleteTableDocIdentidad()
                database.daoTblBasica().clearPrimaryKeyDocIdentidad()
                database.daoTblBasica().deleteTableFrecuenciaDias()
                database.daoTblBasica().clearPrimaryKeyFrecuenciaDias()
                database.daoTblBasica().deleteTableMoneda()
                database.daoTblBasica().clearPrimaryKeyMoneda()
                database.daoTblBasica().deleteTableProvincia()
                database.daoTblBasica().clearPrimaryKeyProvincia()
                database.daoTblBasica().deleteTableUnidadMedida()
                database.daoTblBasica().clearPrimaryKeyUnidadMedida()
                database.daoTblBasica().deleteTableListaPrecio()
                database.daoTblBasica().clearPrimaryKeyListaPrecio()
                database.daoTblBasica().deleteTableVendedor()
                database.daoTblBasica().clearPrimaryKeyVendedor()
                withContext(Dispatchers.IO)
                {
                    listaCondicionPagoResponse.forEach {
                        database.daoTblBasica().insertCondicionPago(
                            EntityCondicionPago(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaDepartamentoResponse.forEach {
                        database.daoTblBasica().insertDepartamento(
                            EntityDepartamento(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                    listaDistritoResponse.forEach {
                        database.daoTblBasica().insertDistrito(
                            EntityDistrito(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2,it.Referencia3)
                        )
                    }
                    listaDocIdentidadResponse.forEach {
                        database.daoTblBasica().insertDocIdentidad(
                            EntityDocIdentidad(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaFrecuenciaDiasResponse.forEach {
                        database.daoTblBasica().insertFrecuenciaDias(
                            EntityFrecuenciaDias(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                    listaMonedaResponse.forEach {
                        database.daoTblBasica().insertMoneda(
                            EntityMoneda(0,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    listaProvinciaResponse.forEach {
                        database.daoTblBasica().insertProvincia(
                            EntityProvincia(0,it.Codigo,it.Nombre,it.Numero,it.Referencia2)
                        )
                    }
                    listaUnidadMedidaResponse.forEach {
                        database.daoTblBasica().insertUnidadMedida(
                            EntityUnidadMedida(0,it.Codigo,it.Nombre,it.Numero,it.Referencia1)
                        )
                    }
                    ListaPrecioRespuesta.forEach {
                        database.daoTblBasica().insertListaPrecio(
                            EntityListaPrecio(0,it.codigo,it.nombre,it.moneda)
                        )
                    }
                    listVendedores.forEach {
                        database.daoTblBasica().insertVendedor(
                            EntityVendedor(0,it.Codigo,it.Nombre,it.Numero)
                        )
                    }
                }

                withContext(Dispatchers.IO) {
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllCondicionPago())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDepartamento())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDistrito())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllDocIdentidad())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllFrecuenciaDias())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllMoneda())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllProvincia())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllUnidadMedida())
                    println("**************  TABLA CONDICION DE PAGO ***************")
                    println(database.daoTblBasica().getAllListaPrecio())
                    println("**************  TABLA VENDEDOR ***************")
                    println(database.daoTblBasica().getAllVendedor())
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
            println("Valor ${!database.daoTblBasica().isExists()}")
            if(!database.daoTblBasica().isExists()) {
                runOnUiThread {
                    getDataRoom()
                    println("paso aqui")
                }
            }
        }
    }


    fun AlertMessage(mensaje: String?) {
        val builder = AlertDialog.Builder(this@ActyLoginPasscode)
        builder.setTitle("Información")
        builder.setMessage(mensaje)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which -> dialog.dismiss() }
        val dialogMessage = builder.create()
        dialogMessage.show()
    }
}