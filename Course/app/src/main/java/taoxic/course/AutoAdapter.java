package taoxic.course;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XJTUSE-PC on 2015/12/15.
 */
public class AutoAdapter extends BaseAdapter {
    private Context context;
    private List<String> mObjects;
    private List<Integer> mObjects2;
    private final Object mLock = new Object();
    private int mResource;
    private int mDropDownResource;
    private int mFieldId = 0;
    private boolean mNotifyOnChange = true;
    private Context mContext;
    private ArrayList<String> mOriginalValues;
    private LayoutInflater mInflater;
    private int maxMatch=8;//最大显示8条数据

    public AutoAdapter(Context context, int resource,int textViewResourceId, List<String> objects, List<Integer> objects2) {
        init(context, resource, textViewResourceId, objects, objects2);
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(context);//针对外面传递过来的Context变量，
        //tv.setText(texts[position]);//设置要显示的内容
        if(position%3==2){//第三个变颜色
            tv.setTextColor(Color.RED);
        }else{
            tv.setTextColor(Color.BLACK);
        }
        return tv;
    }

    private void init(Context context, int resource, int textViewResourceId,List<String> objects, List<Integer> objects2) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mObjects2 = objects2;
        mFieldId = textViewResourceId;
    }
}
