package ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager

import ade.yemi.youtubewhatsappsaver.Adapters.ImageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.getImagePath
import ade.yemi.youtubewhatsappsaver.fragments.BaseViewStubFragment
import android.os.Environment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ImageFrag : BaseViewStubFragment(){
    companion object {
        const val WHATSAPP_STATUS_FOLDER_PATH = "/WhatsApp/Media/.Statuses/"
    }
    private var recyclerView : RecyclerView? = null
    private var gridLayoutManager : GridLayoutManager? = null
    private var imageAdapter : ImageAdapter ? = null
    override fun onCreateViewAfterViewStubInflated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.recyclerview)
        gridLayoutManager = GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        var imagepaths = getImagePath()
        imageAdapter = ImageAdapter(imagepaths, requireContext())
        recyclerView?.adapter = imageAdapter
    }

    override fun getViewStubLayoutResource(): Int {
        return R.layout.fragment_image
    }
}