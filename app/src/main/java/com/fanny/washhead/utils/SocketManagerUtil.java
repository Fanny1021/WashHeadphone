package com.fanny.washhead.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by Fanny on 18/3/21.
 */

public class SocketManagerUtil extends Thread {

    private Socket socket;
    private BufferedWriter bw;
    private Handler handler;
    private boolean noTask;
    private boolean unexceptedClosed;
    private int what;
    private String msg;
    private boolean isConnected;

    public SocketManagerUtil(Socket socket, Handler handler) throws IOException {
        this.socket = socket;
        this.handler = handler;
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        noTask = true;
        isConnected = true;
    }

    public void send(String s) {
        if (!isConnected) {
            return;
        }
        what = 0;
        msg = s;
        noTask = false;
        synchronized (this) {
            notify();
        }
    }

    public void close() {
        if (socket.isClosed()) {
            return;
        }
        what = -1;
        noTask = false;
        synchronized (this) {
            notify();
        }
    }

    private class Listener extends Thread {
        private BufferedReader br;

        Listener(BufferedReader br) {
            this.br = br;
        }

        @Override
        public void run() {
            String s;
            while (true){
                try {
                    while ((s = br.readLine()) != null) {
                        Message msg = new Message();
                        msg.what = Constant.TcpMsg_MSG;
                        msg.obj = s + '\n';
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (!socket.isClosed()) {
                        unexceptedClosed = true;
//                        close();
                        Message msg = new Message();
                        msg.what = Constant.TcpMsg_UNEXPECTED_CLOSE;
                        handler.sendMessage(msg);
                    }
                    return;
                }

                if(s==null){
                    unexceptedClosed = true;
                    close();
                    Message msg = new Message();
                    msg.what = Constant.TcpMsg_UNEXPECTED_CLOSE;
                    handler.sendMessage(msg);
                }

            }

        }
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Listener(br).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            synchronized (this) {
                while (noTask) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            noTask = true;
            switch (what) {
                case 0:
                    try {
                        bw.write(msg);
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = Constant.TcpMsg_UNEXPECTED_CLOSE;
                        handler.sendMessage(msg);
                        handler.sendEmptyMessage(Constant.TcpMsg_UNEXPECTED_CLOSE);
                    }
                    break;
                case -1:
                    try {
                        socket.close();
                        if (!unexceptedClosed) {
                            Message msg = new Message();
                            msg.what = Constant.TcpMsg_CLIENT_CLOSE_OK;
                            handler.sendMessage(msg);
//                            handler.sendEmptyMessage(Constant.TcpMsg_CLIENT_CLOSE_OK);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (!unexceptedClosed) {
                            Message msg = new Message();
                            msg.what = Constant.TcpMsg_CLIENT_CLOSE_ERR;
                            handler.sendMessage(msg);
//                            handler.sendEmptyMessage(Constant.TcpMsg_CLIENT_CLOSE_ERR);
                        }
                    }
                    isConnected = false;
                    break;
            }
        }
    }
}
