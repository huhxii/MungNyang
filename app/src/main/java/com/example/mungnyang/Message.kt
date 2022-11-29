package com.example.mungnyang

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor():this("","")
}
