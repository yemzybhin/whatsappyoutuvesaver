package ade.yemi.youtubewhatsappsaver.Adapters

import ade.yemi.moreapps.models.Ads
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.CheckEmpty
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AllAdsAdapter ( private val items: List<Ads>): RecyclerView.Adapter<AllAdsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.myvideo_ad, parent, false)
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var adspace = view.findViewById<LinearLayout>(R.id.adspace)
        var message = view.findViewById<TextView>(R.id.addmessage)
        var title = view.findViewById<TextView>(R.id.addtitle)
        var description = view.findViewById<TextView>(R.id.addescription)
        var adimage = view.findViewById<ImageView>(R.id.adimage)
        var adbutton = view.findViewById<TextView>(R.id.adbuttonlink)
        var postad = view.findViewById<TextView>(R.id.postanad)
        var vidlink = view.findViewById<CardView>(R.id.watchadvideo)

        fun bind(data: Ads) {
            var adslist = data
            if (adslist.visible != false){
                adspace.visibility= View.VISIBLE
                message.text= adslist.message
                title.text = adslist.Title!!.toUpperCase()
                vidlink.visibility = View.VISIBLE
                adimage.visibility = View.GONE
                var prefereceStuffs = Preferencestuff(view.context)
                if (adslist.VideoLink!!.CheckEmpty() != true){
                    vidlink.visibility = View.VISIBLE
                    vidlink.setOnClickListener {
                        vidlink.clicking()
                        try {
                            val uriUri = Uri.parse(adslist.VideoLink)
                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                            view.context.startActivity(launchBrowser)
                            prefereceStuffs.setPoint(prefereceStuffs.getPoint() + 2)
                            Toast.makeText(view.context, "2 Points Added", Toast.LENGTH_SHORT).show()
                        }catch (e : Exception){
                            Toast.makeText(view.context, "Could not open video. No point Added", Toast.LENGTH_SHORT).show()
                        }
                         }
                }else{
                    vidlink.visibility = View.GONE
                }
                if (adslist.Imagelink!!.CheckEmpty() != true){
                    adimage.visibility = View.VISIBLE
                    Glide.with(view.context).load(adslist.Imagelink).into(adimage)
                }else{
                    adimage.visibility = View.GONE
                }
                if (adslist.Description!!.CheckEmpty() != true){
                    description.visibility = View.VISIBLE
                    description.text = adslist.Description
                }else{
                    description.visibility = View.GONE
                }

                if (adslist.Link != ""){
                    adbutton.setOnClickListener {
                        adbutton.clicking()
                        try {
                            val uriUri = Uri.parse(adslist.Link)
                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                            view.context.startActivity(launchBrowser)
                            prefereceStuffs.setPoint(prefereceStuffs.getPoint() + 2)
                            Toast.makeText(view.context, "2 Points Added", Toast.LENGTH_SHORT).show()
                        }catch (e : Exception){
                            Toast.makeText(view.context, "Could not open link. No point Added", Toast.LENGTH_SHORT).show()
                        }
                        }
                    val animation = AnimationUtils.loadAnimation(view.context, R.anim.notification)
                    postad.startAnimation(animation)
                    postad.setOnClickListener {
                        val uriUri = Uri.parse("https://wa.me/message/ECPGO4HTT5LCN1")
                        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                        view.context.startActivity(launchBrowser)
                    }
                }else{
                    adbutton.visibility = View.GONE
                    postad.visibility = View.GONE
                }
            }else{
                adspace.visibility= View.GONE
            }
            //enable java 8
        }
    }
}