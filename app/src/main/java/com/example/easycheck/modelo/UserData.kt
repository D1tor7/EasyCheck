package com.example.easycheck.modelo

data class UserData(
    val email: String,
    val name: String,
    val cellphone: String,
    val dni: String,
    val password:String
){
    constructor() : this("", "", "", "", "")
}
