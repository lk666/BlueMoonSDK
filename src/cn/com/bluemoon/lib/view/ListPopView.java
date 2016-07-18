
package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.com.bluemoon.lib.qrcode.R;

public class ListPopView extends PopupWindow {

    private Context mContext;
    private View view;
    private OnSelectListener listener;
    private HistoryListAdapter adapter;
    private ListView listView;

    public ListPopView(Context context, OnSelectListener listener) {
        this.mContext = context;
        this.listener = listener;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.layout_search_history, null);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
//        setFocusable(true);
        setOutsideTouchable(false);
        setInputMethodMode(INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(view);
        setBackgroundDrawable(null);
        listView = (ListView) view.findViewById(R.id.listView_history);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_top_enter));

    }

    private void setData(List<String> listHistory){
        if(adapter==null){
            adapter = new HistoryListAdapter(mContext);
        }
        adapter.setList(listHistory);
        if(listView.getAdapter()==null){
            listView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

    }

    public void showPopwindow(View popStart,List<String> list) {
        setData(list);
        showAsDropDown(popStart);
    }

    public interface OnSelectListener {
        void select(String str);
        void delete(List<String> list);
    }

    class HistoryListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<String> list;

        public HistoryListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public void setList(List<String> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder ;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_search_history, null);
                viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
                viewHolder.imgDel = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.txtContent.setText(list.get(position));
            viewHolder.imgDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                    if(listener!=null){
                        listener.delete(list);
                    }
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.select(list.get(position));
                    }
                }
            });

            return convertView;
        }

        class ViewHolder{
            TextView txtContent;
            ImageView imgDel;
        }

    }
}
