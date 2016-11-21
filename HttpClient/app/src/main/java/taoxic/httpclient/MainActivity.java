package taoxic.httpclient;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView  autotext;
    private ArrayAdapter<String> arrayAdapter;
    private SQLiteDatabase db=null;
    private MyDbHelper helper=null;
    private String [] arr={"aa","aab","aac"};
    private ArrayList list = new ArrayList();
    private Boolean flag = false;
    private ListView lis;
    private ImageView iv;
    private Drawable drawable;
    private Button btre,gyzm;
    private Bitmap bp;
    byte[] data;
    HttpClient client;
    private EditText tyzm,tnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv = (ImageView)findViewById(R.id.iv);
        btre = (Button)findViewById(R.id.btre);
        gyzm = (Button)findViewById(R.id.gyzm);
        autotext= (AutoCompleteTextView)findViewById(R.id.attv);
        tyzm = (EditText)findViewById(R.id.tyzm);
        tnum =(EditText)findViewById(R.id.tnum);

        client = new DefaultHttpClient();

        outd();//缓存数据
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            getYzm();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //iv.setBackgroundDrawable(Drawable.createFromPath("/sdcard/yzm.jpg"));
//        iv.setBackgroundDrawable();
        helper = new MyDbHelper(this,"xt.db",null,1);
        db = helper.getWritableDatabase();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        autotext.setAdapter(arrayAdapter);

        autotext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list = outd();
            }
        });
        gyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv.setImageBitmap(bitmap);
            }
        });
        btre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                byte[] data = ImageViewService.getImage();
                //把二进制图片转成位图
                try {
                    getCourse(tnum.getText().toString(),tyzm.getText().toString());
                    Log.i("xu",tnum.getText().toString()+"---"+tyzm.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void createdb(View v) throws Exception{
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        SQLiteDatabase db  = this.openOrCreateDatabase(path + "/tao.db", SQLiteDatabase.OPEN_READWRITE, null);
        String sql = "create table teachers(id text primary key,cname text)";
        db.execSQL(sql);
        db.close();
    }
    public void putdb(View v){

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        final SQLiteDatabase db  = this.openOrCreateDatabase(path+"/tao.db",SQLiteDatabase.OPEN_READWRITE,null);
        db = helper.getWritableDatabase();
        new Thread(){
            public void run(){
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpUriRequest request = new HttpGet("http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                    HttpResponse resp = client.execute(request);
                    int code = resp.getStatusLine().getStatusCode();
                    Header[] headers = resp.getAllHeaders();

                    HttpGet httpGet = new HttpGet("http://121.248.70.214/jwweb/ZNPK/Private/List_JS.aspx?xnxq=20150&js=");
                    httpGet.setHeader("Host","121.248.70.214");
                    httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
                    httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                    httpGet.setHeader("Accept-Encoding","gzip, deflate");
                    httpGet.setHeader("Referer", "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                    httpGet.setHeader("Connection","keep-alive");
                    HttpResponse resp1 = client.execute(httpGet);
                    HttpEntity entity = resp1.getEntity();
                    Log.i("xu","=========666=");
                    String ss = EntityUtils.toString(entity);
                    ss = ss.replaceAll("<script", "");
                    Document doc = Jsoup.parse(ss);
                    Elements es =doc.getElementsByTag("option");
                    Log.i("xu","=========="+es.get(5).attr("value").toString());
                    for(int i=0;i<es.size();i++){
//                        System.out.println(es.get(i).text());
//                        System.out.println(es.get(i).attr("value"));
                        String sql = "insert into teachers(id,cname) values(?,?)";
                        db.execSQL(sql, new String[]{es.get(i).attr("value"),es.get(i).text()});
                    }
                    db.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
    public List<Teacher> parseHtmlToTeacher(InputStream in,String charsetName,String baseUri) throws IOException{
        db = helper.getWritableDatabase();
        List<Teacher> ts = new ArrayList<Teacher>();
        Document doc = Jsoup.parse(in,charsetName,baseUri);
        Elements optionEles = doc.select("option[value^=000]");
        for(int i=0;i<optionEles.size();i++){
            Teacher t = new Teacher();
            t.setTnum(optionEles.get(i).attr("value"));
            t.setName(optionEles.get(i).text());
            if(flag){
                String sql = "insert into teachers(id,cname,tnum,isCache) values(null,?,?,0)";
                db.execSQL(sql, new String[]{optionEles.get(i).attr("value"),optionEles.get(i).text()});
            }
            ts.add(t);
        }
        db.close();
        return ts;
    }

    public void outdb(View v){

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        SQLiteDatabase db  = this.openOrCreateDatabase(path+"/tao.db",SQLiteDatabase.OPEN_READWRITE,null);
        try {
            db = helper.getWritableDatabase();
            String sql = "select * from teachers";
            Cursor c = db.rawQuery(sql,null);
            while(c.moveToNext()){
                String id= c.getString(0);
                String cname = c.getString(1);
                Log.i("xu",id+"..."+cname);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void outd(){
        //        db = helper.getWritableDatabase();
        ArrayList<Teacher> ts = new ArrayList<Teacher>();
        new Thread(){
            public void run(){
                try {
                    File file = new File("taoxic.txt");

                    HttpUriRequest request = new HttpGet("http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                    HttpResponse resp = client.execute(request);
                    int code = resp.getStatusLine().getStatusCode();
                    Header[] headers = resp.getAllHeaders();

                    HttpGet httpGet = new HttpGet("http://121.248.70.214/jwweb/ZNPK/Private/List_JS.aspx?xnxq=20150&js=");
                    httpGet.setHeader("Host","121.248.70.214");
                    httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
                    httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    httpGet.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                    httpGet.setHeader("Accept-Encoding","gzip, deflate");
                    httpGet.setHeader("Referer", "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                    httpGet.setHeader("Connection", "keep-alive");
                    HttpResponse resp1 = client.execute(httpGet);
                    HttpEntity entity = resp1.getEntity();
                    Log.i("xu","=========666=");
                    Log.i("xu","*************z");
//                    String ss = EntityUtils.toString(entity);
//                    ss = ss.replaceAll("<script", "");
//                    FileWriter fw = new FileWriter(file.getName());
//                    BufferedWriter bw = new BufferedWriter(fw);
//                    bw.write(ss);
//                    bw.close();
                    Log.i("xu", "*************");



//                    HttpEntity entity2 = resp1.getEntity();
//                    byte[] b =EntityUtils.toByteArray(entity);
//                    bp = BitmapFactory.decodeByteArray(b,0, b.length);

//                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//                    BufferedImage bi1 =ImageIO.read(bais);
//                    File w2 = new File("c://yzm.jpg");
//                    ImageIO.write(bi1, "jpg", w2);




//                    HttpPost httpPost = new HttpPost("http://121.248.70.214/jwweb/ZNPK/TeacherKBFB_rpt.aspx");
//                    httpPost.setHeader("Host","121.248.70.214");
//                    httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
//                    httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                    httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//                    httpPost.setHeader("Accept-Encoding","gzip, deflate");
//                    httpPost.setHeader("Referer", "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
//                    //httpGet.setHeader("Cookie","ASP.NET_SessionId=u3jkv5i5ibym4n45kfjep355; ASP.NET_SessionId_NS_Sig=oenCV6md3D16vlC_");
//                    httpPost.setHeader("Connection","keep-alive");
//
//                    MultipartEntity mutiEntity = new MultipartEntity();
//                    mutiEntity.addPart("Sel_XNXQ",new StringBody("20150"));
//                    mutiEntity.addPart("Sel_JS",new StringBody("0003043"));
//                    mutiEntity.addPart("type",new StringBody("2"));
//                    mutiEntity.addPart("txt_yzm",new StringBody("jkp2"));
//                    httpPost.setEntity(mutiEntity);
//
//                    HttpResponse resp2 = client.execute(httpPost);
//                    HttpEntity entity2 = resp2.getEntity();
//
//                    String ss2 = EntityUtils.toString(entity);
//                    System.out.println(ss2);
//                    Document doc = Jsoup.parse(ss2);
//                    Elements es =doc.getElementsByTag("td");
//
//                    System.out.println(es.size());
//                    for(int i=0;i<es.size();i++){
//                        System.out.println(es.get(i).text());
//                    }
//                    Document doc = Jsoup.parse(ss);
//                    Elements es =doc.getElementsByTag("option");
//                    Log.i("xu","=========="+es.get(5).attr("value").toString());
//                    for(int i=0;i<es.size();i++){
////                        System.out.println(es.get(i).text());
////                        System.out.println(es.get(i).attr("value"));
//                        String sql = "insert into teachers(id,cname) values(?,?)";
//                        db.execSQL(sql, new String[]{es.get(i).attr("value"),es.get(i).text()});
//                    }
//                    db.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getYzm() throws Exception{
        new Thread() {
            @Override
            public void run()  {
                HttpGet httpGet1 = new HttpGet("http://121.248.70.214/jwweb/sys/ValidateCode.aspx");
                httpGet1.setHeader("Host","121.248.70.214");
                httpGet1.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
                httpGet1.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                httpGet1.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                httpGet1.setHeader("Accept-Encoding","gzip, deflate");
                httpGet1.setHeader("Referer","http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                //httpGet.setHeader("Cookie","ASP.NET_SessionId=u3jkv5i5ibym4n45kfjep355; ASP.NET_SessionId_NS_Sig=oenCV6md3D16vlC_");
                httpGet1.setHeader("Connection","keep-alive");

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
            }
        }.start();
    }
    public void getCourse( String num, String yzm) throws Exception{
        final String fnum = num;
        final String fyzm = yzm;
        new Thread(){
            @Override
            public void run() {
                Log.i("xu","+++++++++");
                HttpPost httpPost = new HttpPost("http://121.248.70.214/jwweb/ZNPK/TeacherKBFB_rpt.aspx");
                httpPost.setHeader("Host","121.248.70.214");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
                httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
                httpPost.setHeader("Accept-Encoding","gzip, deflate");
                httpPost.setHeader("Referer", "http://121.248.70.214/jwweb/ZNPK/TeacherKBFB.aspx");
                //httpGet.setHeader("Cookie","ASP.NET_SessionId=u3jkv5i5ibym4n45kfjep355; ASP.NET_SessionId_NS_Sig=oenCV6md3D16vlC_");
                httpPost.setHeader("Connection","keep-alive");
                Log.i("xu", "+++++++++");
                String ss2 = null;
//
//                    MultipartEntity mutiEntity = new MultipartEntity();
//                    Log.i("xu","++1");
                try {
//                    Charset cs = Charset.forName("UTF-8");
//                    Log.i("xu","----------");
//                    StringBody bd = new StringBody("20150");
//                    Log.i("xu","++2");
//                    mutiEntity.addPart("Sel_XNXQ", bd);
//                    Log.i("xu", "++3");
//                    Log.i("xu", "+++++2");
//                    mutiEntity.addPart("Sel_JS",new StringBody(fnum));//"0003043"
//                    mutiEntity.addPart("type",new StringBody("2"));
//                    mutiEntity.addPart("txt_yzm",new StringBody(fyzm));
//                    httpPost.setEntity(mutiEntity);
                    List<NameValuePair> paris =new ArrayList<NameValuePair>();
                    NameValuePair p1 = new BasicNameValuePair("Sel_XNXQ","20150");
                    NameValuePair p2 = new BasicNameValuePair("Sel_JS",fnum);
                    NameValuePair p3 = new BasicNameValuePair("type","2");
                    NameValuePair p4 = new BasicNameValuePair("txt_yzm",fyzm);
                    paris.add(p1);
                    paris.add(p2);
                    paris.add(p3);
                    paris.add(p4);
                    UrlEncodedFormEntity entity3 = new UrlEncodedFormEntity(paris);
                    httpPost.setEntity(entity3);


                    HttpResponse resp2 = client.execute(httpPost);
                    HttpEntity entity2 = resp2.getEntity();

                    ss2 = EntityUtils.toString(entity2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("xu","vvvvvvvvv");
                System.out.println(ss2);
                Document doc = Jsoup.parse(ss2);
                Elements es =doc.getElementsByTag("td");

                System.out.println(es.size());
                for(int i=0;i<es.size();i++){
                    Log.i("xu",es.get(i).text());
                }
            }
        }.start();

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