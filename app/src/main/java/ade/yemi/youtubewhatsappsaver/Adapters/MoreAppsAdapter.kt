package ade.yemi.youtubewhatsappsaver.Adapters

import ade.yemi.moreapps.models.App
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MoreAppsAdapter (private val items: List<App>): RecyclerView.Adapter<MoreAppsAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreAppsAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.singleapp, parent, false)
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MoreAppsAdapter.MyViewHolder, position: Int) {
        holder.bind(items[position])
    }
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.iv_moreappsimage)
        val appname = view.findViewById<TextView>(R.id.tv_appnameonplaystore)
        val button = view.findViewById<ImageView>(R.id.iv_downloadapp)

        fun bind(data: App) {
            appname.text = data.name
            var playstorelink = data.playStoreUrl

            Glide.with(view.context).load(data.appIconUrl).centerCrop().into(image)
            button.setOnClickListener {
                button.clicking()
                val uriUri = Uri.parse(playstorelink)
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                view.context.startActivity(launchBrowser)
            }
            //enable java 8
        }
    }
}