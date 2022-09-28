package com.unosoft.ecomercialapp.ui.Cotizacion

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.unosoft.ecomercialapp.DATAGLOBAL
import com.unosoft.ecomercialapp.R
import com.unosoft.ecomercialapp.databinding.ActivityVisorPdfcotizacionBinding
import com.unosoft.ecomercialapp.helpers.VisorPDF


class VisorPDFCotizacion : AppCompatActivity() {
    private lateinit var binding: ActivityVisorPdfcotizacionBinding

    private var permission = 0
    private val requestPermisisonLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        permission = if (it){
            1
        }else{
            0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisorPdfcotizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //******************************************
        iniciarPDF()
        evento()
    }

    private fun iniciarPDF() {
        val datos = intent.getStringExtra("ID")
        val pdfView = findViewById<PDFView>(R.id.pdfView);
        val urlPdf = "${DATAGLOBAL.prefs.getURLBase()}api/Cotizacion/pdf/$datos"
        VisorPDF(pdfView).execute(urlPdf)
    }

    private fun evento() {
        binding.ivDescargar.setOnClickListener {
            requestPermisisonLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission==1){
                descargarPDF()
            }
        }
    }

    private fun descargarPDF() {

        val datos = intent.getStringExtra("ID")
        val urlPdf = "${DATAGLOBAL.prefs.getURLBase()}api/Cotizacion/pdf/$datos"

        try{
            val request = DownloadManager.Request(Uri.parse(urlPdf))
            request.setTitle(datos)
            request.setMimeType("application/pdf")
            request.allowScanningByMediaScanner()
            request.setAllowedOverMetered(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$datos.pdf")
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }catch (e: Exception){
            Toast.makeText(this, "Descarga Fallida", Toast.LENGTH_SHORT).show()
        }

    }

}