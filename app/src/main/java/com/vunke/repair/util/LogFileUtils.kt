package com.vunke.repair.util

import android.text.TextUtils

import java.io.File
import java.io.FileInputStream
import java.text.DecimalFormat

/**
 * Created by zhuxi on 2017/9/9.
 */
object  LogFileUtils {
    val SIZETYPE_B = 1//获取文件大小单位为B的double值

    val SIZETYPE_KB = 2//获取文件大小单位为KB的double值

    val SIZETYPE_MB = 3//获取文件大小单位为MB的double值

    val SIZETYPE_GB = 4//获取文件大小单位为GB的double值

    /**
     * 删除指定目录下文件及目录
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {// 处理目录
                    val files = file.listFiles()
                    for (i in files!!.indices) {
                        deleteFolderFile(files[i].absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {// 如果是文件，删除
                        file.delete()
                    } else {// 目录
                        if (file.listFiles()!!.size == 0) {// 目录下没有文件或者目录，删除
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     *
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     *
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     *
     * @return double值的大小
     */

    fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {

        val file = File(filePath)

        var blockSize: Long = 0
        if (!file.exists()) {
            return blockSize.toDouble()
        }
        try {

            if (file.isDirectory) {

                blockSize = getFileSizes(file)

            } else {

                blockSize = getFileSize(file)

            }

        } catch (e: Exception) {

            e.printStackTrace()
            LogUtil.e("获取文件大小", "获取失败!")

        }

        return FormetFileSize(blockSize, sizeType)

    }

    /**
     *
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     *
     * @return 计算好的带B、KB、MB、GB的字符串
     */

    fun getAutoFileOrFilesSize(filePath: String): String {

        val file = File(filePath)

        var blockSize: Long = 0

        try {

            if (file.isDirectory) {

                blockSize = getFileSizes(file)

            } else {

                blockSize = getFileSize(file)

            }

        } catch (e: Exception) {

            e.printStackTrace()

            LogUtil.e("获取文件大小", "获取失败!")

        }

        return FormetFileSize(blockSize)

    }

    /**
     *
     * 获取指定文件大小
     *
     * @param file
     *
     * @return
     *
     * @throws Exception
     */

    @Throws(Exception::class)
    private fun getFileSize(file: File): Long {

        var size: Long = 0

        if (file.exists()) {

            var fis: FileInputStream? = null

            fis = FileInputStream(file)

            size = fis.available().toLong()

        } else {

            file.createNewFile()

            LogUtil.e("获取文件大小", "文件不存在!")

        }

        return size

    }


    /**
     *
     * 获取指定文件夹
     *
     * @param f
     *
     * @return
     *
     * @throws Exception
     */

    @Throws(Exception::class)
    private fun getFileSizes(f: File): Long {
        var size: Long = 0
        val flist = f.listFiles()
        if (flist != null && flist.size != 0) {
            for (i in flist.indices) {

                if (flist[i].isDirectory) {
                    size = size + getFileSizes(flist[i])
                } else {
                    size = size + getFileSize(flist[i])
                }
            }
        }
        return size
    }

    /**
     *
     * 转换文件大小
     *
     * @param fileS
     *
     * @return
     */

    private fun FormetFileSize(fileS: Long): String {

        val df = DecimalFormat("#.00")

        var fileSizeString = ""

        val wrongSize = "0B"

        if (fileS == 0L) {

            return wrongSize

        }

        if (fileS < 1024) {

            fileSizeString = df.format(fileS.toDouble()) + "B"

        } else if (fileS < 1048576) {

            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"

        } else if (fileS < 1073741824) {

            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"

        } else {

            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"

        }

        return fileSizeString

    }

    /**
     *
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     *
     * @param sizeType
     *
     * @return
     */

    private fun FormetFileSize(fileS: Long, sizeType: Int): Double {

        val df = DecimalFormat("#.00")

        var fileSizeLong = 0.0

        when (sizeType) {

            SIZETYPE_B ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))!!

            SIZETYPE_KB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))!!

            SIZETYPE_MB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))!!

            SIZETYPE_GB ->

                fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))!!

            else -> {
            }
        }

        return fileSizeLong

    }
}
