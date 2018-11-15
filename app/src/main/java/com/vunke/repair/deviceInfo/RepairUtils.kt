package com.vunke.repair.deviceInfo

import android.app.Activity
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.RemoteException
import android.text.TextUtils
import android.widget.Button
import com.google.zxing.ReaderException
import com.vunke.repair.R
import com.vunke.repair.activity.MainActivity
import com.vunke.repair.modle.DeviceInfo
import com.vunke.repair.modle.ErrorInfo
import com.vunke.repair.util.LogUtil
import com.vunke.repair.util.SharedPreferencesUtil
import com.vunke.repair.util.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit



/**
 * Created by zhuxi on 2018/10/16.
 */
object RepairUtils {


         fun RepairPPPOE(errorInfo: ErrorInfo,context: Context) {
            LogUtil.i(TAG,"RepairPPPOE")
            try{
                var pppoeName = DeviceUtils.query("pppoe_name",context.contentResolver)
                var pppoePassword = DeviceUtils.query("pppoe_password",context.contentResolver)
                Utils.LogEncryption(TAG,"修改之前的PPPOE帐号:$pppoeName 和密码:$pppoePassword ")
                var deviceInfo = RepairUtils.queryDeviceInfo(context.contentResolver)
                if (TextUtils.isEmpty(deviceInfo?.pppoe_name)|| TextUtils.isEmpty(deviceInfo?.pppoe_password)){
                    LogUtil.i(TAG,"query deviceInfo_bak failed ,  pppoeInfo is null,repair failed")
                    startRepairActivity(errorInfo,context,false);
                }else if (TextUtils.isEmpty(pppoeName)|| TextUtils.isEmpty(pppoePassword)){
                    if (TextUtils.isEmpty(pppoeName)){
                        Utils.LogEncryption(TAG,"PPPOE帐号为空,通过备份的帐号:${deviceInfo?.pppoe_name} 修复错误的PPPOE帐号")
                        LogUtil.i(TAG,"RepairPPPOE get pppoe_name is null,update pppoe_name")
                        DeviceUtils.update("pppoe_name",deviceInfo?.pppoe_name,context.contentResolver)
                    }
                    if (TextUtils.isEmpty(pppoePassword)){
                        Utils.LogEncryption(TAG,"PPPOE密码为空,通过备份的密码:${deviceInfo?.pppoe_password} 修复错误的PPPOE密码")
                        LogUtil.i(TAG,"RepairPPPOE get pppoe_password is null,update pppoe_password")
                        DeviceUtils.update("pppoe_password",deviceInfo?.pppoe_password,context.contentResolver)
                    }
                    startRepairActivity(errorInfo,context,true);
                }else
                    if (pppoeName.equals(deviceInfo?.pppoe_name)&&pppoePassword.equals(deviceInfo?.pppoe_password)){
                        Utils.LogEncryption(TAG,"PPPOE帐号或密码和备份帐号和密码一致，不修复")
                        LogUtil.i(TAG," pppoe_name and pppoe_password No modification is required.")
                        startRepairActivity(errorInfo,context,false);
                    }else{
                        if (!pppoeName.equals(deviceInfo?.pppoe_name)){
                            Utils.LogEncryption(TAG,"PPPOE帐号和备份帐号不匹配,通过备份的帐号:${deviceInfo?.pppoe_name} 修复错误的PPPOE帐号")
                            LogUtil.i(TAG,"update pppoe_name")
                            DeviceUtils.update("pppoe_name",deviceInfo?.pppoe_name,context.contentResolver)
                        }
                        if (!pppoePassword.equals(deviceInfo?.pppoe_password)){
                            Utils.LogEncryption(TAG,"PPPOE密码和备份密码不匹配,通过备份的密码:${deviceInfo?.pppoe_password} 修复错误的PPPOE密码")
                            LogUtil.i(TAG,"RepairPPPOE update pppoe_password")
                            DeviceUtils.update("pppoe_password",deviceInfo?.pppoe_password,context.contentResolver)
                        }
                        startRepairActivity(errorInfo,context,true);
                    }
            }catch (e:NullPointerException){
                e.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }catch (e2:Exception){
                e2.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }
        }

