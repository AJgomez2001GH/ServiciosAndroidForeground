package com.empresa.serviciosandroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

// Declaracion de la corrutina
private val serviceScope = CoroutineScope(Dispatchers.Default)

class MyCustomServiceForeground: Service() {

    private val CHANNEL_ID = "canal_servicio"

    override fun onCreate() {
        super.onCreate()

        //Verifica si estás en Android Oreo (API 26) o superior.
        //Crea un NotificationChannel con el ID que definiste.
        //IMPORTANTE: Sin este canal, la notificación no aparece y Android matará tu servicio.
        //Requerido en Android 8+ (Oreo) para que se muestren las notificaciones.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Canal Servicio",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    //Se ejecuta cada vez que alguien llama a startService() o ContextCompat.startForegroundService(), es decir cada vez que se llama a el servico
    //Crea una notificación asociada al canal.
    //Llama a startForeground():
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Se crea la notificacion
        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Servicio en primer plano")
            .setContentText("Servicio ejecutándose")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        startForeground(1, notification)

        // Aquí haces la tarea en background
        println("***************Servicio iniciado***************")
        Log.d("ServiceForeground", "***************Servicio iniciado***************")

        //Puedes agregar hilos o corrutinas para hacer ejecuciones indefinidas
        corrutina()

        //Le dice a Android que reanude automáticamente el servicio si lo mata (ideal para servicios importantes).
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.cancel() // Cancela todas las tareas de forma segura
        super.onDestroy()
    }



    override fun onBind(intent: Intent?): IBinder? = null
}



fun corrutina(){
    serviceScope.launch {
        while (true) {
            Log.d("Service", "Trabajando...")
            delay(3000) // Esta es cancelable, no lanza excepción fea
        }
    }
}