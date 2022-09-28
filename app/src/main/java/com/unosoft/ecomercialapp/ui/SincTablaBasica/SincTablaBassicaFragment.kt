package com.unosoft.ecomercialapp.ui.SincTablaBasica

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.MainActivity
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ListaPrecio
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.TablaBasicaApi
import com.unosoft.ecomercialapp.api.VendedorApi
import com.unosoft.ecomercialapp.apiInterface
import com.unosoft.ecomercialapp.databinding.FragmentSincTablaBassicaBinding
import com.unosoft.ecomercialapp.db.EntityCondicionPago
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
import com.unosoft.ecomercialapp.entity.TableBasic.CondicionPagoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DepartamentoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DistritoResponse
import com.unosoft.ecomercialapp.entity.TableBasic.DocIdentidadResponse
import com.unosoft.ecomercialapp.entity.TableBasic.FrecuenciaDiasResponse
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.ProvinciaResponse
import com.unosoft.ecomercialapp.entity.TableBasic.UnidadMedidaResponse
import com.unosoft.ecomercialapp.entity.Vendedor.VendedorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SincTablaBassicaFragment : Fragment() {

    private var _binding: FragmentSincTablaBassicaBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentSincTablaBassicaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client!!.create(LoginApi::class.java)
        apiInterface2 = APIClient.client!!.create(TablaBasicaApi::class.java)
        apiInterface3 = APIClient.client!!.create(ListaPrecio::class.java)
        apiInterface4 = APIClient.client!!.create(VendedorApi::class.java)

        sincronizarTablaBasica()

    }

    private fun redireccionarHome() {

        startActivity(
            Intent(activity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        )

    }

    private fun sincronizarTablaBasica() {
        val dialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE,)

        dialog.setTitleText("Actualizar tabla maestro")
        dialog.setContentText("Se sincronizara la tabla maestro ¿Desea continuar?")

        dialog.setConfirmText("SI").setConfirmButtonBackgroundColor(Color.parseColor("#013ADF"))
        dialog.setConfirmButtonTextColor(Color.parseColor("#ffffff"))

        dialog.setCancelText("NO").setCancelButtonBackgroundColor(Color.parseColor("#c8c8c8"))
        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
            requireActivity().onBackPressed()
            sDialog.cancel()
        }

        dialog.setConfirmClickListener { sDialog ->
            cargarTablaBasica()
            sDialog.cancel()
        }

        dialog.show()
    }

    private fun cargarTablaBasica() {
        val pd = ProgressDialog(activity)
        pd.setMessage("Sincronizando datos")
        pd.setCancelable(false)
        pd.create()
        pd.show()

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

                activity?.runOnUiThread {
                    println("*********************************************************")
                    println("********** EXITO DE CARGA DE TABLA BASICA  **************")
                    println("*********************************************************")
                    pd.cancel()
                    requireActivity().onBackPressed()
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

                activity?.runOnUiThread {
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
        getDataRoom()
    }


}