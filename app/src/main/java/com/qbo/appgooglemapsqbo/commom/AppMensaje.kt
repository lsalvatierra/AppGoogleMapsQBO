package com.qbo.appgooglemapsqbo.commom

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.qbo.appgooglemapsqbo.R

object AppMensaje {

    fun enviarMensaje(vista: View, mensaje: String, tipo : TipoMensaje){
        val snackbar = Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG)
        val snackBarView: View = snackbar.view
        if(tipo == TipoMensaje.ERROR){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance,
                    R.color.snackbarerror))
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(MiApp.instance,
                    R.color.snackbarexito))
        }
        snackbar.show()
    }

    /*fun enviarMensajeSuccess(vista: View, mensajeError: String){
        val snackbar = Snackbar.make(vista, mensajeError, Snackbar.LENGTH_LONG)
        val snackBarView: View = snackbar.view

        snackbar.show()
    }*/
}