package sg.entvision.tmssystem.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_container.*

import kotlinx.android.synthetic.main.activity_item_list.toolbar
import org.koin.android.ext.android.inject
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.adapter.TabAdapter
import sg.entvision.tmssystem.base.BaseActivity
import sg.entvision.tmssystem.repository.Database
import sg.entvision.tmssystem.view.delivery.ActiveDeliveryFragment
import sg.entvision.tmssystem.view.delivery.DeliveredDeliveryFragment
import sg.entvision.tmssystem.view.delivery.PendingDeliveryFragment
import sg.entvision.tmssystem.view.delivery.UndeliveredDeliveryFragment
import timber.log.Timber

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ContainerActivity : BaseActivity(R.layout.activity_container) {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private val data: Database by inject()

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.top_menu, menu)
//        return true
//    }

    override fun viewReady(savedInstanceState: Bundle?) {
        data.openHelper.writableDatabase
        data.openHelper.readableDatabase
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile -> {

                    true
                }
                R.id.settings -> {

                    true
                }
                else -> {

                    false
                }
            }
        }
        var path = getDatabasePath("TMSAPP.db")
        Timber.e("Path of DB : $path : ${data.openHelper.databaseName}")
        val adapter = TabAdapter(supportFragmentManager)
        adapter.addFragment(ActiveDeliveryFragment(), "Active")
        adapter.addFragment(PendingDeliveryFragment(), "Pending")
        adapter.addFragment(DeliveredDeliveryFragment(), "Delivered")
        adapter.addFragment(UndeliveredDeliveryFragment(), "Undelivered")
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 4
        tabs.setupWithViewPager(viewpager)

    }


}
