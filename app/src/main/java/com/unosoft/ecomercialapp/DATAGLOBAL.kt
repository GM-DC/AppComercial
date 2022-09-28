package com.unosoft.ecomercialapp

import android.app.Application
import androidx.room.Room
import com.example.apppedido.Prefs
import com.unosoft.ecomercialapp.db.TablaBasica
import com.unosoft.ecomercialapp.entity.TableBasic.UnidadMedidaResponse

class DATAGLOBAL: Application() {

    companion object{
        lateinit var prefs: Prefs
        lateinit var database: TablaBasica
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        database =  Room.databaseBuilder(this, TablaBasica::class.java, "database").fallbackToDestructiveMigration().build()
    }
}