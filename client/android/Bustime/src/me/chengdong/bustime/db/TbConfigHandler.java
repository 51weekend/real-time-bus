/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-7.
 */

package me.chengdong.bustime.db;

import me.chengdong.bustime.db.helper.MainSQLiteOpenHelper;
import me.chengdong.bustime.model.Config;
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
public class TbConfigHandler {
    private static final String TAG = TbConfigHandler.class.getSimpleName();

    public static final String TABLE = "tb_config";

    private MainSQLiteOpenHelper mSQLite = null;

    private static final String HAS_STATION_DATA = "station_data";
    private static final String HAS_LINE_DATA = "line_data";
    private static final String LINE_NUMBER_EDIT_VALUE = "line_number_edit_value";
    private static final String STATION_NAME_EDIT_VALUE = "station_name_edit_value";

    private static final String COLUMN_KEY = "key";
    private static final String COLUMN_VAL = "value";

    public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE + " (" + COLUMN_KEY + " TEXT PRIMARY KEY, "
            + COLUMN_VAL + " TEXT);";
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

    public static final String SELECT_BY_KEY = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_KEY + "=? ";

    public static final String UPDATE_SQL = "update " + TABLE + " set " + COLUMN_VAL + "=? where " + COLUMN_KEY + "=? ";

    public static final String INSERT_SQL = "INSERT INTO " + TABLE + " (" + COLUMN_KEY + ", " + COLUMN_VAL
            + ") VALUES(?, ?)";

    public TbConfigHandler(Context context) {
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

            oCursor = m_oData.rawQuery(SELECT_BY_KEY, new String[]{key});
            if (oCursor.moveToFirst()) {

                Object[] paramsValue = new Object[]{val, key};
                m_oData.execSQL(UPDATE_SQL, paramsValue);
            } else {

                Object[] paramsValue = new Object[]{key, val};
                m_oData.execSQL(INSERT_SQL, paramsValue);
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

    public boolean hasStationData() {
        Config config = selectOne(HAS_STATION_DATA);
        if (config == null || !HAS_STATION_DATA.equals(config.getConfigValue())) {
            return false;
        }
        return true;
    }

    public void saveStationData() {
        saveOrUpdate(HAS_STATION_DATA, HAS_STATION_DATA);
    }

    public boolean hasLineData() {
        Config config = selectOne(HAS_LINE_DATA);
        if (config == null || !HAS_LINE_DATA.equals(config.getConfigValue())) {
            return false;
        }
        return true;
    }

    public void saveOrUpdateLineNumber(String editValue) {
        this.saveOrUpdate(LINE_NUMBER_EDIT_VALUE, editValue);
    }

    public String getLineNumber() {
        Config config = this.selectOne(LINE_NUMBER_EDIT_VALUE);
        if (config == null || StringUtil.isEmpty(config.getConfigValue())) {
            return "";
        }
        return config.getConfigValue();
    }

    public void saveLineData() {
        saveOrUpdate(HAS_LINE_DATA, HAS_LINE_DATA);
    }

    public Config selectOne(String key) {
        Config config = new Config();
        if (StringUtil.isEmpty(key)) {
            return config;
        }

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getReadableDatabase();

            oCursor = m_oData.rawQuery(SELECT_BY_KEY, new String[]{key});
            if (oCursor.moveToNext()) {
                return getConfigFromCursor(oCursor);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "select config error", e);
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
        return config;
    }

    private Config getConfigFromCursor(Cursor oCursor) {
        Config config = new Config();
        try {
            config.setConfigKey(oCursor.getString(oCursor.getColumnIndex(COLUMN_KEY)));
        } catch (Exception e) {
        }

        try {
            config.setConfigValue(oCursor.getString(oCursor.getColumnIndex(COLUMN_VAL)));
        } catch (Exception e) {
        }

        return config;

    }

    public void saveStationName(String stationName) {
        this.saveOrUpdate(STATION_NAME_EDIT_VALUE, stationName);
    }

    public String getStationName() {
        Config config = this.selectOne(STATION_NAME_EDIT_VALUE);
        if (config == null || StringUtil.isEmpty(config.getConfigValue())) {
            return "";
        }
        return config.getConfigValue();

    }

}
