package com.github.eunsiljo.filedownloadmanagerlib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.github.eunsiljo.filedownloadmanagerlib.utils.FileDownloadUtils;
import com.github.eunsiljo.filedownloadmanagerlib.utils.log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by EunsilJo on 2017. 6. 20..
 */

public class FileDownloadManager {
    public static String TAG = FileDownloadManager.class.getSimpleName();

    public static final int FILE_ERROR = -1000;
    public static final int FILE_ERROR_NOT_FOUND = -1001;
    public static final int FILE_ERROR_NOT_ENOUGH_STORAGE = -1002;
    
    private Context mContext;
    private ExecutorService mThreadPool;
    private static final int MAXIMUM_POOL_SIZE = 1;

    public FileDownloadManager(Context context) {
        mContext = context;
        mThreadPool = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
    }

    private FileHandler mHandler = new FileHandler(Looper.getMainLooper());

    // =============================================================================
    // Functions
    // =============================================================================

    public synchronized void check(String url, OnFileListener<Integer> listener){
        mThreadPool.execute(new FileDownloadChecker(url, listener));
    }

    public synchronized void download(String url, String outputDirPath, String fileName, OnFileListener<String> listener){
        mThreadPool.execute(new FileDownloader(url, outputDirPath, fileName, listener));
    }

    public synchronized void unzip(String filePath, String targetDirPath, OnFileListener<String> listener){
        mThreadPool.execute(new FileUnziper(filePath, targetDirPath, listener));
    }

    public synchronized void clear(String dirPath, OnFileListener<String> listener){
        mThreadPool.execute(new FileClear(dirPath, listener));
    }

    // =============================================================================
    // Runnables
    // =============================================================================
    
    private class FileDownloadChecker implements Runnable {
        private String fileServer;
        private OnFileListener<Integer> listener;

        public FileDownloadChecker(@NonNull String url, OnFileListener<Integer> listener) {
            this.fileServer = url;
            this.listener = listener;
        }

        @Override
        public void run() {
            FileResult<Integer> result = new FileResult<>();
            result.listener = listener;
            mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_START, result));
            
