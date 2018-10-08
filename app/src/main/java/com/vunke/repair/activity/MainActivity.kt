package com.vunke.repair.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.vunke.repair.R
import com.vunke.repair.modle.ErrorInfo
import com.vunke.repair.util.DesUtil
import com.vunke.repair.util.QRCodeUtil
import com.vunke.repair.util.RebootUtil
import com.vunke.repair.util.UrlManager
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {
    var TAG = "MainActivity"
    var errorInfo:ErrorInfo? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
    }

    /**
     * 通过查询数据库获取认证信息
     *
     * @param context  上下文
     */
    fun queryUserId(context: Context): String {
        Log.i(TAG, "queryUserId: ")
        val localUri = Uri
                .parse("content://com.starcor.mango.hndx.provider/deviceinfo")
        val localCursor = context.contentResolver.query(localUri, null, null, null, null)
        try {
          if(localCursor!=null){
              if (localCursor!!.moveToFirst()) {
                  return localCursor.getString(localCursor
                          .getColumnIndex("user_id"))
                  localCursor?.close()
              }
          }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            localCursor?.close()
        }
        return ""
    }

    fun enCodeAccNbr(msisdn: String): String {
        // 用des加密用户手机号，送给对接平台
        var result = ""

        try {
            val key = "ideal123"
            val enAccnbr = DesUtil.encrypt(msisdn, key)
            result = enAccnbr.replace("\\+".toRegex(), "A_a")// +是被替换的字符串，A_a是要替换成的内容
            //result = result.replaceAll("\r|\n", "")// 去掉后面的回车换行符
            result = URLEncoder.encode(result, "utf-8")
            Log.i(TAG,"enCodeAccNbr enAccnbr:$enAccnbr")
            // URl不能直接传递特殊参数+
            // ，+会被认为是空格，所以传递时，按对端系统要求，将+替换成A_a
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
    fun  getData(){
        var userId = queryUserId(this)
        var newUserId = enCodeAccNbr(userId)
//        var intent = intent
        errorInfo =  ErrorInfo()
        errorInfo?.errorCode = "9101"
//        errorInfo?. errorCode = intent?.getStringExtra("ErrorCode")
//        errorInfo?. errorInfo = intent?.getStringExtra("ErrorInfo")
//        errorInfo?. errorType = intent?.getStringExtra("ErrorType")
//        errorInfo?. errorCaused  = intent?.getStringExtra("ErrorCaused ")
//        errorInfo?. errorMessage  = intent?.getStringExtra("ErrorMessage ")
       var bitmap = QRCodeUtil.createQRCodeBitmap(UrlManager.BaseUrl+"faultCode="+errorInfo?.errorCode+"&channelId=beiman&iptvNum="+newUserId,300,300)
        main_qrcode.setImageBitmap(bitmap)
    }
    fun reboot(){
        RebootUtil.reboot(this)
    }
}
