package sg.entvision.tmssystem.di

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sg.entvision.tmssystem.view.delivery_detail.ItemViewModel
import sg.entvision.tmssystem.view.direction.DirectionViewModel

val viewModelModule = module {
    viewModel { ItemViewModel(androidApplication(), get()) }
    viewModel { DirectionViewModel(get()) }
}