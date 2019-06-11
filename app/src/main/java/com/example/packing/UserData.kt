package com.example.packing

import java.io.Serializable

data class UserData(var id:String,var name:String,var idcard:String,var list_vehicle:List<VehicleData>,var pass:String,var room:String):Serializable {
}