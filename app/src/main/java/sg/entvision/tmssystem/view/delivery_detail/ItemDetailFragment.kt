package sg.entvision.tmssystem.view.delivery_detail

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.base.BaseMVVMFragment
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.utility.extentions.isGPSEnabled
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ContainerActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment :
    BaseMVVMFragment<ItemViewModel>(R.layout.item_detail, ItemViewModel::class) {
    private var mImage: String? = null
    private val CAMERA_REQUEST: Int = 89
    private var item: DummyPendingAdapter.DummyItem? = null
    private var ids = 0
    val DIRECTORY =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/ProfOfDelivery/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var file = File(DIRECTORY)
        if (!file.exists()) {
            file.mkdir()
        }
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = DummyPendingAdapter.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.content
                mImage = "image_${item?.id}"
                ids = item?.id?.toInt()!!
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            prof.visibility = View.VISIBLE
            imgPackageProf.setImageBitmap(photo)
            try {
                FileOutputStream(getImagePath()).use { out ->
                    photo.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                    out.close()
                }
                Toast.makeText(requireContext(), "Photo Has been saved", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getImagePath(): String {
        val pic_name = "image_$ids"
        return "$DIRECTORY$pic_name.jpg"
    }


    private fun getImage(): Bitmap? {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/ProfOfDelivery/"
        val directory = File(path)
        directory.listFiles()?.let {
            val files: Array<File> = it
            Log.e(javaClass.name, "File Size : ${files.size}")
            for (i in files.indices) {
                if (files[i].name.contains("_$ids")) {
                    Log.e(javaClass.name, "Name File : ${files[i].name}")
                    return BitmapFactory.decodeFile(files[i].path)
                }
            }
        }
        return null
    }

    override fun viewReady(view: View) {
        item?.let {
            view.item_detail.text = it.details
        }
        arguments?.let {
            if (it.getInt(Consts.TYPE, 0) == 99) {
                prof.visibility = View.GONE
            } else if (it.getInt(Consts.TYPE, 0) == 90) {
                prof.visibility = View.GONE
                infos.visibility = View.GONE
                containerAction.visibility = View.GONE
            }
        }
        btnCaptureProfPicture.setOnClickListener {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
        btnCallRecipient.setOnClickListener {
            var intent = Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:" + btnCallRecipient.text.toString().replace("Call +", "").trim())
            )
            startActivity(intent)
        }
        btnCallShipper.setOnClickListener {
            var intent = Intent(
                Intent.ACTION_CALL,
                Uri.parse("tel:" + btnCallShipper.text.toString().replace("Call +", "").trim())
            )
            startActivity(intent)
        }
        btnCaptureSignature.setOnClickListener {
            val intent = Intent(requireActivity(), SignatureActivity::class.java)
            intent.putExtra(Consts.ID, ids)
            startActivity(intent)
        }
        getImage()?.let {
            imgPackageProf.setImageBitmap(it)
        }
        btnCaptureQR.setOnClickListener {
            startActivity(Intent(requireActivity(), QRActivity::class.java))
        }
        grantPermission()
    }


    private fun grantPermission() {
        Dexter.withActivity(requireActivity())
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            ).withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        getFromLocation()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {

                }
            }).check()
    }

    fun getFromLocation() =
        if (requireContext().isGPSEnabled()) viewModel.trackLocation() else viewModel.locationSetup()


    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
