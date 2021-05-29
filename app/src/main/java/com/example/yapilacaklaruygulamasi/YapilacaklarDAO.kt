package com.example.yapilacaklaruygulamasi

import android.content.ContentValues

class YapilacaklarDAO {

    fun GorevSil(veriTabani: VeriTabaniYardimcisi,yapilacak_id : Int) {
        val db = veriTabani.writableDatabase
        db.delete("yapilacaklar","yapilacak_id=?",arrayOf(yapilacak_id.toString()))
        db.close()
    }

    fun GorevEkle(veriTabani: VeriTabaniYardimcisi,yapilacak_ad : String) {
        val db = veriTabani.writableDatabase

        val values = ContentValues()
        values.put("yapilacak_ad",yapilacak_ad)
        db.insertOrThrow("yapilacaklar",null,values)
        db.close()
    }

    fun GorevGuncelle(veriTabani: VeriTabaniYardimcisi,yapilacak_id : Int,yapilacak_ad: String) {
        val db = veriTabani.writableDatabase

        val values = ContentValues()
        values.put("yapilacak_ad",yapilacak_ad)
        db.update("yapilacaklar",values,"yapilacak_id=?",arrayOf(yapilacak_id.toString()))
        db.close()
    }

    fun tumGorevler(veriTabani: VeriTabaniYardimcisi) :ArrayList<Yapilacaklar>{

        var db = veriTabani.writableDatabase
        val yapilacaklarListe = ArrayList<Yapilacaklar>()
        val c= db.rawQuery("SELECT * FROM yapilacaklar",null)

        while(c.moveToNext()){
            val gorev = Yapilacaklar(c.getInt(c.getColumnIndex("yapilacak_id")),
                c.getString(c.getColumnIndex("yapilacak_ad")))

            yapilacaklarListe.add(gorev)
        }
        return yapilacaklarListe
    }

    fun gorevAra(veriTabani: VeriTabaniYardimcisi,Kelime:String) :ArrayList<Yapilacaklar>{

        val db = veriTabani.writableDatabase
        val yapilacaklarListe = ArrayList<Yapilacaklar>()
        val c= db.rawQuery("SELECT * FROM yapilacaklar WHERE yapilacak_ad like '%$Kelime%'",null)

        while(c.moveToNext()){
            val gorev = Yapilacaklar(c.getInt(c.getColumnIndex("yapilacak_id")),
                c.getString(c.getColumnIndex("yapilacak_ad")))

            yapilacaklarListe.add(gorev)
        }
        return yapilacaklarListe
    }
}