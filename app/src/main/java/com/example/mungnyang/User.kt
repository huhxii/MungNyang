package com.example.mungnyang

data class User(
    var name: String,
    var email: String,
    var phone: String,
    var uId: String
){
    constructor(): this("","","","")
}
