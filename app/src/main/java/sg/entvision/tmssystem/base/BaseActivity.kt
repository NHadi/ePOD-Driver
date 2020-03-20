package sg.entvision.tmssystem.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

abstract class BaseActivity(var layoutresourceId: Int) : AppCompatActivity(),LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        viewReady(savedInstanceState)
    }

    @LayoutRes
    fun getLayout(): Int = layoutresourceId

    abstract fun viewReady(savedInstanceState: Bundle?)

}