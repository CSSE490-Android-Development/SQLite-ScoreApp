package edu.rosehulman.example;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDbAdapter {
	private static final String DATABASE_NAME = "scores.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "scores";
	
	private static final String ID_KEY = "id";
	private static final int ID_COLUMN = 0;
	private static final String NAME_KEY = "name";
	private static final int NAME_COLUMN = 1;
	private static final String SCORE_KEY = "score";
	private static final int SCORE_COLUMN = 2;
	
	private ScoreDbHelper mOpenHelper;
	private SQLiteDatabase mDb;
	
	public ScoreDbAdapter(Context context) {
		mOpenHelper = new ScoreDbHelper(context);
	}
	
	private ContentValues getContentValuesFromScore(Score s) {
		ContentValues rowValues = new ContentValues();
		rowValues.put(NAME_KEY, s.getName());
		rowValues.put(SCORE_KEY, s.getScore());
		return rowValues;
	}
	
	private Score getScoreFromCursor(Cursor c) {
		Score s = new Score();
		s.setID(c.getInt(ID_COLUMN));
		s.setName(c.getString(NAME_COLUMN));
		s.setScore(c.getInt(SCORE_COLUMN));
		return s;
	}
	
	public Score addScore(Score s) {
		ContentValues rowValues = getContentValuesFromScore(s);
		mDb.insert(TABLE_NAME, null, rowValues);
		
		Cursor c = mDb.query(TABLE_NAME, new String[] {ID_KEY, NAME_KEY, SCORE_KEY}, 
				null, null, null, null, ID_KEY + " DESC", "1");
		c.moveToFirst();
		return getScoreFromCursor(c);
	}
	
	public void removeScore(Score s) {
		mDb.delete(TABLE_NAME, ID_KEY + " = ?", new String[] {Integer.toString(s.getID())});
	}
	

	public Collection<? extends Score> getAllScores() {
		ArrayList<Score> scoreList = new ArrayList<Score>();
		Cursor c = mDb.query(TABLE_NAME, new String[] {ID_KEY, NAME_KEY, SCORE_KEY},
				null, null, null, null, ID_KEY + " DESC");
		c.moveToFirst();
		do {
			scoreList.add(getScoreFromCursor(c));
		} while (c.moveToNext());
		return scoreList;
	}
	
	public void open() {
		mDb = mOpenHelper.getWritableDatabase();
	}
	
	public void close() {
		mDb.close();
	}
	
	private static class ScoreDbHelper extends SQLiteOpenHelper {

		private static String CREATE_STATEMENT;
		private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
		
		static {
			StringBuilder s = new StringBuilder();
			s.append("CREATE TABLE ");
			s.append(TABLE_NAME);
			s.append(" (");
			s.append(ID_KEY);
			s.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
			s.append(NAME_KEY);
			s.append(" TEXT, ");
			s.append(SCORE_KEY);
			s.append(" INTEGER)");
			CREATE_STATEMENT = s.toString();
		}
		
		public ScoreDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_STATEMENT);
			onCreate(db);
		}
		
	}	
}
