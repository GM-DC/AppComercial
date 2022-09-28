package com.unosoft.ecomercialapp.ui.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityAddPedidoBinding
import com.unosoft.ecomercialapp.databinding.ActySplashScreenBinding
import com.unosoft.ecomercialapp.ui.Empresas.ActySelectEmpresa
import com.unosoft.ecomercialapp.ui.LoginPasscode.ActyLoginPasscode

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val i = Intent(this, ActySelectEmpresa::class.java)
        startActivity(i)
        finish()

    }
}