package taoxic.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import taoxic.course.net.HttpUtil;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void getTea(View v) throws Exception {

        Intent intent = new Intent(MainActivity.this,ClientActivity.class);
        startActivity(intent);
        finish();
//        try {
//            JSONArray obj;
//            obj = query();
//            Log.i("xu",""+obj.get(0));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
//    private JSONArray query() throws Exception {
//        String url = HttpUtil.BASE_URL+"getAllTea";
//        return  new JSONArray(HttpUtil.getRequest(url));
//    }

}
