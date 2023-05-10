package com.example.tugasuts

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val listdata_karyawan:ArrayList<DataKaryawan>, context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.viewholder>() {

        private val context:Context

        inner class viewholder(itemView: View):RecyclerView.ViewHolder(itemView){
            val Nama:TextView
            val nip:TextView
            val jkel:TextView
            val jabatan:TextView
            val listitem: LinearLayout

            init {
                Nama = itemView.findViewById(R.id.nama)
                nip = itemView.findViewById(R.id.nip)
                jkel = itemView.findViewById(R.id.jkel)
                jabatan = itemView.findViewById(R.id.jabatan)
                listitem = itemView.findViewById(R.id.list_item)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val V:View= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview,parent,false)
    return viewholder(V)
    }

    var listener:dataListener? = null

    interface dataListener{
        fun Deletedata(data: DataKaryawan?, position: Int)
    }

    override fun onBindViewHolder(holder: viewholder, @SuppressLint("RecyclerView") position: Int) {
        val nama:String? = listdata_karyawan.get(position).nama
        val nip:String? = listdata_karyawan.get(position).nip
        val jkel:String? = listdata_karyawan.get(position).jkel
        val jabatan: String? = listdata_karyawan.get(position).jabatan

        holder.Nama.text = "Nama: $nama"
        holder.nip.text = "NIP: $nip"
        holder.jkel.text = "Jenis Kelamin: $jkel"
        holder.jabatan.text = "Jabatan: $jabatan"
        holder.listitem.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
              holder.listitem.setOnClickListener{view->
                  val aksi = arrayOf("Ubah","Hapus")
                  val alrt:AlertDialog.Builder = AlertDialog.Builder(view.context)
                  alrt.setItems(aksi,DialogInterface.OnClickListener{dialog,i->
                      when(i){
                          0->{
                              val bundle = Bundle()
                              bundle.putString("dataNama",listdata_karyawan[position].nama)
                              bundle.putString("dataNip",listdata_karyawan[position].nip)
                              bundle.putString("Jkel",listdata_karyawan[position].jkel)
                              bundle.putString("Jabatan",listdata_karyawan[position].jabatan)
                              bundle.putString("getPrimaryKey",listdata_karyawan[position].key)

                              val intent = Intent(view.context,updatedata::class.java)
                              intent.putExtras(bundle)
                              context.startActivity(intent)
                          }
                          1->{
                              listener?.Deletedata(listdata_karyawan.get(position),(position))
                              }

                      }
                  })
                  alrt.create()
                  alrt.show()
                  true
              }
                return true
            }
        })

    }
    override fun getItemCount(): Int {
        return listdata_karyawan.size
    }


    init {
    this.context = context
        this.listener = context as Mylistdata
}


}