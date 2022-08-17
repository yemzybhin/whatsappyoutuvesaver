package ade.yemi.youtubewhatsappsaver.Activities

import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.fragments.AboutsPage
import ade.yemi.youtubewhatsappsaver.fragments.WhatsappPage
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        var incomming = intent.getStringExtra("FragmentToSetTo")
        when(incomming){
            "Whatsapp" -> {
                var incomming2 = intent.getStringExtra("WhatsAppType")
                var fragment = WhatsappPage(incomming2!!)
                replacefragment(fragment)
            }
            "Abouts" -> {
                var fragment = AboutsPage()
                replacefragment(fragment)
            }
            else -> {
                var fragment = AboutsPage()
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
    }
    fun loading(){
        var load = Dialog(this)
        load.setCancelable(false)
        load.setContentView(R.layout.loading_popuup)
        load.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        load.show()
    }
}