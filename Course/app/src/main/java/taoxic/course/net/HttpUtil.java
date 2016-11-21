package taoxic.course.net;

import android.provider.Settings;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by XJTUSE-PC on 2015/12/14.
 */
public class HttpUtil {
    public static HttpClient client = new DefaultHttpClient();
    //public static final String BASE_URL = "http://192.168.20.192:8080/xu/";
    public static final String BASE_URL = "http://192.168.20.97:8080/zzweb/";

    public static String getRequest(final String url) throws Exception{
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpGet get = new HttpGet(url);
                HttpResponse httpResponse = client.execute(get);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        return task.get();
    }
    public static String postRequest(final String url,final Map<String,String> rawParams) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {

                HttpPost post = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key:rawParams.keySet()){
                    params.add(new BasicNameValuePair(key,rawParams.get(key)));
                }
                post.setEntity(new UrlEncodedFormEntity(params,"gbk"));
                HttpResponse httpResponse = client.execute(post);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    return result;
                }
                return null;
            }
        });
        new Thread(task).start();
        return task.get();
    }
}
