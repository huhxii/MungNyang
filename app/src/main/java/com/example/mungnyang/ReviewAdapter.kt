package com.example.mungnyang

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mungnyang.databinding.ReviewItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ReviewAdapter(val context: Context, val reviewList: MutableList<Information>, val photoList: MutableList<PhotoInformation>): RecyclerView.Adapter<ReviewAdapter.WifiViewHolder>() {
    lateinit var databaseReference: DatabaseReference
    var address: String? = null

    override fun onBindViewHolder(wifiViewholder: WifiViewHolder, position: Int) {
        val binding = (wifiViewholder as WifiViewHolder).binding
        databaseReference = Firebase.database.reference

        val reviewData = reviewList.get(position)

        databaseReference.child("Photo").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dataSnapshot in snapshot.children){
                    val animalPhoto = dataSnapshot.getValue(PhotoInformation::class.java)
                    if(animalPhoto!!.photoNo.equals(reviewData.infoNo)){
                        Glide.with(context).load("https://${animalPhoto.url}").into(binding.ivRevPicture)
                        address = "http://${animalPhoto.url}"
                    }
                }//end of for
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("com.example.database", "onCancelled")
            }
        })

        binding.tvRevName.text = reviewData.name

        binding.revLinearLayout.setOnClickListener {
            val intent = Intent(binding.root.context, DetailInfoActivity::class.java)
            intent.putExtra("imageUrl", address)
            Log.d("com.example.database", "${address}")
            intent.putExtra("no", reviewData.infoNo)
            intent.putExtra("name", reviewData.name)
            intent.putExtra("gender", reviewData.gender)
            intent.putExtra("age", reviewData.age)
            intent.putExtra("kind", reviewData.kind)
            intent.putExtra("date", reviewData.date)
//            intent.putExtra("text", reviewData.text)
            startActivity(binding.root.context, intent, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        var binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WifiViewHolder(binding)
    }

    override fun getItemCount() = reviewList.size

    class WifiViewHolder(val binding: ReviewItemBinding) : RecyclerView.ViewHolder(binding.root)
}