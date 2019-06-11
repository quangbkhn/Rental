package com.example.packing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.eclipse.paho.android.service.MqttAndroidClient

class VehicleAdapter(var context:Context,var list:List<VehicleData>,var user:UserData):RecyclerView.Adapter<VehicleAdapter.BikeViewHolder>() {

    interface  Dialog{
        fun confirm(name:String,user:UserData)
    }
    interface CallBack{
        fun setCallBack(vehicle:VehicleData,user:UserData)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.list_hire,parent,false)
        return BikeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newList: List<VehicleData>){
        this.list = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BikeViewHolder, position: Int) {
        var item = holder.itemView
        var name = item.findViewById<TextView>(R.id.name_bike)
        name.text = list[position].name.toString()
        var dock = item.findViewById<TextView>(R.id.dock_bike)
        dock.text = list[position].position.toString()
        val layout = item.findViewById<LinearLayout>(R.id.onclicklistner)
        layout.setOnClickListener {
            (context as BIKE).confirm(list[position].position,user)
            (context as BIKE).setCallBack(list[position],user)
        }
    }

    class BikeViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){}
}