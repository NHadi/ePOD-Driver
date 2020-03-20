package sg.entvision.tmssystem.view.delivery_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import sg.entvision.tmssystem.R


class QRActivity : AppCompatActivity(), View.OnClickListener,
    ZXingScannerView.ResultHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Capture QRCode from Packag"
        initScannerView()
        initDefaultView()
    }

    private lateinit var mScannerView: ZXingScannerView
    private var isCaptured = false

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        setContentView(mScannerView)
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }

    private fun initDefaultView() {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_reset -> {
                mScannerView.resumeCameraPreview(this)
                initDefaultView()
            }
            else -> {
                /* nothing to do in here */
            }
        }
    }

    override fun handleResult(p0: Result?) {
        Toast.makeText(this@QRActivity, p0?.text, Toast.LENGTH_LONG).show()
        finish()
    }
}