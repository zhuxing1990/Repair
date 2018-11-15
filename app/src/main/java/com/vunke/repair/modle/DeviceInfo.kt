package com.vunke.repair.modle

/**
 * Created by zhuxi on 2018/10/15.
 */
object DeviceInfo {
    var pppoe_name :String ?= null
    var pppoe_password :String ?= null
    var ipoe_name :String ?= null
    var ipoe_password :String ?= null
    var username :String ?= null
    var password :String ?= null

    override fun toString(): String {
        return "DeviceInfo(pppoe_name=$pppoe_name, pppoe_password=$pppoe_password, ipoe_name=$ipoe_name, ipoe_password=$ipoe_password, username=$username, password=$password)"
    }

}