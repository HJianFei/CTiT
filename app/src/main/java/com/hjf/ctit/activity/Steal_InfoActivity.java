package com.hjf.ctit.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hjf.ctit.R;
import com.hjf.ctit.constants.ActivityConstants;
import com.hjf.ctit.constants.Constants;
import com.hjf.ctit.entity.Steal_Info_Detail;
import com.hjf.ctit.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * 被盗信息详情
 */
public class Steal_InfoActivity extends AppCompatActivity {

    private String mBarcode;
    private TextView tv_barcode, tv_size, tv_color, tv_description, tv_price;
    private TextView tv_prodtctNo, tv_provider, tv_place_location, tv_session;
    private Steal_Info_Detail.DataBean mData = new Steal_Info_Detail.DataBean();
    private String imgPath;
    private ImageView iv_info;
    private String mTitle;
    private String db_Address = ActivityConstants.STEAL_INFO_DETAIL;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1 || msg.arg2 == 1) {
                initView();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.loading)
                        .showImageOnFail(R.mipmap.ic_launcher)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
//                System.out.println(imgPath+mData.getImgPath());
                ImageLoader.getInstance().displayImage(imgPath+mData.getImgPath(), iv_info, options);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steal__info);
        mBarcode = getIntent().getExtras().getString("barcode");
        mTitle = getIntent().getExtras().getString("title");
        //初始化数据
        initDatas();
        String url=(String) SharedPreferencesUtils.getParam(this, Constants.DB_ADDRESS, Constants.DEFAULT_DB_ADDRESS);
        imgPath = "http://" + url + ":8080/cargosys/";
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        tv_barcode = (TextView) findViewById(R.id.tv_barcode);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_place_location = (TextView) findViewById(R.id.tv_place_location);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_prodtctNo = (TextView) findViewById(R.id.tv_productNo);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_session = (TextView) findViewById(R.id.tv_session);
        tv_provider = (TextView) findViewById(R.id.tv_provider);
        iv_info = (ImageView) findViewById(R.id.iv_info);

        tv_barcode.setText(mData.getBarcode());
        tv_color.setText(mData.getColor());
        tv_description.setText(mData.getDecription());
        tv_place_location.setText(mData.getPlaceLocation());
        tv_price.setText(mData.getPrice() + "");
        tv_prodtctNo.setText(mData.getProductNo());
        tv_size.setText(mData.getSize());
        tv_session.setText(mData.getSeason());
        tv_provider.setText(mData.getProvider());
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        String db_address = (String) SharedPreferencesUtils.getParam(this,
                Constants.DB_ADDRESS, Constants.DEFAULT_DB_ADDRESS);
        if (!db_address.equals(Constants.DEFAULT_DB_ADDRESS)) {
            db_Address = "http://" + db_address + ":8080/cargosys/app/baseInfo/getBaseInfoByBarcode?barcode=";

        }
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(db_Address + mBarcode)
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
                Gson gson = new Gson();
                Steal_Info_Detail steal_info_detail = gson.fromJson(s, Steal_Info_Detail.class);
                mData = steal_info_detail.getData();
                Message message = Message.obtain();
                message.arg1 = 1;
                mHandler.sendMessage(message);
            }
        });

    }
}
