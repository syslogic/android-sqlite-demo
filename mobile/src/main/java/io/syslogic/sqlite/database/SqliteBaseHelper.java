package io.syslogic.sqlite.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.syslogic.sqlite.interfaces.ILogReceiver;


public class SqliteBaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = SqliteBaseHelper.class.getSimpleName();

    private static SQLiteDatabase db;

    /** Constants */
    private static final String DATABASE_NAME          = "local.db";
    private static final int DATABASE_VERSION          = 1;

    /** Tables */
    private static final String TABLE_SQLITE_SEQUENCE  = "sqlite_sequence";
    public static final String TABLE_ATTACHMENTS       = "attachment";

    /** Columns */
    private static final String KEY_ATTACHMENT_ID      = "att_id";
    private static final String KEY_ATTACHMENT_NAME    = "att_name";

    private ILogReceiver mListener;

    public static synchronized SqliteBaseHelper getInstance(Context context, ILogReceiver listener) {
        return new SqliteBaseHelper(context, listener);
    }

    /** Constructor */
    public SqliteBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public SqliteBaseHelper(Context context, ILogReceiver logReceiver) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if(db == null) {db = getWritableDatabase();}
        this.mListener = logReceiver;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ATTACHMENTS + "(" +  KEY_ATTACHMENT_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  KEY_ATTACHMENT_NAME  + " TEXT)");
        } catch(SQLException e){
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            Log.d(LOG_TAG, DATABASE_NAME + " has been created");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertSampleRecords(SQLiteDatabase db, int itemCount) {
        String sql;
        try {
            for(int i=0; i < itemCount; i++) {
                sql = "INSERT INTO " + TABLE_ATTACHMENTS + " (" + KEY_ATTACHMENT_NAME + ") VALUES (\"Attachment " + String.valueOf(i+1) + "\")";
                // this.mListener.onMessage(sql);
                db.execSQL(sql);
            }
        } catch(SQLException e){
            this.mListener.onException(e);
        } finally {
            this.mListener.onMessage("sample records added: " + String.valueOf(itemCount) + ".");
        }
    }

    public void resetAutoIncrement(SQLiteDatabase db, String tableName, boolean deleteRecords, int methodId) {

        String sql = null;

        if(deleteRecords) {
            try {
                sql = "DELETE FROM " + tableName + ";";
                db.execSQL(sql);
            } catch (SQLException e) {
                this.mListener.onException(e);
            } finally {
                this.mListener.onMessage(sql);
            }
        }

        try {
            if(methodId == 0) {
                sql = "UPDATE " + TABLE_SQLITE_SEQUENCE + " SET seq=0 WHERE name=\"" + tableName + "\";";
            } else {
                sql = "DELETE FROM " + TABLE_SQLITE_SEQUENCE + " WHERE name=\"" + tableName + "\";";
            }
            db.execSQL(sql);

        } catch(SQLException e){
            this.mListener.onException(e);
        } finally {
            this.mListener.onMessage(sql);
        }
    }

    /* returns -1 when no auto-increment record exists for the tableName */
    public void getAutoIncrement(SQLiteDatabase db, String tableName) {
        int value = -1;
        try {
            Cursor cursor = db.rawQuery("SELECT seq FROM " + TABLE_SQLITE_SEQUENCE + " WHERE name=\"" + tableName + "\";", null);
            if (cursor.moveToFirst()) {value = cursor.getInt(0);}
            if (! cursor.isClosed()) {cursor.close();}
        } catch(SQLiteException e){
            this.mListener.onException(e);
        }  finally {
            this.mListener.onMessage("AUTOINCREMENT value: " + String.valueOf(value) + "<hr/>");
        }
    }
}