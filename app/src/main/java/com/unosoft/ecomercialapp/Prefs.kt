package com.example.apppedido

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Prefs (contexto:Context){

    val contexto = contexto

    val SHARE_DB = "Mydtb"

    val SHARE_CDGVENDEDOR = "CDGVENDEDOR"
    val SHARE_TIPOCAMBIO = "TIPOCAMBIO"
    val SHARE_IDPEDIDO = "IDPEDIDO"

    val SHARE_URLBASE = "URLBASE"
    val SHARE_USER = "USER"
    val SHARE_COMPANY = "COMPANY"

    val SHARE_PERMISOS = "PERMISOS"
    val SHARE_IGV = "IGV"


    val storege = contexto.getSharedPreferences(SHARE_DB,0)

    //*********    PRUEBA  ************
    /*
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val storege = EncryptedSharedPreferences.create(
        SHARE_DB,
        masterKeyAlias,
        contexto,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    */
    //*********************************

    //******************  GUARDAR DATOS  ****************************
    fun save_CdgVendedor(CDGVENDEDOR:String){
        storege.edit().putString(SHARE_CDGVENDEDOR,CDGVENDEDOR).apply()
    }
    fun save_TipoCambio(TIPOCAMBIO:String){
        storege.edit().putString(SHARE_TIPOCAMBIO,TIPOCAMBIO).apply()
    }
    fun save_IdPedido(IDPEDIDO:String){
        storege.edit().putString(SHARE_IDPEDIDO,IDPEDIDO).apply()
    }

    fun save_URLBase(URLBASE:String){
        storege.edit().putString(SHARE_URLBASE,URLBASE).apply()
    }
    fun save_User(USER:String){
        storege.edit().putString(SHARE_USER,USER).apply()
    }

    fun save_Permiso(PERMISOS:String){
        storege.edit().putString(SHARE_PERMISOS,PERMISOS).apply()
    }

    fun save_IGV(IGV:String){
        storege.edit().putString(SHARE_IGV,IGV).apply()
    }

    fun save_Company(COMPANY:String){
        storege.edit().putString(SHARE_COMPANY,COMPANY).apply()
    }

    //********************* CONSULTAR DATOS ************************

    fun getCdgVendedor(): String {
        return storege.getString(SHARE_CDGVENDEDOR,"")!!
    }
    fun getTipoCambio(): String {
        return storege.getString(SHARE_TIPOCAMBIO,"")!!
    }
    fun getIdPedido(): String{
        return storege.getString(SHARE_IDPEDIDO,"")!!
    }

    fun getURLBase(): String {
        return storege.getString(SHARE_URLBASE,"")!!
    }
    fun getUser(): String {
        return storege.getString(SHARE_USER,"")!!
    }

    fun getPermiso(): String {
        return storege.getString(SHARE_PERMISOS,"")!!
    }
    fun getIGV(): String {
        return storege.getString(SHARE_IGV,"")!!
    }

    fun getCompany(): String{
        return storege.getString(SHARE_COMPANY,"")!!
    }

    fun wipe(){
        storege.edit().clear().apply()
    }
}