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
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.adapter.DeliveryAdapter
import sg.entvision.tmssystem.base.BaseMVVMFragment
import sg.entvision.tmssystem.dummy.DummyDeliveryAdapter
import sg.entvision.tmssystem.utility.MarginItemDecoration
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailActivity
import sg.entvision.tmssystem.view.delivery_detail.ItemDetailFragment
import sg.entvision.tmssystem.view.delivery_detail.SignatureActivity
import sg.entvision.tmssystem.view.direction.DirectionRouteActivity
import sg.entvision.tmssystem.widget.CustomUndeliveredAction

class ActiveDeliveryFragment :
    BaseMVVMFragment<DeliveryViewModel>(R.layout.each_list_fragment, DeliveryViewModel::class) {

    private var twoPane: Boolean = false
    private val CAMERA_REQUEST: Int = 89

    override fun viewReady(view: View) {
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            DeliveryAdapter(
                requireActivity(),
                DummyDeliveryAdapter.ITEMS,
                twoPane,
                object : DeliveryAdapter.ActionListener {
                    override fun signature() {
                        startActivity(Intent(requireActivity(), SignatureActivity::class.java))
                    }

                    override fun capture() {
                        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    }

                    override fun delivered() {
                        Toast.makeText(
                            requireContext(),
                            "This Delivery Added to Delivered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun undelivered() {
                        CustomUndeliveredAction(
                            requireActivity(),
                            true,
                            object : CustomUndeliveredAction.ClickListener {
                                override fun btnPositive(reason: String) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Cannot delivered because $reason",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }).showDialog()
                    }

                    override fun route() {
                        startActivity(
                            Intent(
                                requireContext(),
                                DirectionRouteActivity::class.java
                            )
                        )
                    }

                    override fun home() {
                        requireActivity().finish()
                    }
                }
            )
        recyclerView.addItemDecoration(MarginItemDecoration(10))
    }


}