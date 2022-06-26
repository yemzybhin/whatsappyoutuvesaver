package ade.yemi.youtubewhatsappsaver.Activities

import ade.yemi.moreapps.Network.RetrofitInterface1
import ade.yemi.moreapps.models.AppContent
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.CheckEmpty
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var adcheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        var whatsapp = findViewById<CardView>(R.id.whatsappcard)
        var youtube = findViewById<CardView>(R.id.youtubecard)
        var points = findViewById<TextView>(R.id.pointcount)
        var abouts = findViewById<CardView>(R.id.abouts)
        var adspace = findViewById<CardView>(R.id.adspace)
        var adimage = findViewById<ImageView>(R.id.adimage)
        var admessage = findViewById<TextView>(R.id.message)


        var preferencestuff = Preferencestuff(this)
        points.text = "${preferencestuff.getPoint()} Points"

        var intent = Intent(this, Activity2 :: class.java)
        whatsapp.setOnClickListener {
            whatsapp.clicking()

                if (checkpermmission1() == true){
                    loading()
                    Handler().postDelayed({
                    intent.putExtra("FragmentToSetTo", "Whatsapp")
                    startActivity(intent)
                   }, 0)
                }else{
                    checkperm21()
                }
        }
        youtube.setOnClickListener {
            youtube.clicking()
            loading()
            Handler().postDelayed({
                intent.putExtra("FragmentToSetTo", "Youtube")
                startActivity(intent)
            },0)
        }

        abouts.setOnClickListener {
            abouts.clicking()
            loading()
            Handler().postDelayed({
                intent.putExtra("FragmentToSetTo", "Abouts")
                startActivity(intent)
            },0)

        }
        getdata1(adspace, adimage, admessage)
        adspace.visibility = View.GONE
    }


    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
    fun checkpermmission1() : Boolean{
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false
        } else {
            return true
        }
    }
    fun checkperm21(){
        var REQUEST_CODE = 300
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
    }
    internal fun loading(){
        var load = Dialog(this)
        load.setCancelable(false)
        load.setContentView(R.layout.loading_popuup)
        load.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        load.show()

    }
    private fun getdata1(adspace: CardView, adimage : ImageView, message: TextView){
        var rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface1.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var API = rf.create(RetrofitInterface1::class.java)
        var call =API.post
        call?.enqueue(object : Callback<AppContent?> {
            override fun onResponse(
                    call: Call<AppContent?>,
                    response: Response<AppContent?>
            ) {
                var appContent: AppContent? = response.body() as AppContent
                var adslist = appContent?.ads

                if (adslist!![0].visible != false) {
                    adspace.visibility = View.VISIBLE
                    adcheck = true
                    if (adslist!![0].SmallImagelink!!.CheckEmpty() != true) {
                        Glide.with(applicationContext).load(adslist!![0].SmallImagelink).centerCrop()
                                .into(adimage)
                    } else {
                        adimage.visibility = View.GONE
                        adspace.visibility = View.VISIBLE
                    }
                    message.text = adslist!![0].message
                    val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.adscale)
                    adspace.startAnimation(animation)
                    adspace.setOnClickListener {
                        adspace.clicking()
                        if (adslist!![0].Link != "") {
                            val uriUri = Uri.parse(adslist!![0].Link)
                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                            startActivity(launchBrowser)
                        } else {
                            Toast.makeText(
                                    applicationContext,
                                    "${adslist!![0].Description}",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<AppContent?>, t: Throwable) {
                adspace.visibility = View.GONE
            }
        })
    }

}