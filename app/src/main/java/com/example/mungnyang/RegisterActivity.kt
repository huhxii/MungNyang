package com.example.mungnyang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mungnyang.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterBinding
    lateinit var userAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인증 초기화
        userAuth = Firebase.auth

        //회원가입 로고 이미지
        Glide.with(this).load(R.raw.register).override(700,700).into(binding.ivRAMung)

        //전화번호 입력 시 (-) 자동생성
        binding.edtRAPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        //텍스트 로그인 이벤트
        binding.tvRALogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //버튼 회원가입 이벤트
        binding.btnRARegister.setOnClickListener {
            val email = binding.edtRAEmail.text.toString().trim()
            val password = binding.edtRAPassword.text.toString().trim()

            register(email, password)
        }

    }

    //회원가입 기능
    private fun register(email: String, password: String) {
        userAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원가입이 성공적으로 되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "회원가입을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}