package ade.yemi.youtubewhatsappsaver.fragments

import ade.yemi.moreapps.Network.RetrofitInterface1
import ade.yemi.moreapps.models.AppContent
import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Adapters.AllAdsAdapter
import android.os.Bundle
import android.view.View
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AllAds : BaseViewStubFragment(){
    private lateinit var  myAdapter: RecyclerView.Adapter<*>
    private lateinit var manager: RecyclerView.LayoutManager
    override fun onCreateViewAfterViewStubInflated(view: View, savedInstanceState: Bundle?) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        var cancel = view.findViewById<CardView>(R.id.pastquestionscancel)
        var progressBar = view.findViewById<ProgressBar>(R.id.showloading)
        progressBar.visibility = View.VISIBLE
        manager = LinearLayoutManager(requireContext())
        getdata(recyclerView, progressBar)

        cancel.setOnClickListener {
            cancel.clicking()
            (activity as Activity2).replacefragment(AboutsPage())
        }
    }
    override fun getViewStubLayoutResource(): Int {
        return R.layout.fragment_all_ads
    }
    private fun getdata(recyclerView: RecyclerView, progressBar: ProgressBar){
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

                progressBar.visibility = View.GONE

                recyclerView.apply {
                    myAdapter = AllAdsAdapter(adslist!!)
                    layoutManager = manager
                    adapter = myAdapter
                }
            }
            override fun onFailure(call: Call<AppContent?>, t: Throwable) {
                Toast.makeText(requireContext(), "Could not load Ads. Check connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

}