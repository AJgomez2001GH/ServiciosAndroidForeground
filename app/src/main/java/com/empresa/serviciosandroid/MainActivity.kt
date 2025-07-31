package com.empresa.serviciosandroid

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Instancias de los objetos del XML
        val btnStartForegroundService = findViewById<Button>(R.id.btnStartForegroundService)
        val btnStopForegroundService = findViewById<Button>(R.id.btnStopForegroundService)

        //Para declarar el servicio Foreground
        val intentServiceForeground = Intent(this, MyCustomServiceForeground::class.java)

        //Para iniciar el servicio:
        btnStartForegroundService.setOnClickListener {
            println("***************Boton iniciar pulsado***************")
            ContextCompat.startForegroundService(this, intentServiceForeground)

            // Codigo para solicitar el permiso de notificaciones en tiempo de ejecución (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
                }
            }



        }


        //Para detener el servicio:
        btnStopForegroundService.setOnClickListener {
            println("***************Boton parar pulsado***************")
            stopService(intentServiceForeground)
        }
    }
}