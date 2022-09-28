package com.unosoft.ecomercialapp.helpers;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class VisorPDF extends AsyncTask<String,Void, InputStream> {

    PDFView pdfView;

    public VisorPDF(PDFView pdfView) {
        this.pdfView = pdfView;
    }

    @Override
    protected InputStream doInBackground(String... strings) {
        InputStream inputStream = null;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode()==200){
                System.out.println("EXITO");
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }
        }catch (IOException e){
            System.out.println(e);
            return null;
        }
        return  inputStream;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        pdfView.fromStream(inputStream).load();
        System.out.println("123");
    }
}
