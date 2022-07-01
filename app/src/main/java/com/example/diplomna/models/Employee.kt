package com.example.diplomna.models

data class Employee
    (val id : Int,
     val nickname: String,
     val firstName: String,
     val middleName: String,
     val lastName: String,
     val email: String,
     val positionId : Int,
     val securityQuestionId : Int,
     val securityAnswer : String,
     val salt : String,
     val password : String)