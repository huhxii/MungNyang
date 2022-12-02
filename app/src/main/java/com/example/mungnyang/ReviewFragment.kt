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
    lateinit var infoList: MutableList<Information>
    lateinit var photoList: MutableList<PhotoInformation>
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewBinding.inflate(layoutInflater)
        infoList = mutableListOf()
        photoList = mutableListOf()

        val retrofit = Retrofit.Builder()
            .baseUrl(MungNyangOpenApi.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val infoService = retrofit.create(MungNyangInfo::class.java)
        val photoService = retrofit.create(MungNyangPhoto::class.java)
        infoService.getInfo(MungNyangOpenApi.API_KEY, MungNyangOpenApi.LIMIT).enqueue(object : Callback<AdoptAnimal> {
            override fun onResponse(call: Call<AdoptAnimal>, response: Response<AdoptAnimal>) {
                val data = response.body()
                data?.let{
                    it.TbAdpWaitAnimalView.list_total_count
                    for (loadData in it.TbAdpWaitAnimalView.row) {
                        val infoDAO = InfoDAO()
                        var infoNo = loadData.ANIMAL_NO.toString()
                        var name = loadData.NM
                        var gender = loadData.SEXDSTN
                        var age = loadData.AGE
                        var kind = loadData.BREEDS
                        var date = loadData.ENTRNC_DATE
                        var text = loadData.INTRCN_CN
                        val info = Information(infoNo, name, gender, age, kind, date, text)

                        infoList.add(Information(infoNo, name, gender, age, kind, date, text))
                        infoDAO.insertInfo(info).addOnSuccessListener {
                        }?.addOnFailureListener {
                            Log.d("com.example.database", "infoData insert failed")
                        }
                        adapter = ReviewAdapter(binding.root.context, infoList, photoList)
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
        photoService.getPhoto(MungNyangOpenApi.API_KEY, MungNyangOpenApi.LIMIT).enqueue(object : Callback<AnimalPhoto> {
            override fun onResponse(call: Call<AnimalPhoto>, response: Response<AnimalPhoto>) {
                val data = response.body()
                data?.let{
                    it.TbAdpWaitAnimalPhotoView.list_total_count
                    for (loadData in it.TbAdpWaitAnimalPhotoView.row) {
                        var infoDAO = InfoDAO()
                        var docid = infoDAO.photoDatabaseReference?.push()?.key
                        var photoNo = loadData.ANIMAL_NO.toString()
                        var photoKind = loadData.PHOTO_KND
                        var photoNumber = loadData.PHOTO_NO.toString()
                        var url = loadData.PHOTO_URL
                        if(photoKind.equals("THUMB")){
                            val photo = PhotoInformation(docid, photoNo, photoKind, photoNumber, url)
                            photoList.add(PhotoInformation(docid, photoNo, photoKind, photoNumber, url))
                            infoDAO.insertPhoto(photo).addOnSuccessListener {
                            }?.addOnFailureListener {
                                Log.d("com.example.database", "photoData insert failed")
                            }
                        }
                        adapter = ReviewAdapter(binding.root.context, infoList, photoList)
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