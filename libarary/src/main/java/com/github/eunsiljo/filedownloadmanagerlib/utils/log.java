package com.github.eunsiljo.filedownloadmanagerlib.utils;

import android.util.Log;

/**
 * Custom Log Class
 * Created by Ted
 */
public class log {

    public static String TAG = log.class.getSimpleName();
    public static boolean isDebug = false;

    private log() {}

    /**
     * 동일하게 TAG 명 지정하고 싶을 경우 TAG 명 셋팅.
     *
     * @param tagName tag name
     */
    public static void setTAG(String tagName) {
        TAG = tagName;
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int v(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(TAG, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String userTag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(userTag, msg);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(TAG, msg, tr);
    }

    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String userTag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.v(userTag, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int d(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(TAG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String userTag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(userTag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(TAG, msg, tr);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String userTag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.d(userTag, msg, tr);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int i(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(TAG, msg);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String userTag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(userTag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(TAG, msg, tr);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String userTag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.i(userTag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int w(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(TAG, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String userTag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(TAG, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String userTag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(userTag, msg, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tr An exception to log
     */
    public static int w(Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(TAG, tr);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static int w(String userTag, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.w(userTag, tr);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param msg The message you would like logged.
     */
    public static int e(String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(TAG, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String userTag, String msg) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(userTag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(TAG, msg, tr);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String userTag, String msg, Throwable tr) {
        if (!isDebug) {
            return -1;
        }
        return Log.e(userTag, msg, tr);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (!isDebug) {
            return "";
        }
        return Log.getStackTraceString(tr);
    }

    public static String getClassMethMsgString(Object obj, String meth, String message) {

        if (!isDebug) {
            return "";
        }
        return new StringBuilder(obj.getClass().getName()).append(":\n").append(meth).append(":\n")
                .append(message).append("\n").toString();
    }
}
