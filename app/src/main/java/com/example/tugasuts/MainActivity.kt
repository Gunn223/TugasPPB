package com.example.tugasuts


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tugasuts.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() , View.OnClickListener{

    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    private lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnner = binding.spinerjabatan
        val jabatan = resources.getStringArray(R.array.jabatan)
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,jabatan)
        spinnner.adapter = adapter
        spinnner.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        //            end spiner
        binding.btnsimpan.setOnClickListener(this)

        binding.lihatdata.setOnClickListener(this)
        binding.logoutbtn.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

    }
  private fun isEmpty(s:String):Boolean{
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View) {
        when (p0.getId()) {

            R.id.btnsimpan -> {
                val getuserid = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getNama: String = binding.namakaryawan.getText().toString()
                val getNip: String = binding.nomernip.getText().toString()
                var gender = "perempuan"
                if (binding.laki.isChecked) {
                    gender = "Laki-Laki"
                }
                val getJabatan = binding.spinerjabatan.selectedItem.toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getNama) || isEmpty(getNip) || isEmpty(gender) || isEmpty(getJabatan)) {
                    Toast.makeText(this@MainActivity, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    getReference.child("Admin").child(getuserid).child("DataKaryawan").push()
                        .setValue(DataKaryawan(getNama, getNip, gender, getJabatan))
                        .addOnCompleteListener(this) {
                            binding.namakaryawan.setText("")
                            binding.nomernip.setText("")
                            binding.Perempuan.isChecked = false
                            binding.laki.isChecked = false
                            Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
            R.id.logoutbtn -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(this@MainActivity, "logout berhasil", Toast.LENGTH_SHORT)
                            .show()
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
            R.id.lihatdata -> {
                startActivity(Intent(this@MainActivity, Mylistdata::class.java))
            }

        }


    }
}