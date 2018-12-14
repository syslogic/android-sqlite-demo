package io.syslogic.sqlite.constants;

/** Constants */
public class Constants {

    public static final String DATABASE_NAME            = "room.db";
    public static final int DATABASE_VERSION            = 1;

    /** Tables */
    public static final String TABLE_SQLITE_SEQUENCE     = "sqlite_sequence";
    public static final String KEY_SQLITE_SEQUENCE_NAME  = "name";
    public static final String KEY_SQLITE_SEQUENCE_VALUE = "seq";

    public static final String TABLE_ATTACHMENTS         = "attachment";
    public static final String KEY_ATTACHMENT_ID         = "attachment_id";
    public static final String KEY_ATTACHMENT_NAME       = "attachment_name";
}
