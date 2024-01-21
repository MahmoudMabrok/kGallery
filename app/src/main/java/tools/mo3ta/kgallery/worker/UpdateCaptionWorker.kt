package tools.mo3ta.kgallery.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.DIHelper


class UpdateCaptionWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val URI_INPUT = "uri"
        const val CAPTION = "caption"

        fun createInputData(uri: String, caption: String) = workDataOf(
            URI_INPUT to uri,
            CAPTION to caption
        )
    }

    private val repo by lazy { DIHelper.getInstance(context).repo }

    private val uri = workerParams.inputData.getString(URI_INPUT)
    private val caption = workerParams.inputData.getString(CAPTION)
    override fun doWork(): Result {
        uri?.let {
            caption?.let {
                // forced as i need scope
                GlobalScope.launch {
                    val imageItem = repo.loadImage(uri)
                    repo.updateImage(imageItem.copy(caption = caption))
                }
            }
        }
        return Result.success()
    }

}
