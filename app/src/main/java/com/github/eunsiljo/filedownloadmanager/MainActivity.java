package com.github.eunsiljo.filedownloadmanager;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.eunsiljo.filedownloadmanager.view.CustomProgressBar;
import com.github.eunsiljo.filedownloadmanagerlib.FileDownloadManager;
import com.github.eunsiljo.filedownloadmanagerlib.utils.FileDownloadUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView btnRefresh;
    private TextView txtCheckDesc;
    private TextView btnCheck;
    private CustomProgressBar progressDownload;
    private TextView txtDownloadDesc;
    private TextView btnDownload;
    private TextView txtUnzipDesc;
    private TextView btnUnzip;
    private TextView txtSampleDesc;
    private ImageView imgSample;

    private CircleProgressBar progressLoading;

    private FileDownloadManager mFileDownloadManager;

    private static final String FILE_URL = "https://github.com/EunsilJo/FileDownloadManager/raw/master/material-design-icons.zip";
    private static final String DIR_PATH = "/samples";
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        initListener();
        initData();
    }

    private void initLayout() {
        btnRefresh = (TextView)findViewById(R.id.btnRefresh);
        txtCheckDesc = (TextView)findViewById(R.id.txtCheckDesc);
        btnCheck = (TextView)findViewById(R.id.btnCheck);
        progressDownload = (CustomProgressBar)findViewById(R.id.progressDownload);
        txtDownloadDesc = (TextView)findViewById(R.id.txtDownloadDesc);
        btnDownload = (TextView)findViewById(R.id.btnDownload);
        txtUnzipDesc = (TextView)findViewById(R.id.txtUnzipDesc);
        btnUnzip = (TextView)findViewById(R.id.btnUnzip);
        txtSampleDesc = (TextView)findViewById(R.id.txtSampleDesc);
        imgSample = (ImageView)findViewById(R.id.imgSample);

        progressLoading = (CircleProgressBar)findViewById(R.id.progressLoading);

        mFileDownloadManager = new FileDownloadManager(this);
    }

    private void initListener() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoading()){
                    return;
                }
                refresh();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoading()){
                    return;
                }
                check();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoading()){
                    return;
                }
                download();
            }
        });

        btnUnzip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoading()){
                    return;
                }
                unzip();
            }
        });
    }

    private void initData(){
        refresh();
    }

    private void check(){
        mFileDownloadManager.check(FILE_URL,
                new FileDownloadManager.OnFileListener<Integer>() {
                    @Override
                    public void onFileStart() {
                        txtCheckDesc.setText(getString(R.string.check_desc_start));
                    }

                    @Override
                    public void onFileProgress(int progress) {
                        txtCheckDesc.setText(getString(R.string.check_desc_ing));
                    }

                    @Override
                    public void onFileComplete(Integer result) {
                        txtCheckDesc.setText(String.format(getString(R.string.check_desc_end),
                                FileDownloadUtils.getStringSize((long)result)));
                        btnDownload.setEnabled(true);
                    }

                    @Override
                    public void onFileError(int error) {
                        Toast.makeText(MainActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
                        txtCheckDesc.setText(getString(R.string.check_desc_error));
                    }
                });
    }

    private void download(){
        mFileDownloadManager.download(FILE_URL,
                FileDownloadUtils.getStoragePath(getApplicationContext()),
                "samples.zip",
                new FileDownloadManager.OnFileListener<String>() {
                    @Override
                    public void onFileStart() {
                        txtDownloadDesc.setText(getString(R.string.download_desc_start));
                    }

                    @Override
                    public void onFileProgress(int progress) {
                        if(progressDownload.getVisibility() != View.VISIBLE){
                            progressDownload.setVisibility(View.VISIBLE);
                        }
                        progressDownload.setProgress(progress);
                        txtDownloadDesc.setText(String.format(getString(R.string.download_desc_ing),
                                progress));
                    }

                    @Override
                    public void onFileComplete(String result) {
                        txtDownloadDesc.setText(getString(R.string.download_desc_end));

                        if(result != null){
                            mFilePath = result;
                        }
                        btnUnzip.setEnabled(true);
                    }

                    @Override
                    public void onFileError(int error) {
                        Toast.makeText(MainActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
                        txtDownloadDesc.setText(getString(R.string.download_desc_error));
                    }
                });
    }

    private void unzip(){
        mFileDownloadManager.unzip(mFilePath,
                FileDownloadUtils.getStoragePath(getApplicationContext()) + DIR_PATH,
                new FileDownloadManager.OnFileListener<String>() {
                    @Override
                    public void onFileStart() {
                        txtUnzipDesc.setText(getString(R.string.unzip_desc_start));
                    }

                    @Override
                    public void onFileProgress(int progress) {
                        txtUnzipDesc.setText(String.format(getString(R.string.unzip_desc_ing),
                                progress));
                    }

                    @Override
                    public void onFileComplete(String result) {
                        txtUnzipDesc.setText(getString(R.string.unzip_desc_end));

                        if(result != null) {
                            //show a sample file
                            String sampleName = "ic_sentiment_very_satisfied_black_24dp.png";
                            String samplePath = "/social/" + sampleName;
                            File sample = new File(result + samplePath);
                            if (sample.exists()) {
                                txtSampleDesc.setText(sampleName);
                                imgSample.setImageBitmap(BitmapFactory.decodeFile(sample.getAbsolutePath()));
                            }
                        }
                    }

                    @Override
                    public void onFileError(int error) {
                        Toast.makeText(MainActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
                        txtUnzipDesc.setText(getString(R.string.unzip_desc_error));
                        txtSampleDesc.setText("");
                        imgSample.setImageBitmap(null);
                    }
                });
    }

    private void refresh(){
        txtCheckDesc.setText("");
        btnCheck.setEnabled(false);
        txtDownloadDesc.setText("");
        progressDownload.setProgress(0);
        progressDownload.setVisibility(View.INVISIBLE);
        btnDownload.setEnabled(false);
        txtUnzipDesc.setText("");
        btnUnzip.setEnabled(false);
        txtSampleDesc.setText("");
        imgSample.setImageBitmap(null);


        mFileDownloadManager.clear(FileDownloadUtils.getStoragePath(getApplicationContext()) + DIR_PATH,
                new FileDownloadManager.OnFileListener<String>() {
                    @Override
                    public void onFileStart() {
                        startLoading();
                    }

                    @Override
                    public void onFileProgress(int progress) {
                    }

                    @Override
                    public void onFileComplete(String result) {
                        stopLoading();
                        btnCheck.setEnabled(true);
                    }

                    @Override
                    public void onFileError(int error) {
                        stopLoading();
                        Toast.makeText(MainActivity.this, getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getErrorMessage(int error){
        String msg = getString(R.string.toast_error);
        switch (error){
            case FileDownloadManager.FILE_ERROR_NOT_ENOUGH_STORAGE:
                msg = getString(R.string.toast_error_not_enough_storage);
                break;
            case FileDownloadManager.FILE_ERROR_NOT_FOUND:
                msg = getString(R.string.toast_error_not_found);
                break;
        }
        return msg;
    }

    private void startLoading() {
        if(progressLoading != null && progressLoading.getVisibility() != View.VISIBLE){
            progressLoading.setVisibility(View.VISIBLE);
        }
    }

    private void stopLoading() {
        if(progressLoading != null && progressLoading.getVisibility() != View.INVISIBLE){
            progressLoading.setVisibility(View.GONE);
        }
    }

    private boolean isLoading(){
        return (progressLoading != null && progressLoading.getVisibility() == View.VISIBLE);
    }
}
