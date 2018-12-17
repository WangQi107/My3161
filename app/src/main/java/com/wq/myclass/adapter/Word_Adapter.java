package com.wq.myclass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wq.myclass.R;
import com.wq.myclass.bean.Word;
import com.wq.myclass.transform.XCRoundImageView;

import java.util.List;

public class Word_Adapter extends BaseAdapter {
    private Context ctx;
    private List<Word> words;

    public Word_Adapter(Context ctx, List<Word> words) {
        this.ctx = ctx;
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
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
            convertView = inflater.inflate(R.layout.item_words, null);
            holder = new ViewHolder();
            holder.wordheader = convertView.findViewById(R.id.word_header);
            holder.wordname = convertView.findViewById(R.id.word_name);
            holder.wordtime = convertView.findViewById(R.id.word_time);
            holder.wordcontent = convertView.findViewById(R.id.word_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Word w = words.get(position);
        if (w.getHeader() == 1) {
            holder.wordheader.setImageResource(R.mipmap.ic_header_boy);
        } else if (w.getHeader() == 2) {
            holder.wordheader.setImageResource(R.mipmap.ic_header_girl);
        } else if (w.getHeader() == 3) {
            holder.wordheader.setImageResource(R.mipmap.ic_header_teacher);
        }
        holder.wordname.setText(String.valueOf(w.getName()));
        holder.wordtime.setText(String.valueOf(w.getTime()));
        holder.wordcontent.setText(String.valueOf(w.getContent()));
        return convertView;
    }

    class ViewHolder {
        private XCRoundImageView wordheader;
        private TextView wordname;
        private TextView wordtime;
        private TextView wordcontent;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
