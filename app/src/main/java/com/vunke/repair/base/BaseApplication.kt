package com.vunke.repair.base

import android.app.Application
import com.vunke.repair.util.LogUtil
import com.vunke.repair.util.LogcatHelper
import com.vunke.repair.util.Utils

/**
 * Created by zhuxi on 2018/10/15.
 */
class BaseApplication: Application(){
    var TAG = "BaseApplication"
    override fun onCreate() {
        super.onCreate()
        LogUtil.i(TAG,"onCreate ")
       var versionName =  Utils.getVersionName(this)
        LogUtil.i(TAG,"getVersionName:$versionName")
       var versionCode = Utils.getVersionCode(this)
        LogUtil.i(TAG,"getVersionCode:$versionCode")
        LogcatHelper.getInstance(this).start();
    }

    override fun onTerminate() {
        super.onTerminate()
        LogcatHelper.getInstance(this).stop();
    }
}