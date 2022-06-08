package com.example.diplomna

import DatabaseHandler
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.diplomna.models.Employee
import com.example.diplomna.models.Vehicle
import com.example.diplomna.util.SHA256
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addDefaultAdmin()
        invalidateInsurance()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    private fun addDefaultAdmin(){
        val databaseHandler = DatabaseHandler(this)
        val nicknames = databaseHandler.getNicknames()
        val salt = SHA256.salt()
        val admin = Employee(0,"admin","admin","admin","Админ",salt,SHA256.encrypt(salt+"admin"))
        if(admin.nickname !in nicknames){
            databaseHandler.addEmployee(admin)
        }
    }

    private fun invalidateInsurance(){
        val db = DatabaseHandler(this)
        val vehicles = db.getAllVehicles(context = this)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        for(vehicle in vehicles){
            val insuranceDate = simpleDateFormat.parse(vehicle.date)
            val currentDate = Date()
            val diff: Long = currentDate.time - insuranceDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            if(days>365){
                db.updateVehicle(Vehicle(vehicle.id,vehicle.clientId,vehicle.licencePlate,
                    vehicle.VIN,vehicle.registrationCertificate,
                    vehicle.engine,vehicle.type,vehicle.brand,vehicle.model,
                    vehicle.date,vehicle.price,isValid = false))
            }
        }
    }
}