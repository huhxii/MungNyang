package com.example.mungnyang

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mungnyang.animalData.AdoptAnimal
import com.example.mungnyang.animalPhoto.AnimalPhoto
import com.example.mungnyang.databinding.FragmentReviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReviewFragment : Fragment() {
    lateinit var binding: FragmentReviewBinding
    lateinit var adapter: ReviewAdapter
    lateinit var adoptList: MutableList<AnimalVO>
    lateinit var photoList: MutableList<PhotoVO>
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater)
        adoptList = mutableListOf()
        photoList = mutableListOf()

        val adoptRetrofit = Retrofit.Builder()
            .baseUrl(AdoptOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val photoRetrofit = Retrofit.Builder()
            .baseUrl(PhotoOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val adoptService = adoptRetrofit.create(AdoptOpenService::class.java)
        val photoService = photoRetrofit.create(PhotoOpenService::class.java)

        adoptService.getAdopt(AdoptOpenApi.API_KEY, AdoptOpenApi.LIMIT).enqueue(object : Callback<AdoptAnimal> {
            override fun onResponse(call: Call<AdoptAnimal>, response: Response<AdoptAnimal>) {
                val data = response.body()
                data?.let{
                    it.TbAdpWaitAnimalView.list_total_count
                    for (adopt in it.TbAdpWaitAnimalView.row) {
                        val adoptDAO = AdoptDAO()
                        var number = adopt.ANIMAL_NO
                        var state = adopt.ADP_STTUS
                        var name = adopt.NM
                        var age = adopt.AGE
                        var gender = adopt.SEXDSTN
                        var weight = adopt.BDWGH
                        var breed = adopt.BREEDS
                        var date = adopt.ENTRNC_DATE
                        val adopt = AnimalVO(number, state, name, age, gender, weight, breed, date)
                        adoptList.add(AnimalVO(number, state, name, age, gender, weight, breed, date))
                        adoptDAO.insertAnimal(adopt)?.addOnSuccessListener {
                            Log.d("com.example.mungnyang" , "${number} ${name} ${breed} 성공")
                        }?.addOnFailureListener {
                            Log.d("com.example.mungnyang" , "${number} ${name} ${breed} 실패")
                        }
                        adapter = ReviewAdapter(binding.root.context, adoptList, photoList)
                        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
                        binding.recyclerView.setHasFixedSize(true)
                        binding.recyclerView.adapter = adapter
                    }
                }?: let{
                    Log.d("com.example.database", "infoList is Empty")
                }
            }
            override fun onFailure(call: Call<AdoptAnimal>, t: Throwable) {
                Log.d("com.example.test", "infoList Load Error ${t.printStackTrace()}")
            }
        })

        photoService.getAnimal(PhotoOpenApi.API_KEY, PhotoOpenApi.LIMIT).enqueue(object : Callback<AnimalPhoto> {
            override fun onResponse(call: Call<AnimalPhoto>, response: Response<AnimalPhoto>) {
                val data = response.body()
                data?.let{
                    it.TbAdpWaitAnimalPhotoView.list_total_count
                    for (animal in it.TbAdpWaitAnimalPhotoView.row) {
                        val adoptDAO = AdoptDAO()
                        val ani_number = animal.ANIMAL_NO
                        val photo_url = animal.PHOTO_URL
                        val photo_kind = animal.PHOTO_KND
                        if(photo_kind.equals("THUMB")){
                            val photo = PhotoVO(ani_number, photo_url, photo_kind)
                            photoList.add(PhotoVO(ani_number, photo_url, photo_kind))
                            adoptDAO.insertPhoto(photo).addOnSuccessListener {
                            }?.addOnFailureListener {
                                Log.d("com.example.database", "photoData insert failed")
                            }
                        }
                        adapter = ReviewAdapter(binding.root.context, adoptList, photoList)
                        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
                        binding.recyclerView.setHasFixedSize(true)
                        binding.recyclerView.adapter = adapter
                    }
                }?: let{
                    Log.d("com.example.database", "photoList is Empty")
                }
            }
            override fun onFailure(call: Call<AnimalPhoto>, t: Throwable) {
                Log.d("com.example.test", "photoList Load Error ${t.printStackTrace()}")
            }
        })

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                Glide.with(this)
                    .load(it.data?.data)
                    .centerCrop()
                imageUri = it.data?.data
            }
        }

        binding.fab.setOnClickListener {
            if(binding.fab.visibility == View.VISIBLE){
                binding.fabChat.visibility = View.VISIBLE
                binding.fabCam.visibility = View.VISIBLE
                binding.fab.setImageResource(R.drawable.minus)
                binding.fabChat.setOnClickListener {
                }
                binding.fabCam.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    requestLauncher.launch(intent)
                }
                binding.fab.setOnClickListener {
                    binding.fabChat.visibility = View.INVISIBLE
                    binding.fabCam.visibility = View.INVISIBLE
                    binding.fab.setImageResource(R.drawable.add)
                }
            }
        }
        return binding.root
    }
}