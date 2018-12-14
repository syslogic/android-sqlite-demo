package io.syslogic.sqlite.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.syslogic.sqlite.database.SqliteBaseHelper;
import io.syslogic.sqlite.database.model.Attachment;

@Dao
public interface AttachmentDao {

    @Query("SELECT * FROM " + SqliteBaseHelper.TABLE_ATTACHMENTS)
    List<Attachment> getItems();

    @Query("SELECT * FROM " + SqliteBaseHelper.TABLE_ATTACHMENTS + " WHERE " + SqliteBaseHelper.KEY_ATTACHMENT_ID + " LIKE :itemId")
    Attachment getItem(int itemId);

    @Query("SELECT COUNT(*) FROM " + SqliteBaseHelper.TABLE_ATTACHMENTS)
    int getCount();

    @Query("DELETE FROM " + SqliteBaseHelper.TABLE_ATTACHMENTS)
    void deleteAll();
}
