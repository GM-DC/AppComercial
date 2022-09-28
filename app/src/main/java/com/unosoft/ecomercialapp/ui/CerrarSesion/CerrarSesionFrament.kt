package com.unosoft.ecomercialapp.ui.CerrarSesion

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.database
import com.unosoft.ecomercialapp.DATAGLOBAL.Companion.prefs
import com.unosoft.ecomercialapp.MainActivity
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityMainBinding
import com.unosoft.ecomercialapp.databinding.FragmentCerrarSesionFramentBinding
import com.unosoft.ecomercialapp.ui.Cotizacion.ActivityAddCotizacion
import com.unosoft.ecomercialapp.ui.Empresas.ActySelectEmpresa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CerrarSesionFrament : Fragment() {

    private var _binding: FragmentCerrarSesionFramentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentCerrarSesionFramentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialogueCerrar()
    }

    private fun dialogueCerrar() {

        val dialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE,)

        dialog.setTitleText(R.string.Cerrar_Sesión)
        dialog.setContentText("Se cerrara la sesión y eliminar los datos temporales ¿Desea continuar?")

        dialog.setConfirmText("SI").setConfirmButtonBackgroundColor(Color.parseColor("#013ADF"))
        dialog.setConfirmButtonTextColor(Color.parseColor("#ffffff"))

        dialog.setCancelText("NO").setCancelButtonBackgroundColor(Color.parseColor("#c8c8c8"))
        dialog.setCancelable(false)

        dialog.setCancelClickListener { sDialog -> // Showing simple toast message to user
                requireActivity().onBackPressed()
                sDialog.cancel()
            }
        dialog.setConfirmClickListener { sDialog ->
                cerrarSesion()
                sDialog.cancel()
            }
        dialog.show()

    }

    private fun cerrarSesion() {
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

            activity?.runOnUiThread {
                println("***************************************")
                println("*********** CERRAR SESION *************")
                println("***************************************")
                prefs.wipe()
                val intent = Intent(activity, ActySelectEmpresa::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().finish()
            }

        }
    }
}