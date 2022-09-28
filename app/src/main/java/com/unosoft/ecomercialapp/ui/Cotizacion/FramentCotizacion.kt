package com.unosoft.ecomercialapp.ui.Cotizacion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Cotizaciones.listcotizacionesadapter
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ApiCotizacion
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.databinding.FragmentCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cotizacion.cotizacionesDto
import com.unosoft.ecomercialapp.ui.pedidos.ActivityAddPedido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FramentCotizacion : Fragment() {
    private var _binding: FragmentCotizacionBinding? = null
    private val binding get() = _binding!!

    //************* INICIALIZACIONDE VARIABLES **************
    private lateinit var adapterCotizaciones: listcotizacionesadapter
    private val listacotizaciones = ArrayList<cotizacionesDto>()
    var apiInterface: ApiCotizacion? = null
    var apiInterface2: LoginApi? = null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentCotizacionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(ApiCotizacion::class.java) as ApiCotizacion
        apiInterface2 = APIClient.client?.create(LoginApi::class.java) as LoginApi

        initRecyclerView()
        buscarCotizacion()

        getData(prefs.getCdgVendedor())

        eventsHandlers()

    }
    private fun eventsHandlers() {
        _binding?.iconAgregarCotizacion?.setOnClickListener { addNewQuatation() }
    }
    private fun addNewQuatation() {
        CoroutineScope(Dispatchers.IO).launch {
            DATAGLOBAL.database.daoTblBasica().deleteTableListProct()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyListProct()
            DATAGLOBAL.database.daoTblBasica().deleteTableDataCabezera()
            DATAGLOBAL.database.daoTblBasica().clearPrimaryKeyDataCabezera()
            requireActivity().runOnUiThread {
                val intent = Intent(activity, ActivityAddCotizacion::class.java)
                startActivity(intent)
            }
        }
    }
    private fun buscarCotizacion() {
        var sv_buscadorCotizacion = view?.findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_buscadorCotizacion)

        sv_buscadorCotizacion?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
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
    fun filter(text: String) {
        val filterdNamePlato: ArrayList<cotizacionesDto> = ArrayList()
        for (i in listacotizaciones.indices) {
            if (listacotizaciones[i].numero_Cotizacion.lowercase().contains(text.lowercase())) {
                filterdNamePlato.add(listacotizaciones[i])
            }
        }
        adapterCotizaciones.filterList(filterdNamePlato)
    }

    fun initRecyclerView() {
        val rv_cotizaciones = view?.findViewById<RecyclerView>(R.id.recyclerCotizaciones)
        rv_cotizaciones?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterCotizaciones = listcotizacionesadapter(listacotizaciones) { dataclassCotizacion -> onItemDatosCotizacion(dataclassCotizacion) }
        rv_cotizaciones?.adapter = adapterCotizaciones
    }
    private fun getData(cdg_ven:String){
        CoroutineScope(Dispatchers.IO).launch {
            val cotizaciones = apiInterface!!.fetchAllCotizaciones("$cdg_ven")
            activity?.runOnUiThread {

                if(cotizaciones.isSuccessful){
                    
                    binding.llCargando.isVisible = false
                    binding.llContenedor.isVisible = true

                    listacotizaciones.clear()
                    listacotizaciones.addAll(cotizaciones.body()!!)
                    adapterCotizaciones.notifyDataSetChanged()
                }
            }
        }
    }

    fun onItemDatosCotizacion(dataclassCotizacion: cotizacionesDto) {
        prefs.save_IdPedido(dataclassCotizacion.id_cotizacion.toString())
        val intent = Intent(activity, ActivityEditCotizacion::class.java)

        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putSerializable("DATOSCOTIZACION", dataclassCotizacion)
        intent.putExtras(bundle)

        startActivity(intent)
    }

    override fun onResume() {
        getData(prefs.getCdgVendedor())
        super.onResume()
    }
}