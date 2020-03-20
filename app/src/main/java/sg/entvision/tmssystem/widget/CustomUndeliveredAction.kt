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
class CustomUndeliveredAction @SuppressLint("ValidFragment") constructor(
    var activity: Activity,
    var cancelable: Boolean,
    var listener: ClickListener
) {

    var dialog: Dialog? = null

    interface ClickListener {
        fun btnPositive(reason: String)
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
        dialog?.setContentView(R.layout.dialog_undelivered)

        val reasonGroup: RadioGroup = dialog?.findViewById(R.id.rgGroup)!!
        val btnSubmit: MaterialButton = dialog?.findViewById(R.id.btnSubmit)!!
        val edtOtherReason: EditText = dialog?.findViewById(R.id.edtOthers)!!
        edtOtherReason.animate().alpha(0F)

        reasonGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.other) {
                edtOtherReason.animate().alpha(1F)
            } else {
                edtOtherReason.animate().alpha(0F)
            }
        }
        btnSubmit.setOnClickListener {
            var reason =
                dialog?.findViewById<RadioButton>(reasonGroup.checkedRadioButtonId)?.text.toString()
            if (reasonGroup.checkedRadioButtonId == R.id.other) {
                reason = edtOtherReason.text.toString()
            }
            dialog?.dismiss()
            listener.btnPositive(reason = reason)
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