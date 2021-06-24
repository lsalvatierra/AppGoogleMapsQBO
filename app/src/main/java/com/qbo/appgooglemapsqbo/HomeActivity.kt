package com.qbo.appgooglemapsqbo

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.qbo.appgooglemapsqbo.commom.AppMensaje
import com.qbo.appgooglemapsqbo.commom.Constantes
import com.qbo.appgooglemapsqbo.commom.TipoMensaje
import com.qbo.appgooglemapsqbo.databinding.ActivityHomeBinding
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnmaps.setOnClickListener {
            if(verificarPermisoGPS()){
                    startActivity(
                        Intent(applicationContext,
                            MapsActivity::class.java)
                    )
            }else{
                solicitarPermisoGPS()
            }
        }
    }

    private fun verificarPermisoGPS(): Boolean{
        val result = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun solicitarPermisoGPS(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            Constantes.ID_REQUEST_PERMISSION_GPS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constantes.ID_REQUEST_PERMISSION_GPS){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(
                    Intent(
                        applicationContext,
                        MapsActivity::class.java
                    )
                )
            }else{
                AppMensaje.enviarMensaje(binding.root, getString(R.string.errorpermisogps), TipoMensaje.ERROR)
            }
        }
    }
}