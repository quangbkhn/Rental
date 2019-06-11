package com.example.packing

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.UnsupportedEncodingException

object Helper {

    fun mqttPublish(client: MqttAndroidClient, topic: String, payload: String) {

        val encodedPayload: ByteArray
        try {
            encodedPayload = payload.toByteArray(Charsets.UTF_8)
            val message = MqttMessage(encodedPayload)
            client.publish(topic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun mqttSubscribe(client: MqttAndroidClient, topic: String) {
        val qos = 1
        val token = client.subscribe(topic, qos)
        token.actionCallback = object : IMqttActionListener {
            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.i("MQTT:SUBSCRIBE", "FAILED")
            }

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.i("MQTT:SUBSCRIBE", "SUCCESS")
            }
        }
    }
    fun request(link:String,vehicle:VehicleData,time:String,context:Context,user:UserData){
        val request = Volley.newRequestQueue(context)
        val stringRequest = object: StringRequest(Request.Method.PUT,link,object: Response.Listener<String>{
            override fun onResponse(response: String?) {
                Log.d("respond",response)
            }

        },Response.ErrorListener {

        }){
            override fun getParams(): MutableMap<String, String> {
                val param:MutableMap<String,String> = HashMap<String,String>()
                param["bikeName"] = vehicle.name
                param["startTime"]=time

                return param
            }
        }
        request.add(stringRequest)
        Log.e("request","da request")
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun request(link:String, dock:String, rfid:String, context:Context){
        val date = Calendar.getInstance().time
        val DATE_PORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val dateFormat = SimpleDateFormat(DATE_PORMAT_12)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val text = dateFormat.format(date)
        val request = Volley.newRequestQueue(context)
        val stringRequest = object: StringRequest(Request.Method.POST,link,object: Response.Listener<String>{
            override fun onResponse(response: String?) {
                Log.d("respond",response)
            }

        },Response.ErrorListener {

        }){
            override fun getParams(): MutableMap<String, String> {
                val param:MutableMap<String,String> = HashMap<String,String>()
                param["bikeKey"] = rfid
                param["endTime"]= text
                param["position"] = dock
                return param
            }
        }
        request.add(stringRequest)
        Log.e("request","da tra xe")
    }

    fun request(link:String, data:String, context:Context){
        val request = Volley.newRequestQueue(context)
        val stringRequest = object: StringRequest(Request.Method.PUT,link,object: Response.Listener<String>{
            override fun onResponse(response: String?) {
            }

        },Response.ErrorListener {

        }){
            override fun getParams(): MutableMap<String, String> {
                val param:MutableMap<String,String> = HashMap<String,String>()
                param["chargerTime"]= data
                return param
            }
        }
        request.add(stringRequest)
        Log.e("request","da request")
    }
    fun request(link:String, context:Context,data:String){
        val request = Volley.newRequestQueue(context)
        val stringRequest = object: StringRequest(Request.Method.PUT,link,object: Response.Listener<String>{
            override fun onResponse(response: String?) {
            }

        },Response.ErrorListener {

        }){
            override fun getParams(): MutableMap<String, String> {
                val param:MutableMap<String,String> = HashMap<String,String>()
                param["batteryAmount"]= data
                return param
            }
        }
        request.add(stringRequest)
        Log.e("request","da request")
    }
}