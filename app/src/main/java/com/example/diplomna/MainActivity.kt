package com.example.diplomna

import DatabaseHandler
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.diplomna.models.Employee
import com.example.diplomna.util.SHA256

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addDefaultAdmin()
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
}