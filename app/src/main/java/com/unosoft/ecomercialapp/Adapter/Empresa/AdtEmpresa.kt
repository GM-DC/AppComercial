package com.unosoft.ecomercialapp.Adapter.Empresa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.entity.Empresa.dcEmpresa
import com.unosoft.ecomercialapp.entity.ProductListCot.productlistcot

class AdtEmpresa (var data:ArrayList<dcEmpresa>, private val onClickListener: (dcEmpresa) -> Unit): RecyclerView.Adapter<AdtEmpresa.holderEmpresa>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderEmpresa {
        val layoutInflater = LayoutInflater.from(parent.context)
        return holderEmpresa(layoutInflater.inflate(R.layout.cardview_empresa,parent,false))
    }

    override fun onBindViewHolder(holder: holderEmpresa, position: Int) {
        holder.holderEmpresa(data[position],onClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class holderEmpresa(private val view: View): RecyclerView.ViewHolder(view){
        fun holderEmpresa (data: dcEmpresa,onClickListener: (dcEmpresa) -> Unit){

            val tv_empresas = view.findViewById<TextView>(R.id.tv_empresas)
            val tv_ruc = view.findViewById<TextView>(R.id.tv_ruc)
            val tv_usuario = view.findViewById<TextView>(R.id.tv_usuario)

            tv_empresas.text = data.nameEmpresa
            tv_ruc.text = "RUC: ${data.ruc}"
            tv_usuario.text = "Usuario: ${data.usuario}"

            itemView.setOnClickListener { onClickListener(data)  }
        }
    }

    fun filterList(nameItemBuscado: ArrayList<dcEmpresa>) {
        data = nameItemBuscado
        notifyDataSetChanged()
    }
}