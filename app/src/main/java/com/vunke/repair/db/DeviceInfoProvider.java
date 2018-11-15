package com.vunke.repair.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vunke.repair.deviceInfo.DeviceInfoTables;
import com.vunke.repair.util.LogUtil;

/**
 * Created by zhuxi on 2018/10/15.
 */

public class DeviceInfoProvider extends ContentProvider {
    private static final String TAG = "DeviceInfoProvider";
    private final static String AUTHORITH = "com.vunke.repair.device";
    private final static String PATH = "/device_info";
    private final static String PATHS = "/device_info/#";


    private final static UriMatcher mUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    private static final int CODE_DIR = 1;
    private static final int CODE_ITEM = 2;
    static {
        mUriMatcher.addURI(AUTHORITH, PATH, CODE_DIR);
        mUriMatcher.addURI(AUTHORITH, PATHS, CODE_ITEM);
    }
    private DeviceInfoSQLite dbHelper;
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        dbHelper = new DeviceInfoSQLite(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        try {
            db = dbHelper.getWritableDatabase();
            db = dbHelper.getWritableDatabase();
            String sql = "select * from "+ DeviceInfoTables.INSTANCE.getTABLE_NAME();
            LogUtil.i(TAG, "query: :"+sql);
            cursor = db.rawQuery(sql , null);
            return cursor;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case CODE_DIR:
                return "vnd.android.cursor.dir/device_info";
            case CODE_ITEM:
                return "vnd.android.cursor.item/device_info";

            default:
                throw new IllegalArgumentException("异常参数");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        //插入数据
        switch (mUriMatcher.match(uri)) {
            case 1:
                long c = db.insert(DeviceInfoTables.INSTANCE.getTABLE_NAME(), null, contentValues);
                LogUtil.i(TAG,"insert success:"+c);
                break;
            case 2:
                break;
        }
        // db.execSQL("delete from groupinfo where rowid not in(select max(rowid) from groupinfo group by user_id)");
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogUtil.i(TAG,"delete all");
        int number = 0;
        db = dbHelper.getWritableDatabase();
        number = db.delete(DeviceInfoTables.INSTANCE.getTABLE_NAME(), selection, selectionArgs);
        return number;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int number = 0;
        db = dbHelper.getWritableDatabase();
        number = db.update(DeviceInfoTables.INSTANCE.getTABLE_NAME(), contentValues, selection, selectionArgs);
        return number;
    }
}
