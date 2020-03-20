package sg.entvision.tmssystem.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list_content.view.content
import kotlinx.android.synthetic.main.item_list_content.view.id_text
import kotlinx.android.synthetic.main.item_list_content_activate.view.*
import kotlinx.android.synthetic.main.item_list_content_pending.view.*
import kotlinx.android.synthetic.main.item_list_content_pending.view.btnShowDetail
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.dummy.DummyDeliveryAdapter
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment

class DeliveryAdapter(
    private val parentActivity: FragmentActivity,
    private val values: List<DummyDeliveryAdapter.DummyItem>,
    private val twoPane: Boolean,
    private val listener: ActionListener
) :
    RecyclerView.Adapter<DeliveryAdapter.ViewHolder>() {

    private val CAMERA_REQUEST = 89
    private var selected: Int = -1
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as DummyDeliveryAdapter.DummyItem
            if (twoPane) {
                val fragment = ItemDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                            putInt(Consts.TYPE, 99)
                        }
                    }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    putExtra(Consts.TYPE, 99)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content_activate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
        with(holder.btnShowDetail) {
            tag = item
            setOnClickListener(onClickListener)
        }
        with(holder.itemView) {
            tag = item
            setOnClickListener {
                selected = position
                notifyDataSetChanged()
            }
        }
        holder.itemView.apply {
            this.btnSignature.setOnClickListener {
                listener.signature()
            }
            this.btnCapture.setOnClickListener {
                listener.capture()
            }
            this.btnConfirmDelivered.setOnClickListener {
                listener.delivered()
            }
            this.btnUndelivered.setOnClickListener {
                listener.undelivered()
            }
            this.btnRoute.setOnClickListener {
                listener.route()
            }
            this.backHome.setOnClickListener {
                listener.home()
            }
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
        val btnShowDetail = view.btnShowDetail
        val btnCapture: Button = view.btnCapture
    }

    interface ActionListener {
        fun signature()
        fun capture()
        fun delivered()
        fun undelivered()
        fun route()
        fun home()
    }
}