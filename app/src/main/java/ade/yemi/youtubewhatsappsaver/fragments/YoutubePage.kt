package ade.yemi.youtubewhatsappsaver.fragments

import android.os.Bundle
import android.view.View
import ade.yemi.youtubewhatsappsaver.R
class YoutubePage : BaseViewStubFragment() {
    private var adcheck = false
    override fun onCreateViewAfterViewStubInflated(
        view: View,
        savedInstanceState: Bundle?
    ) {
//        var videolink = view.findViewById<EditText>(R.id.videolink)
//        var watcha = view.findViewById<Button>(R.id.proceedtowatchadd)
//        var cancel = view.findViewById<CardView>(R.id.cancel)
//
//
//        var adslayout = view.findViewById<LinearLayout>(R.id.adspace)
//        var message = view.findViewById<TextView>(R.id.addmessage)
//        var title = view.findViewById<TextView>(R.id.addtitle)
//        var description = view.findViewById<TextView>(R.id.addescription)
//        var adimage = view.findViewById<ImageView>(R.id.adimage)
//        var adbutton = view.findViewById<TextView>(R.id.adbuttonlink)
//        var postad = view.findViewById<TextView>(R.id.postanad)
//        var videolink2 = view.findViewById<CardView>(R.id.watchadvideo)
//
//
//        var loadinglayout = view.findViewById<ProgressBar>(R.id.instructionsloading)
//        var youtubelayout = view.findViewById<LinearLayout>(R.id.youtubestuff)
//        var reloadlayout = view.findViewById<Button>(R.id.reloadlayout)
//
//        reloadlayout.visibility = View.GONE
//        youtubelayout.visibility = View.GONE

//        getdata1(loadinglayout, youtubelayout, reloadlayout)
//        reloadlayout.setOnClickListener {
//            reloadlayout.clicking()
//            getdata1(loadinglayout, youtubelayout, reloadlayout)
//        }
//        adslayout.visibility = View.GONE
//        getdata(videolink2, adslayout, message, title, description, adimage, adbutton, postad)
//
//
//
//        var icon = view.findViewById<ImageView>(R.id.icon)
//        val animation = AnimationUtils.loadAnimation(context, R.anim.fading)
//        icon.startAnimation(animation)
//
//        var link = videolink.text
//        watcha.setOnClickListener {
//            watcha.clicking()
//            var checkpermis = (activity as Activity2).checkpermmission()
//            if (checkpermis == true){
//                when{
//                    videolink.text.isEmpty() -> Toast.makeText(requireContext(), "Please paste a link", Toast.LENGTH_SHORT).show()
//                    !videolink.text.contains("you") -> Toast.makeText(requireContext(), "This is not a youtube Link", Toast.LENGTH_SHORT).show()
//                    videolink.text.isNotEmpty() && videolink.text.contains("you")->{
//                        watchad(link.toString(),10, requireContext(), "Youtube Video Download")
//                    }
//                    else -> Toast.makeText(requireContext(), "Invalid Link Format", Toast.LENGTH_SHORT).show()
//                }
//            }
//            else{
//                (activity as Activity2).checkperm2()
//            }
//
//        }
//        cancel.setOnClickListener {
//            cancel.clicking()
//            startActivity(Intent(requireContext(), MainActivity::class.java))
//        }
    }
    override fun getViewStubLayoutResource(): Int {
        return R.layout.fragment_youtube_page
    }
//    private fun getdata( videolink : CardView, adspace: LinearLayout, message : TextView, title : TextView, description: TextView, adimage : ImageView, adbutton: TextView, postad : TextView){
//
//        var rf = Retrofit.Builder()
//                .baseUrl(RetrofitInterface1.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        var API = rf.create(RetrofitInterface1::class.java)
//        var call =API.post
//        call?.enqueue(object : Callback<AppContent?> {
//            override fun onResponse(
//                    call: Call<AppContent?>,
//                    response: Response<AppContent?>
//            ) {
//                var appContent: AppContent? = response.body() as AppContent
//                var adslist = appContent?.ads
//
//
//
//                if (adslist!![0].visible != false){
//                    adspace.visibility= View.VISIBLE
//                    message.text= adslist!![0].message
//                    title.text = adslist!![0].Title!!.toUpperCase()
//                    adcheck = true
//                    if (adslist!![0].VideoLink != null){
//                        videolink.visibility = View.VISIBLE
//                        videolink.setOnClickListener {
//                            videolink.clicking()
//                            val uriUri = Uri.parse(adslist!![0].VideoLink)
//                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
//                            startActivity(launchBrowser)
//                        }
//                    }else{
//                        videolink.visibility = View.GONE
//                    }
//                    if (adslist!![0].Imagelink!!.CheckEmpty() != true){
//                        Glide.with(requireContext()).load(adslist!![0].Imagelink).into(adimage)
//                    }else{
//                        adimage.visibility = View.GONE
//                    }
//                    if (adslist!![0].Description!!.CheckEmpty() != true){
//                        description.visibility = View.VISIBLE
//                        description.text = adslist!![0].Description
//                    }else{
//                        description.visibility = View.GONE
//                    }
//
//                    if (adslist!![0].Link != ""){
//                        adbutton.setOnClickListener {
//                            adbutton.clicking()
//                            val uriUri = Uri.parse(adslist!![0].Link)
//                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
//                            startActivity(launchBrowser) }
//                        val animation = AnimationUtils.loadAnimation(context, R.anim.notification)
//                        postad.startAnimation(animation)
//                        postad.setOnClickListener {
//                            val uriUri = Uri.parse("https://wa.me/message/ECPGO4HTT5LCN1")
//                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
//                            startActivity(launchBrowser)
//                        }
//                    }else{
//                        adbutton.visibility = View.GONE
//                        postad.visibility = View.GONE
//                    }
//                }
//            }
//            override fun onFailure(call: Call<AppContent?>, t: Throwable) {
//                adspace.visibility= View.GONE
//            }
//        })
//    }
//
//    private fun getdata1(progressBar: ProgressBar, linearLayout: LinearLayout, reload : Button){
//
//        var rf = Retrofit.Builder()
//                .baseUrl(RetrofitInterface2.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        var API = rf.create(RetrofitInterface2::class.java)
//        var call =API.post
//        call?.enqueue(object : Callback<ShowYoutubeLayout?> {
//            override fun onResponse(
//                    call: Call<ShowYoutubeLayout?>,
//                    response: Response<ShowYoutubeLayout?>
//            ) {
//                var appContent: ShowYoutubeLayout? = response.body() as ShowYoutubeLayout
//                progressBar.visibility = View.GONE
//                reload.visibility = View.GONE
//
//                var check = appContent!!.success
//
//                if (check == true) {
//                    linearLayout.visibility = View.VISIBLE
//                } else {
//                    linearLayout.visibility = View.GONE
//                }
//            }
//            override fun onFailure(call: Call<ShowYoutubeLayout?>, t: Throwable) {
//                Toast.makeText(requireContext(), "Kindly check your internet connection.", Toast.LENGTH_SHORT).show()
//                reload.visibility = View.VISIBLE
//                progressBar.visibility = View.GONE
//            }
//        })
//    }
}