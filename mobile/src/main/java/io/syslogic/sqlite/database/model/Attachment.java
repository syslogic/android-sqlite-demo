package io.syslogic.sqlite.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.syslogic.sqlite.database.SqliteBaseHelper;

@Entity(tableName = SqliteBaseHelper.TABLE_ATTACHMENTS)
public class Attachment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SqliteBaseHelper.KEY_ATTACHMENT_ID)
    private int id;

    @ColumnInfo(name = SqliteBaseHelper.KEY_ATTACHMENT_NAME)
    private String name;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(int value) {
        this.id = value;
    }

    public void setName(String value) {
        this.name = value;
    }
}
