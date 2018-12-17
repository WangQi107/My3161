package com.wq.myclass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wq.myclass.R;
import com.wq.myclass.bean.Notice;

import java.util.List;

public class Notice_Adapter extends BaseAdapter {
    private Context ctx;
    private List<Notice> notices;

    public Notice_Adapter(Context ctx, List<Notice> notices) {
        this.ctx = ctx;
        this.notices = notices;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            convertView = inflater.inflate(R.layout.item_notice, null);
            holder = new ViewHolder();
            holder.noticetitle = convertView.findViewById(R.id.notice_title);
            holder.noticename = convertView.findViewById(R.id.notice_name);
            holder.noticetime = convertView.findViewById(R.id.notice_time);
            holder.noticecontent = convertView.findViewById(R.id.notice_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Notice n = notices.get(position);
        holder.noticetitle.setText(String.valueOf(n.getTitle()));
        holder.noticename.setText(String.valueOf(n.getName()));
        holder.noticetime.setText(String.valueOf(n.getTime()));
        holder.noticecontent.setText(String.valueOf(n.getContent()));
        return convertView;
    }

    class ViewHolder {
        private TextView noticetitle;
        private TextView noticename;
        private TextView noticetime;
        private TextView noticecontent;
    }
}
