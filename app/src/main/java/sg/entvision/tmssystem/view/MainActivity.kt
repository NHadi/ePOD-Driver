package sg.entvision.tmssystem.view

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_navigate.*
import sg.entvision.tmssystem.R
import sg.entvision.tmssystem.base.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_navigate) {
    override fun viewReady(savedInstanceState: Bundle?) {
        btnTakeJob.setOnClickListener {
            startActivity(Intent(this, ContainerActivity::class.java))
        }
    }
}