            try {
                result.progress = 0;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ING, result));

                URL url = new URL(fileServer);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                result.result = c.getContentLength();
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_COMPLETE, result));
            } catch (Exception e) {
                log.e(TAG, "Error: " + e.getMessage());
                result.error = FILE_ERROR;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            }
        }
    }

    private class FileDownloader implements Runnable {
        private String fileServer;
        private String outputDirPath;
        private String fileName;
        private OnFileListener<String> listener;

        public FileDownloader(@NonNull String url, @NonNull String outputDirPath, @NonNull String fileName,
                              OnFileListener<String> listener) {
            this.fileServer = url;
            this.outputDirPath = outputDirPath;
            this.fileName = fileName;
            this.listener = listener;
        }

        @Override
        public void run() {
            FileResult<String> result = new FileResult<>();
            result.listener = listener;
            mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_START, result));

            try {
                URL url = new URL(fileServer);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                int lengthOfFile = c.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());

                long freeBytes = FileDownloadUtils.getAvailableMemorySize(outputDirPath);
                if (freeBytes >= lengthOfFile) {
                    if (fileName == null || fileName.length() == 0) {
                        fileName = FileDownloadUtils.getFileName(fileServer);
                    }
                    String filePath = outputDirPath + "/" + fileName;
                    File zipFile = new File(filePath);
                    if (zipFile.exists()) {
                        zipFile.delete();
                    }

                    OutputStream output = new FileOutputStream(filePath);
                    byte data[] = new byte[1024];
                    long total = 0;
                    int count = 0;
                    result.progress = 0;
                    mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ING, result));
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        result.progress = (int) ((total * 100) / lengthOfFile);
                        mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ING, result));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();

                    result.result = filePath;
                    mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_COMPLETE, result));
                } else {
                    result.error = FILE_ERROR_NOT_ENOUGH_STORAGE;
                    mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
                }
            } catch (FileNotFoundException fe) {
                log.e(TAG, "Error: " + fe.getMessage());
                result.error = FILE_ERROR_NOT_FOUND;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            } catch (Exception e) {
                log.e(TAG, "Error: " + e.getMessage());
                result.error = FILE_ERROR;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            }
        }
    }

    private class FileUnziper implements Runnable {
        private String filePath;
        private String targetDirPath;
        private OnFileListener<String> listener;

        public FileUnziper(@NonNull String filePath, @NonNull String targetDirPath, OnFileListener<String> listener) {
            this.filePath = filePath;
            this.targetDirPath = targetDirPath;
            this.listener = listener;
        }

        @Override
        public void run() {
            FileResult<String> result = new FileResult<>();
            result.listener = listener;
            mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_START, result));
            
            try {
                File zipFile = new File(filePath);
                File targetDir = new File(targetDirPath);

                log.i(TAG, "[METHOD] void unzipAll(zipFile:" + zipFile + ", targetDir:" + targetDir + ")");

                ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
                ZipEntry zentry = null;

                targetDir.mkdirs();
                log.d(TAG, "targetDir: " + targetDir);

                // unzip all entries
                int count = 0;
                while ((zentry = zis.getNextEntry()) != null) {
                    String fileNameToUnzip = zentry.getName();
                    File targetFile = new File(targetDir, fileNameToUnzip);

                    // if directory
                    if (zentry.isDirectory()) {
                        (new File(targetFile.getAbsolutePath())).mkdirs();
                    } else {
                        // make parent dir
                        (new File(targetFile.getParent())).mkdirs();
                        unzipEntry(zis, targetFile);
                        log.d(TAG, "Unzip file: " + targetFile);
                        result.progress = ++count;
                        mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ING, result));
                    }
                }

                zis.close();
                zipFile.delete();

                result.result = targetDirPath;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_COMPLETE, result));
            } catch (FileNotFoundException fe) {
                log.e(TAG, "Error: " + fe.getMessage());
                result.error = FILE_ERROR_NOT_FOUND;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            } catch (Exception e) {
                e.printStackTrace();
                log.e(TAG, "Error: " + e.getMessage());
                result.error = FILE_ERROR;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            }
        }

        private File unzipEntry(ZipInputStream zis, File targetFile) throws IOException {
            FileOutputStream fos = new FileOutputStream(targetFile);
            BufferedInputStream in = new BufferedInputStream(zis);
            BufferedOutputStream out = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();

            return targetFile;
        }
    }

    private class FileClear implements Runnable {
        private String dirPath;
        private OnFileListener<String> listener;

        public FileClear(@NonNull String dirPath, OnFileListener<String> listener) {
            this.dirPath = dirPath;
            this.listener = listener;
        }

        @Override
        public void run() {
            FileResult<String> result = new FileResult<>();
            result.listener = listener;
            mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_START, result));

            try {
                result.progress = 0;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ING, result));

                File dir = new File(dirPath);
                clearDir(dir);

                result.result = dirPath;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_COMPLETE, result));
            } catch (Exception e) {
                log.e(TAG, "Error: " + e.getMessage());
                result.error = FILE_ERROR;
                mHandler.sendMessage(mHandler.obtainMessage(FileHandler.MESSAGE_ERROR, result));
            }
        }

        private void clearDir(File dir){
            if(dir.exists()){
                String[] children = dir.list();
                for(String s : children){
                    if(!s.equals("lib")){
                        if(deleteDir(new File(dir, s))){
                            log.i(TAG, "File " + dir.getPath() + "/" + s +" DELETED");
                        }else {
                            log.i(TAG, "File " + dir.getPath() + "/" + s +" DELETED FAILED");
                        }
                    }
                }
                dir.delete();
            }
        }

        private boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }
    }

    // =============================================================================
    // Helper
    // =============================================================================
    
    public interface OnFileListener<T> {
        void onFileStart();
        void onFileProgress(int progress);
        void onFileComplete(T result);
        void onFileError(int error);
    }

    public class FileResult<T> {
        public OnFileListener<T> listener;
        public T result;
        public int progress;
        public int error;
    }

    public class FileHandler extends Handler {

        public static final int MESSAGE_START = 1;
        public static final int MESSAGE_ING = 2;
        public static final int MESSAGE_COMPLETE = 3;
        public static final int MESSAGE_ERROR = 4;

        public FileHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FileResult result = (FileResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_START:
                    result.listener.onFileStart();
                    break;
                case MESSAGE_ING:
                    result.listener.onFileProgress(result.progress);
                    break;
                case MESSAGE_COMPLETE:
                    result.listener.onFileComplete(result.result);
                    break;
                case MESSAGE_ERROR:
                    result.listener.onFileError(result.error);
                    break;
            }
        }
    }
}
