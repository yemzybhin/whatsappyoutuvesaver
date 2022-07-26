package ade.yemi.youtubewhatsappsaver.Data

import android.content.Context
import android.content.SharedPreferences

class Preferencestuff {
    var context: Context? = null
    var sharef: SharedPreferences? = null
    constructor(context: Context){
        this.context = context
        this.sharef = context.getSharedPreferences("preferencesstuff", Context.MODE_PRIVATE)
    }
    fun setPoint(int: Int){
        var editor = sharef!!.edit()
        editor.putInt("points", int)
        editor.commit()
    }
    fun getPoint(): Int{
        return sharef!!.getInt("points", 0)
    }
    fun setLocalAds(adsString: String){
        var editor = sharef!!.edit()
        editor.putString("localAds", adsString)
        editor.commit()
    }
    fun getLocalAds(): String?{
        return sharef!!.getString("localAds", "")
    }
    fun setloacalApps(appString: String){
        var editor = sharef!!.edit()
        editor.putString("localApps", appString)
        editor.commit()
    }
    fun getlocalApps(): String?{
        return sharef!!.getString("localApps", "")
    }
}