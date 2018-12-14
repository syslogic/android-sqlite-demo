package io.syslogic.sqlite.database.legacy;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import io.syslogic.sqlite.constants.Constants;
import io.syslogic.sqlite.interfaces.ILogReceiver;

public class MaintenanceHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase db;

    private ILogReceiver mListener;

    public static synchronized MaintenanceHelper getInstance(Context context, ILogReceiver listener) {
        return new MaintenanceHelper(context, listener);
    }

    /** Constructor */
    public MaintenanceHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, Constants.DATABASE_NAME, factory, Constants.DATABASE_VERSION);
    }

    public MaintenanceHelper(Context context, ILogReceiver logReceiver) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
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
        /* the database is being created by Room */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* migrations are being performed by Room */
    }

    public void resetAutoIncrement(SQLiteDatabase db, String tableName, int methodId) {

        String sql = null;

        try {
            sql = "DELETE FROM " + tableName + ";";
            db.execSQL(sql);
        } catch (SQLException e) {
            this.mListener.onException(e);
        } finally {
            this.mListener.onMessage(sql);
        }

        try {

            if(methodId == 0) {
                sql = "UPDATE " + Constants.TABLE_SQLITE_SEQUENCE + " SET seq=0 WHERE name=\"" + tableName + "\";";
            } else {
                sql = "DELETE FROM " + Constants.TABLE_SQLITE_SEQUENCE + " WHERE name=\"" + tableName + "\";";
            }
            db.execSQL(sql);

        } catch(SQLException e){
            this.mListener.onException(e);
        } finally {
            this.mListener.onMessage(sql);
        }
    }

    /** returns -1 when no auto-increment record can be obtained for the tableName */
    public void getAutoIncrement(SQLiteDatabase db, String tableName) {
        int value = -1;
        try {
            Cursor cursor = db.rawQuery("SELECT " + Constants.KEY_SQLITE_SEQUENCE_VALUE + " FROM " + Constants.TABLE_SQLITE_SEQUENCE + " WHERE name=\"" + tableName + "\";", null);
            if (cursor.moveToFirst()) {value = cursor.getInt(0);}
            if (! cursor.isClosed()) {cursor.close();}
        } catch(SQLiteException e){
            this.mListener.onException(e);
        }  finally {
            this.mListener.onMessage("AUTOINCREMENT value: " + String.valueOf(value) + "<hr/>");
        }
    }

    /** just required for testing */
    public void insertSampleRecords(SQLiteDatabase db, int itemCount) {
        String sql;
        try {
            for(int i=0; i < itemCount; i++) {
                sql = "INSERT INTO " + Constants.TABLE_ATTACHMENTS + " (" + Constants.KEY_ATTACHMENT_NAME + ") VALUES (\"Attachment " + String.valueOf(i+1) + "\")";
                // this.mListener.onMessage(sql);
                db.execSQL(sql);
            }
        } catch(SQLException e){
            this.mListener.onException(e);
        } finally {
            this.mListener.onMessage("sample records added: " + String.valueOf(itemCount) + ".");
        }
    }
}