         fun RepairIPOE(errorInfo: ErrorInfo,context: Context) {
            LogUtil.i(TAG,"RepairIPOE")
            try {
                var ipoeName = DeviceUtils.query("ipoe_name",context.contentResolver)
                var ipoePassword = DeviceUtils.query("ipoe_password",context.contentResolver)
                LogUtil.i(TAG,"修改之前的IPOE帐号:$ipoeName 和密码:$ipoePassword ")
                var deviceInfo = RepairUtils.queryDeviceInfo(context.contentResolver)
                if (TextUtils.isEmpty(deviceInfo?.ipoe_name)|| TextUtils.isEmpty(deviceInfo?.ipoe_password)){
                    LogUtil.i(TAG,"query deviceInfo failed , ipoeInfo is null,repair failed")
                    startRepairActivity(errorInfo,context,false);
                }else
                if (TextUtils.isEmpty(ipoeName)|| TextUtils.isEmpty(ipoePassword)){
                    if (TextUtils.isEmpty(ipoeName)){
                        Utils.LogEncryption(TAG,"IPOE帐号为空,通过备份的帐号:${deviceInfo?.ipoe_name} 修复错误的IPOE帐号")
                        LogUtil.i(TAG,"RepairIPOE get ipoeName is null")
                        DeviceUtils.update("ipoe_name",deviceInfo?.ipoe_name,context.contentResolver)
                    }
                    if(TextUtils.isEmpty(ipoePassword)){
                        Utils.LogEncryption(TAG,"IPOE密码为空,通过备份的密码:${deviceInfo?.ipoe_password} 修复错误的IPOE密码")
                        LogUtil.i(TAG,"RepairIPOE get ipoe_password is null")
                        DeviceUtils.update("ipoe_password",deviceInfo?.ipoe_password,context.contentResolver)
                    }
                    startRepairActivity(errorInfo,context,true);
                }else if (ipoeName.equals(deviceInfo?.ipoe_name)&& ipoePassword.equals(deviceInfo?.ipoe_password)){
                    LogUtil.i(TAG,"IPOE帐号或密码和备份帐号和密码一致，不修复")
                    LogUtil.i(TAG," ipoe_name and ipoe_password No modification is required.")
                    startRepairActivity(errorInfo,context,false);
                }else{
                    if (!ipoeName.equals(deviceInfo?.ipoe_name)){
                        Utils.LogEncryption(TAG,"IPOE帐号和备份帐号不匹配,通过备份的帐号:${deviceInfo?.ipoe_name} 修复错误的IPOE帐号")
                        LogUtil.i(TAG,"update ipoeName")
                        DeviceUtils.update("ipoe_name",deviceInfo?.ipoe_name,context.contentResolver)
                    }
                    if (!ipoePassword.equals(deviceInfo?.ipoe_password)){
                        Utils.LogEncryption(TAG,"IPOE密码和备份密码不匹配,通过备份的密码:${deviceInfo?.ipoe_password} 修复错误的IPOE密码")
                        LogUtil.i(TAG,"update ipoe_password")
                        DeviceUtils.update("ipoe_password",deviceInfo?.ipoe_password,context.contentResolver)
                    }
                    startRepairActivity(errorInfo,context,true);
                }
            }catch (e:NullPointerException){
                e.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }catch (e2:Exception){
                e2.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }
        }
         fun RepairUserInfo(errorInfo: ErrorInfo,context: Context) {
            LogUtil.i(TAG,"RepairUserInfo")
            try {
                var user_name = DeviceUtils.query("username",context.contentResolver)
                var pass_word = DeviceUtils.query("password",context.contentResolver)
                Utils.LogEncryption(TAG,"修改之前的业务帐号:$user_name 和密码:$pass_word ")
                var deviceInfo = RepairUtils.queryDeviceInfo(context.contentResolver)
                if (TextUtils.isEmpty(deviceInfo?.username)|| TextUtils.isEmpty(deviceInfo?.password)){
                    LogUtil.i(TAG,"query deviceInfo failed , userInfo is null,repair failed")
                    startRepairActivity(errorInfo,context,false);
                }else
                if (TextUtils.isEmpty(user_name)|| TextUtils.isEmpty(pass_word)){
                    if (TextUtils.isEmpty(user_name)){
                        Utils.LogEncryption(TAG,"业务帐号为空,通过备份的帐号:${deviceInfo?.username} 修复错误的业务帐号")
                        LogUtil.i(TAG,"RepairUserInfo get user_name is null")
                        DeviceUtils.update("username",deviceInfo?.username,context.contentResolver)
                    }
                    if(TextUtils.isEmpty(pass_word)){
                        Utils.LogEncryption(TAG,"业务密码为空,通过备份的密码:${deviceInfo?.password} 修复错误的业务密码")
                        LogUtil.i(TAG,"RepairUserInfo get pass_word is null")
                        DeviceUtils.update("password",deviceInfo?.password,context.contentResolver)
                    }
                    startRepairActivity(errorInfo,context,true);
                }else if (user_name.equals(deviceInfo?.username)&& pass_word.equals(deviceInfo?.password)){
                    LogUtil.i(TAG,"业务帐号或密码和备份帐号和密码一致，不修复")
                    LogUtil.i(TAG," username and pass_word No modification is required.")
                    startRepairActivity(errorInfo,context,true);
                }else{
                    if (!user_name.equals(deviceInfo?.username)){
                        Utils.LogEncryption(TAG,"业务帐号和备份帐号不匹配,通过备份的帐号:${deviceInfo?.username} 修复错误的业务帐号")
                        LogUtil.i(TAG,"update username")
                        DeviceUtils.update("username",deviceInfo?.username,context.contentResolver)
                    }
                    if (!pass_word.equals(deviceInfo?.password)){
                        Utils.LogEncryption(TAG,"业务密码和备份帐号不匹配,通过备份的帐号:${deviceInfo?.password} 修复错误的业务密码")
                        LogUtil.i(TAG,"update password")
                        DeviceUtils.update("password",deviceInfo?.password,context.contentResolver)
                    }
                    startRepairActivity(errorInfo,context,true);
                }
            }catch (e:NullPointerException){
                e.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }catch (e2:Exception){
                e2.printStackTrace()
                startRepairActivity(errorInfo,context,false);
            }
        }
     fun startRepairActivity( errorInfo:ErrorInfo?,context: Context,isRespar:Boolean?) {
         var isShow =  SharedPreferencesUtil.getBooleanValue(context,"isShow",true);
         LogUtil.i(TAG, "isShow:$isShow")
//         Toast.makeText(context,":$isShow", Toast.LENGTH_SHORT).show()
         if (isShow){
             var intent2 = Intent(context, MainActivity::class.java)
             intent2.putExtra("ErrorCode", errorInfo?.errorCode)
             intent2.putExtra("ErrorInfo",errorInfo?.errorInfo)
             intent2.putExtra("ErrorType", errorInfo?.errorType)
             intent2.putExtra("ErrorCaused", errorInfo?.errorCaused)
             intent2.putExtra("ErrorMessage", errorInfo?.errorMessage)
             intent2.putExtra("isRespar", isRespar)
             intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
             context?.startActivity(intent2);
             LogUtil.i(TAG,"startMainActivity")

         }else{
//             SharedPreferencesUtil.setBooleanValue(context,"isShow",true);
            LogUtil.i(TAG,"mangguo apk is start,don't showing")
         }
    }
    fun queryDeviceInfo(cr: ContentResolver): DeviceInfo? {
        var deviceInfo=DeviceInfo
        val queryUri = Uri.parse("content://com.vunke.repair.device/device_info")
        var c: Cursor? = null
        try {
            c = cr.query(queryUri, null, null, null, null)
            if (c != null) {
                while (c.moveToNext()) {
                    val name = c.getString(c.getColumnIndex("name"))
                    val value = c.getString(c.getColumnIndex("value"))
                    deviceInfo = DeviceUtils.getDeviceInfo(name, value, deviceInfo)
                }
                Utils.LogEncryption(TAG,"queryAll deviceInfo:$deviceInfo")
                return deviceInfo
            }
        }catch (e1: ReaderException){
            e1.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            if (c != null)c.close()
        }
        return null
    }
    var TAG = "RepairUtils"
    fun saveDeviceInfo(contentResolver: ContentResolver,deviceInfo:DeviceInfo){
        var uri = Uri.parse("content://com.vunke.repair.device/device_info")
        try {
            contentResolver.delete(uri,null,null)
            val ops = ArrayList<ContentProviderOperation>()
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "username")
                    .withValue("value", deviceInfo.username)
                    .build())
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "password")
                    .withValue("value", deviceInfo.password)
                    .build())
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "pppoe_name")
                    .withValue("value", deviceInfo.pppoe_name)
                    .build())
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "pppoe_password")
                    .withValue("value", deviceInfo.pppoe_password)
                    .build())
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "ipoe_name")
                    .withValue("value", deviceInfo.ipoe_name)
                    .build())
            ops.add(ContentProviderOperation.newInsert(uri)
                    .withValue("name", "ipoe_password")
                    .withValue("value", deviceInfo.ipoe_password)
                    .build())
            contentResolver.applyBatch("com.vunke.repair.device", ops)
        }catch (e2: RemoteException) {
            LogUtil.i(TAG, "insert failed");
            e2.printStackTrace();
        }catch (e3: OperationApplicationException){
            LogUtil.i(TAG, "insert failed");
            e3.printStackTrace()
        }catch (e:Exception){
            LogUtil.i(TAG, "insert failed");
            e.printStackTrace()
        }

    }
     fun stopClick(button:Button,time:Long,isAuth:Boolean,context: Activity) {
        button.setClickable(false)
        Observable.interval(0, 1, TimeUnit.SECONDS).filter(object : Predicate<Long> {
            override fun test(t: Long): Boolean {
                return t <= time
            }
        }).map(object : Function<Long, Long> {
            override fun apply(t: Long): Long {
                return -(t-time)
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Long>() {
                    override fun onComplete() {
                        this.dispose()
                    }

                    override fun onNext(t: Long) {
                        if (t > 0) {
                            button.setText("请等待" + t + "秒")
                        } else {
                            this.dispose()
                            button.setClickable(true)
                            if (isAuth){
                                button.setText("重新认证")
                                Utils.StartEPG("com.vunke.auth",context)
                                context.finish()
                            }else{
                                button.setText(R.string.repair)
                            }

                        }
                    }

                    override fun onError(e: Throwable) {
                        button.setClickable(true)
                        if (isAuth){
                            button.setText("重新认证")
                        }else{
                            button.setText(R.string.repair)
                        }
                        this.dispose()
                    }

                })
    }
}