package com.enid.kwliving.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.enid.kwliving.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by big_love on 2016/12/16.
 */

public class ClientUDPReceiver {
    InetAddress inetAddress = null;
    /**
     * 服务端的局域网IP,或动态获得IP.
     */
    private static String ip;
    /**
     * 接收到服务器端的广播,连接到服务器可以开始通信
     */
    private static final int CODE_RECEIVE_SERVER_CAST = 0x1011;
    /**
     * 收到服务器端发送的消息
     */
    private static final int CODE_RECEIVE_SERVER_MESSAGE = 0x1012;
    /**
     * 发送信息到服务器成功
     */
    private static final int CODE_POST_TO_SERVER_SUCCESS = 0x1022;

    private Socket socket;

    /**
     * 用户输入
     */
    private String userInput;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_RECEIVE_SERVER_CAST:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                socket = new Socket(ip, Constant.PORT);
                                new ReceiverThread();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    String content = msg.getData().getString("content");
                    if (mClientReceiverCallBack != null) {
                        mClientReceiverCallBack.onConnected(content);
                    }
                    break;
                case CODE_RECEIVE_SERVER_MESSAGE:
                    Bundle bundle = msg.getData();
                    String contentMsg = bundle.getString("content");
                    setCallBackMessage(contentMsg);
                    break;
                case CODE_POST_TO_SERVER_SUCCESS:
                    Bundle bundle1 = msg.getData();
                    String content1 = bundle1.getString("content");
                    setCallBackMessage(content1);

                    break;
                default:
                    break;
            }
        }
    };

    private void setCallBackMessage(String s) {
        if (mClientReceiverCallBack != null) {
            mClientReceiverCallBack.onResponse(s);
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

    public ClientUDPReceiver(Context context) {
        new UDPClientThread(context);
    }

    private class UDPClientThread extends Thread {
        static final String BROADCAST_IP = "224.0.0.1";
        //监听的端口号
        static final int BROADCAST_PORT = 1234;

        public UDPClientThread(Context context) {
            start();
        }

        @Override
        public void run() {
            super.run();
            //多点广播套接字
            MulticastSocket multicastSocket = null;
            try {
                multicastSocket = new MulticastSocket(BROADCAST_PORT);
                inetAddress = InetAddress.getByName(BROADCAST_IP);
                multicastSocket.joinGroup(inetAddress);//加入广播地址

                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                while (true) {
                    multicastSocket.receive(datagramPacket);//接收广播
                    Thread.sleep(3000);
                    ip = new String(buf, 0, datagramPacket.getLength());
                    multicastSocket.leaveGroup(inetAddress);
                    multicastSocket.close();//关闭

                    //接收服务端广播，更新UI
                    sendHandlerMessage("连接到服务器：" + ip,CODE_RECEIVE_SERVER_CAST);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收服务器端发送的消息
     */
    private class ReceiverThread extends Thread {
        public ReceiverThread() {
            start();
        }

        @Override
        public void run() {
            super.run();
            String content;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    if (socket.isClosed() || !socket.isConnected()) {
                        Log.i("ClientUDPReceiver", "socket.isClosed:" + socket.isClosed() + ",socket.isConnected:" + socket.isConnected());
                        return;
                    }
                    if ((content = reader.readLine()) != null) {
                        sendHandlerMessage(content,CODE_RECEIVE_SERVER_MESSAGE);
//                        mHandler.sendMessage(mHandler.obtainMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客服端与服务器端通信
     */
    public class Client implements Runnable {
        private String mIpAddress;

        public Client(String ipAddress) {
            this.mIpAddress = ipAddress;
        }

        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(mIpAddress);//
                Socket s = new Socket(serverAddress, Constant.PORT);
                s.setSoTimeout(2000);
                OutputStream outputStream = s.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println(userInput);
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String msg = reader.readLine();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("content", msg);
                message.setData(bundle);
                message.what = CODE_POST_TO_SERVER_SUCCESS;
                mHandler.sendMessage(message);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(String s) {
        this.userInput = s;
        new Thread(new Client(ip)).start();
    }

    private ClientReceiverCallBack mClientReceiverCallBack;

    public void setListener(ClientReceiverCallBack callBack) {
        this.mClientReceiverCallBack = callBack;
    }

    public interface ClientReceiverCallBack {
        void onResponse(String msg);

        void onConnected(String ip);
    }
}
