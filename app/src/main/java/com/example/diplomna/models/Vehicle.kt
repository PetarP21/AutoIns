package com.example.diplomna.models

class Vehicle
    (val id : Int,
     val clientId: Int,
     val licencePlate: String,
     val VIN: String,
     val registrationCertificate: String,
     val engine: Int,
     val type: VehicleTypes,
     val brand: String,
     val model: String,
     val date: String,
     val price: Double,
     val isValid: Boolean)