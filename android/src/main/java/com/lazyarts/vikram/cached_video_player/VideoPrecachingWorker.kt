package com.lazyarts.vikram.cached_video_player

import androidx.work.Worker
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import kotlinx.coroutines.Job

class VideoPrecachingWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters){
    private val TAG = "VideoPrecachingWorker"

    private var videosList: ArrayList<String>? = null

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory


    companion object {
        private const val VIDEO_LIST = "VIDEO_LIST"

        fun buildWorkRequest(yourParameter:  List<String>): OneTimeWorkRequest {
            val data = Data.Builder().putStringArray(VIDEO_LIST, yourParameter.toTypedArray()).build()
            return OneTimeWorkRequestBuilder<VideoPrecachingWorker>().apply { setInputData(data) }.build()
        }
    }

    override fun doWork(): Result {
        val mContext = applicationContext
        cacheDataSourceFactory = CacheDataSourceFactory(
            mContext,  // TODO: need a way to set these programmatically. Maybe fork VideoPlayerPlatformInterface
            1024 * 1024 * 1024,
            1024 * 1024 * 100
        )
        videosList = inputData.getStringArray(VIDEO_LIST)?.toCollection(ArrayList())
        if (!videosList.isNullOrEmpty()) {
            preCacheVideo(videosList)
        }
        val outputData = workDataOf("task" to "task details")
        return Result.success(outputData)
    }

    private fun preCacheVideo(videosList: ArrayList<String>?): Result {
        var videoUrl: String? = null
        if (!videosList.isNullOrEmpty()) {
            videoUrl = videosList[0]
            videosList.removeAt(0)
        } else {
            return Result.success()
        }
        if (!videoUrl.isNullOrBlank()) {
            val videoUri = Uri.parse(videoUrl)
            val dataSpec = DataSpec(videoUri, 0, 1024 * 1024)

            val progressListener =
                CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                    val downloadPercentage: Double = (bytesCached * 100.0
                            / requestLength)
                    Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUri")
                }
            val res = cacheVideo(dataSpec, progressListener)
            return if(res){
                preCacheVideo(videosList)
            }else{
                Result.failure()
            }
        }
        return Result.failure()
    }

    private fun cacheVideo(
        dataSpec: DataSpec,
        progressListener: CacheWriter.ProgressListener
    ) :Boolean{
        runCatching {
            CacheWriter(
                cacheDataSourceFactory.createDataSource() as CacheDataSource,
                dataSpec,
                null,
                progressListener
            ).cache()
        }.onFailure {
            it.printStackTrace()
            return false
        }
        return true
    }
}