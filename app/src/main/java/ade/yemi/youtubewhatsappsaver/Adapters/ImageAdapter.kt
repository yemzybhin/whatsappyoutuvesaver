package ade.yemi.youtubewhatsappsaver.Adapters

import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.clicking
import ade.yemi.youtubewhatsappsaver.Ultilities.loading
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream


class ImageAdapter(private val items: ArrayList<String>, var context: Context): RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {


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

    override fun onBindViewHolder(holder: ImageAdapter.MyViewHolder, position: Int) {

        holder.bind(items[position], context)
        var data = items[position]
        holder.itemView.setOnClickListener {
            holder.itemView.clicking()
            watchadwhatsapp(2, context, data, "Whatsapp Save Image")
        }
    }
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.imagetoshow)
        val saveimage = view.findViewById<TextView>(R.id.imagesave)
        fun bind(data: String, context: Context) {

            val path: String = data
            val imgFile = File(path)
            val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
            image.setImageBitmap(myBitmap)
            saveimage.setOnClickListener {
                saveimage.clicking()
                    watchadwhatsapp(2, context, data, "Whatsapp Save Image")
            }
        }
    }
}

fun Bitmap.saveImage(context: Context): Uri? {
    if (android.os.Build.VERSION.SDK_INT >= 29) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "img_${SystemClock.uptimeMillis()}")

        val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            return uri
        }
    } else {
        val directory =
                File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        .toString() + separator + "test_pictures"
                )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName =  "img_${SystemClock.uptimeMillis()}"+ ".jpeg"
        val file = File(directory, fileName)
        saveImageToStream(this, FileOutputStream(file))
        if (file.absolutePath != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return Uri.fromFile(file)
        }
    }
    return null
}
fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
fun watchadwhatsapp(points: Int, context: Context, imagepath: String, todo: String){
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
            val path: String = imagepath
            val imgFile = File(path)
            val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
            when(todo){
                "Whatsapp Save Image" -> {
                    try {
                        myBitmap.saveImage(context)
                        Toast.makeText(
                            context,
                            "Image Saved Successfully, $points points removed",
                            Toast.LENGTH_SHORT
                        ).show()
                        preferencestuff.setPoint(preferencestuff.getPoint() - points)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error Saving Image", Toast.LENGTH_SHORT).show()
                    }
                }
                "Whatsapp Save Video" -> {
                    Toast.makeText(context, "$imagepath", Toast.LENGTH_SHORT).show()

                    Activity2().savevideo(imgFile)
                    //saveVideoToInternalStorage(imagepath, context)
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

