package ade.yemi.youtubewhatsappsaver.Ultilities

import ade.yemi.moreapps.models.AllAppDetails
import ade.yemi.moreapps.models.AppContent
import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


fun View.clicking(){
    val animation = AnimationUtils.loadAnimation(context, R.anim.click)
    this.startAnimation(animation)
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(10)
    }
}
@SuppressLint("NewApi")
fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
fun loading(context: Context){
    var load = Dialog(context)
    load.setCancelable(false)
    load.setContentView(R.layout.loading_popuup)
    load.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    load.show()

}
fun String.CheckEmpty(): Boolean{
    var text1 = this.replace(" ", "")
    if (text1.isEmpty()){
        return true
    }else{
        return false
    }
}
fun galleryrefresh(context: Context, file: File){
    var contentUri = Uri.fromFile(file)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
        var mediaSCanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
       // var f = File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        mediaSCanIntent.setData(contentUri)
        context.sendBroadcast(mediaSCanIntent)
    }else{
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, contentUri))
    }
}

fun AdToJsonString(appContent : AppContent) : String{
    val gson = Gson()
    val json = gson.toJson(appContent)
    return json
}

fun generateAds(context: Context, jsonString : String) : AppContent {
    val jsonFileString = jsonString
    val gson = Gson()
    val listQuestionType = object : TypeToken<AppContent>() {}.type
    var Question1models: AppContent = gson.fromJson(jsonFileString, listQuestionType)
    return Question1models
}

fun AppToJsonString(appDetails: AllAppDetails) : String{
    val gson = Gson()
    val json = gson.toJson(appDetails)
    return json
}

fun generateApps(context: Context, jsonString : String) : AllAppDetails {
    val jsonFileString = jsonString
    val gson = Gson()
    val listQuestionType = object : TypeToken<AllAppDetails>() {}.type
    var Question1models: AllAppDetails = gson.fromJson(jsonFileString, listQuestionType)
    return Question1models
}
