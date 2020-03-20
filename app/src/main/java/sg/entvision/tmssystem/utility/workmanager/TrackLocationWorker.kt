package sg.entvision.tmssystem.utility.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.koin.core.KoinComponent
import org.koin.core.inject
import sg.entvision.tmssystem.repository.LocationRepository
import timber.log.Timber

class TrackLocationWorker constructor(
        context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams),KoinComponent {



    override fun doWork(): Result {
        val repository: LocationRepository by inject()
        return try {
            repository.getLocation()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Failure in doing work")
            Result.failure()
        }
    }
}