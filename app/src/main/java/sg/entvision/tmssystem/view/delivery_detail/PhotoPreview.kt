package sg.entvision.tmssystem.view.delivery_detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.photo_preview.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.base.BaseActivity
import sg.entvision.tmssystem.consts.Consts
import java.io.File

class PhotoPreview : BaseActivity(R.layout.photo_preview) {
    private var type: Int = 0
    private var ids: Int = 0

    override fun viewReady(savedInstanceState: Bundle?) {
        ids = intent.getIntExtra(Consts.ID, 0)
        type = intent.getIntExtra(Consts.TYPE, 0)
        if (type == 1) {
            title = "Photo Preview"
            getImage()?.let {
                imgPreview.setImageBitmap(it)
            } ?: Toast.makeText(this@PhotoPreview, "Photo Not Found", Toast.LENGTH_SHORT).show()
        } else {
            title = "Signature Preview"
            getSignature()?.let {
                imgPreview.setImageBitmap(it)
            } ?: Toast.makeText(this@PhotoPreview, "Signature Not Found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImage(): Bitmap? {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/ProfOfDelivery/"
        val directory = File(path)
        directory.listFiles()?.let {
            val files: Array<File> = it
            Log.e(javaClass.name, "File Size : ${files.size}")
//            for (i in files.indices) {
            if (files.isNotEmpty()) {
                if (files[0] != null) {
//                    Log.e(javaClass.name, "Name File : ${files[i].name}")
                    return BitmapFactory.decodeFile(files[0].path)
                }
            } else {
                return null
            }
//            }
        }
        return null
    }

    private fun getSignature(): Bitmap? {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/UserSignature/"
        val directory = File(path)
        directory.listFiles()?.let {
            val files: Array<File> = it
            Log.e(javaClass.name, "File Size : ${files.size}")
//            for (i in files.indices) {
//                if (files[i].name.contains("${ids}_")) {
//                    Log.e(javaClass.name, "Name File : ${files[i].name}")
            if (files[0] != null) {
                return BitmapFactory.decodeFile(files[0].path)
            }
        }
        return null
    }

}