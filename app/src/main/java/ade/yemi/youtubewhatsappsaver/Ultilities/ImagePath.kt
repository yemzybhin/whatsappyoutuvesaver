package ade.yemi.youtubewhatsappsaver.Ultilities

import ade.yemi.youtubewhatsappsaver.fragments.WhatsAppViewPager.ImageFrag
import android.os.Environment
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun getImagePath(whatsApptype : String): ArrayList<String> {
    // image path list
    // fetching file path from storage
    if (whatsApptype == "WhatsApp"){
        val list: ArrayList<String> = ArrayList()
        val file = File(Environment.getExternalStorageDirectory().toString() + ImageFrag.WHATSAPP_STATUS_FOLDER_PATH)
        val listFile = file.listFiles()
        if (listFile != null && listFile.isNullOrEmpty()) {
            Arrays.sort(listFile)
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
        return list
    } else{
        val list: ArrayList<String> = ArrayList()
        val file2 = File(Environment.getExternalStorageDirectory().toString() + ImageFrag.WHATSAPP_STATUS_FOLDER_PATH2)
        val listFile2 = file2.listFiles()
        if (listFile2 != null && listFile2.isNullOrEmpty()) {
            Arrays.sort(listFile2)
        }
        if (listFile2 != null) {
            for (imgFile in listFile2) {
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
}