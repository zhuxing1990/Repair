package com.vunke.repair.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.ConnectivityManager
import android.text.TextUtils
import java.util.*

/**
 * Created by zhuxi on 2018/10/17.
 */
object Utils {
    var TAG = "Utils"
    /**
     * 根据包名启动服务
     *
     * @param packageName
     * @param context
     */
    fun StartServer(packageName: String, className: String, Action: String, context: Context) {
        if (TextUtils.isEmpty(packageName)) {
            LogUtil.i(TAG, "包名为空")
            return
        }
        val pi: PackageInfo?
        try {
            pi = context.packageManager.getPackageInfo(packageName, 0)
            if (pi != null) {
                LogUtil.i(TAG, "StartServer packageName:$packageName")
                LogUtil.i(TAG, "StartServer className:$className")
                LogUtil.i(TAG, "StartServer Action:$Action")
                //				Intent intent = new Intent(Intent.ACTION_MAIN);
                //				ComponentName cn = new ComponentName(packageName, className);
                //				intent.setAction(Action);
                //				context.startService(intent);
                val intent = Intent(className)
                intent.`package` = packageName
                intent.action = Action
                context.startService(intent)
                LogUtil.i(TAG, "StartServer to AuthApk,start time:" + Date())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 根据包名启动APK
     *
     * @param packageName
     * @param context
     */
    fun StartEPG(packageName: String, context: Context) {
        var packageName = packageName
        if (TextUtils.isEmpty(packageName)) {
            LogUtil.i("tv_launcher", "包名为空")
            return
        }
        val pi: PackageInfo
        try {
            pi = context.packageManager.getPackageInfo(packageName, 0)
            val resolveIntent = Intent(Intent.ACTION_MAIN, null)
            resolveIntent.`package` = pi.packageName
            val pManager = context.packageManager
            val apps = pManager.queryIntentActivities(resolveIntent, 0)
            val ri = apps.iterator().next() as ResolveInfo
            if (ri != null) {
                packageName = ri.activityInfo.packageName
                val className = ri.activityInfo.name
                val intent = Intent(Intent.ACTION_MAIN)
                val cn = ComponentName(packageName, className)
                intent.component = cn
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }
    /**
     * 判断当前网络是否连接
     *
     * @param context
     * @return
     */
    fun isNetConnected(context: Context): Boolean {
        try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {

                val info = connectivity.activeNetworkInfo

                if (info != null) {
                    var istrue = false
                    istrue = if (istrue) info.isConnected else info.isAvailable
                    return istrue
                }
            }
        } catch (e: Exception) {
            return false
        }

        return false
    }

    /**
     * @param context
     * @return versionName 版本名字
     */
    fun getVersionName(context: Context): String {
        var versionName = ""
        try {
            versionName = context.packageManager.getPackageInfo(
                    context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return "0"
        }

        return versionName
    }

    /**
     * @param context
     * @return versionCode 版本号
     */
    fun getVersionCode(context: Context): Int {
        var versionCode = 0
        try {
            versionCode = context.packageManager.getPackageInfo(
                    context.packageName, 0).versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

        return versionCode
    }
    fun LogEncryption(tag:String,str:String ){
        try {
            LogUtil.i(tag,DesUtil.enCodeAccNbr(str));
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}