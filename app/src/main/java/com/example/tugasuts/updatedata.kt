package com.example.tugasuts

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasuts.databinding.ActivityupdatedataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class updatedata :AppCompatActivity() {

    private var database:DatabaseReference?= null
    private var auth:FirebaseAuth? = null
    private var cekNama:String?= null
    private var cekNip:String? = null
    private var cekJkel:String? = null
    private var jabatan:String? = null
    private lateinit var binding:ActivityupdatedataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityupdatedataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title ="Ubah Data"

        auth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference
        data
        binding.Updatbtn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v : View?) {
                cekNama = binding.updatenamakaryawan.getText().toString()
                cekNip = binding.updatenomernip.getText().toString()

                 cekJkel = binding.updatePerempuan.text.toString()
                cekJkel = binding.updatelaki.text.toString()
//                Spinnner
                jabatan = binding.updatespinerjabatan.selectedItem.toString()

                if (isEmpty(cekNama!!)|| isEmpty(cekNip!!) || isEmpty(cekJkel!!) || isEmpty(jabatan!!) ){
                    Toast.makeText(this@updatedata,
                        "Data Tidak Boleh Kosong",
                        Toast.LENGTH_SHORT).show()
                }else{
                    val setdata_karyawan = DataKaryawan()

                    if (binding.updatePerempuan.isChecked) {
                        setdata_karyawan.jkel = binding.updatePerempuan.text.toString()
                    } else if (binding.updatelaki.isChecked) {
                        setdata_karyawan.jkel = binding.updatelaki.text.toString()
                    }
                    setdata_karyawan.nama = binding.updatenamakaryawan.getText().toString()
                    setdata_karyawan.nip = binding.updatenomernip.getText().toString()
                    setdata_karyawan.jabatan = binding.updatespinerjabatan.selectedItem.toString()
                    UpdateKaryawan(setdata_karyawan)
                }
            }
        })


    }

    private fun isEmpty(cekNama: String): Boolean {
        return TextUtils.isEmpty(cekNama)
    }
    private val data:Unit
        private get() {
            val getnama = intent.extras!!.getString("dataNama")
            val getNip = intent.extras!!.getString("dataNip")
            val getjkel= intent.extras!!.getString("Jkel")
            val getJabatan = intent.extras!!.getString("Jabatan")



            binding.updatenamakaryawan!!.setText(getnama)
            binding.updatenomernip!!.setText(getNip)
            binding.updatespinerjabatan!!.setTag(getJabatan)

            val spinnerJenisKelamin = findViewById<Spinner>(R.id.updatespinerjabatan)
            ArrayAdapter.createFromResource(
                this,
                R.array.jabatan,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerJenisKelamin.adapter = adapter
                spinnerJenisKelamin.setSelection(adapter.getPosition(getJabatan))
            }


            if (getjkel.equals("perempuan")){
                binding.updatePerempuan.isChecked = true
            }else{
                binding.updatelaki.isChecked = true
            }

        }
    private fun UpdateKaryawan(kanca:DataKaryawan){


        val userID = auth!!.uid
        val getkey =intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataKaryawan")
            .child(getkey!!)
            .setValue(kanca)
            .addOnSuccessListener {
                binding.updatenamakaryawan!!.setText("")
                binding.updatenomernip!!.setText("")
                binding.updatePerempuan.isChecked = false
                binding.updatelaki.isChecked = false
                binding.updatespinerjabatan!!.setTag("")

                Toast.makeText(this@updatedata,"Data Berhasil Diubah",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}