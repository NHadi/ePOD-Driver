package sg.entvision.tmssystem.view.delivery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.each_list_fragment.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.adapter.PendingAdapter
import sg.entvision.tmssystem.base.BaseMVVMFragment
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.utility.MarginItemDecoration
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment

class PendingDeliveryFragment :
    BaseMVVMFragment<DeliveryViewModel>(R.layout.each_list_fragment, DeliveryViewModel::class) {

    private var pendingAdapt: PendingAdapter? = null
    private var twoPane: Boolean = false

    override fun onPause() {
        super.onPause()
        pendingAdapt?.selected = -1
        pendingAdapt?.notifyDataSetChanged()
        frameButton.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun viewReady(view: View) {
        setupRecyclerView(item_list)
        txtAccept.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "You take job at ${pendingAdapt?.selected!! + 1}",
                Toast.LENGTH_SHORT
            ).show()
            frameButton.visibility = View.GONE
        }

        txtCancel.setOnClickListener {
            frameButton.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        pendingAdapt = PendingAdapter(
            requireActivity(),
            DummyPendingAdapter.ITEMS,
            twoPane, object : PendingAdapter.showSelectionJob {
                override fun takeJob(boolean: Boolean) {
                    if (boolean) {
                        frameButton.visibility = View.VISIBLE
                    }
                }
            }
        )
        recyclerView.adapter = pendingAdapt
        recyclerView.addItemDecoration(MarginItemDecoration(10))
    }

}