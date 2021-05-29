package com.example.yapilacaklaruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.sqlitekullanimihazirveritabani.DatabaseCopyHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    private lateinit var yapilacaklarListe: ArrayList<Yapilacaklar>
    private lateinit var adapter: YapilacaklarAdapter
    private lateinit var veriTabani: VeriTabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        veritabaniKopyala()

        toolbarMainActivity.title = "GÜNLÜK PLAN"
        setSupportActionBar(toolbarMainActivity)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)


        veriTabani = VeriTabaniYardimcisi(this)

        tumGorevlerAl()

        fab.setOnClickListener {
            alertGoster()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        val item = menu.findItem(R.id.arama_butonu)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    fun alertGoster() {
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarimi, null)
        val editTextGorevAdi = tasarim.findViewById(R.id.editTextGorevAdi) as EditText

        val ad = AlertDialog.Builder(this)
        ad.setTitle("Yeni Görev Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("EKLE") { dialogInterface, i ->

            val gorev_ad = editTextGorevAdi.text.toString().trim()

            YapilacaklarDAO().GorevEkle(veriTabani, gorev_ad)

            tumGorevlerAl()

            Toast.makeText(applicationContext, "$gorev_ad görevi eklendi!", Toast.LENGTH_SHORT).show()
        }

        ad.setNegativeButton("İPTAL") { dialogInterface, i ->
        }

        ad.create().show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Log.e("Gönderilen arama", query)
        aramaYap(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Log.e("Gönderilen arama", newText)
        aramaYap(newText)
        return true
    }

    fun tumGorevlerAl() {
        yapilacaklarListe = YapilacaklarDAO().tumGorevler(veriTabani)
        adapter = YapilacaklarAdapter(this, yapilacaklarListe, veriTabani)

        recyclerView.adapter = adapter
    }

    fun aramaYap(Kelime: String) {
        yapilacaklarListe = YapilacaklarDAO().gorevAra(veriTabani, Kelime)
        adapter = YapilacaklarAdapter(this@MainActivity, yapilacaklarListe, veriTabani)

        recyclerView.adapter = adapter
    }

    fun veritabaniKopyala() {
        val copyHelper = DatabaseCopyHelper(this@MainActivity)
        try {
            copyHelper.createDataBase()
            copyHelper.openDataBase()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}