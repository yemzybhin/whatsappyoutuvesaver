package ade.yemi.youtubewhatsappsaver.Activities

import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.fragments.AboutsPage
import ade.yemi.youtubewhatsappsaver.fragments.WhatsappPage
import ade.yemi.youtubewhatsappsaver.fragments.YoutubePage
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.lang.Exception

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        var incomming = intent.getStringExtra("FragmentToSetTo")
        when(incomming){
            "Whatsapp" -> {
                var fragment = WhatsappPage()
                replacefragment(fragment)
            }
            "Youtube" -> {
                var fragment = YoutubePage()
                replacefragment(fragment)
            }
            "Abouts" -> {
                var fragment = AboutsPage()
                replacefragment(fragment)
            }
            else -> {
                var fragment = YoutubePage()
                replacefragment(fragment)
            }

        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
       // super.onBackPressed()
    }

    internal fun replacefragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        fragmentTransaction.replace(R.id.fragmenttoset, fragment)
        fragmentTransaction.commit()
    }
    fun checkpermmission() : Boolean{
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false
        } else {
            return true
        }
    }
    fun checkperm2(){
        var REQUEST_CODE = 200
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
    }
    fun savevideo( file: File){
        Toast.makeText(this, "$file", Toast.LENGTH_SHORT).show()
//        var file = File(filepath)
//        var fileuri = Uri.fromFile(file)
//        val inputStream = contentResolver.openInputStream(fileuri)
//        var filename = "WhatsApp Status ${System.currentTimeMillis()}.mp4"
//        try {
//            val values = ContentValues()
//            values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
//            values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
//            values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOCUMENTS+"/Videos/")
//            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"),values)
//            val outputStream = uri?.let {
//                contentResolver.openOutputStream(it)!!
//            }
//            if (inputStream != null){
//                outputStream?.write(inputStream.readBytes())
//            }
//            outputStream?.close()
//            Toast.makeText(this, "Video Saved", Toast.LENGTH_SHORT).show()
//        }catch (e : Exception){
//            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//        }
    }
    fun loading(){
        var load = Dialog(this)
        load.setCancelable(false)
        load.setContentView(R.layout.loading_popuup)
        load.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        load.show()
    }

}