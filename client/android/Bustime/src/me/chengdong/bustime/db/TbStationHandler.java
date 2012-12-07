/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-7.
 */

package me.chengdong.bustime.db;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.db.helper.MainSQLiteOpenHelper;
import me.chengdong.bustime.model.Station;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.StringUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * TODO.
 *
 * @author chengdong
 */
public class TbStationHandler {
    private static final String TAG = TbStationHandler.class.getSimpleName();

    public static final String TABLE = "tb_station";

    private MainSQLiteOpenHelper mSQLite = null;

    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";

    public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE + " (" + COLUMN_CODE + " TEXT PRIMARY KEY, "
            + COLUMN_NAME + " TEXT);";

    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

    public TbStationHandler(Context context) {
        mSQLite = new MainSQLiteOpenHelper(context);
    }

    public void saveOrUpdate(String key, String val) {

        if (StringUtil.isBlank(key)) {
            return;
        }

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();

            String sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_CODE + "=? ";
            oCursor = m_oData.rawQuery(sql, new String[]{key});
            if (oCursor.moveToFirst()) {
                sql = "update " + TABLE + " set " + COLUMN_NAME + "=? where " + COLUMN_CODE + "=? ";
                Object[] paramsValue = new Object[]{val, key};
                m_oData.execSQL(sql, paramsValue);
            } else {
                sql = "INSERT INTO " + TABLE + " (" + COLUMN_CODE + ", " + COLUMN_NAME + ") VALUES(?, ?)";
                Object[] paramsValue = new Object[]{key, val};
                m_oData.execSQL(sql, paramsValue);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "save config error：", e);
        } finally {
            if (oCursor != null) {
                oCursor.close();
                oCursor = null;
            }
            if (m_oData != null) {
                m_oData.close();
                m_oData = null;
            }
        }
    }

    public List<Station> selectList(String stationName) {
        List<Station> stations = new ArrayList<Station>();
        if (StringUtil.isEmpty(stationName)) {
            return stations;
        }

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_NAME + " LIKE ?";
            LogUtil.i(TAG, "sql：" + sql);
            oCursor = m_oData.rawQuery(sql, new String[]{"%" + stationName + "%"});
            if (oCursor.moveToNext()) {
                stations.add(getStationFromCursor(oCursor));
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "select list of station error", e);
        } finally {
            if (oCursor != null) {
                oCursor.close();
                oCursor = null;
            }
            if (m_oData != null) {
                m_oData.close();
                m_oData = null;
            }
        }
        return stations;
    }

    private Station getStationFromCursor(Cursor oCursor) {
        Station station = new Station();
        try {
            station.setStandCode(oCursor.getString(oCursor.getColumnIndex(COLUMN_CODE)));
        } catch (Exception e) {
        }

        try {
            station.setStandName(oCursor.getString(oCursor.getColumnIndex(COLUMN_NAME)));
        } catch (Exception e) {
        }

        return station;

    }

}
