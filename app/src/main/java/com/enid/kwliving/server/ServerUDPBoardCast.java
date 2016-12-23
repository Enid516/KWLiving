package com.enid.kwliving.server;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by big_love on 2016/12/16.
 */

public class ServerUDPBoardCast {
    private static final String TAG = "ServerUDPBoardCast";
    private Context mContext;
    private boolean isRuning = true;
    /**
     * 服务端ip地址
     */
    private String serverIP;
    private static int BROADCAST_PORT = 1234;
    private static int SERVER_SOCKET_PORT = 4444;
    private static String HOST = "224.0.0.1";
    private MulticastSocket multicastSocket;
    private InetAddress inetAddress;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    /**
     * 客服端集合
     */
    private List<Socket> socketList = new ArrayList<>();
    /**
     * 服务器端拦截客户端发送的消息
     */
    private static final int CODE_CLIENT_SEND_MESSAGE = 0x2001;
    /**
     * 客服端连接成功
     */
    private static final int CODE_CLIENT_CONNECT_SUCCESS = 0x2002;
    /**
     * 客户端连接服务器失败，socketList为null（或者socket在socketList中不存在）
     */
    private static final int CODE_CLIENT_CONNECT_ERROR = 0x2003;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x1237://服务器连接失败
                    break;
                case CODE_CLIENT_CONNECT_SUCCESS://客服端连接服务器成功
                    break;
                case CODE_CLIENT_SEND_MESSAGE://客服端发送消息
                    break;
                case CODE_CLIENT_CONNECT_ERROR://客户端连接错误
                    break;
            }
            Bundle bundle = msg.getData();
            String s = bundle.getString("content");
            setCallBackMessage(s);
        }
    };

    private void setCallBackMessage(String s) {
        if (mServiceResponseCallBack != null) {
            mServiceResponseCallBack.onResponse(s);
        }
    }

    private void sendHandlerMessage(String content, int what) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        message.setData(bundle);
        message.what = what;
        mHandler.sendMessage(message);
    }

    public ServerUDPBoardCast(Context context) {
        this.mContext = context;
    }

    public void start() {
        //初始化数据
        initData();
        //开始广播
        new UDPBoardCastThread().start();
        //接收客户端
        new AcceptClientThread().start();
    }

    /**
     * 初始化一些数据
     */
    public void initData() {
        try {
            serverIP = getAddressIP();
            inetAddress = InetAddress.getByName(HOST);//多点广播地址组
            multicastSocket = new MulticastSocket(BROADCAST_PORT);//多点广播套接字
            multicastSocket.setTimeToLive(1);
            multicastSocket.joinGroup(inetAddress);//加入广播地址组
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前ip
     * @return
     */
    private String getAddressIP() {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    public String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    /**
     * 服务端发送广播的线程
     */
    public class UDPBoardCastThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isRuning) {
                byte[] data = serverIP.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, BROADCAST_PORT);
                try {
                    multicastSocket.send(datagramPacket);
                    Thread.sleep(3000);//3秒后再发送广播
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 接收客户端的线程
     */

    public class AcceptClientThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                serverSocket = new ServerSocket(SERVER_SOCKET_PORT);
                Socket clientSocket;
                while (true) {
                    clientSocket = serverSocket.accept();
                    //添加客户端到客服端集合
                    if (!isConnect(clientSocket)) {
                        socketList.add(clientSocket);
                        sendHandlerMessage("当前连接数：" + socketList.size(), CODE_CLIENT_CONNECT_SUCCESS);
                    }
                    getExecutorService().execute(new ServerWork(clientSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断socket是否已经在socketList中
     * @param socket
     * @return
     */
    private boolean isConnect(Socket socket) {
        for (int i = 0; i < socketList.size(); i++) {
            if (socketList.get(i).getInetAddress().getHostAddress().equals(socket.getInetAddress().getHostAddress())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 接收客户端发送的消息
     */
    class ServerWork implements Runnable {
        private Socket mSocket;
        private BufferedReader reader;
        private String msg;

        public ServerWork(Socket socket) {
            this.mSocket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                msg = reader.readLine();
                if (msg != null) {
                    if (msg.equals("exit")) {
                        Log.i(TAG, "run: before remove() size is:" + socketList.size());
                        //TODO below remove is not remove the socket
                        socketList.remove(mSocket);
                        socketList.clear();
                        Log.i(TAG, "run: after remove() size is:" + socketList.size());
                        reader.close();
                        msg = "client" + mSocket.getInetAddress() + " exit\n当前连接数是：" + socketList.size();
                        mSocket.close();
                    } else {
                        msg = "client " + mSocket.getInetAddress() + "send message:" + msg;
                        sendClientMsg(mSocket);
                    }
                    sendHandlerMessage(msg, CODE_CLIENT_SEND_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void sendClientMsg(Socket socket) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                if (!isConnect(socket)) {
                    writer.println("服务器已建立连接\n" + msg);
                } else {
                    writer.println("服务器响应消息" + Math.random());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendClientMsg(final int index, final String message) {
        getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                if (socketList == null || socketList.size() < 1) {
                    sendHandlerMessage("没有客户端连接到服务器", CODE_CLIENT_CONNECT_ERROR);
                    return;
                }
                Socket socket = socketList.get(index);
                if (socket != null) {
                    PrintWriter writer;
                    try {
                        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        if (!isConnect(socket)) {
                            writer.println("服务器已建立连接\n");
                        } else {
                            writer.println("服务器响应消息:" + message);
                            Log.i("ServerUDPBoardCast", "服务器响应消息:" + message);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    sendHandlerMessage("没有对应的客户端连接到服务器", CODE_CLIENT_CONNECT_ERROR);
                }
            }
        });
    }

    private ServiceResponseCallBack mServiceResponseCallBack;

    public void setListener(ServiceResponseCallBack callBack) {
        this.mServiceResponseCallBack = callBack;
    }

    public interface ServiceResponseCallBack {
        void onResponse(String s);
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }
    public void exit() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
