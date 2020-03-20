package sg.entvision.tmssystem.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list_content.view.content
import kotlinx.android.synthetic.main.item_list_content.view.id_text
import kotlinx.android.synthetic.main.item_list_content_undelivered.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.dummy.DummyDeliveryAdapter
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment

class UndeliveredAdapter(
    private val parentActivity: FragmentActivity,
    private val values: List<DummyPendingAdapter.DummyItem>,
    private val twoPane: Boolean,
    private val listener: ClickListener
) :
    RecyclerView.Adapter<UndeliveredAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    interface ClickListener {
        fun dialAction(phone: String)
    }

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyPendingAdapter.DummyItem
            if (twoPane) {
                val fragment = ItemDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content_undelivered, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
        holder.btnDial.setOnClickListener {
            listener.dialAction("6593024567")
        }
        with(holder.reason) {
            tag = item
            text = "Reason : \nWrong Delivery Address $position"
            setOnClickListener(onClickListener)
        }
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
        val reason: TextView = view.txtReason
        val btnDial: MaterialButton = view.btnCall
    }
}