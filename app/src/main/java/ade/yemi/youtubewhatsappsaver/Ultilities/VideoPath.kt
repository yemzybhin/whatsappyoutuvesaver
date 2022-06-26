package ade.yemi.youtubewhatsappsaver.Ultilities

import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.VideoFrag
import android.os.Environment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun getVideoPath(): ArrayList<String> {
    // image path list
    val list: ArrayList<String> = ArrayList()
    // fetching file path from storage
    val file = File(Environment.getExternalStorageDirectory().toString() + VideoFrag.WHATSAPP_STATUS_FOLDER_PATHH)
    val file2 = File(Environment.getExternalStorageDirectory().toString() + VideoFrag.WHATSAPP_STATUS_FOLDER_PATHH2)
    val listFile = file.listFiles()
    val listFile2 = file2.listFiles()


    if (listFile != null && listFile.isNullOrEmpty()) {
        Arrays.sort(listFile)
    }
    if (listFile != null) {
        for (imgFile in listFile) {
            if (
                    imgFile.name.endsWith(".gif")
                    || imgFile.name.endsWith(".mp4")
            ) {
                val model = imgFile.absolutePath
                list.add(model)
            }
        }
    }

    if (listFile2 != null && listFile2.isNullOrEmpty()) {
        Arrays.sort(listFile2)
    }
    if (listFile2 != null) {
        for (imgFile in listFile2) {
            if (
                    imgFile.name.endsWith(".gif")
                    || imgFile.name.endsWith(".mp4")
            ) {
                val model = imgFile.absolutePath
                list.add(model)
            }
        }
    }
    // return imgPath List
    return list
}