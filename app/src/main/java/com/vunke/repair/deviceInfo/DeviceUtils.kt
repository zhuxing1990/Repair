package com.vunke.repair.deviceInfo

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri




/**
 * Created by zhuxi on 2018/9/27.
 */
object DeviceUtils {

    fun query(columeName: String, cr: ContentResolver): String? {
        val queryUri = Uri.parse("content://android.stbconfig.pppoeinfo")
        val c = cr.query(queryUri, null, null, null, null)
        try {
            if (c != null) {
                while (c.moveToNext()) {
                    val name = c.getString(c.getColumnIndex("name"))
                    if (columeName == name) {
                        val value = c.getString(c.getColumnIndex("value"))
                        c.close()
                        return value
                    }
                }
                c.close()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            c.close()
        }
        return null
    }

    fun update(columeName: String, valueString: String?, cr: ContentResolver): Boolean {
        var valueString = valueString
        if (valueString == null) {
            valueString = ""
        }
        val updateUri = Uri.parse("content://android.stbconfig.pppoeinfo")
        val value = ContentValues()
        value.put("value", valueString)
        val m = cr.update(updateUri, value, "name=?", arrayOf(columeName))
        return m > 0
    }

    fun insert(columeName: String, valueString: String?, cr: ContentResolver): Boolean {
        var valueString = valueString
        if (valueString == null) {
            valueString = ""
        }
        val insertUri = Uri.parse("content://android.stbconfig.pppoeinfo")
        val value = ContentValues()
        value.put("name", columeName)
        value.put("value", valueString)
        val uriRsp = cr.insert(insertUri, value)
        return uriRsp != null
    }
}