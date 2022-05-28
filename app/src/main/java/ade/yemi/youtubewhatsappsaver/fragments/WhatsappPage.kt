package ade.yemi.youtubewhatsappsaver.fragments

import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Activities.MainActivity
import ade.yemi.youtubewhatsappsaver.Adapters.WhatsAppViewAdapter
import android.os.Bundle
import android.view.View
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.ImageFrag
import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.VideoFrag
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class WhatsappPage : BaseViewStubFragment() {
    override fun onCreateViewAfterViewStubInflated(view: View, savedInstanceState: Bundle?) {
        var viewpager = view.findViewById<ViewPager>(R.id.viewpager)
        var tabLayout = view.findViewById<TabLayout>(R.id.tablayout)
        var cancel = view.findViewById<CardView>(R.id.cancel)
        var icon = view.findViewById<ImageView>(R.id.icon)
        val animation = AnimationUtils.loadAnimation(context, R.anim.fading)
        icon.startAnimation(animation)
        val whatsAppViewAdapter = WhatsAppViewAdapter(childFragmentManager)
        whatsAppViewAdapter.addfragment(VideoFrag(), "VIDEOS")
        whatsAppViewAdapter.addfragment(ImageFrag(), "IMAGES")
        viewpager.adapter = whatsAppViewAdapter
        tabLayout.setupWithViewPager(viewpager)

        cancel.setOnClickListener {
            cancel.clicking()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
    override fun getViewStubLayoutResource(): Int {
       return  R.layout.fragment_whatsapp_page
    }

}