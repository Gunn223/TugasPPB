package com.example.tugasuts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.IdpResponse
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasuts.databinding.LoginActivityBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(),View.OnClickListener{
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1

//    layout view binding
    private lateinit var binding:LoginActivityBinding

//      menggunkan view binding untuk mencari id di dalam layout
    // Inisialisasi objek FirebaseAuth di dalam onCreate() Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progress.visibility = View.GONE
        binding.btnlogin.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()


        //cek apakah user sudah pernah masuk/login atau belum jika auth!!.curentuser = null maka tidak ada user yang masuk ke app
        if (auth!!.currentUser == null){
            // Tidak ada pengguna yang masuk, lakukan sesuatu di sini
        }else{
            // Pengguna sudah masuk, alihkan ke MainActivity
            intent= Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            // Mengambil intent result dari FirebaseUI Authentication
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Login Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GitHubBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),RC_SIGN_IN)
    }
}