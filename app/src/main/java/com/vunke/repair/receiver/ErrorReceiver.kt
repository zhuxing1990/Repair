package com.vunke.repair.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.vunke.repair.activity.MainActivity
import com.vunke.repair.modle.ErrorInfo

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
                Log.i(TAG,"getAction :$action")
                if (action.equals(ActonName)){
                    var errorInfo= ErrorInfo()
                    errorInfo. errorCode = intent?.getStringExtra("ErrorCode")
                    errorInfo. errorInfo = intent?.getStringExtra("ErrorInfo")
                    errorInfo. errorType = intent?.getStringExtra("ErrorType")
                    errorInfo. errorCaused  = intent?.getStringExtra("ErrorCaused ")
                    errorInfo. errorMessage  = intent?.getStringExtra("ErrorMessage ")
                    Log.i(TAG,"onReceiver: error:${errorInfo.toString()}")
                    var  intent2 = Intent(context, MainActivity::class.java)
                    intent2.putExtra("ErrorCode",intent?.getStringExtra("ErrorCode"))
                    intent2.putExtra("ErrorInfo",intent?.getStringExtra("ErrorInfo"))
                    intent2.putExtra("ErrorType",intent?.getStringExtra("ErrorType"))
                    intent2.putExtra("ErrorCaused",intent?.getStringExtra("ErrorCaused"))
                    intent2.putExtra("ErrorMessage",intent?.getStringExtra("ErrorMessage"))
                    context?.startActivity(intent2);
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}