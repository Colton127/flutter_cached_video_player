
package com.lazyarts.vikram.cached_video_player;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CachedVideoPlayerHelper implements MessagesHelper.VideoPlayerHelperApi {

    private final Context applicationContext;
    private final CachedVideoPlayerPlugin methodCallHandler;

    CachedVideoPlayerHelper(
            Context applicationContext,
            CachedVideoPlayerPlugin methodCallHandler
    ) {
        this.applicationContext = applicationContext;
        this.methodCallHandler = methodCallHandler;
    }

    @Override
    public void precacheVideos(@NonNull List<MessagesHelper.VideoItem> videos, MessagesHelper.Result<Boolean> result) {
        scheduleWork(videos, result);
    }

    @Override
    public void precacheVideo(@NonNull MessagesHelper.VideoItem video, MessagesHelper.Result<Boolean> result) {
        List<MessagesHelper.VideoItem> list = Collections.singletonList(video);
        scheduleWork(list, result);
    }

    @Override
    public void preparePlayerAfterError(@NonNull Long textureId) {
        methodCallHandler.prepare(textureId);
    }

    ;

    private void scheduleWork(List<MessagesHelper.VideoItem> yourParameter, MessagesHelper.Result<Boolean> result) {
        WorkManager workManager = WorkManager.getInstance(applicationContext);
        OneTimeWorkRequest exampleWorkRequest = VideoPrecachingWorker.Companion.buildWorkRequest(yourParameter);
        workManager.enqueueUniqueWork("upload_videos", ExistingWorkPolicy.KEEP, exampleWorkRequest);
        workManager.getWorkInfoByIdLiveData(exampleWorkRequest.getId())
                .observeForever(new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo == null) return;
                        if (workInfo.getState().isFinished()) {
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                result.success(true);
                            } else {
                                result.success(false);
                            }
                        }
                    }
                });
    }

}
