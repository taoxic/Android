package taoxic.classroom;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void sign(){
        new Thread(){
            @Override
            public void run() {

                try {
                    Socket socket = new Socket("192.168.10.62",8080);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(msg.getBytes());
                    System.out.println("发送信息"+msg);
                    InputStream is= socket.getInputStream();
                    byte[] buf = new byte[1024];
                    String str = new String(buf, 0, is.read(buf));
                    Log.i("xu",str+" get");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
