package com.example.packing

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_bike.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONException
import org.json.JSONObject

import android.icu.util.TimeZone


const val URLBIKE = "https://ebikes-rentalminihotels.herokuapp.com/api/bikes"
const val TOPIC = "dock_man/rfid/bike/"
class BIKE    : AppCompatActivity(), VehicleAdapter.Dialog,VehicleAdapter.CallBack {


    lateinit var client: MqttAndroidClient
    lateinit var adap: VehicleAdapter
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        val intent = this.intent
        val user = intent.getSerializableExtra("datauser") as UserData

        adap = VehicleAdapter(this@BIKE, listOf(), user)
        rv_bike.layoutManager = LinearLayoutManager(this@BIKE, LinearLayoutManager.VERTICAL, false)
        rv_bike.adapter = adap
        getbike()
        back.setOnClickListener {
            val i = Intent(this@BIKE,TraXe::class.java)
            this.startService(i)
        }
    }


    override fun setCallBack(vehicle: VehicleData,user:UserData) {
    val globalState = applicationContext as UtralAppliction
    val client = globalState.client
    val TOPIC1 = TOPIC+vehicle.position
    client.setCallback(object:MqttCallback{
        @RequiresApi(Build.VERSION_CODES.O)
        override fun messageArrived(topic: String, message: MqttMessage) {
            when(topic){
                TOPIC1 -> {
                    if (message.toString().equals(vehicle.bikekey)){
                        Helper.mqttPublish(client,"dock_man/request/bike/${vehicle.position}","P")
                        val date = Calendar.getInstance().time
                        val DATE_PORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                        val dateFormat = SimpleDateFormat(DATE_PORMAT_12)
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val text = dateFormat.format(date)
                        Toast.makeText(this@BIKE,text,Toast.LENGTH_LONG).show()
                        Helper.request("https://ebikes-rentalminihotels.herokuapp.com/api/customers/${user.id}/hire",vehicle,text,this@BIKE,user
                        )
                    }
                }

            }
        }

        override fun connectionLost(cause: Throwable?) {
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
        }

    })
    }
    override fun confirm(name: String, user: UserData) {
        val globalState = applicationContext as UtralAppliction
        val client = globalState.client
        val alertDialog = AlertDialog.Builder(this@BIKE)
        alertDialog.setTitle("Confirm!")
        alertDialog.setIcon(R.drawable.bike)
        alertDialog.setMessage("Would you want to hire this bike?")
        alertDialog.setPositiveButton("yes"
        ) { p0, p1 ->
            Toast.makeText(this@BIKE, "ban vau chon xe tai dock $name ", Toast.LENGTH_LONG).show()
            Helper.mqttPublish(client, "dock_man/request/bike/$name", "(")
            Helper.mqttSubscribe(client,"dock_man/respond/bike/$name")
            Helper.mqttSubscribe(client,"dock_man/rfid/bike/$name")
        }.show()
    }


    private fun getbike() {
        Toast.makeText(this@BIKE, "loading...", Toast.LENGTH_LONG).show()
        var listBike = mutableListOf<VehicleData>()
        var request = Volley.newRequestQueue(this@BIKE)
        var jsonRequest = JsonObjectRequest(Request.Method.GET, URLBIKE, null,
            Response.Listener<JSONObject> { response ->
                try {
                    var jsonarray = response.getJSONArray("data")
                    for (i in 0 until jsonarray.length()) {
                        var bike = VehicleData(
                            jsonarray.getJSONObject(i).getString("_id"),
                            jsonarray.getJSONObject(i).getString("name"),
                            jsonarray.getJSONObject(i).getString("bikeKey"),
                            jsonarray.getJSONObject(i).getBoolean("available"),
                            jsonarray.getJSONObject(i).getString("price"),
                            jsonarray.getJSONObject(i).getString("position")
                        )
                        if(bike.available.equals(true)){listBike.add(bike)}

                        Toast.makeText(this@BIKE, bike.name, Toast.LENGTH_LONG).show()
                        Log.d("databike", "name${bike.name}")
                        Log.d("databike", "dock${bike.bikekey}")

                    }
                    adap.updateData(listBike)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(this@BIKE, "loading bike data is fail", Toast.LENGTH_LONG).show() })
        Log.d("datatest", listBike.size.toString())
        request.add(jsonRequest)

    }

}
