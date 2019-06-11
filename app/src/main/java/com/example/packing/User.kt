package com.example.packing

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_user.*
import java.net.InterfaceAddress
class User : AppCompatActivity() {

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val intent = this.intent
        val user:UserData = intent.getSerializableExtra("data") as UserData
        name.text = user.name
        room.text = user.room
        val adap = UserAdapter(this@User,user.list_vehicle)
        rv_hire.layoutManager = LinearLayoutManager(this@User,LinearLayoutManager.VERTICAL,false)
        rv_hire.adapter = adap

        btn_next.setOnClickListener {
            val inte = Intent(this@User,BIKE::class.java)
            inte.putExtra("datauser",user)
            startActivity(inte)
        }
    }
}
