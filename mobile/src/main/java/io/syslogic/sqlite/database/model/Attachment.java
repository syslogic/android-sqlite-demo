package io.syslogic.sqlite.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import io.syslogic.sqlite.constants.Constants;

@Entity(tableName = Constants.TABLE_ATTACHMENTS)
public class Attachment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.KEY_ATTACHMENT_ID)
    private int id;

    @ColumnInfo(name = Constants.KEY_ATTACHMENT_NAME)
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
