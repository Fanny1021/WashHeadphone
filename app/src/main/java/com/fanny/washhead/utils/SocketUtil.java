package com.fanny.washhead.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Fanny on 17/4/20.
 */

public class SocketUtil {

    public static Socket socket;
    public static SocketAddress socAddress;

    public static InputStream in;
    public static OutputStream out;
    private char data[];
    private BufferedReader br;
    public static int rcvDataLength;

    private static byte[] buff = new byte[16];
    private byte[] sentbuff = new byte[16];

    public static int connectStaus = 0;

    public static void setConnectStaus(int staus) {
        connectStaus = staus;
    }

    public static void setSocket(Socket msocket) {
        socket = msocket;
        in = getInputStream();
        out = getOutputStream();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static InputStream getInputStream() {
        if (socket.isConnected()) {
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return in;
    }

    public static OutputStream getOutputStream() {
        if (socket.isConnected()) {
            try {
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

//    public int connect() {
//        try {
//            socket = new Socket();
//            socAddress = new InetSocketAddress(ip_address, Integer.getInteger(ip_port));
//            socket.setSoTimeout(3000);
//            if (socket == null || !socket.isConnected()) {
//                //网络未连接 －1
//                isConnect=false;
//                return -1;
//            } else {
//                //网络连接成功 1
//                isConnect=true;
//                in = socket.getInputStream();
//                out = socket.getOutputStream();
//                socket.setReceiveBufferSize(100 * 1024);
//                br = new BufferedReader(new InputStreamReader(in, "GBK"));
//                return 1;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //网络连接异常
//            isConnect=false;
//            return 0;
//        }
//
//    }


//    public SocketUtil(char[] data) {
//        this.data = data;
//    }

    /**
     * 发送字符数据
     *
     * @param senddata
     */
    public static void SendData(char senddata[]) {
        if (socket != null) {
            try {
//                senddata[6] = CreatXOR(senddata, 6);
                String msg = new String(senddata);
                out.write((msg.toString() + "\n").getBytes("GBK"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送字节数据
     *
     * @param data
     */
    public static void SendDataByte(byte[] data) {
        if (socket != null) {
            try {
                out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static char CreatXOR(char RecData[], int len) {
        char uXOR;
        uXOR = RecData[0];
        for (int i = 1; i < len; i++) {
            uXOR = (char) (uXOR ^ RecData[i]);
        }
        if (uXOR == 0) uXOR = 6;
        return uXOR;
    }

    public static boolean ReceiveData() {
        if (socket == null) {
            return false;
        }
        try {
            rcvDataLength = in.read(buff);
            while (rcvDataLength > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
