package com.vunke.repair.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.vunke.repair.deviceInfo.RepairUtils
import com.vunke.repair.modle.ErrorInfo
import com.vunke.repair.util.LogUtil

/**
 * Created by zhuxi on 2018/10/19.
 */
class  RepairService : Service(){
    var TAG = "RepairService"
    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!=null){
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
                RepairUtils.RepairPPPOE(errorInfo,applicationContext)
                stopSelf()
            }else if(errorInfo?.errorCode .equals("10010")||
                    errorInfo?.errorCode .equals("10011")||
                    errorInfo?.errorCode.equals("1305")){//DHCP OR IPPOE 拨号失败
                LogUtil.i(TAG," DHCP/IPOE拨号失败")
                RepairUtils.RepairIPOE(errorInfo,applicationContext)
                stopSelf()
            }else if(errorInfo?.errorCode.equals("9101")||errorInfo?.errorCode.equals("9103")||errorInfo?.errorCode.equals("9108")){//机顶盒帐号密码错误
                LogUtil.i(TAG,"机顶盒帐号密码错误")
                RepairUtils.RepairUserInfo(errorInfo,applicationContext);
                stopSelf()
            }else{
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}