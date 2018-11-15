package com.vunke.repair.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.vunke.repair.deviceInfo.RepairUtils
import com.vunke.repair.modle.ErrorInfo
import com.vunke.repair.service.RepairService
import com.vunke.repair.util.LogUtil

/**
 * Created by zhuxi on 2018/9/27.
 */
class ErrorReceiver: BroadcastReceiver() {
var ActonName = "android.stb.SEND_ERROR_MESSAGE"
var TAG = "ErrorReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            var action = intent?.action;
            if (!TextUtils.isEmpty(action)){
                LogUtil.i(TAG,"getAction :$action")
                if (action.equals(ActonName)){
                    var errorInfo= ErrorInfo()
                    errorInfo. errorCode = intent?.getStringExtra("ErrorCode")
                    errorInfo. errorInfo = intent?.getStringExtra("ErrorInfo")
                    errorInfo. errorType = intent?.getStringExtra("ErrorType")
                    errorInfo. errorCaused  = intent?.getStringExtra("ErrorCaused")
                    errorInfo. errorMessage  = intent?.getStringExtra("ErrorMessage")
                    LogUtil.i(TAG,"onReceiver: error:${errorInfo.toString()}")
                    if (errorInfo?.errorCode .equals("10021")||
                            errorInfo?.errorCode .equals("10022")||
                            errorInfo?.errorCode .equals("10023")||
                            errorInfo?.errorCode.equals("1404")    ){//PPPOE拨号失败
                        LogUtil.i(TAG," PPPOE拨号失败")
                        startRepairService(context,errorInfo);
                    }else if(errorInfo?.errorCode .equals("10010")||
                            errorInfo?.errorCode .equals("10011")||
                            errorInfo?.errorCode.equals("1305")){//DHCP OR IPPOE 拨号失败
                        LogUtil.i(TAG," DHCP/IPOE拨号失败")
                        startRepairService(context,errorInfo);
                    }else if(errorInfo?.errorCode.equals("9101")||errorInfo?.errorCode.equals("9103")||errorInfo?.errorCode.equals("9108")){//机顶盒帐号密码错误
                        LogUtil.i(TAG," 机顶盒帐号密码错误")
                        startRepairService(context,errorInfo);
                    }else{
                        RepairUtils.startRepairActivity(errorInfo,context!!,false)
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun startRepairService(context: Context?, errorInfo:ErrorInfo?) {
        var intent2 = Intent(context, RepairService::class.java)
        intent2.putExtra("ErrorCode", errorInfo?.errorCode)
        intent2.putExtra("ErrorInfo",errorInfo?.errorInfo)
        intent2.putExtra("ErrorType", errorInfo?.errorType)
        intent2.putExtra("ErrorCaused", errorInfo?.errorCaused)
        intent2.putExtra("ErrorMessage", errorInfo?.errorMessage)
        context?.startService(intent2)
    }


}