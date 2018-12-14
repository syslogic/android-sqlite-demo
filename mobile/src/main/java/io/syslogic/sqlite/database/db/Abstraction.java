package io.syslogic.sqlite.database.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.syslogic.sqlite.constants.Constants;
import io.syslogic.sqlite.database.dao.AttachmentDao;
import io.syslogic.sqlite.database.model.Attachment;

@Database(entities = {Attachment.class}, version = 1)
public abstract class Abstraction extends RoomDatabase {

    private static Abstraction sInstance;

    public abstract AttachmentDao attachmentDao();

    public static Abstraction getAbstraction(Context context) {
        if (sInstance == null) {
            sInstance = Room
            .databaseBuilder(context.getApplicationContext(), Abstraction.class, Constants.DATABASE_NAME)
            .allowMainThreadQueries()
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