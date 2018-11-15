package com.vunke.repair.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vunke.repair.deviceInfo.DeviceInfoTables
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable


/**
 * Created by zhuxi on 2018/10/15.
 */

internal class DeviceInfoSQLite(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var TAG = "DeviceInfoSQLite"
    companion object {
        val DATABASE_NAME = "deviceinfo_bak.db"
        val DATABASE_VERSION = 1
//        val instance by lazy { DeviceInfoSQLite() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(DeviceInfoTables.TABLE_NAME, true,
                DeviceInfoTables.NAME to TEXT,
                DeviceInfoTables.VALUE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.dropTable(DeviceInfoTables.TABLE_NAME, true)
            onCreate(db)
    }



}