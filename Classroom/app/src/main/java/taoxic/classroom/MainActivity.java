package taoxic.classroom;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    private MyService.MyBinder binder;
    private MyServiceConnect connection;
    private Intent intent1;
    private NetworkInterface networkInterface;
    WifiInfo wifiInfo;


    private Button btsign;
    private TextView tvinf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        connection = new MyServiceConnect();
        intent1 = new Intent();
        intent1.setClass(MainActivity.this,MyService.class);
        bindService(intent1,connection,BIND_AUTO_CREATE);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
       wifiInfo = wifiManager.getConnectionInfo();


    }


    public void findview(){
        btsign = (Button)findViewById(R.id.sign);
        tvinf = (TextView)findViewById(R.id.inf);
    }

    //按键方法
    public void sign(View v){
        Intent intent = new Intent();
        intent.setAction("text");
        startService(intent);

        binder.setMac(wifiInfo.getMacAddress());
        binder.startT();
        tvinf.setText("打卡中，请稍等");
        fin();
    }
    public void fin(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putBoolean("state",binder.getState());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.getData().getBoolean("state")){
                tvinf.setText("打卡成功");
            }else{
                tvinf.setText("打卡失败，请重新打卡");
            }
        }
    };









    class MyServiceConnect implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
