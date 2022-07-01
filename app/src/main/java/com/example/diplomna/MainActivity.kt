package com.example.diplomna

import DatabaseHandler
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.diplomna.models.*
import com.example.diplomna.util.SHA256
import com.example.diplomna.util.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addPositions()
        addVehicleTypes()
        addSecurityQuestions()
        addValidityOptions()
        addDefaultAdmin()
        invalidateInsurance()
    }

    override fun onStop() {
        super.onStop()
        val isRemembered = SharedPref.readBoolean("CHECKBOX")
        if(!isRemembered){
            SharedPref.clear()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    private fun addDefaultAdmin(){
        val databaseHandler = DatabaseHandler(this)
        val nicknames = databaseHandler.getNicknames()
        val salt = SHA256.salt()
        val admin = Employee(0,"admin","admin","admin","admin","admin",databaseHandler.getIdByPosition("Админ"),
            databaseHandler.getIdBySecurityQuestion(getString(SecurityQuestions.JOB.id)),SHA256.encrypt(salt+"admin"),salt,SHA256.encrypt(salt+"admin"))
        if(admin.nickname !in nicknames){
            databaseHandler.addEmployee(admin)
        }
    }

    private fun addPositions(){
        val databaseHandler = DatabaseHandler(this)
        val positionsDb = databaseHandler.getPositions()
        val positions = Positions.values().map { it.id }.map { getString(it) }
        for (position in positions){
            if(position !in positionsDb){
                databaseHandler.addPosition(Position(0,position))
            }
        }
    }

    private fun addValidityOptions(){
        val databaseHandler = DatabaseHandler(this)
        val validityOptionsDb = databaseHandler.getValidityOptions()
        val validityOptions = ValidityOptions.values().map { it.id }.map { getString(it) }
        for(option in validityOptions){
            if(option !in validityOptionsDb){
                databaseHandler.addValidity(Validity(0,option.toInt()))
            }
        }
    }

    private fun addSecurityQuestions(){
        val databaseHandler = DatabaseHandler(this)
        val securityQuestionDb = databaseHandler.getSecurityQuestions()
        val securityQuestions = SecurityQuestions.values().map { it.id }.map { getString(it) }
        for(securityQuestion in securityQuestions){
            if(securityQuestion !in securityQuestionDb){
                databaseHandler.addSecurityQuestion(SecurityQuestion(0,securityQuestion))
            }
        }
    }

    private fun addVehicleTypes(){
        val databaseHandler = DatabaseHandler(this)
        val vehicleTypesDb = databaseHandler.getVehicleTypes()
        val vehicleTypes = VehicleTypes.values().map { it.id }.map { getString(it) }
        for (vehicleType in vehicleTypes){
            if(vehicleType !in vehicleTypesDb){
                databaseHandler.addVehicleType(VehicleType(0,vehicleType))
            }
        }
    }

    private fun invalidateInsurance(){
        val db = DatabaseHandler(this)
        val vehicles = db.getAllVehicles()
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        for(vehicle in vehicles){
            val insuranceDate = simpleDateFormat.parse(vehicle.date)
            val currentDate = Date()
            val calendar = Calendar.getInstance()
                calendar.time = currentDate
            val year = calendar.get(Calendar.YEAR)
            var daysBetween = 365
            if(isYearLeap(year) || isYearLeap(year+1)){
                daysBetween = 366
            }
            val diff: Long = currentDate.time - insuranceDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            if(days>daysBetween){
                db.updateVehicle(
                    Vehicle(vehicle.id,vehicle.clientId,vehicle.licencePlate,
                        vehicle.VIN,vehicle.registrationCertificate,
                        vehicle.engine,vehicle.vehicleTypeId,vehicle.brand,vehicle.model,
                        vehicle.date,vehicle.price,getString(ValidityOptions.NO.id).toInt())
                )
            }
        }
    }

    private fun isYearLeap(year: Int): Boolean {
        val leap: Boolean = if (year % 4 == 0) {
            if (year % 100 == 0) {
                year % 400 == 0
            } else
                true
        } else
            false
        return leap
    }
}