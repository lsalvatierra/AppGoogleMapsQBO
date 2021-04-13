package com.qbo.appgooglemapsqbo

import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
                try {
                    startActivity(
                        Intent(
                            applicationContext,
                            MapsActivity::class.java
                        )
                    )
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else{
                solicitarPermisoGPS()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun verificarPermisoGPS(): Boolean{
        val result = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        var exito = false
        if (result == PackageManager.PERMISSION_GRANTED) exito = true
        return exito
    }

    private fun solicitarPermisoGPS(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )
    }
}