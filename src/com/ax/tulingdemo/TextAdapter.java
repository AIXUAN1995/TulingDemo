package com.ax.tulingdemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextAdapter extends BaseAdapter{

	private List<ListData> lists;
	private Context mContext;
	private RelativeLayout layout;
	private TextView tv_time, tv_html;
	private EditText et;
	public TextAdapter(List<ListData> lists, Context mContext) {
		// TODO Auto-generated constructor stub
		this.lists = lists;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if (lists.get(position).getFlag() == ListData.RECEIVE) {
			layout = (RelativeLayout) inflater.inflate(R.layout.leftitem, null);
			tv_html = (TextView) layout.findViewById(R.id.tv_html);
			tv_html.setVisibility(View.GONE);
		}else if (lists.get(position).getFlag() == ListData.SEND) {
			layout = (RelativeLayout) inflater.inflate(R.layout.rightitem, null);
		}
		
		et = (EditText) layout.findViewById(R.id.et);
		tv_time = (TextView) layout.findViewById(R.id.tv_time);
		tv_time.setText(lists.get(position).getTime());
		et.setText(lists.get(position).getContent());
		hideSoftInputMethod(et);
		
		//tv.setTextIsSelectable(true);
		if (lists.get(position).getHtml() != null) {
			tv_html.setMovementMethod(LinkMovementMethod.getInstance()); 
			tv_html.setText(lists.get(position).getHtml());
			tv_html.setVisibility(View.VISIBLE);
		}
		return layout;
	}
	
	
	/*
	 * 设置EditText获得焦点是键盘不要弹出
	 * 有待测试
	 */
	public void hideSoftInputMethod(EditText ed) {
		((Activity)mContext).getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//		InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(et.getWindowToken(),0);

		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}
		if (methodName == null) {
			ed.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName,
						boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(ed, false);
			} catch (NoSuchMethodException e) {
				ed.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

}
