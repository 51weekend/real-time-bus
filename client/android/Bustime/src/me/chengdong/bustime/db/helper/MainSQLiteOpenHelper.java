/*
 * Copyright (c) 2011 Shanda Corporation. All rights reserved.
 *
 * Created on 2011-8-7.
 */
package me.chengdong.bustime.db.helper;

import me.chengdong.bustime.db.TbConfigHandler;
import me.chengdong.bustime.db.TbStationHandler;
import me.chengdong.bustime.utils.LogUtil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * 封装对SQLite数据库的操作.
 * 
 * @author chengdong
 */
public class MainSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = MainSQLiteOpenHelper.class.getSimpleName();

    private static final int VERSION = 1;

    private static final String DB_NAME = "bustime.db";

    public MainSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 配置
        db.execSQL(TbConfigHandler.CREATE_TABLE_SQL);

        db.execSQL(TbStationHandler.CREATE_TABLE_SQL);

        LogUtil.d(TAG, " database created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 0) {
            // TODO 这里重新做、不用删除
            db.execSQL(TbConfigHandler.DROP_TABLE_SQL);
            db.execSQL(TbConfigHandler.CREATE_TABLE_SQL);

            db.execSQL(TbStationHandler.DROP_TABLE_SQL);
            db.execSQL(TbStationHandler.CREATE_TABLE_SQL);

        }

        LogUtil.d(TAG, " database upgraded.");
    }
}
