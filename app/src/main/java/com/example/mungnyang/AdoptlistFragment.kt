package com.example.mungnyang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mungnyang.databinding.FragmentAdoptlistBinding

class AdoptlistFragment : Fragment() {
    lateinit var binding: FragmentAdoptlistBinding
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



        return binding.root
    }

}