package io.syslogic.sqlite.interfaces;

public interface ILogReceiver {
    void onMessage(String message);
    void onException(Exception e);
}
