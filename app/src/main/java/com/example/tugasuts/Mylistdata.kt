package com.example.tugasuts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuts.databinding.MylistdataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Mylistdata:AppCompatActivity(), RecyclerViewAdapter.dataListener {

    private var recyclerview: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var datakaryawan = ArrayList<DataKaryawan>()
    private var auth: FirebaseAuth? = null
    private lateinit var binding: MylistdataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MylistdataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerview = findViewById(R.id.datalist)
        supportActionBar!!.title = "DataKaryawan"
        auth = FirebaseAuth.getInstance()
        MyrecycleView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Memuat Data...", Toast.LENGTH_LONG).show()

        val getuserid: String = auth?.getCurrentUser()?.getUid().toString()
        val getrefrence = database.getReference()
        getrefrence.child("Admin").child(getuserid).child("DataKaryawan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        datakaryawan.clear()
                        for (snapshot in dataSnapshot.children) {
                            val karyawan = snapshot.getValue(DataKaryawan::class.java)
                            karyawan?.key = snapshot.key
                            datakaryawan.add(karyawan!!)
                        }
                        adapter = RecyclerViewAdapter(datakaryawan, this@Mylistdata)
                        recyclerview?.adapter = adapter
                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data DiTampilkan", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        binding.datakosong.isVisible = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Data Gagaldimuat", Toast.LENGTH_LONG)
                        .show()
                    Log.e("mylistadata", error.details + "" + error.message)
                }
            })
    }

    fun MyrecycleView() {
        layoutManager = LinearLayoutManager(this)
        recyclerview?.layoutManager = layoutManager
        recyclerview?.setHasFixedSize(true)

        val itemDecoration =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerview?.addItemDecoration(itemDecoration)
    }

    override fun Deletedata(data: DataKaryawan?, position: Int) {
        val getUserId: String = auth?.getCurrentUser()?.getUid().toString()
        val getRefrence = database.getReference()
        if (getRefrence != null) {
            getRefrence.child("Admin")
                .child(getUserId)
                .child("DataKaryawan")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@Mylistdata, "Data Terhapus", Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, Mylistdata::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            Toast.makeText(this@Mylistdata, " Refrence Kosong", Toast.LENGTH_SHORT).show()
        }
    }
}

