package com.example.packing

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

const val URLCUSTOMER = "https://ebikes-rentalminihotels.herokuapp.com/api/customers"

class MainActivity : AppCompatActivity() {

    //    lateinit var user:UserData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val globalState = applicationContext as UtralAppliction
        val client = globalState.client
        val options = globalState.options
        connect(client, options)
        var list = getcustomer()
        Log.d("datamain", list.size.toString())
        button.setOnClickListener {
            var a = false
            var name: String = username.text.toString()
            var pass: String = password.text.toString()
            for (i in 0 until list.size) {
                if (name.equals(list.get(i).idcard) && pass.equals(list.get(i).pass)) {
                    var intent = Intent(this@MainActivity, User::class.java)
                    intent.putExtra("data", list.get(i))
                    startActivity(intent)
                    a = true
                }
            }
            if (!a) {

                Toast.makeText(this@MainActivity, "ban can nhap du va dung thong tin", Toast.LENGTH_LONG).show()
            }
            for (i in 0 until list.size) {
                Log.d("data", name.equals(list.get(i)).toString() + pass.equals(list.get(i)).toString())
                Log.d("data", pass)
            }
        }
    }

    private fun getcustomer(): List<UserData> {
        Toast.makeText(this@MainActivity, "Loading...", Toast.LENGTH_LONG).show()
        var listuser = mutableListOf<UserData>()
        var request = Volley.newRequestQueue(this@MainActivity)
        var jsonrequest =
            JsonObjectRequest(Request.Method.GET, URLCUSTOMER, null, object : Response.Listener<JSONObject> {
                override fun onResponse(response: JSONObject) {
                    Toast.makeText(this@MainActivity, "lay du lieu", Toast.LENGTH_LONG).show()

                    try {
                        var jsonarray = response.getJSONArray("data")
                        for (i in 0 until jsonarray.length()) {
                            var jsonarray1 = jsonarray.getJSONObject(i).getJSONArray("bikeServices")
                            var list = mutableListOf<VehicleData>()
                            for (j in 0 until jsonarray1.length()) {
                                list.add(
                                    VehicleData(
                                        jsonarray1.getJSONObject(j).getJSONObject("bike").getString("_id"),
                                        jsonarray1.getJSONObject(j).getJSONObject("bike").getString("name"),
                                        jsonarray1.getJSONObject(j).getJSONObject("bike").getString("bikeKey"),
                                        jsonarray1.getJSONObject(j).getJSONObject("bike").getBoolean("available"),
                                        jsonarray1.getJSONObject(j).getJSONObject("bike").getString("price"),
                                        "null"
                                    )
                                )

                            }
                            var user = UserData(
                                jsonarray.getJSONObject(i).getString("_id"),
                                jsonarray.getJSONObject(i).getString("name")
                                ,
                                jsonarray.getJSONObject(i).getString("idCardNumber")
                                ,
                                list,
                                jsonarray.getJSONObject(i).getString("password"),
                                jsonarray.getJSONObject(i).getJSONObject("roomServices").getJSONObject("room").getString(
                                    "roomNumber"
                                )
                            )
                            listuser.add(user)

                            Toast.makeText(
                                this@MainActivity,
                                jsonarray.getJSONObject(i).getString("name"),
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("data", jsonarray.getJSONObject(i).getString("name"))
                            Log.d("data", user.pass)

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(this@MainActivity, "fail json request", Toast.LENGTH_LONG).show()
                }

            })
        Log.d("datauser", listuser.size.toString())
        request.add(jsonrequest)
        return listuser

    }

    private fun connect(client: MqttAndroidClient, options: MqttConnectOptions) {
        val token = client.connect(options)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Toast.makeText(this@MainActivity, "MQTT Connected", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Toast.makeText(this@MainActivity, "MQTT Connect fail", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
