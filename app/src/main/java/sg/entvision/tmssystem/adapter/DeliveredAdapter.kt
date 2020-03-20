package sg.entvision.tmssystem.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list_content.view.content
import kotlinx.android.synthetic.main.item_list_content.view.id_text
import kotlinx.android.synthetic.main.item_list_content_delivered.view.*
import kotlinx.android.synthetic.main.item_list_content_pending.view.*
import kotlinx.android.synthetic.main.item_list_content_pending.view.rdButton
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.dummy.DummyDeliveryAdapter
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment

class DeliveredAdapter(
    private val parentActivity: FragmentActivity,
    private val values: List<DummyPendingAdapter.DummyItem>,
    private val twoPane: Boolean,
    private val listener: ClickListenerView
) :
    RecyclerView.Adapter<DeliveredAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private var selected = -1

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
            .inflate(R.layout.item_list_content_delivered, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content

        with(holder.txtReason) {
            tag = item
            setOnClickListener(onClickListener)
        }
        holder.sigButton.setOnClickListener {
            listener.showSignature(item.id.toInt())
        }
        holder.photoButton.setOnClickListener {
            listener.showPhoto(item.id.toInt())
        }
        with(holder.itemView) {
            tag = item
            setOnClickListener {
                selected = position
                notifyDataSetChanged()
            }
        }
    }

    interface ClickListenerView {
        fun showPhoto(id: Int)
        fun showSignature(id: Int)
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
        val txtReason = view.txtReason
        val rdButton = view.rdButton
        val sigButton = view.btnSignature
        val photoButton = view.btnCapture
    }

    interface showSelectionJob {
        fun takeJob(boolean: Boolean)
    }
}