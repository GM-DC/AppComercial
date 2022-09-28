package com.unosoft.ecomercialapp.ui.Cotizacion

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.api.PDFApi
import com.unosoft.ecomercialapp.databinding.ActivityAddCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.DetCotizacion
import com.unosoft.ecomercialapp.entity.Cotizacion.EnviarCotizacion
import com.unosoft.ecomercialapp.helpers.utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddCotizacion : AppCompatActivity() {

    private lateinit var binding: ActivityAddCotizacionBinding

    var apiInterface3: PDFApi? = null
    var apiInterface2: ClientApi? = null
    var apiInterface: ApiCotizacion? = null

    var tipomoneda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //****************************************************************
        apiInterface3 = APIClient.client?.create(PDFApi::class.java)
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java)

        eventsHandlers()
        inicialDatos()
    }
    private fun eventsHandlers() {
        binding.ivDatosClientAddCot.setOnClickListener { editDateClient() }
        binding.ivProductoAddCot.setOnClickListener { addressCartQuotation() }
        binding.icObservacion.setOnClickListener { observacion() }
        binding.btnSaveCotizacion.setOnClickListener { enviarCotizacion() }
        binding.btnCancelCotizacion.setOnClickListener { cancelarCotizacion() }
    }

    private fun cancelarCotizacion() {
        CoroutineScope(Dispatchers.IO).launch{
            if(database.daoTblBasica().isExistsEntityDataCabezera() || database.daoTblBasica().isExistsEntityListProct()){
                runOnUiThread {
                    alerDialogue()
                }
            }else{
                runOnUiThread {
                    super.onBackPressed()
                }
            }
        }
    }

    private fun alerDialogue() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE,)

        dialog.setTitleText("Cancelar")
        dialog.setContentText("Si cancela, se perdera todo el progreso ¿Desea cancelar?")

        dialog.setConfirmText("SI").setConfirmButtonBackgroundColor(Color.parseColor("#013ADF"))
        dialog.setConfirmButtonTextColor(Color.parseColor("#ffffff"))

        dialog.setCancelText("NO").setCancelButtonBackgroundColor(Color.parseColor("#c8c8c8"))

        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
            sDialog.cancel()
        }

        dialog.setConfirmClickListener { sDialog ->
            this.onBackPressed()
            finish()
            sDialog.cancel()
        }

        dialog.show()
    }


    private fun inicialDatos() {

        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        binding.tvFecOrden.text = "Fecha y hora:"
        binding.tvCodCotizacion.text = "Numero: "
        binding.tvCliente.text = "Nombre Cliente "
        binding.tvRuc.text = "RUC: "
        binding.tvMoneda.text = "Moneda: "
        binding.tvCondicionPago.text = "Condición de Pago"

        CoroutineScope(Dispatchers.IO).launch {

            if(database.daoTblBasica().isExistsEntityDataCabezera()){

                val nombreCliente = database.daoTblBasica().getAllDataCabezera()[0].nombreCliente
                val rucCliente = database.daoTblBasica().getAllDataCabezera()[0].rucCliente
                val tipoMoneda = database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda
                val condicionPago = database.daoTblBasica().getAllDataCabezera()[0].condicionPago

                tipomoneda = tipoMoneda!!

                    runOnUiThread {

                        binding.tvFecOrden.text = "Fecha y hora: ${formatter.format(date)}"
                        binding.tvCodCotizacion.text = "Numero: "
                        binding.tvCliente.text = "Nombre Cliente ${nombreCliente}"
                        binding.tvRuc.text = "RUC: ${rucCliente}"
                        binding.tvMoneda.text = "Moneda: ${tipoMoneda}"
                        binding.tvCondicionPago.text = "Condición de Pago ${condicionPago}"

                        binding.tvSubTotalAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvValorVentaAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvValorIGVAddCotizacion.text = "${tipoMoneda} 0.0"
                        binding.tvImporteTotal.text = "${tipoMoneda} 0.0"
                    }

            }

            if(database.daoTblBasica().isExistsEntityProductList()){

                val montoSubTotal = database.daoTblBasica().getAllListProct()[0].montoSubTotal
                val montoTotalIGV = database.daoTblBasica().getAllListProct()[0].montoTotalIGV
                val montoTotal = database.daoTblBasica().getAllListProct()[0].montoTotal
                val tipoMoneda = database.daoTblBasica().getAllDataCabezera()[0].tipoMoneda


                runOnUiThread {

                    binding.tvSubTotalAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoSubTotal)}"
                    binding.tvValorVentaAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoSubTotal)}"
                    binding.tvValorIGVAddCotizacion.text = "${tipoMoneda} ${utils().pricetostringformat(montoTotalIGV)}"
                    binding.tvImporteTotal.text = "${tipoMoneda} ${utils().pricetostringformat(montoTotal)}"

                }
            }
        }
    }
    private fun editDateClient() {
        val intent = Intent(this, EditCabezera::class.java)
        startActivity(intent)
        finish()
    }
    private fun addressCartQuotation() {

        CoroutineScope(Dispatchers.IO).launch {

            val valor = database.daoTblBasica().isExistsEntityDataCabezera()
            println("******** VALORES ********")
            println("$valor")

            runOnUiThread {
               if (valor){
                    val intent = Intent(this@ActivityAddCotizacion, ActivityCardQuotation::class.java)
                    intent.putExtra("TIPOMONEDA",tipomoneda)
                    startActivity(intent)
                    finish()

               }else{
                    Toast.makeText(this@ActivityAddCotizacion, "Rellenar datos clientes", Toast.LENGTH_SHORT).show()
               }
            }
        }
    }
    private fun enviarCotizacion() {
        CoroutineScope(Dispatchers.IO).launch {
            var validarCabezera = database.daoTblBasica().isExistsEntityDataCabezera()
            var validarListProct = database.daoTblBasica().isExistsEntityListProct()
            val listaCotizado = ArrayList<DetCotizacion>()
            runOnUiThread{
                if(validarCabezera){
                    if (validarListProct){
                        CoroutineScope(Dispatchers.IO).launch {
                            val datosLista = database.daoTblBasica().getAllListProct()
                            val datosCabezera = database.daoTblBasica().getAllDataCabezera()[0]
                            val datoslogin = database.daoTblBasica().getAllDataLogin()[0]
                            var secuencia = 0

                            datosLista.forEach {
                                secuencia += 1
                                listaCotizado.add(DetCotizacion(
                                    0,
                                    it.id_Producto,
                                    it.cantidad,
                                    it.precioUnidad,
                                    0,
                                    it.precioUnidad*it.cantidad*1.18,
                                    it.precioUnidad*it.cantidad,
                                    "",
                                    secuencia,
                                    it.precio_Venta,
                                    "1",
                                    1,
                                    "1",
                                    0,
                                    0,
                                    "",
                                    "",
                                    "",
                                    it.cdg_Unidad,
                                    0,
                                    0,
                                    it.precioUnidad,
                                    it.cdg_Unidad,
                                    "${utils().fechaActual()}",
                                    "",
                                    "S",
                                    0,
                                    0,
                                    1,
                                    0,
                                    0,
                                    0,
                                    0
                                ))
                            }

                            val datosCotizacion = EnviarCotizacion(
                                iD_COTIZACION = datoslogin.iD_COTIZACION.toInt(),
                                numerO_COTIZACION="",
                                codigO_VENDEDOR=prefs.getCdgVendedor(),
                                codigO_VENDEDOR_ASIGNADO=datosCabezera.codVendedor!!,
                                codigO_CPAGO=datoslogin.cdgpago,
                                codigO_MONEDA=datosCabezera.codMoneda!!,
                                fechA_COTIZACION="${utils().fechaActual()}",
                                numerO_OCLIENTE="",
                                importE_STOT=datosLista[0].montoSubTotal,
                                importE_DESCUENTO=0,
                                valoR_VENTA=datosLista[0].montoSubTotal,
                                importE_IGV=datosLista[0].montoTotalIGV,
                                importE_TOTAL=datosLista[0].montoTotal,
                                porcentajE_DESCUENTO=0,
                                porcentajE_IGV=datoslogin.poR_IGV.toDouble(),
                                observacion=binding.tvObsCotizacion.text.toString(),
                                estado="",
                                iD_CLIENTE=datosCabezera.idCliente!!.toInt(),
                                iD_CLIENTE_FACTURA= datosCabezera.idCliente!!.toInt(),
                                importE_ISC=0,
                                contacto="",
                                emaiL_CONTACTO="",
                                usuariO_CREACION=datoslogin.usuariocreacion,
                                fechA_CREACION="${utils().fechaActual()}",
                                usuariO_MODIFICACION="",
                                fechA_MODIFICACION="${utils().fechaActual()}",
                                codigO_EMPRESA=datoslogin.codigO_EMPRESA,
                                codigO_SUCURSAL=datoslogin.sucursal,
                                tipO_FECHA_ENTREGA="",
                                diaS_ENTREGA=0,
                                fechA_ENTREGA="${utils().fechaActual()}",
                                usuariO_AUTORIZA=datoslogin.usuarioautoriza,
                                fechA_AUTORIZACION="${utils().fechaActual()}",
                                lugaR_ENTREGA="",
                                comision=0,
                                redondeo=datoslogin.redondeo,
                                validez=datoslogin.validez,
                                motivo="",
                                correlativo="",
                                centrO_COSTO="",
                                tipO_CAMBIO=datoslogin.tipocambio,
                                iD_COTIZACION_PARENT=0,
                                telefonos="",
                                sucursal=datoslogin.sucursal,
                                tipO_ENTREGA="",
                                diaS_ENTREGA2=0,
                                observacioN2="",
                                costo=0,
                                iD_OPORTUNIDAD=0,
                                motivO_PERDIDA="",
                                competencia="",
                                notA_PERDIDA="",
                                separaR_STOCK="",
                                tipO_DSCTO="",
                                iD_PROYECTO=0,
                                swT_VISADO="",
                                usuario=prefs.getCdgVendedor(),
                                noM_MONEDA="",
                                direccion="",
                                ruc=datosCabezera.rucCliente!!,
                                persona="",
                                tipO_CLIENTE="",
                                detCotizacion=listaCotizado
                            )

                            println("*******************************************************")
                            println("********     ${datosCabezera.rucCliente!!}      *******")
                            println("********************************************************")


                            val response = apiInterface!!.postCreateCotizacion(datosCotizacion)
                               runOnUiThread {
                                  if(response.isSuccessful){
                                     println("**********************************")
                                     println("********      EXITO        *******")
                                     println("**********************************")
                                     Toast.makeText(this@ActivityAddCotizacion, "Exito", Toast.LENGTH_SHORT).show()

                                      visualizarPDF(response.body()!!.iD_COTIZACION)

                                      binding.tvCodCotizacion.text = "N° Cotizacion: ${response.body()!!.iD_COTIZACION}"
                                      Toast.makeText(this@ActivityAddCotizacion, "${response.body()!!.iD_COTIZACION}", Toast.LENGTH_SHORT).show()

                                  }else{
                                     println("**********************************")
                                     println("********      ERROR        *******")
                                     println("**********************************")
                                     Toast.makeText(this@ActivityAddCotizacion, "ERROR", Toast.LENGTH_SHORT).show()
                                  }
                               }
                        }
                    }else{
                        Toast.makeText(this@ActivityAddCotizacion, "Falta ingresar productos", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@ActivityAddCotizacion, "Falta datos cliente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun visualizarPDF(idCotizacion:Int) {
        val intent = Intent(this, VisorPDFCotizacion::class.java)
        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putString("ID", "$idCotizacion")
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun observacion() {
        //***********  Alerta de Dialogo  ***********
        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_observacion)
        dialogue.show()
        //***********Declara elementos *****************
        var et_detalle = dialogue.findViewById<EditText>(R.id.et_detalle)
        val bt_guardarDetalle = dialogue.findViewById<Button>(R.id.bt_guardarDetalle)
        //*********** BOTON GUARDAR DEL DIALOGO ********
        bt_guardarDetalle.setOnClickListener {
            var detalle:String = et_detalle.text.toString()
            binding.tvObsCotizacion.text = detalle
            dialogue.hide()
        }
    }
    override fun onBackPressed() {
        clearTable()
        super.onBackPressed()
    }
    private fun clearTable() {
        CoroutineScope(Dispatchers.IO).launch {
            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()

            database.daoTblBasica().deleteTableDataCabezera()
            database.daoTblBasica().clearPrimaryKeyDataCabezera()

            println(database.daoTblBasica().getAllListProct())
        }
    }

}