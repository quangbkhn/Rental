package com.example.packing

import java.io.Serializable

data class VehicleData(var id:String,var name:String,var bikekey:String,var available:Boolean,var price:String,var position:String):Serializable {
}