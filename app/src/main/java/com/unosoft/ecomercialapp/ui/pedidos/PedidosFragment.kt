package com.unosoft.ecomercialapp.ui.pedidos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Pedidos.listpedidosadapter
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.LoginApi
import com.unosoft.ecomercialapp.api.PedidoApi
import com.unosoft.ecomercialapp.databinding.FragmentPedidosBinding
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PedidosFragment : Fragment() {
    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    //************* INICIALIZACIONDE VARIABLES **************
    private lateinit var adapterPedidos: listpedidosadapter
    private val listapedidos = ArrayList<pedidosDto>()
    var apiInterface: PedidoApi? = null
    var apiInterface2: LoginApi? = null

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentPedidosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(PedidoApi::class.java) as PedidoApi
        apiInterface2 = APIClient.client?.create(LoginApi::class.java) as LoginApi

        initRecyclerView()
        buscarCotizacion()



        getDataPedido(prefs.getCdgVendedor())

        eventsHandlers()
    }

    private fun eventsHandlers() {
        _binding?.iconAgregarPedido?.setOnClickListener { addNewPedido() }
    }

    private fun addNewPedido() {
        CoroutineScope(Dispatchers.IO).launch {

            database.daoTblBasica().deleteTableListProct()
            database.daoTblBasica().clearPrimaryKeyListProct()
            database.daoTblBasica().deleteTableDataCabezera()
            database.daoTblBasica().clearPrimaryKeyDataCabezera()

            requireActivity().runOnUiThread {
                val intent = Intent(activity, ActivityAddPedido::class.java)
                startActivity(intent)
            }
        }
    }

    private fun buscarCotizacion() {
        var sv_buscadorPedido = view?.findViewById<androidx.appcompat.widget.SearchView>(R.id.sv_buscadorPedido)
        sv_buscadorPedido?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
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
        val filterdNamePedido: ArrayList<pedidosDto> = ArrayList()
        for (i in listapedidos.indices) {
            if (listapedidos[i].numero_Pedido.lowercase().contains(text.lowercase())) {
                filterdNamePedido.add(listapedidos[i])
            }
        }
        adapterPedidos.filterList(filterdNamePedido)
    }

    fun initRecyclerView() {
        val rv_pedidos = view?.findViewById<RecyclerView>(R.id.rv_recyclerpedidos)
        rv_pedidos?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterPedidos = listpedidosadapter(listapedidos) { dataclassPedido -> onItemDatosPedidos(dataclassPedido) }
        rv_pedidos?.adapter = adapterPedidos
    }

    fun onItemDatosPedidos(dataclassPedido: pedidosDto) {
        prefs.save_IdPedido(dataclassPedido.id_pedido.toString())
        //**********************************************************************
        val intent = Intent(activity, ActivityEditPedido::class.java)

        //ENVIAR DATOS
        val bundle = Bundle()
        bundle.putSerializable("DATOSPEDIDOS", dataclassPedido)
        intent.putExtras(bundle)


        println("********************************")
        println("********* DOCUMENTO ************")
        println("********************************")

        println(dataclassPedido)

        startActivity(intent)
    }

    private fun getDataPedido(cdg_ven:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getPedido("$cdg_ven")
            activity?.runOnUiThread {
                if(response.isSuccessful){

                    binding.llCargando.isVisible = false
                    binding.llContenedor.isVisible = true

                    listapedidos.clear()
                    listapedidos.addAll(response.body()!!)
                    adapterPedidos.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        getDataPedido(prefs.getCdgVendedor())
        super.onResume()
    }

}
