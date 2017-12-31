FileDownloadManager
===================
[![](https://jitpack.io/v/EunsilJo/FileDownloadManager.svg)](https://jitpack.io/#EunsilJo/FileDownloadManager) [![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)

Android Library that help you to **download and unzip file** easily.

## How to import
Add it in your root build.gradle at the end of repositories:
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```java
dependencies {
	compile 'com.github.EunsilJo:FileDownloadManager:1.0.2'
}
```

## How to use
#### AndroidManifest.xml
Need to include these permissions in your AndroidManifest.xml file.
```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
#### Activity.java
Create FileDownloadManager and call methods.
```java
FileDownloadManager mFileDownloadManager = new FileDownloadManager(Context);
```
```java
mFileDownloadManager.check(...)
mFileDownloadManager.download(...)
mFileDownloadManager.unzip(...)
mFileDownloadManager.clear(...)
```

#### FileDownloadManager.java
A FileDownloadManager runs with one ExecutorService.
```java
private ExecutorService mThreadPool;
private static final int MAXIMUM_POOL_SIZE = 1;
```
```java
public FileDownloadManager(Context context) {
    mContext = context;
    mThreadPool = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
}
```
You can receive state and result.
```java
public interface OnFileListener<T> {
    void onFileStart();
    void onFileProgress(int progress);
    void onFileComplete(T result);
    void onFileError(int error);
}
```
    
### Check
<img src="https://github.com/EunsilJo/FileDownloadManager/blob/master/screenshots/1.png?raw=true" height="400"/>

```java
public synchronized void check(String url, OnFileListener<Integer> listener)
```

* *String url* : location of download file
* *OnFileListener<Integer> listener* : can receive the size of file.

### Download
<img src="https://github.com/EunsilJo/FileDownloadManager/blob/master/screenshots/2.png?raw=true" height="400"/>

```java
public synchronized void download(String url, String outputDirPath, String fileName, OnFileListener<String> listener)
```

* *String url* : location of download file
* *String outputDirPath* : directory path to download and save
* *String fileName* : file name to download and save
* *OnFileListener<String> listener* : can receive the file path downloaded.

### Unzip
<img src="https://github.com/EunsilJo/FileDownloadManager/blob/master/screenshots/3.png?raw=true" height="400"/> <img src="https://github.com/EunsilJo/FileDownloadManager/blob/master/screenshots/4.png?raw=true" height="400"/>

```java
public synchronized void unzip(String filePath, String targetDirPath, OnFileListener<String> listener)
```

* *String filePath* : location of zip file
* *String targetDirPath* : directory path to unzip
* *OnFileListener<String> listener* : can receive the file path unzipped(targetDirPath).

### Clear
<img src="https://github.com/EunsilJo/FileDownloadManager/blob/master/screenshots/5.png?raw=true" height="400"/>

```java
public synchronized void clear(String dirPath, OnFileListener<String> listener)
```

* *String dirPath* : directory path to clear
* *OnFileListener<String> listener* : can receive the directory path cleared(dirPath).

### +
Please check the demo app to see examples.