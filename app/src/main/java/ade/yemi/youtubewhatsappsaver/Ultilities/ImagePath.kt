package ade.yemi.youtubewhatsappsaver.Ultilities

import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.ImageFrag
import android.os.Environment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun getImagePath(): ArrayList<String> {
    // image path list
    val list: ArrayList<String> = ArrayList()
    // fetching file path from storage
    val file = File(Environment.getExternalStorageDirectory().toString() + ImageFrag.WHATSAPP_STATUS_FOLDER_PATH)
    val listFile = file.listFiles()
    if (listFile != null && listFile.isNullOrEmpty()) {
        Arrays.sort(listFile)//, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
    }
    if (listFile != null) {
        for (imgFile in listFile) {
            if (
                    imgFile.name.endsWith(".jpg")
                    || imgFile.name.endsWith(".jpeg")
                    || imgFile.name.endsWith(".png")
            ) {
                val model = imgFile.absolutePath
                list.add(model)
            }
        }
    }
    // return imgPath List
    return list
}