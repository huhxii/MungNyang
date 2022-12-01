package com.example.mungnyang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mungnyang.animalData.AdoptAnimal
import com.example.mungnyang.animalPhoto.AnimalPhoto
import com.example.mungnyang.databinding.FragmentAdoptlistBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AdoptlistFragment : Fragment() {
    lateinit var binding: FragmentAdoptlistBinding
    lateinit var adapter: AdoptAdapter
    lateinit var adoptDatabase: DatabaseReference
    lateinit var adoptList: MutableList<AnimalVO>
    var mainContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdoptlistBinding.inflate(inflater, container, false)
        mainContainer = container

        adoptDatabase = Firebase.database.reference

        adoptList = mutableListOf()

        adapter = AdoptAdapter(binding.root.context, adoptList)

        binding.recyclerView.layoutManager = LinearLayoutManager(container?.context)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl(AdoptOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofit2 = Retrofit.Builder()
            .baseUrl(PhotoOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(AdoptOpenService::class.java)
        val service2 = retrofit2.create(PhotoOpenService::class.java)

        service.getAdopt(AdoptOpenApi.API_KEY, AdoptOpenApi.LIMIT)
            .enqueue(object : Callback<AdoptAnimal>{
                override fun onResponse(call: Call<AdoptAnimal>, response: Response<AdoptAnimal>) {
                    val data = response.body()
                    data?.let {
                        for(adopt in it.TbAdpWaitAnimalView.row){
                            val adoptDAO = AdoptDAO()

                            Log.d("com.example.mungnyang", "Data Load Success")
                            var number = adopt.ANIMAL_NO
                            var state = adopt.ADP_STTUS
                            var name = adopt.NM
                            var age = adopt.AGE
                            var gender = adopt.SEXDSTN
                            var weight = adopt.BDWGH
                            var breed = adopt.BREEDS
                            var date = adopt.ENTRNC_DATE

                            val animalVO = AnimalVO(number, state, name, age, gender, weight, breed, date)

                            adoptDAO.insertAnimal(animalVO)?.addOnSuccessListener {
                                Log.d("com.example.mungnyang" , "${number} ${name} ${breed} 성공")
                            }?.addOnFailureListener {
                                Log.d("com.example.mungnyang" , "${number} ${name} ${breed} 실패")
                            }
                        }
                        adoptDatabase.child("AdoptList").addValueEventListener(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(postSnapshot in snapshot.children){
                                    val currentAnimal = postSnapshot.getValue(AnimalVO::class.java)
                                    adoptList.add(currentAnimal!!)
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })
                    }?: let{
                        Log.d("com.example.mungnyang", "No Data")
                    }
                }

                override fun onFailure(call: Call<AdoptAnimal>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        service2.getAnimal(PhotoOpenApi.API_KEY, PhotoOpenApi.LIMIT)
            .enqueue(object  : Callback<AnimalPhoto>{
                override fun onResponse(call: Call<AnimalPhoto>, response: Response<AnimalPhoto>) {
                    val data = response.body()
                    data?.let {
                        for(animal in it.TbAdpWaitAnimalPhotoView.row){
                            val adoptDAO = AdoptDAO()

                            Log.d("com.example.mungnyang", "Data Load Success")
                            val ani_number = animal.ANIMAL_NO
                            val photo_url = animal.PHOTO_URL
                            val photo_kind = animal.PHOTO_KND
                            if(photo_kind.equals("THUMB")) {
                                val photoVO = PhotoVO(ani_number, photo_url, photo_kind)

                                adoptDAO.insertPhoto(photoVO)?.addOnSuccessListener {
                                    Log.d("com.example.mungnyang", "${ani_number} ${photo_url} ${photo_kind} 성공")
                                }?.addOnFailureListener{
                                    Log.d("com.example.mungnyang", "${ani_number} ${photo_url} ${photo_kind} 실패")
                                }
                            }
                        }
                    }?: let{
                        Log.d("com.example.mungnyang", "No Data")
                    }
                }

                override fun onFailure(call: Call<AnimalPhoto>, t: Throwable) {
                    Log.d("com.example.mungnyang", "Data Load Error ${t.toString()}")
                    Toast.makeText(context, "Data Load Error", Toast.LENGTH_SHORT).show()
                }
            })





        return binding.root
    }

}