/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-7.
 */

package me.chengdong.bustime.db;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.db.helper.MainSQLiteOpenHelper;
import me.chengdong.bustime.model.Line;
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
public class TbLineHandler {
	private static final String TAG = TbLineHandler.class.getSimpleName();

	public static final String TABLE = "tb_line";

	private MainSQLiteOpenHelper mSQLite = null;

	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_S_NAME = "sname";
	private static final String COLUMN_RUN_TIME = "run_time";
	private static final String COLUMN_START_STATION = "start_station";
	private static final String COLUMN_END_STATION = "end_station";

	public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE
			+ " (" + COLUMN_CODE + " TEXT PRIMARY KEY, " + COLUMN_NAME
			+ " TEXT, " + COLUMN_S_NAME + "  TEXT," + COLUMN_RUN_TIME
			+ "  TEXT," + COLUMN_START_STATION + "  TEXT," + COLUMN_END_STATION
			+ "  TEXT);";

	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

	public TbLineHandler(Context context) {
		mSQLite = new MainSQLiteOpenHelper(context);
	}

	public void saveOrUpdate(String lineGuid, String lineNumber,
			String runTime, String startStation, String endStation) {

		if (StringUtil.isBlank(lineGuid) || StringUtil.isEmail(lineNumber)) {
			return;
		}

		SQLiteDatabase m_oData = null;
		Cursor oCursor = null;
		try {
			m_oData = this.mSQLite.getWritableDatabase();

			String sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_CODE
					+ "=? ";
			oCursor = m_oData.rawQuery(sql, new String[] { lineGuid });
			boolean hasData = oCursor.moveToFirst();
			String lineName;
			if (StringUtil.isNumeric(lineNumber)) {
				lineName = COLUMN_NAME;
			} else {
				lineName = COLUMN_S_NAME;
			}
			if (hasData) {

				sql = "update " + TABLE + " set " + lineName + "=? ,"
						+ COLUMN_RUN_TIME + "= ? ," + COLUMN_START_STATION
						+ "= ? ," + COLUMN_END_STATION + "= ? " + " where "
						+ COLUMN_CODE + "=? ";
				Object[] paramsValue = new Object[] { lineNumber, runTime,
						startStation, endStation, lineGuid };
				m_oData.execSQL(sql, paramsValue);
			} else {
				sql = "INSERT INTO " + TABLE + " (" + COLUMN_CODE + ", "
						+ lineName + ", " + COLUMN_RUN_TIME + ", "
						+ COLUMN_START_STATION + ", " + COLUMN_END_STATION
						+ ") VALUES(?,?,?,?,?)";
				Object[] paramsValue = new Object[] { lineGuid, lineNumber,
						runTime, startStation, endStation };
				m_oData.execSQL(sql, paramsValue);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "save line error：", e);
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

	public List<Line> selectList(String lineNumber) {
		List<Line> lines = new ArrayList<Line>();
		if (StringUtil.isEmpty(lineNumber)) {
			return lines;
		}

		SQLiteDatabase m_oData = null;
		Cursor oCursor = null;
		try {
			m_oData = this.mSQLite.getReadableDatabase();
			String sql = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_S_NAME
					+ " LIKE ? or " + COLUMN_NAME + " = ? limit 10";
			oCursor = m_oData.rawQuery(sql, new String[] {
					"%" + lineNumber + "%", lineNumber });

			if (oCursor != null && oCursor.moveToFirst()) {
				int codeIndex = oCursor.getColumnIndex(COLUMN_CODE);
				int nameIndex = oCursor.getColumnIndex(COLUMN_NAME);
				int snameIndex = oCursor.getColumnIndex(COLUMN_S_NAME);
				int runtimeIndex = oCursor.getColumnIndex(COLUMN_RUN_TIME);
				int startIndex = oCursor.getColumnIndex(COLUMN_START_STATION);
				int endIndex = oCursor.getColumnIndex(COLUMN_END_STATION);
				while (!oCursor.isAfterLast()) {
					Line line = new Line();
					line.setLineGuid(oCursor.getString(codeIndex));
					String lineName = oCursor.getString(snameIndex);
					if (StringUtil.isEmpty(lineName)) {
						lineName = oCursor.getString(nameIndex);
					}
					line.setLineNumber(lineName);
					line.setRunTime(oCursor.getString(runtimeIndex));
					line.setStartStation(oCursor.getString(startIndex));
					line.setEndStation(oCursor.getString(endIndex));
					lines.add(line);
					oCursor.moveToNext();
				}
			}

		} catch (Exception e) {
			LogUtil.e(TAG, "select list of line error", e);
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
		return lines;
	}

}
