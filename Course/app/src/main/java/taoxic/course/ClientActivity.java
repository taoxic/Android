package taoxic.course;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taoxic.course.net.HttpUtil;

public class ClientActivity extends AppCompatActivity {

    private AutoCompleteTextView attv;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list = new ArrayList<String>();

    private ImageView img;
    private String tnum="0003043";
    private String yzm="";
    private ProgressDialog progressDialog;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter1;
    private SimpleAdapter adapter2;
    private List<Map<String, Object>> tlist;
    private List<Map<String, Object>> tlist1;
    private List<Map<String, Object>> xlist;
    private List<Map<String, Object>> ylist;
    private List<Map<String, Object>> slist;
    private List<Map<String, String>> tlistall;
    private Map<String, Object> map = new HashMap<String, Object>();
    private Map<String, Object> mapx = new HashMap<String, Object>();
    private ListView lv;
    private ListView lv1;
    private ListView lv2;
    private int FSTATE=1;
    private Boolean X = true;
    HttpClient client;
    byte[] data;
    //ViewFlipper vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
       client = new DefaultHttpClient();

        attv = (AutoCompleteTextView)findViewById(R.id.attv);

         tlistall = new ArrayList<Map<String, String>>();

        try {
            getAllTea();
        } catch (Exception e) {
            e.printStackTrace();
        }


        list.add("a");
        list.add("aa");
        list.add("aaaa");
        list.add("bbds");
        list.add("bbddd");
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        attv.setAdapter(arrayAdapter);

        map.put("text_content", "-------课程-------");        //get filename
        map.put("text_content1", "-----类型-----");
        map.put("text_content2", "---班级---");
        map.put("text_content3", "-----时间-----");
        map.put("text_content4", "--地点-");


        tlist = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this, tlist,R.layout.item,new String[]{"text_content","text_content1","text_content2","text_content3","text_content4","text_content5"}, new int[]{R.id.itemtext,R.id.itemtext1,R.id.itemtext2,R.id.itemtext3,R.id.itemtext4,R.id.itemtext5});
        tlist.add(map);

        xlist = new ArrayList<Map<String, Object>>();
        adapter1= new SimpleAdapter(this, xlist,R.layout.item,new String[]{"text_content","text_content1","text_content2","text_content3","text_content4","text_content5"}, new int[]{R.id.itemtext,R.id.itemtext1,R.id.itemtext2,R.id.itemtext3,R.id.itemtext4,R.id.itemtext5});
        xlist.add(map);

        ylist = new ArrayList<Map<String, Object>>();
        adapter2= new SimpleAdapter(this, ylist,R.layout.item,new String[]{"text_content","text_content1","text_content2","text_content3","text_content4","text_content5"}, new int[]{R.id.itemtext,R.id.itemtext1,R.id.itemtext2,R.id.itemtext3,R.id.itemtext4,R.id.itemtext5});
        ylist.add(map);


        attv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Boolean flag = true;
