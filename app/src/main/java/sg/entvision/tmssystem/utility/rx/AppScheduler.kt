package sg.entvision.tmssystem.utility.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import sg.entvision.tmssystem.utility.rx.Scheduler

class AppScheduler : Scheduler {
    override fun mainThread() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}