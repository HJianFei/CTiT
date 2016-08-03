package com.hjf.ctit.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hjf.ctit.R;
import com.hjf.ctit.activity.ConnDetailActivity;
import com.hjf.ctit.activity.Steal_InfoActivity;
import com.hjf.ctit.adapter.CommonAdapter;
import com.hjf.ctit.adapter.ViewHolder;
import com.hjf.ctit.constants.ActivityConstants;
import com.hjf.ctit.constants.Constants;
import com.hjf.ctit.entity.Steal_Info;
import com.hjf.ctit.utils.SharedPreferencesUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HJianFei on 2016-5-28.
 *
 * @Decription 被盗信息
 */
public class MsgFragment extends Fragment {

    private String db_Address = ActivityConstants.STEAL_INFO_IP;
    private int i = 0;
    private ListView lv_msg;
    private CommonAdapter<Steal_Info.DataBean> mAdapter;
    private View view;
    private ConnDetailActivity mActivity;
    private List<Steal_Info.DataBean> mDataBean = new ArrayList<>();
    private PullToRefreshView mPullToRefreshView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1 || msg.arg2 == 1) {
                mPullToRefreshView.setRefreshing(false);
                initView(view);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (ConnDetailActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_msg, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_msg = (ListView) view.findViewById(R.id.lv_msg_steal);
        mAdapter = new CommonAdapter<Steal_Info.DataBean>(mActivity, mDataBean, R.layout.msg_layout) {
            @Override
            public void convert(ViewHolder helper, Steal_Info.DataBean item) {
                helper.setText(R.id.barcode, item.getBarcode());
                helper.setText(R.id.description, item.getDecription());
                helper.setText(R.id.created, item.getCreated());
                helper.setImage(R.id.iv_list, item.getImgPath());
            }
        };
        lv_msg.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //listItem的点击事件
        lv_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Steal_Info.DataBean dataBean = mDataBean.get(i);
                Intent intent = new Intent(mActivity, Steal_InfoActivity.class);
                intent.putExtra("barcode", dataBean.getBarcode());
                intent.putExtra("title", dataBean.getDecription());
                startActivity(intent);

            }
        });
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        //设置服务器IP
        String db_address = (String) SharedPreferencesUtils.getParam(mActivity,
                Constants.DB_ADDRESS, Constants.DEFAULT_DB_ADDRESS);
        if (!db_address.equals(Constants.DEFAULT_DB_ADDRESS)) {
            db_Address = "http://" + db_address + ":8080/cargosys/app/baseInfo/getWarnInfo";

        }
//
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(db_Address)
                .get()
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message message = Message.obtain();
                message.arg2 = 1;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s = response.body().string();
//                System.out.println(s);
                Gson gson = new Gson();
                Steal_Info steal_info = gson.fromJson(s, Steal_Info.class);
                mDataBean = steal_info.getData();
                Message message = Message.obtain();
                message.arg1 = 1;
                mHandler.sendMessage(message);
            }
        });
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Message message = Message.obtain();
//                message.arg2 = 1;
//                mHandler.sendMessage(message);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String s = response.body().string();
//                System.out.println(s);
//                Gson gson = new Gson();
//                Steal_Info steal_info = gson.fromJson(s, Steal_Info.class);
//                mDataBean = steal_info.getData();
//                Message message = Message.obtain();
//                message.arg1 = 1;
//                mHandler.sendMessage(message);
//
//            }
//        });

    }


//    public void refresh() {
//        if (mAdapter != null) {
//            i++;
//            System.out.println(">>>:"+i);
//            initDatas();
//            mAdapter.notifyDataSetChanged();
//        }
//
//    }
}
