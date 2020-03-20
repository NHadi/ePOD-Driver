package sg.entvision.tmssystem.view.delivery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.adapter.UndeliveredAdapter
import sg.entvision.tmssystem.base.BaseMVVMFragment
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.utility.MarginItemDecoration
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment
import sg.entvision.tmssystem.widget.CustomDialerAction

class UndeliveredDeliveryFragment :
    BaseMVVMFragment<DeliveryViewModel>(R.layout.each_list_fragment, DeliveryViewModel::class) {
    private var twoPane: Boolean = false

    override fun viewReady(view: View) {
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = UndeliveredAdapter(
            requireActivity(),
            DummyPendingAdapter.ITEMS,
            twoPane,
            object : UndeliveredAdapter.ClickListener {
                override fun dialAction(phone: String) {
                    CustomDialerAction(
                        requireActivity(),
                        phone,
                        false,
                        object : CustomDialerAction.ClickListener {
                            override fun Call(_phone: String) {
                                var intent = Intent(
                                    Intent.ACTION_CALL,
                                    Uri.parse(
                                        "tel:$_phone"
                                    )
                                )
                                startActivity(intent)
                            }

                            override fun Message(_phone: String) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.fromParts("sms", _phone, null)
                                    )
                                )
                            }
                        }
                    ).showDialog()
                }
            })
        recyclerView.addItemDecoration(MarginItemDecoration(10))
    }
}
