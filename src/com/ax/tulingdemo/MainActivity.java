package com.ax.tulingdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements HttpGetDataListener, OnClickListener, OnTouchListener {

	private HttpData httpData;
	private List<ListData> lists;
	private ListView lv;
	private EditText et_send;
	private Button btn_send;
	private String content_str;
	private TextAdapter adapter;
	private String [] welcome_tips;
	private double currentTime, oldTime = 0;
	private int finishCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lv = (ListView) findViewById(R.id.lv);
		et_send = (EditText) findViewById(R.id.et_send);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		
		lv.setOnTouchListener(this);
		et_send.setOnTouchListener(this);
		btn_send.setOnTouchListener(this);
		

		lists = new ArrayList<ListData>();
		adapter = new TextAdapter(lists, this);
		lv.setAdapter(adapter);
		
		ListData listData = new ListData(getWelcomeTip(), ListData.RECEIVE, getTime(), null);
		lists.add(listData);
	}
	
	public String getWelcomeTip(){
		String welcome_tip = null;
		welcome_tips = getResources().getStringArray(R.array.welcome_tips);
		int index = (int) (Math.random()*(welcome_tips.length-1));
		welcome_tip = welcome_tips[index];
		return welcome_tip;
	}

	@Override
	public void getDataUrl(String data) {
		parseText(data);
	}

	public void parseText(String str) {
		try {
			JSONObject jb = new JSONObject(str);
			ListData listData;
			if (jb.length() == 3) {
				//获取链接添加链接
				String url = "<a href='"+jb.getString("url")+"'>点击链接了解一下</a>";
				Spanned html = Html.fromHtml(url);
				listData = new ListData(jb.getString("text"), ListData.RECEIVE, getTime(), html);
			}else{
				listData = new ListData(jb.getString("text"), ListData.RECEIVE, getTime(), null);
			}
			lists.add(listData);
			adapter.notifyDataSetChanged();//adapter进行重新适配
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		content_str = et_send.getText().toString();
		et_send.setText("");
		String drops = content_str.replace(" ", "");//去掉空格
		String drope = drops.replace("\n", "");		//去掉回车
		ListData listData = new ListData(content_str, ListData.SEND, getTime(), null);
		lists.add(listData);
		
		//当数据大于30条时移除前十条
		if (lists.size()>30) {
			for(int i=0; i<10; i++){
				lists.remove(i);
			}
		}
		adapter.notifyDataSetChanged();
		
		//如果网络可用
		if (isNetworkAvailable(this)) {
			httpData = (HttpData) new HttpData(
					"http://www.tuling123.com/openapi/api?key=e9da543ce03143deb44d5d6ba046cb41&info="
							+ drope, this).execute();
		}else {
			ListData ld = new ListData("小灵需要联网才能和你对话哦", ListData.RECEIVE, getTime(), null);
			lists.add(ld);
			adapter.notifyDataSetChanged();
		}
	}
	
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if (currentTime - oldTime >= 5*1000*60) {
			oldTime = currentTime;
			return str;
		}
		return "";
	}
	
	//判断网络连接是否可用
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } else {
            //如果仅仅是用来判断网络连接
        	//则可以使用 cm.getActiveNetworkInfo().isAvailable();  
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    }
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		finishCount++;
		if (finishCount == 1) {
			Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
		}else if(finishCount == 2){			
			super.finish();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		finishCount = 0;
		return false;
	}
	
}
