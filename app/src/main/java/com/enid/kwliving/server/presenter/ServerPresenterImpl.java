package com.enid.kwliving.server.presenter;

import android.content.Context;

import com.enid.kwliving.server.ServerUDPBoardCast;
import com.enid.kwliving.server.view.IServerView;

/**
 * Created by Enid on 2017/5/19.
 */

public class ServerPresenterImpl implements IServerPresenter {

    private ServerUDPBoardCast serverUDPBoardCast;
    private IServerView iServerView;

    public ServerPresenterImpl(IServerView iServerView) {
        this.iServerView = iServerView;
    }

    @Override
    public void startServer(Context context) {
        if (null == context)
            throw new NullPointerException("Start server,the parameter Context is null");
        if (null == serverUDPBoardCast)
            serverUDPBoardCast = new ServerUDPBoardCast(context,this);
        else
            serverUDPBoardCast.start();
        iServerView.serverStarted();
    }

    @Override
    public void stopServer() {
        if (null == serverUDPBoardCast)
            throw new NullPointerException("The Object ServerPresenterImpl is null");
        else
            serverUDPBoardCast.exit();
        iServerView.serverStopped();
    }

    @Override
    public void broadCastMsg(String msg) {
        serverUDPBoardCast.sendClientMsg(msg);
    }

    @Override
    public void updateUIMsg(String msg) {
        iServerView.updateMsg(msg);
    }
}
