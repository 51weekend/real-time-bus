/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-7.
 */

package me.chengdong.bustime.db;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.db.helper.MainSQLiteOpenHelper;
import me.chengdong.bustime.model.SingleLine;
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
public class TbSingleLineHandler {
    private static final String TAG = TbSingleLineHandler.class.getSimpleName();

    public static final String TABLE = "tb_singleLine";

    private MainSQLiteOpenHelper mSQLite = null;

    private static final String COLUMN_LINE_GUID = "lineGuid";
    private static final String COLUMN_STAND_CODE = "standCode";
    private static final String COLUMN_STAND_NAME = "standName";

    public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE + " (" + COLUMN_LINE_GUID + " TEXT, "
            + COLUMN_STAND_CODE + " TEXT, " + COLUMN_STAND_NAME + " TEXT);";
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

    private static final String INSERT_SQL = "INSERT INTO " + TABLE + " (" + COLUMN_STAND_CODE + ", "
            + COLUMN_STAND_NAME + ", " + COLUMN_LINE_GUID + ") VALUES(?, ?, ?)";
    private static final String SELECT_BY_LINE_GUID_SQL = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_LINE_GUID
            + "=? ";

    public TbSingleLineHandler(Context context) {
        mSQLite = new MainSQLiteOpenHelper(context);
    }

    public List<SingleLine> selectList(String lineGuid) {
        List<SingleLine> singleLines = new ArrayList<SingleLine>();
        if (StringUtil.isEmpty(lineGuid)) {
            return singleLines;
        }

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getReadableDatabase();
            oCursor = m_oData.rawQuery(SELECT_BY_LINE_GUID_SQL, new String[]{lineGuid});
            if (oCursor != null && oCursor.moveToFirst()) {
                int lineGuidIndex = oCursor.getColumnIndex(COLUMN_LINE_GUID);
                int standCodeIndex = oCursor.getColumnIndex(COLUMN_STAND_CODE);
                int standNameIndex = oCursor.getColumnIndex(COLUMN_STAND_NAME);

                while (!oCursor.isAfterLast()) {
                    SingleLine singleLine = new SingleLine();
                    singleLine.setLineGuid(oCursor.getString(lineGuidIndex));
                    singleLine.setStandCode(oCursor.getString(standCodeIndex));
                    singleLine.setStandName(oCursor.getString(standNameIndex));
                    singleLines.add(singleLine);
                    oCursor.moveToNext();
                }

            }
        } catch (Exception e) {
            LogUtil.e(TAG, "select list of singleLine error", e);
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
        return singleLines;
    }

    public void save(SingleLine singleLine) {

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();
            m_oData.execSQL(INSERT_SQL,
                    new String[]{singleLine.getStandCode(), singleLine.getStandName(), singleLine.getLineGuid()});
        } catch (Exception e) {
            LogUtil.e(TAG, "save singleLine error：", e);
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

    public boolean exist(String lineGuid) {

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();
            oCursor = m_oData.rawQuery(SELECT_BY_LINE_GUID_SQL, new String[]{lineGuid});
            if (oCursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "query singleLine error：", e);
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
        return false;

    }

}
