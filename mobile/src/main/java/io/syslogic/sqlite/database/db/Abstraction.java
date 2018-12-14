package io.syslogic.sqlite.database.db;

import android.content.Context;

import androidx.annotation.NonNull;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import io.syslogic.sqlite.database.dao.AttachmentDao;
import io.syslogic.sqlite.database.model.Attachment;

@Database(
    version = 1,
    entities = {
        Attachment.class
    }
)
public abstract class Abstraction extends RoomDatabase {

    private static Abstraction sInstance;

    public abstract AttachmentDao companyDao();

    public static Abstraction getAbstraction(final Context context) {
        if (sInstance == null) {
            sInstance = Room
            .databaseBuilder(context.getApplicationContext(), Abstraction.class, "room.db")
            // .allowMainThreadQueries()
            .addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }
            })
            .build();
        }
        return sInstance;
    }

    public static Abstraction getInstance() {
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }
}