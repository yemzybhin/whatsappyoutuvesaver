package ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager

import ade.yemi.youtubewhatsappsaver.Adapters.ImageAdapter
import ade.yemi.youtubewhatsappsaver.Adapters.VideoAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.getImagePath
import ade.yemi.youtubewhatsappsaver.Ultilities.getVideoPath
import ade.yemi.youtubewhatsappsaver.fragments.BaseViewStubFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VideoFrag : BaseViewStubFragment() {
    companion object {
        const val WHATSAPP_STATUS_FOLDER_PATHH = "/WhatsApp/Media/.Statuses/"
        const val WHATSAPP_STATUS_FOLDER_PATHH2 = "/WhatsApp Business/Media/.Statuses/"
    }
    private var recyclerView : RecyclerView? = null
    private var gridLayoutManager : GridLayoutManager? = null
    private var videoAdapter : VideoAdapter? = null

    override fun onCreateViewAfterViewStubInflated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        var videoPath = getVideoPath()
        videoAdapter = VideoAdapter(videoPath, requireContext())
        recyclerView?.adapter = videoAdapter
    }

    override fun getViewStubLayoutResource(): Int {
        return R.layout.fragment_video
    }
}