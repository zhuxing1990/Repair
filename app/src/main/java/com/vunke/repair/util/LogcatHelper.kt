package com.vunke.repair.util

import android.content.Context
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * log日志统计保存
 *
 * @author way
 */

class LogcatHelper private constructor(context: Context) {
    private var mLogDumper: LogDumper? = null
    private val mPId: Int
    private val fileName = "vunke/repair"
    /**r
     *
     * 初始化目录
     *
     */
    fun init(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .absolutePath + File.separator + fileName
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = (context.filesDir.absolutePath
                    + File.separator + fileName)
        }
        val file = File(PATH_LOGCAT!!)
        if (!file.exists()) {
            file.mkdirs()
        } else {
            val fileOrFilesSize = LogFileUtils.getFileOrFilesSize(file.path, 3)
            LogUtil.i("tv_launcher", "init: fileOrFilesSize:$fileOrFilesSize")
            if (fileOrFilesSize > 2) {
                LogUtil.i("tv_launcher", "init: delect file")
                LogFileUtils.deleteFolderFile(file.path, true)
                if (!file.exists()) {
                    file.mkdirs()
                }
            }
        }

    }

    init {
        init(context)
        mPId = android.os.Process.myPid()
    }

    fun start() {
        LogUtil.i("tv_launcher", "LogcatHelper start:")
        if (mLogDumper == null)
            mLogDumper = LogDumper(mPId.toString(), PATH_LOGCAT)
        if (!mLogDumper!!.isAlive) {
            mLogDumper!!.start()
        } else {
            mLogDumper!!.startLogs()
        }
    }

    fun stop() {
        LogUtil.i("tv_launcher", "LogcatHelper stop:")
        if (mLogDumper != null) {
            mLogDumper!!.stopLogs()
            mLogDumper = null
        }
    }

    private inner class LogDumper(private val mPID: String, dir: String?) : Thread() {

        private var logcatProc: Process? = null
        private var mReader: BufferedReader? = null
        private var mRunning = true
        internal var cmds: String? = null
        private var out: FileOutputStream? = null

        init {
            try {
                out = FileOutputStream(File(dir, "repair-"
                        + getFileName() + ".log"))
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             */

            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            cmds = "logcat  | grep \"($mPID)\""//打印所有日志信息
            // cmds = "logcat -s way";//打印标签过滤信息
            //            cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";

        }

        fun stopLogs() {
            mRunning = false
        }

        fun startLogs() {
            mRunning = true
        }

        override fun run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds)
                mReader = BufferedReader(InputStreamReader(
                        logcatProc!!.inputStream), 1024)
                var line: String? = null
                while(mRunning){
                    mReader!!.forEachLine {
                        out!!.write("$dateEN  $it\n".toByteArray())
                    }
                }
//                while (mRunning && (line = mReader.readLine()) != null) {
//                    if (!mRunning) {
//                        break
//                    }
//                    if (line.length == 0) {
//                        continue
//                    }
//                    if (out != null && line.contains(mPID)) {
//                        out!!.write("$dateEN  $line\n"
//                                .toByteArray())
//                    }
//                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (logcatProc != null) {
                    logcatProc!!.destroy()
                    logcatProc = null
                }
                if (mReader != null) {
                    try {
                        mReader!!.close()
                        mReader = null
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (out != null) {
                    try {
                        out!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    out = null
                }

            }

        }

    }

    companion object {

        private var INSTANCE: LogcatHelper? = null
        private var PATH_LOGCAT: String? = null

        fun getInstance(context: Context): LogcatHelper {
            if (INSTANCE == null) {
                INSTANCE = LogcatHelper(context)
            }
            return INSTANCE as LogcatHelper
        }

        fun getFileName(): String {
            val format = SimpleDateFormat("yyyy-MM-dd_HH_mm")
            return format.format(Date(System.currentTimeMillis()))
        }

        // 2012-10-03 23:41:31
        val dateEN: String
            get() {
                val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return format1.format(Date(System.currentTimeMillis()))
            }
    }
}