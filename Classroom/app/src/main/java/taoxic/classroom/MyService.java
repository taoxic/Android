package taoxic.classroom;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class MyService extends Service {

    private String mac = "x";
    private Boolean state = false;
    private NetHelp netHelp;
    private String  checkInf = "";
    private DatagramSocket checksocket;
    private byte[] checkbuf;
    private DatagramPacket checkpacket;
    private DatagramPacket cpacket;

    public void sendMac(final String mac){
        netHelp = new NetHelp();
        netHelp.setX(mac);
        netHelp.setIp("192.168.20.107");
        netHelp.setPort(8925);
        netHelp.startThread();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                    Log.i("xu", netHelp.getGetInfo() + "ssssssssss");
                    if (netHelp.getGetInfo().equals("ok")){
                        state = true;
                        new Thread(){
                            @Override
                            public void run() {

                                while (true) {

                                    try {
                                        checksocket = new DatagramSocket();
                                        checkbuf = new byte[1024];
                                        checkpacket = new DatagramPacket(checkbuf, checkbuf.length);
                                        checksocket.receive(checkpacket);
                                        checkInf = new String(checkpacket.getData(), 0, checkpacket.getLength());
                                        if(checkInf !=null){
                                            cpacket = new DatagramPacket("here".getBytes(), 0,"here".length(), InetAddress.getByName("192.168.20.107"),8925);
                                            checksocket.send(cpacket);
                                            checkInf = null;
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }.start();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    public MyService() {
    }

    @Override
    public void onCreate() {

        Log.i("xu","start ser...");
    }


    @Override
    public IBinder onBind(Intent intent) {

        return  new MyBinder();
    }
    class MyBinder extends Binder {
        public void setMac(String mac1){
            mac = mac1;
        }
        public void startT(){
            Log.i("xu","fasong2"+mac);
            sendMac(mac);
        }
        public Boolean getState(){
            return state;
        }
    }


}
