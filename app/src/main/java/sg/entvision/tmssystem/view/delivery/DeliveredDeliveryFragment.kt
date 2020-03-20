package sg.entvision.tmssystem.view.delivery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.adapter.DeliveredAdapter
import sg.entvision.tmssystem.base.BaseMVVMFragment
import sg.entvision.tmssystem.consts.Consts
import sg.entvision.tmssystem.dummy.DummyPendingAdapter
import sg.entvision.tmssystem.utility.MarginItemDecoration
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment
import sg.entvision.tmssystem.view.delivery_detail.PhotoPreview

class DeliveredDeliveryFragment :
    BaseMVVMFragment<DeliveryViewModel>(R.layout.each_list_fragment, DeliveryViewModel::class) {

    private var twoPane: Boolean = false

    override fun viewReady(view: View) {
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            DeliveredAdapter(
                requireActivity(),
                DummyPendingAdapter.ITEMS,
                twoPane,
                object : DeliveredAdapter.ClickListenerView {
                    override fun showPhoto(id: Int) {
                        startActivity(
                            Intent(requireContext(), PhotoPreview::class.java).putExtra(
                                Consts.ID, id
                            ).putExtra(Consts.TYPE, 1)
                        )
                    }

                    override fun showSignature(id: Int) {
                        startActivity(
                            Intent(requireContext(), PhotoPreview::class.java).putExtra(
                                Consts.ID, 0
                            ).putExtra(Consts.TYPE, 0)
                        )
                    }
                }
            )
        recyclerView.addItemDecoration(MarginItemDecoration(10))
    }

}