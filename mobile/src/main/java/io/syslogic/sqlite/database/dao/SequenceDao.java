package io.syslogic.sqlite.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import io.syslogic.sqlite.database.SqliteBaseHelper;

@Dao
public interface SequenceDao {

    @Query("SELECT " + SqliteBaseHelper.KEY_SQLITE_SEQUENCE_VALUE + " FROM " + SqliteBaseHelper.TABLE_SQLITE_SEQUENCE + " WHERE " + SqliteBaseHelper.KEY_SQLITE_SEQUENCE_NAME + "=:tableName")
    int getSeq(String tableName);
}
