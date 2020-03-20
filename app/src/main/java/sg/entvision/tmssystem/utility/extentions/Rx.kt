package sg.entvision.tmssystem.utility.extentions

import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import sg.entvision.tmssystem.utility.rx.Scheduler

fun <T> Flowable<T>.fromWorkerToMain(scheduler: Scheduler): Flowable<T> =
        this.subscribeOn(scheduler.io()).observeOn(scheduler.mainThread())

fun Disposable.addTo(compositeDisposable: CompositeDisposable) =
        compositeDisposable.add(this)
//
//fun <T : Any, U: Any> Observable<T>.filterClass(kClass: KClass<U>): Observable<U> = this
//        .filter { it::class == kClass }
//        .map { it is KClass<U> }
//
//
//fun <T : Any, U: Any> Flowable<T>.filterClass(kClass: KClass<U>): Flowable<U> = this
//        .filter { it::class == kClass }
//        .map { kClass.cast(it) }
