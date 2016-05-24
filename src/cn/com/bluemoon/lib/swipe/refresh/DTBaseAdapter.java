package cn.com.bluemoon.lib.swipe.refresh;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class DTBaseAdapter<T> extends BaseAdapter {
	public ArrayList<T> mList;
	public long mTime;

	public DTBaseAdapter() {
		mList = new ArrayList<T>();
	}

	public long getmTime() {
		return mTime;
	}

	public void setmTime(long mTime) {
		this.mTime = mTime;
	}

	
	public void clearList() {
		if (mList != null)
			mList.clear();
	}

	
	public ArrayList<T> getList() {
		return mList;
	}

	public void removeItem(int index) {
		mList.remove(index);
	}

	public void removeItem(T info) {
		if (mList.contains(info))
			mList.remove(info);
	}

	
	public void addList(List<T> list) {
		if (list != null && list.size() > 0)
			mList.addAll(list);
	}

	public void addItemLast(T info) {
		if (info != null) {
			mList.add(info);
		}
	}

	
	public void addItem(T info, int index) {
		if (info != null) {
			mList.add(index, info);
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
