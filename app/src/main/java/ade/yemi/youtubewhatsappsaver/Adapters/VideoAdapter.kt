package ade.yemi.youtubewhatsappsaver.Adapters

import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import ade.yemi.youtubewhatsappsaver.Ultilities.galleryrefresh
import ade.yemi.youtubewhatsappsaver.Ultilities.loading
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class VideoAdapter(private val items: ArrayList<String>, var context: Context): RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
                R.layout.singleimage,
                parent,
                false
        )
        return MyViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(items[position], context)
        var data = items[position]
        holder.itemView.setOnClickListener {
            holder.itemView.clicking()
            watchadwhatsappp(5, context, data)
        }
    }
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.imagetoshow)
        val saveimage = view.findViewById<TextView>(R.id.imagesave)
        fun bind(data: String, context: Context) {
            var bmThumbnail: Bitmap?
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(data, MediaStore.Video.Thumbnails.MINI_KIND)
            image.setImageBitmap(bmThumbnail)

            saveimage.setOnClickListener {
                saveimage.clicking()
                watchadwhatsappp(5, context, data)
            }
        }
    }
}
fun watchadwhatsappp(points: Int, context: Context, imagepath: String) {
    var view = Dialog(context)
    view.setCancelable(true)
    view.setContentView(R.layout.watchad)
    view.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    view.show()
    var yeswatch = view.findViewById<TextView>(R.id.yeswatch)
    var title = view.findViewById<TextView>(R.id.title)
    var point = view.findViewById<TextView>(R.id.points)
    var comment = view.findViewById<TextView>(R.id.comment)

    title.text = "Saving WhatsApp Video requires $points points"
    var preferencestuff = Preferencestuff(context)
    var currentpoint = preferencestuff.getPoint()


    point.text = "$currentpoint Points"
    if (currentpoint >= points) {
        comment.text = "You Have Enough Points"
        yeswatch.setBackgroundResource(R.color.primarycolor3)
        yeswatch.setTextColor(Color.parseColor("#203640"))
        yeswatch.text = "Save Video"

    } else {
        comment.text = "You Do Not Have Enough Points"
        yeswatch.setBackgroundResource(R.color.primarycolor1)
        yeswatch.setTextColor(Color.parseColor("#FFFFFFFF"))
    }
    yeswatch.setOnClickListener {
        if (currentpoint >= points) {
            val path: String = imagepath
            val uuid: UUID = UUID.randomUUID()

            try {
                val currentFile = File(path)
                val loc = Environment.getExternalStorageDirectory()
                val directory = File(loc.absolutePath.toString() + "/Download")
                directory.mkdir()
                val fileName = String.format(uuid.toString() + ".mp4")
                val newfile = File(directory, fileName)
                if (currentFile.exists()) {
                    val inputStream: InputStream = FileInputStream(currentFile)
                    val outputStream: OutputStream = FileOutputStream(newfile)
                    val buf = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                    outputStream.flush()
                    inputStream.close()
                    outputStream.close()
                    Toast.makeText(context, "Video has just saved!!", Toast.LENGTH_LONG).show()
                    preferencestuff.setPoint(preferencestuff.getPoint() - points)
                    galleryrefresh(context, newfile)

                } else {
                    Toast.makeText(context, "Video has failed for saving!!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
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

