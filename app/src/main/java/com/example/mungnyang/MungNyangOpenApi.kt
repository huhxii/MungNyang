package com.example.mungnyang

import com.example.mungnyang.animalData.AdoptAnimal
import com.example.mungnyang.animalPhoto.AnimalPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//http://openapi.seoul.go.kr:8088/42626668773931683531724b5a4770/json/TbAdpWaitAnimalPhotoView/1/5/
class MungNyangOpenApi {
    companion object{
        const val DOMAIN = "http://openapi.seoul.go.kr:8088/"
        const val API_KEY = "42626668773931683531724b5a4770"
        const val LIMIT = 300
    }
}

interface MungNyangInfo{
    @GET("{API_KEY}/json/TbAdpWaitAnimalView/1/{end}")
    fun getInfo(@Path("API_KEY") key: String, @Path("end") limit: Int): Call<AdoptAnimal>
}

interface MungNyangPhoto{
    @GET("{API_KEY}/json/TbAdpWaitAnimalPhotoView/1/{end}")
    fun getPhoto(@Path("API_KEY") key: String, @Path("end") limit: Int): Call<AnimalPhoto>
}