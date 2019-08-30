package com.bawei.dyj0830.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.bawei.dyj0830.R;
import com.bawei.dyj0830.adapter.MyAdapter;
import com.bawei.dyj0830.base.BaseFragment;
import com.bawei.dyj0830.bean.MyBean;
import com.bawei.dyj0830.sql.MySql;
import com.bawei.dyj0830.utils.Api;
import com.bawei.dyj0830.utils.HttpUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends BaseFragment {

    private PullToRefreshListView prlv;
    private int page=1;
    private int count=5;
    private Handler handler=new Handler();
    private List<MyBean.ResultBean> results=new ArrayList<>();
    @Override
    protected int getLayoutResID() {
        return R.layout.frag01;
    }

    @Override
    protected void initView(View view) {
        prlv = view.findViewById(R.id.prlv);
        prlv.setMode(PullToRefreshListView.Mode.BOTH);
    }

    @Override
    protected void initData() {
        boolean netWork = HttpUtils.getInstance().isNetWork(getActivity());
        getServerData(page);
        if (netWork) {
            prlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                    page=1;
                    getServerData(page);
                    results.clear();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prlv.onRefreshComplete();
                        }
                    },2000);
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                    page++;
                    getServerData(page);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prlv.onRefreshComplete();
                        }
                    },2000);
                }
            });
        }else {
            String json=null;
            MySql mySql = new MySql(getActivity());
            SQLiteDatabase db = mySql.getWritableDatabase();
            Cursor cursor = db.query("mylist", null, null, null, null, null, null);
            while (cursor.moveToNext()){
                json = cursor.getString(cursor.getColumnIndex("json"));
            }
            Gson gson = new Gson();
            MyBean myBean = gson.fromJson(json, MyBean.class);
            List<MyBean.ResultBean> result = myBean.getResult();

            results.addAll(result);
            MyAdapter adapter = new MyAdapter(getActivity(), results);
            prlv.setAdapter(adapter);
        }
    }

    private void getServerData(int page) {
        HttpUtils.getInstance().getJsonData(Api.path + "movie/v1/findHotMovieList" + "?page=" + page + "&count=" + count, new HttpUtils.ICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if (obj!=null) {
                    MySql mySql = new MySql(getActivity());
                    SQLiteDatabase db = mySql.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("json",obj.toString());
                    db.insert("mylist",null,values);
                    Toast.makeText(getActivity(), "缓存成功", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                MyBean myBean = gson.fromJson(obj.toString(), MyBean.class);
                List<MyBean.ResultBean> result = myBean.getResult();
                results.addAll(result);
                MyAdapter adapter = new MyAdapter(getActivity(),results);
                prlv.setAdapter(adapter);
            }

            @Override
            public void onError(String msg) {

            }
        });
        HttpUtils.getInstance().not();
    }
}

