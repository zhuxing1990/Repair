package com.vunke.repair.modle

/**
 * Created by zhuxi on 2018/9/27.
 */
class ErrorInfo {
    var errorCode : String? = null
    var errorInfo : String? = null
    var errorType : String? = null
    var errorCaused : String? = null
    var errorMessage : String? = null
    override fun toString(): String {
        return "ErrorInfo(errorCode=$errorCode, errorInfo=$errorInfo, errorType=$errorType, errorCaused=$errorCaused, errorMessage=$errorMessage)"
    }
}