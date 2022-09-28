package com.unosoft.ecomercialapp.ui.pedidomaster

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Login.DCLoginUser
import com.unosoft.ecomercialapp.entity.Pedidos.pedidosDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PedidoMaster : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pedido_master, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iniciarData()
    }

    @SuppressLint("SetTextI18n")
    private fun iniciarData() {

        val dataRecuver = arguments
        val datos:pedidosDto = dataRecuver?.getSerializable("DATOSPEDIDOS") as pedidosDto

        val tv_numeroPedido = view?.findViewById<TextView>(R.id.tv_numeroPedido)
        val tv_fec_orden = view?.findViewById<TextView>(R.id.tv_fec_orden)
        val tv_cliente = view?.findViewById<TextView>(R.id.tv_cliente)
        val tv_ruc = view?.findViewById<TextView>(R.id.tv_ruc)
        val tv_moneda = view?.findViewById<TextView>(R.id.tv_moneda)
        val TV_STD_DOC = view?.findViewById<TextView>(R.id.TV_STD_DOC)
        val tv_subtotal = view?.findViewById<TextView>(R.id.tv_subtotal)
        val tv_valorventa = view?.findViewById<TextView>(R.id.tv_valorventa)
        val tv_igv = view?.findViewById<TextView>(R.id.tv_igv)
        val tv_importe = view?.findViewById<TextView>(R.id.tv_importe)

        val ic_cotizacion = view?.findViewById<ImageView>(R.id.ic_cotizacion)
        val ic_productos = view?.findViewById<ImageView>(R.id.ic_productos)


        tv_numeroPedido?.text = "NUMERO: ${datos.numero_Pedido}"
        tv_fec_orden?.text = "Fecha Creacion: ${datos.fecha_pedido}"
        tv_cliente?.text = "Nombre Cliente: ${datos.persona}"
        tv_ruc?.text = "RUC: ${datos.ruc}"
        tv_moneda?.text = "Moneda ${datos.nom_moneda}"
        TV_STD_DOC?.text = "Consicion Pago: ${datos.documento}"

        tv_importe?.text = "S./ ${datos.importe_Total}"
        tv_igv?.text = "S./ ${datos.importe_igv}"
        tv_valorventa?.text = "S./ ${datos.valor_venta}"
        tv_subtotal?.text = "S./ ${datos.valor_venta}"


        ic_cotizacion?.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val Unidad = database.daoTblBasica().getAllUnidadMedida()

                activity?.runOnUiThread {
                    println("********** DATA DE TABLA UNIDAD DE MEDIDA **********************")
                    println("$Unidad")
                }
            }
        }

        ic_productos?.setOnClickListener {


        }


    }

}