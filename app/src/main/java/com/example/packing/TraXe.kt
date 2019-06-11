package com.example.packing

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class TraXe : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        val globalState = applicationContext as UtralAppliction
        val client = globalState.client
        Helper.mqttSubscribe(client,"dock_man/rfid/bike/D1234")
        Helper.mqttSubscribe(client,"dock_man/respond/bike/D1234")
        Helper.mqttSubscribe(client,"dock_man/battery/bike/D1234")

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val globalState = applicationContext as UtralAppliction
        val client = globalState.client
        client.setCallback(object: MqttCallback{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun messageArrived(topic: String, message: MqttMessage?) {
                val dock = topic.split("/")
                Log.d("topic",dock[3])
                if(dock[1].equals("rfid")) {
                    Helper.request("https://ebikes-rentalminihotels.herokuapp.com/api/customers/hire",
                        dock[3],message.toString(),this@TraXe)
                    Helper.mqttPublish(client,"dock_man/request/bike/${dock[3]}","Q")
                }
                if(dock[1].equals("battery")){
                    val messageString = message.toString().split("/")
                    if(messageString[0].equals("tl")){
                        //thoi gian sac
                        Helper.request("https://ebikes-rentalminihotels.herokuapp.com/api/bikes/name/${dock[3]}",
                            messageString[1],this@TraXe)
                    }
                    if(messageString[0].equals("%")){
                        //dung luong pin
                        Helper.request("https://ebikes-rentalminihotels.herokuapp.com/api/bikes/name/${dock[3]}",this@TraXe,messageString[1]
                        )
                    }
                }
                if(dock[1].equals("respond")){
                    if(message.toString().equals("y")){

                        Helper.request("https://ebikes-rentalminihotels.herokuapp.com/api/bikes/name/${dock[3]}",
                            this@TraXe,"100"
                        )
                    }
                }
            }

            override fun connectionLost(cause: Throwable?) {
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
            }
        })
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
