package com.vunke.repair.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.vunke.repair.deviceInfo.DeviceUtils
import com.vunke.repair.deviceInfo.RepairUtils
import com.vunke.repair.util.LogUtil
import com.vunke.repair.util.SharedPreferencesUtil
import com.vunke.repair.util.Utils

/**
 * Created by zhuxi on 2018/10/19.
 */
class SaveDeviceInfoReceiver : BroadcastReceiver(){
    var TAG = "SaveDeviceInfoReceiver"
    var ActionName = "com.vunke.repair.savedata"
    var ActionName2 = "com.vunke.repair.showView"
    var ActionName3 = "com.vunke.repair.NotShow"
    override fun onReceive(contenxt: Context, intent: Intent) {
       if (intent!=null){
           var action = intent.action
           if (!TextUtils.isEmpty(action)){

               if (action.equals(ActionName)){
                   LogUtil.i(TAG, "get broadcast:$action")
//                   Toast.makeText(contenxt,"收到广播:$action",Toast.LENGTH_SHORT).show()
                   var deviceInfo =  DeviceUtils.queryAll(contenxt.contentResolver)
                   if (deviceInfo!=null){
                       if (TextUtils.isEmpty(deviceInfo.username)|| TextUtils.isEmpty(deviceInfo.password)|| TextUtils.isEmpty(deviceInfo.pppoe_name)|| TextUtils.isEmpty(deviceInfo.pppoe_password)){
                           LogUtil.i(TAG,"query deviceInfo failed , userInfo or pppoeInfo is null,try again next time ")
                       }else{
                           LogUtil.i(TAG,"start save deviceInfo")
                           RepairUtils.saveDeviceInfo(contenxt.contentResolver,deviceInfo)
                           Utils.LogEncryption(TAG,"get deviceInfo:${deviceInfo.toString()}")
                       }
                   }else{
                       LogUtil.i(TAG,"in it deviceInfo failed,try again next time ")
                   }
               }else if (action.equals(ActionName2)){
                   LogUtil.i(TAG,"get braodcast:$action");
//                   Toast.makeText(contenxt,"收到广播:$action",Toast.LENGTH_SHORT).show()
                   SharedPreferencesUtil.setBooleanValue(contenxt,"isShow",true);
               }else if (action.equals(ActionName3)){
                   LogUtil.i(TAG,"get braodcast:$action");
//                   Toast.makeText(contenxt,"收到广播:$action",Toast.LENGTH_SHORT).show()
                   SharedPreferencesUtil.setBooleanValue(contenxt,"isShow",false);
               }
           }
       }
    }

}