package com.enid.kwliving.server.view;

/**
 * Created by Enid on 2017/5/19.
 */

public interface IServerView {
    void serverStarted();
    void serverStopped();
    void updateMsg(String msg);
}
