/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-12-7.
 */

package me.chengdong.bustime.db;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.db.helper.MainSQLiteOpenHelper;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import me.chengdong.bustime.utils.LogUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * TODO.
 * 
 * @author chengdong
 */
public class TbFavoriteHandler {
	private static final String TAG = TbFavoriteHandler.class.getSimpleName();

	public static final String TABLE = "tb_favorite";

	private MainSQLiteOpenHelper mSQLite = null;

	private static final String COLUMN_CODE = "code";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_TYPE = "type";
	private static final String COLUMN_PROPERTY_ONE = "property_one";
	private static final String COLUMN_PROPERTY_TWO = "property_two";
	private static final String COLUMN_PROPERTY_THREE = "property_three";

	public static final String CREATE_TABLE_SQL = " CREATE TABLE " + TABLE
			+ " (" + COLUMN_CODE + " TEXT PRIMARY KEY, " + COLUMN_NAME
			+ " TEXT, " + COLUMN_PROPERTY_ONE + " TEXT, " + COLUMN_PROPERTY_TWO
			+ " TEXT, " + COLUMN_PROPERTY_THREE + " TEXT, " + COLUMN_TYPE
			+ " NUMERIC);";
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE;

	public static final String SELECT_BY_CODE = "SELECT * FROM " + TABLE
			+ " WHERE " + COLUMN_CODE + "=? ";

	public static final String INSERT_SQL = "INSERT INTO " + TABLE + " ("
			+ COLUMN_CODE + ", " + COLUMN_NAME + ", " + COLUMN_PROPERTY_ONE
			+ ", " + COLUMN_PROPERTY_TWO + ", " + COLUMN_PROPERTY_THREE + ", "
			+ COLUMN_TYPE + ") VALUES(?,?,?,?,?,?)";

	public static final String SELECT_ALL = "select * from " + TABLE
			+ " WHERE " + COLUMN_TYPE + "=? ";
	public static final String DELETE_BY_CODE = "delete from " + TABLE
			+ " WHERE " + COLUMN_CODE + "=? ";

	public TbFavoriteHandler(Context context) {
		mSQLite = new MainSQLiteOpenHelper(context);
	}

	public void saveOrUpdate(Favorite favorite) {

		SQLiteDatabase m_oData = null;
		Cursor oCursor = null;
		try {
			m_oData = this.mSQLite.getWritableDatabase();

			oCursor = m_oData.rawQuery(SELECT_BY_CODE,
					new String[] { favorite.getCode() });
			if (!oCursor.moveToFirst()) {

				Object[] paramsValue = new Object[] { favorite.getCode(),
						favorite.getName(), favorite.getPropertyOne(),
						favorite.getPropertyTwo(), favorite.getPropertyThree(),
						favorite.getType() };
				m_oData.execSQL(INSERT_SQL, paramsValue);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "save favorite error：", e);
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

	public List<Favorite> selectAll(FavoriteType favoriteType) {
		List<Favorite> favorites = new ArrayList<Favorite>();

		SQLiteDatabase m_oData = null;
		Cursor oCursor = null;
		try {
			m_oData = this.mSQLite.getReadableDatabase();
			oCursor = m_oData.rawQuery(SELECT_ALL,
					new String[] { String.valueOf(favoriteType.getType()) });

			favorites = iteratorCursor(oCursor);

		} catch (Exception e) {
			LogUtil.e(TAG, "select list of favorite error", e);
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
		return favorites;
	}

	private List<Favorite> iteratorCursor(Cursor oCursor) {
		List<Favorite> favorites = new ArrayList<Favorite>();
		if (oCursor != null && oCursor.moveToFirst()) {
			int codeIndex = oCursor.getColumnIndex(COLUMN_CODE);
			int nameIndex = oCursor.getColumnIndex(COLUMN_NAME);
			int oneIndex = oCursor.getColumnIndex(COLUMN_PROPERTY_ONE);
			int twoIndex = oCursor.getColumnIndex(COLUMN_PROPERTY_TWO);
			int threeIndex = oCursor.getColumnIndex(COLUMN_PROPERTY_THREE);
			int typeIndex = oCursor.getColumnIndex(COLUMN_TYPE);
			while (!oCursor.isAfterLast()) {
				Favorite favorite = new Favorite();
				favorite.setCode(oCursor.getString(codeIndex));
				favorite.setName(oCursor.getString(nameIndex));
				favorite.setPropertyOne(oCursor.getString(oneIndex));
				favorite.setPropertyTwo(oCursor.getString(twoIndex));
				favorite.setPropertyThree(oCursor.getString(threeIndex));
				favorite.setType(oCursor.getInt(typeIndex));
				favorites.add(favorite);
				oCursor.moveToNext();
			}
		}
		return favorites;
	}

	public void delete(String code) {

		SQLiteDatabase m_oData = null;
		Cursor oCursor = null;
		try {
			m_oData = this.mSQLite.getWritableDatabase();
			m_oData.execSQL(DELETE_BY_CODE, new String[] { code });
		} catch (Exception e) {
			LogUtil.e(TAG, "delete favorite error：", e);
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

}
