package taoxic.classroom;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by XJTUSE-PC on 2015/11/1.
 */
public class NetHelp implements Runnable{

    private String x = "x";
    private String getInfo = "";
    private Thread udpThread = null;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private DatagramPacket rpacket;
    private byte[] buf;
    private String ip = "192.168.20.107";
    private int port = 8925;

    public void setX(String x1){
        x = x1;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getGetInfo() {
        return getInfo;
    }

    private void stopThread() {	//停止线程
        // TODO Auto-generated method stub
        if(udpThread != null){
            udpThread.interrupt();	//若线程堵塞，则中断
        }
        Log.i("xu", "已经关闭线程");
    }
    public void startThread() {	//启动线程
        // TODO Auto-generated method stub
        if(udpThread==null) {
            udpThread = new Thread(this);
            udpThread.start();
            Log.i("xu", "已经启动线程");
        }
    }


    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            packet = new DatagramPacket(x.getBytes(), 0,x.length(), InetAddress.getByName(ip),port);
            socket.send(packet);
            buf = new byte[1024];
            rpacket = new DatagramPacket(buf,buf.length);
            socket.receive(rpacket);
            getInfo = new String(rpacket.getData(), 0, rpacket.getLength());
            Log.i("xu",x+"信息已经发送");
            Log.i("xu",getInfo+"得到的信息");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
