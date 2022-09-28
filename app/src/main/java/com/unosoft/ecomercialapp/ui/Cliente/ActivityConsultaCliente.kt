package com.unosoft.ecomercialapp.ui.Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import com.unosoft.ecomercialapp.Adapter.Clientes.listclientesadapter
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.ClientApi
import com.unosoft.ecomercialapp.databinding.FragmentActivityConsultaClienteBinding
import com.unosoft.ecomercialapp.databinding.FragmentCotizacionBinding
import com.unosoft.ecomercialapp.entity.Cliente.ClientListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityConsultaCliente : Fragment() {

    private var _binding: FragmentActivityConsultaClienteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterCliente : listclientesadapter
    var apiInterface2: ClientApi? = null
    private val listaClient = ArrayList<ClientListResponse>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentActivityConsultaClienteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface2 = APIClient.client?.create(ClientApi::class.java)

        inciciarCliente()
        getDatacliente()
        buscarCliente()
    }

    private fun inciciarCliente() {
        binding.rvRecyclerClienteFragment.layoutManager= LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        adapterCliente = listclientesadapter(listaClient) { data -> onItemDatosClientes(data) }
        binding.rvRecyclerClienteFragment.adapter = adapterCliente
    }
    private fun getDatacliente() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface2!!.getAllClients()
            activity?.runOnUiThread{
                if(response.isSuccessful){
                    listaClient.clear()
                    listaClient.addAll(response.body()!!)

                    binding.llCargando.isVisible = false
                    binding.llContenedor.isVisible = true

                    adapterCliente.notifyDataSetChanged()
                }else{
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun onItemDatosClientes(data: ClientListResponse) {

    }

    private fun buscarCliente() {
        binding.svBuscadorClienteFragment.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                println("$newText")
                filterCliente(newText.toString())
                return false
            }
        })
    }


    fun filterCliente(text: String) {
        val filterdNamePlato: ArrayList<ClientListResponse> = ArrayList()
        for (i in listaClient.indices) {
            if (listaClient[i].nombre.lowercase().contains(text.lowercase())||listaClient[i].ruc.lowercase().contains(text.lowercase())) {
                filterdNamePlato.add(listaClient[i])
            }
        }
        adapterCliente.filterCliente(filterdNamePlato)
    }
    //******** FIN DE CLIENTE ******************

}