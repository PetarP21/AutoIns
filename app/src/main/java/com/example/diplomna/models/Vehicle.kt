package com.example.diplomna.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle
    (val id : Int,
     val clientId: Int,
     val licencePlate: String,
     val VIN: String,
     val registrationCertificate: String,
     val engine: Int,
     val vehicleTypeId: Int,
     val brand: String,
     val model: String,
     val date: String,
     val price: Double,
     val validityId: Int) : Parcelable