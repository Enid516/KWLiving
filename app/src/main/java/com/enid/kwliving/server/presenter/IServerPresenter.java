package com.enid.kwliving.server.presenter;

import android.content.Context;

/**
 * Created by Enid on 2017/5/19.
 */

public interface IServerPresenter {
    void startServer(Context context);
    void stopServer();
    void broadCastMsg(String msg);
    void updateUIMsg(String msg);

}
