package com.example.mungnyang

import com.example.mungnyang.animalData.AdoptAnimal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//http://openapi.seoul.go.kr:8088/5a45627a41636f6431396442665259/json/TbAdpWaitAnimalView/1/5/

class AdoptOpenApi {
    companion object{
        const val DOMAIN = "http://openapi.seoul.go.kr:8088"
        const val API_KEY = "5a45627a41636f6431396442665259"
        const val LIMIT = 32
    }
}

interface AdoptOpenService{
    @GET("{api_key}/json/TbAdpWaitAnimalView/1/{end}")
    fun getAdopt(@Path("api_key") key : String, @Path("end") limit : Int): Call<AdoptAnimal>
}