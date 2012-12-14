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
    private static final String COLUMN_ROAD = "road";
    private static final String COLUMN_TREND = "trend";
    private static final String COLUMN_AREA = "area";
    private static final String COLUMN_LINES = "lines";

    // standName,road,trend,area,standCode

    public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE + " (" + COLUMN_CODE + " TEXT PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, " + COLUMN_ROAD + " TEXT, " + COLUMN_TREND + " TEXT, " + COLUMN_LINES + " TEXT, "
            + COLUMN_AREA + " TEXT);";

    private static final String UPDATE_SQL = "update " + TABLE + " set " + COLUMN_NAME + "=?," + COLUMN_ROAD + "=?,"
            + COLUMN_TREND + "=?," + COLUMN_LINES + "=?," + COLUMN_AREA + "=? where " + COLUMN_CODE + "=? ";
    private static final String INSERT_SQL = "INSERT INTO " + TABLE + " (" + COLUMN_NAME + ", " + COLUMN_ROAD + ", "
            + COLUMN_TREND + ", " + COLUMN_LINES + ", " + COLUMN_AREA + ", " + COLUMN_CODE
            + ") VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_CODE + "=? ";

    private static final String SELECT_LIKE_BY_NAME_SQL = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_NAME
            + " LIKE ?";

    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

    public TbStationHandler(Context context) {
        mSQLite = new MainSQLiteOpenHelper(context);
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
            oCursor = m_oData.rawQuery(SELECT_LIKE_BY_NAME_SQL, new String[]{"%" + stationName + "%"});
            if (oCursor != null && oCursor.moveToFirst()) {
                int codeIndex = oCursor.getColumnIndex(COLUMN_CODE);
                int nameIndex = oCursor.getColumnIndex(COLUMN_NAME);
                int roadIndex = oCursor.getColumnIndex(COLUMN_ROAD);
                int trendIndex = oCursor.getColumnIndex(COLUMN_TREND);
                int linesIndex = oCursor.getColumnIndex(COLUMN_LINES);
                int areaIndex = oCursor.getColumnIndex(COLUMN_AREA);

                while (!oCursor.isAfterLast()) {
                    Station station = new Station();
                    station.setStandCode(oCursor.getString(codeIndex));
                    station.setStandName(oCursor.getString(nameIndex));
                    station.setRoad(oCursor.getString(roadIndex));
                    station.setTrend(oCursor.getString(trendIndex));
                    station.setLines(oCursor.getString(linesIndex));
                    station.setArea(oCursor.getString(areaIndex));
                    stations.add(station);
                    oCursor.moveToNext();
                }

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

    public void saveOrUpdate(String stationString) {
        if (StringUtil.isBlank(stationString)) {
            return;
        }
        String[] station = stationString.split("\\|");

        if (station.length < 5) {
            return;
        }

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();

            oCursor = m_oData.rawQuery(SELECT_SQL, new String[]{station[0]});
            if (oCursor.moveToFirst()) {
                m_oData.execSQL(UPDATE_SQL, station);
            } else {
                m_oData.execSQL(INSERT_SQL, station);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "save station error：", e);
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

    public void saveOrUpdate(Station station) {

        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();
            oCursor = m_oData.rawQuery(SELECT_SQL, new String[]{station.getStandCode()});
            if (oCursor.moveToFirst()) {
                m_oData.execSQL(UPDATE_SQL, new String[]{station.getStandName(), station.getRoad(), station.getTrend(),
                        station.getLines(), station.getArea(), station.getStandCode()});
            } else {
                m_oData.execSQL(INSERT_SQL, new String[]{station.getStandName(), station.getRoad(), station.getTrend(),
                        station.getLines(), station.getArea(), station.getStandCode()});
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "save station error：", e);
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

    public Station selectOne(String stationCode) {
        SQLiteDatabase m_oData = null;
        Cursor oCursor = null;
        try {
            m_oData = this.mSQLite.getWritableDatabase();
            oCursor = m_oData.rawQuery(SELECT_SQL, new String[]{stationCode});
            int codeIndex = oCursor.getColumnIndex(COLUMN_CODE);
            int nameIndex = oCursor.getColumnIndex(COLUMN_NAME);
            int roadIndex = oCursor.getColumnIndex(COLUMN_ROAD);
            int trendIndex = oCursor.getColumnIndex(COLUMN_TREND);
            int linesIndex = oCursor.getColumnIndex(COLUMN_LINES);
            int areaIndex = oCursor.getColumnIndex(COLUMN_AREA);
            if (oCursor.moveToFirst()) {
                Station station = new Station();
                station.setStandCode(oCursor.getString(codeIndex));
                station.setStandName(oCursor.getString(nameIndex));
                station.setRoad(oCursor.getString(roadIndex));
                station.setTrend(oCursor.getString(trendIndex));
                station.setLines(oCursor.getString(linesIndex));
                station.setArea(oCursor.getString(areaIndex));
                return station;
            } else {
                return null;
            }

        } catch (Exception e) {
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
        return null;

    }
}
