package io.syslogic.sqlite.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import io.syslogic.sqlite.constants.Constants;
import io.syslogic.sqlite.database.model.Attachment;

@Dao
public interface AttachmentDao {

    @Query("SELECT * FROM " + Constants.TABLE_ATTACHMENTS)
    List<Attachment> getItems();

    @Query("SELECT * FROM " + Constants.TABLE_ATTACHMENTS + " WHERE " + Constants.KEY_ATTACHMENT_ID + " LIKE :itemId")
    Attachment getItem(int itemId);

    @Query("SELECT COUNT(*) FROM " + Constants.TABLE_ATTACHMENTS)
    int getCount();

    @Query("DELETE FROM " + Constants.TABLE_ATTACHMENTS)
    void deleteAll();
}
