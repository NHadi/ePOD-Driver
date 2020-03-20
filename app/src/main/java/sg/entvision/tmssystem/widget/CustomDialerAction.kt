package sg.entvision.tmssystem.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import sg.entvision.tmssystem.R

/**
 * @property sTitle String
 * @property sMessage String
 * @property btnPos String
 * @property btnNeg String
 * @property listener ClickListener
 * @constructor
 */

@SuppressLint("ValidFragment")
class CustomDialerAction @SuppressLint("ValidFragment") constructor(
    var activity: Activity,
    var phone: String,
    var cancelable: Boolean,
    var listener: ClickListener
) {

    var dialog: Dialog? = null

    interface ClickListener {
        fun Call(phone: String)
        fun Message(phone: String)
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showDialog() {
        dialog = Dialog(activity)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(cancelable)
        dialog?.window?.apply {
            setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog?.setContentView(R.layout.call_action)

        val btnDismiss = dialog?.findViewById<MaterialButton>(R.id.btnDismiss)
        val btnCall = dialog?.findViewById<TextView>(R.id.btnCall)
        val btnMessage = dialog?.findViewById<TextView>(R.id.btnMessage)

        btnDismiss?.setOnClickListener {
            dialog?.dismiss()
        }

        btnCall?.setOnClickListener {
            listener.Call(phone)
        }

        btnMessage?.setOnClickListener {
            listener.Message(phone)
        }

        if (!activity.isDestroyed) {
            dialog?.show()
        }
    }

    fun hideDialog() {
        if (dialog != null) {
            dialog?.dismiss()
        }
    }

}