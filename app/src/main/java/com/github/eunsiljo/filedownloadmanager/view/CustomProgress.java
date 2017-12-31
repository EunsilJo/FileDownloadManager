package com.github.eunsiljo.filedownloadmanager.view;

/**
 * Created by EunsilJo on 2016. 11. 4..
 */

public interface CustomProgress {
    public void setProgressWithAnimation(int progress, int duration);
    public void setOnProgressFinish(OnProgressFinishListener listener);
}
