package com.unosoft.ecomercialapp.ui.ConsultaStock

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.Adapter.Stocks.listconsultastocks
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.api.APIClient
import com.unosoft.ecomercialapp.api.StocksApi
import com.unosoft.ecomercialapp.entity.Stocks.Almacen
import com.unosoft.ecomercialapp.entity.Stocks.ConsultaStocksResponseItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GalleryFragment : Fragment() {

    private val listaConsultaStocks = ArrayList<ConsultaStocksResponseItem>()
    private lateinit var adapterStocks: listconsultastocks
    var apiInterface: StocksApi? = null
    var itemSelect: String? = null


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_stocks, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        apiInterface = APIClient.client?.create(StocksApi::class.java) as StocksApi



        initStocks()
        buscaStock()
        busquedaSpinner()
    }


    private fun busquedaSpinner() {
        val sp_filtro = view?.findViewById<Spinner>(R.id.sp_filtro)
        val et_filtro = view?.findViewById<EditText>(R.id.et_filtro)
        val bt_buscar = view?.findViewById<Button>(R.id.bt_buscar)


        val lista = listOf("Nombre", "Codigo de barra", "Codigo de referente")
        val Adaptador = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, lista)

        sp_filtro?.adapter = Adaptador

        sp_filtro?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemSelect = lista[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val llCargando = requireView().findViewById<LinearLayout>(R.id.ll_cargando)
        llCargando.isVisible = false

        bt_buscar?.setOnClickListener {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            llCargando.isVisible = true

            val filtro = et_filtro?.text.toString()

            if (itemSelect == "Nombre") {
                getDataForName(filtro)
            }
            if (itemSelect == "Codigo de barra") {
                getDataForCdgBarra(filtro)
            }
            if (itemSelect == "Codigo de referente") {
                getDataForCdgRef(filtro)
            }
        }

    }

    fun initStocks() {
        val rv_cunsultaStocks = view?.findViewById<RecyclerView>(R.id.rv_cunsultaStocks)
        rv_cunsultaStocks?.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapterStocks = listconsultastocks(listaConsultaStocks)
        rv_cunsultaStocks?.adapter = adapterStocks
    }

    fun getDataForName(nomeProducto: String) {
        val pd = ProgressDialog(activity)
        pd.setMessage("Cargando....")
        pd.setCancelable(false)
        pd.create()
        pd.show()

        val llContenedor = requireView().findViewById<LinearLayout>(R.id.ll_contenedor)
        val llCargando = requireView().findViewById<LinearLayout>(R.id.ll_cargando)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getStockForName("$nomeProducto")
            activity?.runOnUiThread {
                if (response.isSuccessful) {

                    llContenedor.isVisible = true
                    llCargando.isVisible = false

                    listaConsultaStocks.clear()
                    listaConsultaStocks.addAll(response.body()!!)
                    pd.cancel()
                    adapterStocks.notifyDataSetChanged()
                } else {
                    pd.cancel()
                    println("Error en la conaulta ")
                }
            }
        }
    }

    fun getDataForCdgBarra(cdgBarra: String) {
        val pd = ProgressDialog(activity)
        pd.setMessage("Cargando....")
        pd.setCancelable(false)
        pd.create()
        pd.show()

        val llContenedor = requireView().findViewById<LinearLayout>(R.id.ll_contenedor)
        val llCargando = requireView().findViewById<LinearLayout>(R.id.ll_cargando)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getStockForCdgBarra("$cdgBarra")
            activity?.runOnUiThread {
                if (response.isSuccessful) {

                    llContenedor.isVisible = true
                    llCargando.isVisible = false

                    listaConsultaStocks.clear()
                    listaConsultaStocks.addAll(response.body()!!)
                    pd.cancel()
                    adapterStocks.notifyDataSetChanged()
                } else {
                    pd.cancel()
                    println("Error en la conaulta ")
                }
            }
        }
    }

    fun getDataForCdgRef(CdgRef: String) {
        val pd = ProgressDialog(activity)
        pd.setMessage("Cargando....")
        pd.setCancelable(false)
        pd.create()
        pd.show()

        val llContenedor = requireView().findViewById<LinearLayout>(R.id.ll_contenedor)
        val llCargando = requireView().findViewById<LinearLayout>(R.id.ll_cargando)

        CoroutineScope(Dispatchers.IO).launch {
            val response = apiInterface!!.getStockForCdgRef("$CdgRef")
            activity?.runOnUiThread {
                if (response.isSuccessful) {

                    llContenedor.isVisible = true
                    llCargando.isVisible = false


                    listaConsultaStocks.clear()
                    listaConsultaStocks.addAll(response.body()!!)
                    pd.cancel()
                    adapterStocks.notifyDataSetChanged()
                } else {
                    pd.cancel()
                    println("Error en la conaulta ")
                }
            }
        }
    }

    fun buscaStock() {
        val sv_buscador = view?.findViewById<SearchView>(R.id.sv_consultastocks)
        sv_buscador?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val filterdNomeProducto: ArrayList<ConsultaStocksResponseItem> = ArrayList()
        for (i in listaConsultaStocks.indices) {
            if (listaConsultaStocks[i].producto!!.lowercase().contains(text.lowercase())) {
                filterdNomeProducto.add(listaConsultaStocks[i])
            }
        }
        adapterStocks.filterList(filterdNomeProducto)
    }


}