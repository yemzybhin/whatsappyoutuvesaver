package ade.yemi.youtubewhatsappsaver.Activities

import ade.yemi.moreapps.Network.RetrofitInterface1
import ade.yemi.moreapps.models.AppContent
import ade.yemi.youtubewhatsappsaver.BuildConfig
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.*
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
import android.widget.*
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
        var whatsappBusiness = findViewById<CardView>(R.id.whatsappbusinesscard)
        var points = findViewById<TextView>(R.id.pointcount)
        var abouts = findViewById<CardView>(R.id.abouts)
        var adspace = findViewById<CardView>(R.id.adspace)
        var adimage = findViewById<ImageView>(R.id.adimage)
        var admessage = findViewById<TextView>(R.id.message)



        var updatelayout = findViewById<LinearLayout>(R.id.updatelayout)
        var updatebutton = findViewById<Button>(R.id.updatebutton)
        var cancelupdate = findViewById<CardView>(R.id.cancelupdate)
        updatelayout.visibility = View.GONE


        var preferencestuff = Preferencestuff(this)
        points.text = "${preferencestuff.getPoint()} Points"
        var intent = Intent(this, Activity2 :: class.java)
        whatsapp.setOnClickListener {
            whatsapp.clicking()
                if (checkpermmission1() == true){
                    loading()
                    Handler().postDelayed({
                    intent.putExtra("FragmentToSetTo", "Whatsapp")
                    intent.putExtra("WhatsAppType", "WhatsApp")
                    startActivity(intent)
                   }, 0)
                }else{
                    checkperm21()
                }
        }
        whatsappBusiness.setOnClickListener {
            whatsappBusiness.clicking()
            if (checkpermmission1() == true){
                loading()
                Handler().postDelayed({
                    intent.putExtra("FragmentToSetTo", "Whatsapp")
                    intent.putExtra("WhatsAppType", "whatsappBusiness")
                    startActivity(intent)
                }, 0)
            }else{
                checkperm21()
            }
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



        Handler().postDelayed({
            getversion(updatelayout, updatebutton, cancelupdate)
        }, 1000)
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
                        Glide.with(this@MainActivity).load(adslist!![0].SmallImagelink).centerCrop()
                            .into(adimage)
                    } else {
                        adimage.visibility = View.GONE
                        adspace.visibility = View.VISIBLE
                    }
                    message.text = adslist!![0].message
                    val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.adscale)
                    adspace.startAnimation(animation)
                    adspace.setOnClickListener {
                        adspace.clicking()
                        if (adslist!![0].Link != "") {
                            val uriUri = Uri.parse(adslist!![0].Link)
                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                            startActivity(launchBrowser)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "${adslist!![0].Description}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                var touse = AdToJsonString(appContent!!)
                Preferencestuff(this@MainActivity).setLocalAds(touse)
                //  Toast.makeText(requireContext(), "$appContent", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<AppContent?>, t: Throwable) {
                adspace.visibility = View.GONE
                var touse = Preferencestuff(this@MainActivity).getLocalAds()
                if (touse != ""){
                    var appContent = generateAds(this@MainActivity, touse!!)
                    var adslist = appContent?.ads

                    if (adslist!![0].visible != false) {
                        adspace.visibility = View.VISIBLE
                        adcheck = true
                        if (adslist!![0].SmallImagelink!!.CheckEmpty() != true) {
                            Glide.with(this@MainActivity).load(adslist!![0].SmallImagelink).centerCrop()
                                .into(adimage)
                        } else {
                            adimage.visibility = View.GONE
                            adspace.visibility = View.VISIBLE
                        }
                        message.text = adslist!![0].message
                        val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.adscale)
                        adspace.startAnimation(animation)
                        adspace.setOnClickListener {
                            adspace.clicking()
                            if (adslist!![0].Link != "") {
                                val uriUri = Uri.parse(adslist!![0].Link)
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                                startActivity(launchBrowser)
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${adslist!![0].Description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        })
    }
    private fun getversion(updatelayout : LinearLayout, updatebutton : Button, cancelupdate : CardView){
        var rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface1.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var API = rf.create(RetrofitInterface1::class.java)
        var call =API.versioncheck
        call?.enqueue(object : Callback<Map<String, String>?> {
            override fun onResponse(call: Call<Map<String, String>?>, response: Response<Map<String, String>?>) {
                var versions: Map<String, String>? = response.body() as Map<String, String>
                var UpdateVersion = versions!!["WhatsApp Posts Saver"]

                var versioncode = BuildConfig.VERSION_NAME
                //Toast.makeText(this@MainActivity, "$UpdateVersion and version code $versioncode", Toast.LENGTH_SHORT).show()

                var animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.adscale)
                if (UpdateVersion != versioncode){
                    updatelayout.visibility = View.VISIBLE
                    updatelayout.startAnimation(animation)
                }
                 cancelupdate.setOnClickListener {
                     cancelupdate.clicking()
                     updatelayout.clearAnimation()
                     updatelayout.visibility = View.GONE
                 }
                updatebutton.setOnClickListener {
                    updatebutton.clicking()
                    updatelayout.clearAnimation()
                    updatelayout.visibility = View.GONE
                    val uriUri = Uri.parse("https://play.google.com/store/apps/details?id=ade.yemi.youtubewhatsappsaver")
                    val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                    startActivity(launchBrowser)
                }
            }

            override fun onFailure(call: Call<Map<String, String>?>, t: Throwable) {
            }


        })
    }
}