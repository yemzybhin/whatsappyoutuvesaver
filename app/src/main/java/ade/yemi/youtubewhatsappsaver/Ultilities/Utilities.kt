package ade.yemi.youtubewhatsappsaver.Ultilities

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
import android.widget.Toast
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeUriExtractor
import at.huber.youtubeExtractor.YtFile
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
fun watchad(link : String, points : Int, context: Context, todo : String){
    var view = Dialog(context)
    view.setCancelable(true)
    view.setContentView(R.layout.watchad)
    view.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    view.show()
    var yeswatch = view.findViewById<TextView>(R.id.yeswatch)
    var title = view.findViewById<TextView>(R.id.title)
    var point = view.findViewById<TextView>(R.id.points)
    var comment = view.findViewById<TextView>(R.id.comment)

    title.text = "$todo requires $points points"
    var preferencestuff = Preferencestuff(context)
    var currentpoint = preferencestuff.getPoint()


    point.text = "$currentpoint Points"
    if (currentpoint >= points){
        comment.text = "You Have Enough Points"
        yeswatch.setBackgroundResource(R.color.primarycolor3)
        yeswatch.setTextColor(Color.parseColor("#203640"))
        when(todo){
            "Youtube Video Download" -> {
                yeswatch.text = "Download Video"
            }
            "Whatsapp Save Image" -> {
                yeswatch.text = "Save Image"
            }
            "Whatsapp Save Video" -> {
                yeswatch.text = "Save Video"
            }
        }
    }else{
        comment.text = "You Do Not Have Enough Points"
        yeswatch.setBackgroundResource(R.color.primarycolor1)
        yeswatch.setTextColor(Color.parseColor("#FFFFFFFF"))
    }
    yeswatch.setOnClickListener {
        if (currentpoint >= points){
            when(todo){
                "Youtube Video Download" -> {
                    if (isOnline(context) == true){
                        downloadvideo(link, context)
                        Toast.makeText(context, "Please Wait!!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                    }
                }
                "Whatsapp Save Image" -> {

                }
                "Whatsapp Save Video" -> {

                }
            }

        }else{
            loading(context)
            Handler().postDelayed({
                var intent = Intent(context , Activity2 :: class.java)
                intent.putExtra("FragmentToSetTo", "Abouts")
                context.startActivity(intent)
            },0)
        }

        view.dismiss()
    }
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
fun downloadvideo(values : String, context: Context){
    var filename = "AllDownloader_${System.currentTimeMillis()}.mp4"
    var newlink = ""
    var youTubeUriExtractor = object  : YouTubeUriExtractor(context){
        override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
        ) {
            if(ytFiles != null){
                super.onExtractionComplete(ytFiles, videoMeta)
            }else{
                Toast.makeText(context, "Could not extract video link", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onUrisAvailable(
                videoId: String?,
                videoTitle: String?,
                ytFiles: SparseArray<YtFile>?
        ) {
            if (ytFiles!=null){
                var tag = 22
                newlink = ytFiles.get(tag).url
                //Log.e("to copy the ytfile", "${ytFiles.get(tag)}")
                //Toast.makeText(requireContext(), "me", Toast.LENGTH_SHORT).show()
                var title = "$videoTitle"
                var request = DownloadManager.Request(Uri.parse(newlink))

                request.setTitle(title)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                //request.setDestinationInExternalFilesDir(requireContext(),Environment.DIRECTORY_DOWNLOADS, filename)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                request.allowScanningByMediaScanner()
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                manager!!.enqueue(request)
            }
        }
    }
    youTubeUriExtractor.execute(values)
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
