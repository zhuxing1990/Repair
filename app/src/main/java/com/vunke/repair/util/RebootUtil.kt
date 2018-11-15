package com.vunke.repair.util

import android.content.Context
import android.content.Intent

/**
 * Created by zhuxi on 2018/10/7.
 */
object RebootUtil {
    var TAG = "RebootUtil"
    fun reboot( context: Context){
        try {
            var intent = Intent()
            intent.setAction("android.stb.REBOOT_STB")
            context.sendBroadcast(intent)
            LogUtil.i(TAG,"send Broadcast :android.stb.REBOOT_STB")
        }catch (e:Exception){
            e.printStackTrace()
        }
        try {
            val intent2 = Intent()
            intent2.action = Intent.ACTION_REBOOT
            intent2.putExtra("nowait", 1)
            intent2.putExtra("interval", 1)
            intent2.putExtra("window", 0)
            LogUtil.i(TAG,"this is huawei send Broadcast :"+Intent.ACTION_REBOOT)
            context.sendBroadcast(intent2)
        }catch (e:Exception){
            LogUtil.i(TAG,"this is huawei reboot Broadcast ,don't worry")
            e.printStackTrace()
        }
    }
}