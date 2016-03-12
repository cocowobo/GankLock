package me.wavever.ganklock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import me.wavever.ganklock.MyApplication;
import me.wavever.ganklock.R;
import me.wavever.ganklock.config.Config;
import me.wavever.ganklock.model.Gank;
import me.wavever.ganklock.presenter.MainPresenter;
import me.wavever.ganklock.ui.adapter.MainRecycleViewAdapter;
import me.wavever.ganklock.util.DateUtil;
import me.wavever.ganklock.util.DialogUtil;
import me.wavever.ganklock.view.IMainView;

public class MainActivity extends BaseActivity implements IMainView<Gank>{

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private ImageView mImg;

    private RecyclerView mRecyclerView;
    private CollapsingToolbarLayout collapsToolbar;
    private List<String> mDatas;
    private MainRecycleViewAdapter mAdapter;

    private List<Gank> mList;

    private MainPresenter mainPresenter = new MainPresenter(this,this);

    private String mGirl;


    @Override protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override protected void initPresenter() {
        presenter = new MainPresenter(this,this);
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getData();
        MyApplication.getSp().putBoolean(Config.IS_FIRST_RUN,false);
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mImg = (ImageView) findViewById(R.id.img_main);
        collapsToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapsing_toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        setSupportActionBar(mToolbar);
        collapsToolbar.setTitle(DateUtil.getTodayFormatDate());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        if(MyApplication.getSp().getBoolean(Config.IS_FIRST_RUN,true)){
            DialogUtil.showSingleDialog(this,R.string.tips_first_run);
        }
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_about){
        }        return super.onOptionsItemSelected(item);
    }


    @Override public boolean isGetData() {
        return mainPresenter.isGetData();
    }


    @Override public void showErrorSnack(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG).show();
    }


    @Override public void showUpdate() {

    }


    /**
     *
     */
    @Override public void completeGetData() {
       Snackbar.make(mRecyclerView, R.string.tips_load_finish,
                Snackbar.LENGTH_LONG).show();
        MyApplication.getSp().putBoolean(Config.GET_DATA, true);
    }


    @Override public boolean checkIsOpenLock() {
        return MyApplication.getSp().getBoolean(Config.LOCK_IS_OPEN,false);
    }


    @Override public void getLastData(String lastDate) {
        mainPresenter.getData(lastDate);
        collapsToolbar.setTitle(lastDate);
    }


    @Override public void fillData(List<Gank> list,String girlUrl) {
        mList = list;
        mAdapter = new MainRecycleViewAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);
        Picasso.with(this).load(girlUrl).into(mImg);
        MyApplication.getSp().putString("girl",girlUrl);
    }


    @Override public boolean isGetTodayData() {
        return mainPresenter.isGetData();
    }

    private void getData() {
        mainPresenter.getData(today);
    }


}
