package com.unosoft.ecomercialapp.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.unosoft.ecomercialapp.*
import com.unosoft.ecomercialapp.api.*
import com.unosoft.ecomercialapp.databinding.FragmentHomeBinding
import com.unosoft.ecomercialapp.db.*
import com.unosoft.ecomercialapp.entity.ListaPrecio.ListaPrecioRespuesta
import com.unosoft.ecomercialapp.entity.TableBasic.*
import com.unosoft.ecomercialapp.entity.Vendedor.VendedorResponse
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}