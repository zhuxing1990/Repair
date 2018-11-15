package com.vunke.repair.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.vunke.repair.R
import com.vunke.repair.deviceInfo.DeviceUtils
import com.vunke.repair.deviceInfo.RepairUtils
import com.vunke.repair.modle.ErrorInfo
import com.vunke.repair.util.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var TAG = "MainActivity"
    var errorInfo: ErrorInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtil.i(TAG,"onCreate MainActivity")
        var VersionName = Utils.getVersionName(this)
//        Toast.makeText(this,"当前版本:$VersionName",Toast.LENGTH_SHORT).show()
        initViewLinstener()
        var intent = intent
        getData(intent)
    }

    private fun initViewLinstener() {
        main_but_repair.requestFocus()
        main_but_repair.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
    //                Toast.makeText(applicationContext, "一键修复", Toast.LENGTH_SHORT).show()
                LogUtil.i(TAG, "onClick   一键修复")
                startRepair()
            }
        })
        main_but_back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                LogUtil.i(TAG, "onClick 返回")
                finish()
            }
        })
    }
    fun startRepair(){
        if (errorInfo!=null){
            try {
                if (errorInfo?.errorCode.equals("10021") || errorInfo?.errorCode.equals("10022")||errorInfo?.errorCode.equals("10023")) {//PPPOE拨号失败
                    if (isRespar){
                        reboot()
                    }else{
                        RepairUtils.RepairPPPOE(errorInfo!!, applicationContext)
                    }
                } else if (errorInfo?.errorCode.equals("1404")) {//PPPOE拨号失败
                    LogUtil.i(TAG, "zte PPPOE拨号失败")
                    if (isRespar){
                        reboot()
                    }else{
                        RepairUtils.RepairPPPOE(errorInfo!!, applicationContext)
                    }
                } else if (errorInfo?.errorCode.equals("10010") || errorInfo?.errorCode.equals("10011")) {//DHCP OR IPPOE 拨号失败
                    LogUtil.i(TAG, "huawei DHCP/IPOE拨号失败")
                    if (isRespar){
                        reboot()
                    }else{
                    RepairUtils.RepairIPOE(errorInfo!!, applicationContext)
                    }
                } else if (errorInfo?.errorCode.equals("1305")) {//DHCP OR IPPOE 拨号失败
                    LogUtil.i(TAG, "zte DHCP/IPOE拨号失败")
                    if (isRespar){
                        reboot()
                    }else{
                    RepairUtils.RepairIPOE(errorInfo!!, applicationContext)
                    }
                } else if (errorInfo?.errorCode.equals("9101") || errorInfo?.errorCode.equals("9103") || errorInfo?.errorCode.equals("9108")) {//帐号和密码错误
                    LogUtil.i(TAG, "帐号或密码错误")
                    if (isRespar){
                        reboot()
                    }else{
                        RepairUtils.RepairUserInfo(errorInfo!!, applicationContext)
                    }
                } else {
                    if(!TextUtils.isEmpty(errorInfo?. errorMessage)&& errorInfo?. errorMessage.equals("认证失败")){
                        Utils.StartServer("com.vunke.auth", "com.vunke.auth.AuthService", "com.vunke.auth.reauth", this)
                        isAuth = true
                    }
                }
                RepairUtils.stopClick(main_but_repair,10,isAuth,this)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i(TAG,"onResume")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        LogUtil.i(TAG,"onNewIntent")
        getData(intent)
    }
    var isRespar =false
    var isAuth = false;
    fun getData(intent: Intent) {
        LogUtil.i(TAG,"getData")
        try {

            if (intent.hasExtra("isRespar")){
                isRespar =  intent.getBooleanExtra("isRespar",false);
                LogUtil.i(TAG,"isRespar:$isRespar")
            }
            errorInfo = ErrorInfo()
            if (intent.hasExtra("ErrorCode")){
                errorInfo?. errorCode = intent?.getStringExtra("ErrorCode")
                LogUtil.i(TAG,"ErrorCode:${errorInfo?.errorCode}")
            }
            if (intent.hasExtra("ErrorInfo")){
                errorInfo?. errorInfo = intent?.getStringExtra("ErrorInfo")
                LogUtil.i(TAG,"ErrorCode:${errorInfo?.errorInfo}")
            }
            var ErrorInfo:String ? = ""
            if (!TextUtils.isEmpty(errorInfo!!.errorInfo)){
                ErrorInfo = errorInfo!!.errorInfo
                main_errtitie.setText("错误信息\n+ $ErrorInfo")
            }

            if (intent.hasExtra("ErrorType")){
                errorInfo?. errorType = intent?.getStringExtra("ErrorType")
                LogUtil.i(TAG,"ErrorCode:${errorInfo?.errorType}")
            }
            if (intent.hasExtra("ErrorCaused")){
                errorInfo?. errorCaused  = intent?.getStringExtra("ErrorCaused")
                LogUtil.i(TAG,"ErrorCode:${errorInfo?.errorCaused}")
            }
            if (intent.hasExtra("ErrorMessage")){
                errorInfo?. errorMessage  = intent?.getStringExtra("ErrorMessage")
                LogUtil.i(TAG,"ErrorCode:${errorInfo?.errorMessage}")
            }
            var ErrorCode:String? = ""
            if (!TextUtils.isEmpty(errorInfo!!.errorCode)){
                ErrorCode = errorInfo!!.errorCode
                main_error.setText("错误代码($ErrorCode)")
            }
            if (ErrorCode.equals("1901")){//网线断了
                ErrorCode = "1901"
                main_errtitie.setText("错误信息\n  机顶盒网线未连接光猫，请重新拔插机顶盒与光猫之间的网线，确认网线接光猫网口")
                main_but_repair.visibility = View.GONE
                main_but_back.visibility = View.VISIBLE
                main_but_repair.isClickable = false
                main_but_back.isClickable = true
                main_but_back.setText("确定")
            }else if(ErrorCode.equals("10000")||ErrorCode.equals("10001")){
                ErrorCode = "1901"
                main_errtitie.setText("错误信息\n  机顶盒网线未连接光猫或者光猫未通电")
                main_but_repair.visibility = View.GONE
                main_but_back.visibility = View.VISIBLE
                main_but_repair.isClickable = false
                main_but_back.isClickable = true
                main_but_back.setText("确定")
            }
            else if (ErrorCode.equals("10021")||ErrorCode.equals("10022")||ErrorCode.equals("10023")||ErrorCode.equals("1404")){// PPPOE拨号失败
                ErrorCode = "1002"
                main_but_repair.visibility = View.VISIBLE
                main_but_back.visibility = View.VISIBLE
                main_but_repair.isClickable = true
                main_but_back.isClickable = true
                if (isRespar) {
                    main_but_repair.setText("确认")
                    main_errtitie.setText("错误信息\n  机顶盒拨号失败或拨号密码错误,自动修复完成,请按确认重启机顶盒")
                }else{
                    main_but_repair.setText(R.string.repair)
                    main_errtitie.setText("错误信息\n  机顶盒拨号失败或拨号密码错误")
                }
            }else if(ErrorCode .equals("10010")||ErrorCode .equals("10011")||ErrorCode.equals("1305")){//IPOE拨号失败
                ErrorCode = "1003"
                main_but_repair.visibility = View.VISIBLE
                main_but_back.visibility = View.VISIBLE
                main_but_repair.isClickable = true
                main_but_back.isClickable = true
                if (isRespar) {
                    main_but_repair.setText("确认")
                    main_errtitie.setText("错误信息\n  机顶盒拨号失败或拨号密码错误,自动修复完成,请按确认重启机顶盒")
                }else{
                    main_but_repair.setText(R.string.repair)
                    main_errtitie.setText("错误信息\n  机顶盒拨号失败或拨号密码错误")
                }
            }else  if(errorInfo?.errorCode.equals("9101")||errorInfo?.errorCode.equals("9103")|| errorInfo?.errorCode.equals("9108")){//帐号和密码错误
                main_but_repair.visibility = View.VISIBLE
                main_but_back.visibility = View.GONE
                main_but_repair.isClickable = true
                main_but_back.isClickable = false
                if (isRespar){
                    main_but_repair.setText("确认")
                    if (!TextUtils.isEmpty(errorInfo?.errorInfo)){
                        main_errtitie.setText("错误信息\n  ${errorInfo?.errorInfo},自动修复完成,请按确认重启机顶盒")
                    }else{
                        main_errtitie.setText("错误信息\n  认证失败,账号或密码错误,自动修复完成,请按确认重启机顶盒")
                    }
                }else{
                    main_but_repair.setText(R.string.repair)
                    if (!TextUtils.isEmpty(errorInfo?.errorInfo)){
                        main_errtitie.setText("错误信息\n  ${errorInfo?.errorInfo}")
                    }else{
                        main_errtitie.setText("错误信息\n  认证失败,账号或密码错误")
                    }
                }
            }else {//认证失败
                if(TextUtils.isEmpty(errorInfo?.errorCode)){
                    main_but_repair.visibility = View.GONE
                    main_but_back.visibility = View.VISIBLE
                    main_but_repair.isClickable = false
                    main_but_back.isClickable =true
                    main_errtitie.setText("错误信息\n 未知的异常,按返回退出.")
                }else if(!TextUtils.isEmpty(errorInfo?. errorMessage)|| errorInfo?. errorMessage.equals("认证失败")||errorInfo?.errorCode.equals("1008")){
                    main_but_repair.setText("重新认证")
                    main_but_repair.visibility = View.VISIBLE
                    main_but_back.visibility = View.GONE
                    main_but_repair.isClickable = true
                    main_but_back.isClickable = false
                }
                if (!TextUtils.isEmpty(errorInfo?.errorInfo)){
                    main_errtitie.setText("错误信息\n  ${errorInfo?.errorInfo}")
                }else{
                    main_errtitie.setText("错误信息\n 认证失败或请求超时")
                }
            }
            var userId = DeviceUtils.queryUserId(this)
            if (TextUtils.isEmpty(userId)) {
                userId = "null"
            }
            main_user.setText(R.string.username)
            main_user.append(userId)
            var newUserId = DesUtil.enCodeAccNbr(userId!!)
            var bitmap = QRCodeUtil.createQRCodeBitmap(UrlManager.BaseUrl + "faultCode=" + ErrorCode+ "&channelId=beiman&iptvNum=" + newUserId, 400, 400)
            main_qrcode.setImageBitmap(bitmap)
        }catch (e_null:NullPointerException){
            e_null.printStackTrace()
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  重启机顶盒
     */

    fun reboot() {
        try {
            RebootUtil.reboot(this)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return if (keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i(TAG,"onDestory MainActivity")
    }
}
