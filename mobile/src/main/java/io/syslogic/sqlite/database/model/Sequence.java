package io.syslogic.sqlite.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import androidx.room.PrimaryKey;
import io.syslogic.sqlite.database.SqliteBaseHelper;

@Entity
public class Sequence {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = SqliteBaseHelper.KEY_SQLITE_SEQUENCE_NAME)
    private String name = "";

    @ColumnInfo(name = SqliteBaseHelper.KEY_SQLITE_SEQUENCE_VALUE)
    private int seq;

    public String getName() {
        return this.name;
    }

    public int getSeq() {
        return this.seq;
    }

    public void setName(@NonNull String value) {
        this.name = value;
    }

    public void setSeq(int value) {
        this.seq = value;
    }
}
