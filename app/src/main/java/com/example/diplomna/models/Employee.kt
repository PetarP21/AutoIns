package com.example.diplomna.models

// creating a Data Model Class, without methods
class Employee
    (val id : Int,
     val nickname: String,
     val firstName: String,
     val middleName: String,
     val lastName: String,
     val email: String,
     val positionId : Int,
     val securityQuestion : String,
     val securityAnswer : String,
     val salt : String,
     val password : String)