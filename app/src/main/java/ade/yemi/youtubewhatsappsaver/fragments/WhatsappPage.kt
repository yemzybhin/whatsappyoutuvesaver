package ade.yemi.youtubewhatsappsaver.fragments

import ade.yemi.youtubewhatsappsaver.Activities.MainActivity
import ade.yemi.youtubewhatsappsaver.Adapters.WhatsAppViewAdapter
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.ImageFrag
import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.VideoFrag
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.unity3d.mediation.*
import com.unity3d.mediation.errors.LoadError
import com.unity3d.mediation.errors.SdkInitializationError
import com.unity3d.mediation.errors.ShowError


class WhatsappPage(var WhatsAppType: String) : BaseViewStubFragment() {
    private var interstitialAdUnitId = "Interstitial_Android"
    private var adAvailable = false


    override fun onCreateViewAfterViewStubInflated(view: View, savedInstanceState: Bundle?) {


        var viewpager = view.findViewById<ViewPager>(R.id.viewpager)
        var tabLayout = view.findViewById<TabLayout>(R.id.tablayout)
        var cancel = view.findViewById<CardView>(R.id.cancel)
        var icon = view.findViewById<ImageView>(R.id.icon)
        var title = view.findViewById<TextView>(R.id.titletext)
        if (WhatsAppType == "WhatsApp"){
            icon.setImageResource(R.drawable.whatsapp2)
            title.text = "WhatsApp"
        }else{
            icon.setImageResource(R.drawable.whatsbusiness2)
            title.text = "WhatsApp\nBusiness"
        }

        val animation = AnimationUtils.loadAnimation(context, R.anim.fading)
        icon.startAnimation(animation)


        val whatsAppViewAdapter = WhatsAppViewAdapter(childFragmentManager)
        whatsAppViewAdapter.addfragment(VideoFrag(WhatsAppType), "VIDEOS")
        whatsAppViewAdapter.addfragment(ImageFrag(WhatsAppType), "IMAGES")
        viewpager.adapter = whatsAppViewAdapter
        tabLayout.setupWithViewPager(viewpager)

        var initializationConfiguration = InitializationConfiguration.builder()
                .setGameId(UNITY_GAME_ID)
                .setInitializationListener(object : IInitializationListener {
                    override fun onInitializationComplete() {
                    }
                    override fun onInitializationFailed(p0: SdkInitializationError?, p1: String?) {
                    }
                }).build()

        UnityMediation.initialize(initializationConfiguration)
        var interstitialAd = InterstitialAd(requireActivity(), interstitialAdUnitId)
        val loadListener: IInterstitialAdLoadListener = object : IInterstitialAdLoadListener {
            var reload = this
            override fun onInterstitialLoaded(ad: InterstitialAd) {
                adAvailable = true
                val showListener: IInterstitialAdShowListener = object : IInterstitialAdShowListener {
                    override fun onInterstitialShowed(interstitialAd: InterstitialAd) {
                        // The ad has started to show.
                    }
                    override fun onInterstitialClicked(interstitialAd: InterstitialAd) {
                        // The user has selected the ad.
                    }
                    override fun onInterstitialClosed(interstitialAd: InterstitialAd) {
                        adAvailable = false
                        Handler().postDelayed({
                            interstitialAd.load(reload)
                        }, 15000)
                    }
                    override fun onInterstitialFailedShow(interstitialAd: InterstitialAd, error: ShowError, msg: String) {
                        adAvailable = false
                    }
                }
                interstitialAd.show(showListener)
            }
            override fun onInterstitialFailedLoad(ad: InterstitialAd, error: LoadError, msg: String) {
               // Toast.makeText(requireContext(), "$error, $msg", Toast.LENGTH_SHORT).show()
            }
        }
        interstitialAd.load(loadListener)

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (tabLayout.selectedTabPosition == 1) {
//                    Toast.makeText(requireContext(), "Selected images", Toast.LENGTH_SHORT).show()
//                }
//                Handler().postDelayed({
//                    viewpager.setCurrentItem(tabLayout.selectedTabPosition)
//                },10)
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//        })
        cancel.setOnClickListener {
            cancel.clicking()
            if (adAvailable == false){
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }

        }
    }
    override fun getViewStubLayoutResource(): Int {
       return  R.layout.fragment_whatsapp_page
    }

}