//                if(flag)
//                    hadCache(tnum);
//                else
                    tnum = gettnum(attv.getText().toString());
                isCache(tnum);
                    //getYzm();

                //isCache(tnum);
            }
        });
    }

    public String gettnum(String name){
        Map<String,String>  map = new HashMap<String,String>();
        for(int i=0;i<tlistall.size();i++){
            if(tlistall.get(i).get("name").equals(name)){
                tnum = tlistall.get(i).get("num");
            }
        }
        return tnum;
    }

    public void getAllTea() throws Exception{
        final JSONArray[] oba = {new JSONArray()};
        new Thread() {
                public void run(){
                    try {
                        oba[0] = query1();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for(int i = 0;i< oba[0].length();i++) {
                        JSONObject job = null;
                        try {
                            Map<String,String>  map = new HashMap<String,String>();
                            job = oba[0].getJSONObject(i);
                            list.add(job.get("name").toString());
                            map.put("name",job.get("name").toString());
                            map.put("num",job.get("tnum").toString());
                            tlistall.add(map);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    Message message = new Message();
//                    message.what = 1;
//                    handlerx.sendMessage(message);
                }

        }.start();
    }

    public void getYzm(){
        new Thread() {
            @Override
            public void run()  {
                //HttpGet httpGet1 = new HttpGet("http://192.168.20.192:8080/xu/getCode");
                HttpGet httpGet1 = new HttpGet("http://192.168.20.97:8080/zzweb/getCode");
                try {
                    HttpResponse resp2 = client.execute(httpGet1);
                    HttpEntity entity2 = resp2.getEntity();
                    InputStream in = entity2.getContent();
                    data=Util.readInputStream(in);

                    Log.i("xu","得到验证码二进制");
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    Message message = new Message();
                    message.what = 1;
                    handlerx.sendMessage(message);
            }
        }.start();
    }

    public void isCache( final String num){
        final String x = "isCache?tnum="+num;
        final String s = "queryCacheCourse?tnum="+num;
        final JSONObject[] obj = {new JSONObject()};
        final JSONArray[] ob = {new JSONArray()};
        new Thread(){
            @Override
            public void run() {
                try {
                    obj[0] = queryx(x);
                    if(obj[0].get("isCache").equals("Yes")){
                        try {
                            ob[0] = query2(s);
                            //ob[0] = query2(s);
                            Log.i("xu", "" + ob[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // handler.sendEmptyMessage(0);

                        JSONArray obj1 = ob[0];
                        try {
                            Log.i("xu","zzzzzzzzzzz"+obj1.getJSONObject(3).get("site"));
                            Log.i("xu","ssssssssss"+obj1.length());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //slist = choList();
                        tlist.clear();
                        tlist.add(map);
                        for (int i = 0; i < obj1.length(); i++) {
                            Map<String, Object> mapx = new HashMap<String, Object>();
                            try {
                                JSONObject job = obj1.getJSONObject(i);
                                mapx.put("text_content", job.get("course"));        //get filename
                                mapx.put("text_content1", job.get("type"));
                                mapx.put("text_content2", job.get("classname"));
                                mapx.put("text_content3", job.get("classtime"));
                                mapx.put("text_content4", job.get("site"));
                                tlist.add(mapx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        if (FSTATE==1){
                            FSTATE++;
                            handler1.sendEmptyMessage(0);
                        }else{
                            handler2.sendEmptyMessage(0);
                        }
                        Log.i("xu","get the cache course");
                    }else{
                        getYzm();
                    }
                    Log.i("xu", "11111111111"+obj[0].get("isCache") + "00000000");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    Handler handlerx = new Handler() {
        public void handleMessage(Message msg) {
                if(msg.what==1) {
                        showDialog_Layout(ClientActivity.this);
                }
                super.handleMessage(msg);
        }
    };

    Handler handler1 = new Handler() {
        public void handleMessage(Message msg) {

                LayoutInflater inflater = getLayoutInflater();
                ViewGroup vg = (ViewGroup) findViewById(R.id.ll);
                View v1 = inflater.inflate(R.layout.list, null);
                lv = (ListView) v1.findViewById(R.id.lv);
//                lv1 = (ListView) v1.findViewById(R.id.lv1);
//                lv2 = (ListView) v1.findViewById(R.id.lv2);
                 //lv.setAdapter(null);
                //adapter.notifyDataSetChanged();
                vg.addView(v1);
                lv.setAdapter(adapter);
//                lv1.setAdapter(adapter1);
//                lv2.setAdapter(adapter2);
        }
    };
    Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {

            adapter.notifyDataSetChanged();
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //关闭ProgressDialog
            progressDialog.dismiss();
        }};

    public List<Map<String, Object>> choList(){

        //slist.clear();
        if(FSTATE==1){
            FSTATE++;
            return tlist;

        }else if (FSTATE==2){
            FSTATE++;
            return   xlist;
        }else{
            FSTATE = 1;
            return ylist;
        }
    }

//    public void hadCache(String tnum){
//        try {
//            JSONArray oxb = query0(tnum);
//
//        for(int i=0;i<oxb.length();i++){
//            Map<String, Object> mapx = new HashMap<String, Object>();
//            try {
//                slist = choList();
//                JSONObject job = oxb.getJSONObject(i);
//
//                mapx.put("text_content",job.get("course"));        //get filename
//                mapx.put("text_content1",job.get("type"));
//                mapx.put("text_content2",job.get("classname"));
//                mapx.put("text_content3",job.get("classtime"));
//                mapx.put("text_content4",job.get("site"));
//                slist.add(mapx);
//                if (X)
//                    handler1.sendEmptyMessage(0);
//                X=false;
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void showDialog_Layout(Context context) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.dialoglayout, null);
        img = (ImageView)textEntryView.findViewById(R.id.img);
        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        img.setImageBitmap(bitmap);
        builder.setCancelable(false);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle("输入验证码");
        builder.setView(textEntryView);
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //attv.setText("get");
                        try {
                            final JSONArray[] obj = {new JSONArray()};
                            //final JSONObject obj;
                            //obj.put("name", "xxx");
                            //obj = query(edtInput.getText().toString());
                            yzm = edtInput.getText().toString();
                            progressDialog = ProgressDialog.show(ClientActivity.this, "Loading...", "Please wait...", true, false);
                            //新建线程
                            new Thread() {
                                @Override
                                public void run() {
                                    try {

                                        obj[0] = query0(tnum, yzm);
                                        Log.i("xu", "" + obj[0]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(0);

                                    JSONArray obj1 = obj[0];
                                    try {
                                        Log.i("xu","zzzzzzzzzzz"+obj1.getJSONObject(3).get("site"));
                                        Log.i("xu","ssssssssss"+obj1.length());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //slist = choList();
                                    tlist.clear();
                                    tlist.add(map);
                                    for (int i = 0; i < obj1.length(); i++) {
                                        Map<String, Object> mapx = new HashMap<String, Object>();
                                        try {
                                            JSONObject job = obj1.getJSONObject(i);
                                            mapx.put("text_content", job.get("course"));        //get filename
                                            mapx.put("text_content1", job.get("type"));
                                            mapx.put("text_content2", job.get("classname"));
                                            mapx.put("text_content3", job.get("classtime"));
                                            mapx.put("text_content4", job.get("site"));
                                            tlist.add(mapx);
//                                            if(FSTATE == 1)
//                                                tlist.add(mapx);
//                                            else if(FSTATE==2){
//                                                xlist.add(mapx);
//                                            }else{
//                                                ylist.add(mapx);
//                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                    FSTATE++;
                            if (FSTATE==1){
                                FSTATE++;
                                handler1.sendEmptyMessage(0);
                            }else
                                    handler2.sendEmptyMessage(0);

                                }
                            }.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setTitle("");
                    }
                });
        builder.show();
    }

    private JSONArray query(String yzm) throws Exception {
        Log.i("xu","zxz"+yzm);
        String url = HttpUtil.BASE_URL+"queryCourse?code="+yzm+"&tnum=0003043";
        return  new JSONArray(HttpUtil.getRequest(url));
    }
    private JSONArray query0(String tnum,String yzm) throws Exception {
        String url = HttpUtil.BASE_URL+"queryCourse?code="+yzm+"&tnum="+tnum;
        String s = HttpUtil.getRequest(url);
        Log.i("xu",""+s);
        return  new JSONArray(s);
    }
    private JSONArray query2(String z) throws Exception {
        String url = HttpUtil.BASE_URL+z;
        String s = HttpUtil.getRequest(url);
        Log.i("xu",""+s);
        return  new JSONArray(s);
    }
    public  JSONArray query1() throws Exception {
        String url = HttpUtil.BASE_URL+"getAllTea";
        return  new JSONArray(HttpUtil.getRequest(url));
    }
    public  JSONObject queryx(String x) throws Exception {
        Log.i("xu","send iscache");
        String url = HttpUtil.BASE_URL+x;
        return  new JSONObject(HttpUtil.getRequest(url));
    }

//    private JSONArray query() throws Exception {
//        String url = HttpUtil.BASE_URL+"getAllTea";
//        return  new JSONArray(HttpUtil.getRequest(url));
//    }

}
class Util {
    public static byte[] readInputStream(InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output.toByteArray();
    }
}
