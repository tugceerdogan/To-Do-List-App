package com.example.yapilacaklaruygulamasi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class YapilacaklarAdapter(var mContext: Context,var yapilacaklarListe: ArrayList<Yapilacaklar>
                         ,var veriTabani:VeriTabaniYardimcisi)
                         :RecyclerView.Adapter<YapilacaklarAdapter.KartTasarim>() {

    inner class KartTasarim(tasarim:View): RecyclerView.ViewHolder(tasarim){
        var satirCardView : CardView
        var textViewYapilacakAdi : TextView
        var imageViewNokta : ImageView
        var ImageViewYapildi : ImageView

        init{
            satirCardView= tasarim.findViewById(R.id.satirCardView)
            textViewYapilacakAdi = tasarim.findViewById(R.id.textViewYapilacakAdi)
            imageViewNokta= tasarim.findViewById(R.id.imageViewNokta)
            ImageViewYapildi =tasarim.findViewById(R.id.ImageViewYapildi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KartTasarim {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.kart_tasarim,parent,false)
        return KartTasarim(tasarim)
    }

    override fun getItemCount(): Int {
        return yapilacaklarListe.size
    }

    override fun onBindViewHolder(holder: KartTasarim, position: Int) {
        var gorev =yapilacaklarListe.get(position)

        //textview üzerine yazdırma:
        holder.textViewYapilacakAdi.text ="${gorev.yapilacak_ad}"

        holder.imageViewNokta.setOnClickListener {

            val popupMenu =PopupMenu(mContext,holder.imageViewNokta)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)


            popupMenu.setOnMenuItemClickListener { itemTikla ->

                when(itemTikla.itemId){
                    R.id.action_sil->{
                        Snackbar.make(holder.imageViewNokta,"${gorev.yapilacak_ad} görevin silinsin mi ?",Snackbar.LENGTH_SHORT).setAction("EVET"){
                            Toast.makeText(mContext,"${gorev.yapilacak_ad} görevi silindi",Toast.LENGTH_SHORT).show()

                            YapilacaklarDAO().GorevSil(veriTabani,gorev.yapilacak_id)
                            yapilacaklarListe =YapilacaklarDAO().tumGorevler(veriTabani)

                            notifyDataSetChanged()

                        }.show()
                        true
                    }
                    R.id.action_guncelle-> {

                        alertGoster(gorev)
                        true

                    }else -> false
                }

            }
            popupMenu.show()
        }

        holder.ImageViewYapildi.setOnClickListener {
            Toast.makeText(mContext,"${gorev.yapilacak_ad} görevi yapıldı ",Toast.LENGTH_SHORT).show()

            YapilacaklarDAO().GorevSil(veriTabani,gorev.yapilacak_id)
            yapilacaklarListe=YapilacaklarDAO().tumGorevler(veriTabani)

            notifyDataSetChanged()
        }
    }

    fun alertGoster(gorev:Yapilacaklar){

        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarimi,null)
        val editTextGorevAdi = tasarim.findViewById(R.id.editTextGorevAdi) as EditText

        editTextGorevAdi.setText(gorev.yapilacak_ad)

        val ad = AlertDialog.Builder(mContext)

        ad.setTitle("Görev Güncelle")
        ad.setView(tasarim)

        ad.setPositiveButton("GÜNCELLE"){  dialogInterface,i ->

            val gorev_ad =editTextGorevAdi.text.toString().trim()

            YapilacaklarDAO().GorevGuncelle(veriTabani,gorev.yapilacak_id,gorev_ad)
            yapilacaklarListe =YapilacaklarDAO().tumGorevler(veriTabani)
            notifyDataSetChanged()

            Toast.makeText(mContext, "Görev $gorev_ad şeklinde güncellendi!", Toast.LENGTH_SHORT).show()
        }

        ad.setNegativeButton("İPTAL") { dialogInterface, i ->
        }

        ad.create().show()

    }
}

