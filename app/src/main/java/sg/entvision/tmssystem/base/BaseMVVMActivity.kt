package sg.entvision.tmssystem.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseMVVMActivity<out VM : ViewModel>(@LayoutRes layoutResourceId: Int, viewModelClass: KClass<VM>) :
    BaseActivity(layoutResourceId) {

    protected open val viewModel: VM by viewModel<VM>(viewModelClass)

}