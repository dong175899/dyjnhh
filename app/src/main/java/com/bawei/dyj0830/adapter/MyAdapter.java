package com.bawei.dyj0830.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.dyj0830.R;
import com.bawei.dyj0830.bean.MyBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    Context context;
    List<MyBean.ResultBean> results;
    public MyAdapter(Context context, List<MyBean.ResultBean> results) {
        this.context=context;
        this.results=results;
    }

    @Override
    public int getCount() {
        return results.size();
    }


    @Override
    public Object getItem(int position) {
        return results.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (position%2){
            case 0:
                ViewHolder1 viewHolder1;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.list_one, null);
                    viewHolder1  = new ViewHolder1();
                    viewHolder1.tv_one = convertView.findViewById(R.id.tv_one);
                    viewHolder1.iv_one = convertView.findViewById(R.id.iv_one);
                    convertView.setTag(viewHolder1);
                }else {
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                }
                viewHolder1.tv_one.setText(results.get(position).getName());
                Glide.with(context).load(results.get(position).getImageUrl()).into(viewHolder1.iv_one);
                break;
            case 1:
                ViewHolder2 viewHolder2;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.list_two, null);
                    viewHolder2  = new ViewHolder2();
                    viewHolder2.tv_two = convertView.findViewById(R.id.tv_two);
                    viewHolder2.iv_two = convertView.findViewById(R.id.iv_two);
                    convertView.setTag(viewHolder2);
                }else {
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                }
                viewHolder2.tv_two.setText(results.get(position).getName());
                Glide.with(context).load(results.get(position).getImageUrl()).into(viewHolder2.iv_two);
                break;

        }
        return convertView;
    }

    private class ViewHolder1 {
        TextView tv_one;
        ImageView iv_one;
    }
    private class ViewHolder2 {
        TextView tv_two;
        ImageView iv_two;
    }
}
