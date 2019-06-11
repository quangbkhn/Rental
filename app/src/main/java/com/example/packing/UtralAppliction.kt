package com.example.packing

import android.app.Application
import android.provider.Telephony.Carriers.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
const val SERVER = "tcp://postman.cloudmqtt.com"
const val PORT = 17482
const val USERNAME = "mliyedfs"
const val PASSWORD = "ivDJb1oRl8G2"
class UtralAppliction:Application() {
    lateinit var client: MqttAndroidClient
    lateinit var options: MqttConnectOptions
    override fun onCreate() {
        val clientId = MqttClient.generateClientId()
        options = MqttConnectOptions().also {
            it.userName = USERNAME
            it.password = PASSWORD.toCharArray()
            it.isAutomaticReconnect = true
        }
        client = MqttAndroidClient(this.applicationContext, "$SERVER:$PORT", clientId)
        super.onCreate()
    }


}