package com.example.packing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(var context:Context,var list:List<VehicleData>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var a = LayoutInflater.from((parent.context)).inflate(R.layout.list_userve,parent,false)
        return  UserViewHolder(a)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val itemView = holder.itemView
        val name = itemView.findViewById<TextView>(R.id.name_bike)
        name.text = list[position].name
        val price = itemView.findViewById<TextView>(R.id.dock_bike)
        price.text = list[position].price
    }

    class UserViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){}
}