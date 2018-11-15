package com.vunke.repair.deviceInfo

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils
import com.google.zxing.ReaderException
import com.vunke.repair.R.string.name
import com.vunke.repair.modle.DeviceInfo
import com.vunke.repair.util.Utils


/**
 * Created by zhuxi on 2018/9/27.
 */
object DeviceUtils {
    var TAG = "DeviceUtils"
    /**
     * 通过查询数据库获取业务帐号
     *
     * @param context  上下文
     */
    fun queryUserId(context: Context): String {
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

    /**
     * 通过字段名查询机顶盒设备设备信息的详细数据
     * @param Stiring  字段名
     * @param cr 内容提供者
     */
    fun query(columeName: String, cr: ContentResolver): String? {
        val queryUri = Uri.parse("content://android.stbconfig.pppoeinfo")
        val c = cr.query(queryUri, null, null, null, null)
        try {
            if (c != null) {
                while (c.moveToNext()) {
                    val name = c.getString(c.getColumnIndex("name"))
                    if (columeName == name) {
                        val value = c.getString(c.getColumnIndex("value"))
                        if (c != null)c.close()
                        Utils.LogEncryption(TAG,"query :$columeName = :$value")
                        return value
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            if (c != null)c.close()
        }
        return ""
    }
    /**
     * 查询机顶盒设备所有设备信息
     * @param cr 内容提供者
     */
    fun queryAll(cr: ContentResolver): DeviceInfo? {
        var deviceInfo=DeviceInfo
        val queryUri = Uri.parse("content://android.stbconfig.pppoeinfo")
        var c:Cursor? = null
        try {
         c = cr.query(queryUri, null, null, null, null)
            if (c != null) {
                while (c.moveToNext()) {
                    val name = c.getString(c.getColumnIndex("name"))
                    val value = c.getString(c.getColumnIndex("value"))
                    if (TextUtils.isEmpty(value)){
                        deviceInfo = getDeviceInfo(name,"",deviceInfo)
                    }else{
                        deviceInfo = getDeviceInfo(name,value,deviceInfo)
                    }
                }
                 Utils.LogEncryption(TAG,"queryAll deviceInfo:$deviceInfo")
                return deviceInfo
            }
        }catch (e1:ReaderException){
            e1.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            if (c != null)c.close()
        }
        return null
    }

    /**
     * 根据name判断当前获取设备信息的参数
     * @param name 键名
     * @param value 键值
     * @param deviceInfo 设备信息
     */
    fun getDeviceInfo (name:String,value:String,deviceInfo:DeviceInfo):DeviceInfo{
        if (name.equals("pppoe_name")){
            deviceInfo.pppoe_name = value
        }else if(name.equals("pppoe_password")){
            deviceInfo.pppoe_password = value
        }else if(name.equals("ipoe_name")){
            deviceInfo.ipoe_name = value
        }else if(name.equals("ipoe_password")){
            deviceInfo.ipoe_password = value
        }else if(name.equals("username")){
            deviceInfo.username = value
        }else if(name.equals("password")){
            deviceInfo.password = value
        }
        return deviceInfo
    }
    /**
     * 根据键名来更新设备信息
     * @param columeName 键名
     * @param valueString  需要更新的键值
     * @param cr  内容提供者
     */
    fun update(columeName: String, valueString: String?, cr: ContentResolver): Boolean {
        Utils.LogEncryption(TAG,"update get key:$columeName")
        Utils.LogEncryption(TAG,"update get value:$valueString")
        try {
            var valueString = valueString
            if (valueString == null) {
                valueString = ""
            }
            val updateUri = Uri.parse("content://android.stbconfig.pppoeinfo")
            val value = ContentValues()
            value.put("value", valueString)
            Utils.LogEncryption(TAG,"value:$value")
            val m = cr.update(updateUri, value, "name=?", arrayOf(columeName))
            return m > 0
        }catch (e:Exception){
            e.printStackTrace()
        }
        return false;
    }

    /**
     * 插入新数据到机顶盒
     * @param columeName 键名
     *@param valueString  需要更新的键值
     * @param cr  内容提供者
     */
    fun insert(columeName: String, valueString: String?, cr: ContentResolver): Boolean {
        try {
            var valueString = valueString
            if (valueString == null) {
                valueString = ""
            }
            val insertUri = Uri.parse("content://android.stbconfig.pppoeinfo")
            val value = ContentValues()
            value.put("name", columeName)
            value.put("value", valueString)
            Utils.LogEncryption(TAG,"name:$name")
            Utils.LogEncryption(TAG,"value:$value")
            val uriRsp = cr.insert(insertUri, value)
            return uriRsp != null
        }catch (e:Exception){
            e.printStackTrace()
        }
        return false;
    }
}