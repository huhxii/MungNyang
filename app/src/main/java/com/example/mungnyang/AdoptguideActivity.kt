package com.example.mungnyang

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.mungnyang.databinding.ActivityAdoptguideBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AdoptguideActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback{
    companion object{
        const val LATITUDE = 37.575664
        const val LONGTITUDE = 126.889945
        const val ADOPTNAME = "서울동물복지지원센터"
    }
    lateinit var binding: ActivityAdoptguideBinding
    var apiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptguideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            if(it.all { permission -> permission.value == true }){
                apiClient?.connect()
            }else{
                Toast.makeText(this, "권한거부 이유로 맵앱을 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)?.getMapAsync(this)

        if(
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) !== PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED ){

            requestPermissionLauncher.launch( arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
        }else{
            //8. 현재 이 앱을 설치한 사람의 위치정보를 줄 것을 요청
            apiClient?.connect()
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(data: Int) {
        Log.d("mungnyang", "Location Provider 더 이상 이용이 불가능한 상황 ${data}")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("mungnyang", "Location Provider가 제공되지 않는 상황 ${connectionResult.errorMessage}")
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val center = LatLng(LATITUDE, LONGTITUDE)
        googleMap.addMarker(MarkerOptions().position(center).title(ADOPTNAME))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(center))

        val cameraPosition = CameraPosition.Builder()
            .target(center)
            .zoom(14f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}