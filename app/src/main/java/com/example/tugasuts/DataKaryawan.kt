package com.example.tugasuts

class DataKaryawan {
    var nama:String? = null
    var nip:String? = null
    var jkel:String? = null
    var jabatan:String? = null

    var key:String? = null

constructor()

    constructor(nama:String?,nip:String?,jkel:String?,jabatan:String?){
        this.nama = nama
        this.nip = nip
        this.jkel = jkel
        this.jabatan = jabatan
    }

}