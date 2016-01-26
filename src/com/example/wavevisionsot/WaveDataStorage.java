package com.example.wavevisionsot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WaveDataStorage {

	/** Database Opening count is Maintained */
	public static int dbOpenCount=0;

	/** Database Closing count is Maintained */
	public static int dbCloseCount=0;
	
	/** Object used for creating, insertion into Database object */
	private static SQLiteDatabase sqlitedb;
	private final Context context;					// Context for passing in current classes constructor
	private DatabaseHelper DBHelperObject;			// DatabaseHelper object for getting getting object
	
	/** Database Name Declaration */
	public static final String DATABASE_NAME = "WaveDatabase.db";
	
	/** Database Version Number */
	private static final int DATABASE_VERSION = 1;

	/** Inner class object created for synchronizing when RLRDataStorage object is Created */
	private static DatabaseHelper iDbHelper;
	
	// Devices Table Fields


	public static final String DATABASE_MAIN_DATA= "MainData";
	
	public static final String DOC_INCHARGE= "Doc_incharge";
	
	public static final String CREATEDID= "CreatedId";
	
	public static final String PIC_CODE= "Pincode";
	
	public static final String DATABASE_DOCTOR_D= "Doctor_Details";
	public static final String DOC_NAME= "Doctor_name";
	public static final String DOC_ID= "Doctor_id";
	public static final String DOC_DEFAULT= "Doctor_default";
	public static final String DOC_OT_LIGHT1= "OT_Light1";
	public static final String DOC_OT_LIGHT2= "OT_Light2";
	public static final String DOC_LIGHT1= "Light1";
	public static final String DOC_LIGHT2= "Light2";
	public static final String DOC_XRAY1= "xray1";
	public static final String DOC_XRAY2= "xray2";
	public static final String DOC_TEMP= "temprature";
	public static final String DOC_HUMIDITY= "humidity";

	public static final String OXY_LOW= "oxygen_low";
	public static final String OXY_HIGH= "oxygen_high";
	public static final String N2O_LOW= "n2o_low";
	public static final String N2O_HIGH= "n2o_high";
	public static final String VAC_LOW= "vac_low";
	public static final String VAC_HIGH= "vac_high";
	public static final String AIR_LOW= "air_low";
	public static final String AIR_HIGH= "air_high";
	public static final String ANE_TIME= "ane_time";
	public static final String SUR_TIME= "sur_time";
	
	/** Database Object for Singleton */
	private static WaveDataStorage instance;
	/**
	 * Getting object of DatabaseHelper
	 * @param ctx
	 */
	public WaveDataStorage(Context ctx) {
		Log.i("RLRDataStorage","Context");
		this.context = ctx;
		DBHelperObject = new DatabaseHelper(context);
		synchronized (DATABASE_NAME) {
			if (iDbHelper == null)
				iDbHelper = new DatabaseHelper(ctx);
			}
	}

	/**
	 * Opens the database
	 * @return RLRDataStorage object
	 * @throws SQLException
	 */
	public WaveDataStorage open() {
		try {
			dbOpenCount = dbOpenCount + 1;
			sqlitedb = DBHelperObject.getWritableDatabase();
			System.out.println("RLRDataStorage.open()"+dbOpenCount);
		} catch (SQLException ex) {
			sqlitedb = DBHelperObject.getReadableDatabase();
			Log.d("log_tag", "Exception is Thrown open get Readable");
			ex.printStackTrace();
		}
		return this;
	}

	/**
	 * Closes the database
	 * @return RLRDataStorage object
	 */
	public void close() {
			DBHelperObject.close();
			System.out.println("RLRDataStorage.close()"+dbCloseCount);
	}
	
	
		// Creating Devices table
		private static final String TABLE_DATA_COLLECTED = "CREATE TABLE "
			+ DATABASE_DOCTOR_D + " ("
			+ DOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ DOC_DEFAULT + " text,"
			+ DOC_OT_LIGHT1 + " text,"
			+ DOC_OT_LIGHT2 + " text,"
			+ DOC_LIGHT1 + " text,"
			+ DOC_LIGHT2 + " text,"
			+ DOC_XRAY1 + " text,"
			+ DOC_XRAY2 + " text,"
			+ DOC_TEMP + " text,"
			+ DOC_HUMIDITY + " text,"
			+ OXY_LOW + " text,"
			+ OXY_HIGH + " text,"
			+ N2O_LOW + " text,"
			+ N2O_HIGH + " text,"
			+ VAC_LOW + " text,"
			+ VAC_HIGH + " text,"
			+ AIR_LOW + " text,"
			+ AIR_HIGH + " text,"
			+ ANE_TIME + " text,"
			+ SUR_TIME + " text,"
			+ DOC_NAME + " text);";

	/**
	singleton BamDataStorage.
	 *
	 * @param context
	 * @return BamDataStorage database instance.
	 */
	public static WaveDataStorage GetFor(Context context) {
		if (instance == null)
			instance = new WaveDataStorage(context);
		// if (!instance.isOpen())
		// instance.open();
		return instance;
	}
	
	/**
	 * Creation of Database Tables and Upgradation is done.
	 * @author Bhavin
	 *
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		/**
		 * Get records in a particular table according to sql query
		 * @param tablename
		 * @return a cursor object pointed to the record(s) selected by sql query.
		 */
		public synchronized static Cursor getRecordBySelection(String tablename) {
			return sqlitedb.query(tablename, null, null, null, null, null,null);
		}

		/**
		 * Constructor created for DatabaseHelper
		 * @param context
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		//	Database Tables are created
		@Override
		public void onCreate(SQLiteDatabase db) {
			//db.execSQL(DATABASE_TEST);			
			db.execSQL(TABLE_DATA_COLLECTED);
		}
		
		// Database Upgradation is done
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("BAMDB", "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			//db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TEST);			
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_DATA_COLLECTED);	

			onCreate(db);
		}

	}

	/**
	 * Insert a record into particular table
	 *
	 * @param tablename
	 * @param values
	 * @returnthe row ID of the newly inserted row, or -1 if an error occurred
	 */
	public synchronized long insert(String tablename, ContentValues values) {
		return sqlitedb.insert(tablename, null, values);
	}
	/**
	 * Getting Profile Value from the Database
	 * @return cursor
	 */
	public Cursor getSkillCodeQuestionId(String Value, int item) {
		return sqlitedb.rawQuery("select * from "+DATABASE_DOCTOR_D+" where option = '"+Value+"' and "+DOC_INCHARGE+" ='"+item+"'", null);
	}
	public void deleteAllData(String table){
		sqlitedb.delete(table,null,null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getTableData(String type) {
		return sqlitedb.rawQuery("select * from "+type+" ORDER BY Doctor_name ASC", null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getTotalRows() {
		return sqlitedb.rawQuery("select * from "+DATABASE_MAIN_DATA, null);
	}
	/**
	 * Setting Default Profile to set 0 from 1
	 * @return cursor
	 */
	public int correctRadioButton(int skillCodeID, int score) {
		ContentValues args = new ContentValues();
		args.put(PIC_CODE, score);
		return sqlitedb.update(DATABASE_MAIN_DATA,args,DOC_INCHARGE+" ='"+skillCodeID+"'",null);
	}
	
	/**
	 * Setting Default Profile to set 0 from 1
	 * @return cursor
	 */
	public int saveDocDetails(ContentValues values, String doc) {
		return sqlitedb.update(DATABASE_DOCTOR_D, values, DOC_NAME+" ='"+doc+"'", null);
	}
	

	/**
	 * Setting Default Profile to set 0 from 1
	 * @return cursor
	 */
	public Cursor getDocDetails(String doc) {
		return sqlitedb.rawQuery("select * from "+DATABASE_DOCTOR_D+" where "+DOC_NAME+" ='"+doc+"'", null);
	}
	    
}
