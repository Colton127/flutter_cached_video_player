package com.lazyarts.vikram.cached_video_player

import androidx.work.Worker
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*

class VideoPrecachingWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private val TAG = "VideoPrecachingWorker"

    private var videosList: ArrayList<MessagesHelper.VideoItem>? = null

    private lateinit var cacheDataSourceFactory: CacheDataSourceFactory


    companion object {
        private const val VIDEO_LIST = "VIDEO_LIST"
        private const val SIZED_LIST = "SIZED_LIST"

        fun buildWorkRequest(yourParameter: List<MessagesHelper.VideoItem>): OneTimeWorkRequest {
            val arrayUrls = Array(yourParameter.size) {
                yourParameter[it].videoUrl
            }
            val arraySizes = Array(yourParameter.size) {
                yourParameter[it].size
            }
            val data = Data.Builder().putStringArray(VIDEO_LIST, arrayUrls)
                .putLongArray(SIZED_LIST, arraySizes.toLongArray()).build()
//            val data = Data.Builder().putStringArray(VIDEO_LIST, yourParameter.toTypedArray()).build()
            return OneTimeWorkRequestBuilder<VideoPrecachingWorker>().apply { setInputData(data) }
                .build()
        }
    }

    override fun doWork(): Result {
        val mContext = applicationContext
        cacheDataSourceFactory = CacheDataSourceFactory(
            mContext,  // TODO: need a way to set these programmatically. Maybe fork VideoPlayerPlatformInterface
            1024 * 1024 * 1024,
            1024 * 1024 * 100
        )
        var urlArr = inputData.getStringArray(VIDEO_LIST)
        var sizeArr = inputData.getLongArray(SIZED_LIST)
        if (urlArr != null && sizeArr != null) {
            videosList = ArrayList<MessagesHelper.VideoItem>()
            var position = 0
            for (element in urlArr) {
                urlArr[position]
                videosList!!.add(MessagesHelper.VideoItem.Builder().setVideoUrl(element)
                    .setSize(sizeArr[position]).build())
//                videosList!![position] = MessagesHelper.VideoItem.Builder().setVideoUrl(element)
//                    .setSize(sizeArr[position]).build()
                position++
            }
        }
        if (!videosList.isNullOrEmpty()) {
            preCacheVideo(videosList)
        }
        val outputData = workDataOf("task" to "task details")
        return Result.success(outputData)
    }

    private fun preCacheVideo(videosList: ArrayList<MessagesHelper.VideoItem>?): Result {
        var videoUrl: String? = null
        var videoSize: Long? = null
        if (!videosList.isNullOrEmpty()) {
            videoUrl = videosList[0].videoUrl
            videoSize = videosList[0].size
            videosList.removeAt(0)
        } else {
            return Result.success()
        }
        if (!videoUrl.isNullOrBlank()) {
            val videoUri = Uri.parse(videoUrl)
            val dataSpec = DataSpec(videoUri, 0, videoSize / 100 * 30)
//            val dataSpec = DataSpec(videoUri, 0, 1024 * 1024*4)

            val progressListener =
                CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                    val downloadPercentage: Double = (bytesCached * 100.0
                            / requestLength)
                    Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUri")
                }
            val res = cacheVideo(dataSpec, progressListener)
            return if (res) {
                preCacheVideo(videosList)
            } else {
                Result.failure()
            }
        }
        return Result.failure()
    }

    private fun cacheVideo(
        dataSpec: DataSpec,
        progressListener: CacheWriter.ProgressListener
    ): Boolean {
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