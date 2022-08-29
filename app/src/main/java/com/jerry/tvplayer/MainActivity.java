package com.jerry.tvplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.jerry.tvplayer.m3u.M3UParser;
import com.jerry.tvplayer.m3u.M3U_Entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener, LiveAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private LiveAdapter liveAdapter;
    //key:频道; value: m3u实体列表
    private HashMap<String, List> mChannel = new HashMap<>();
    private List<String> cctvList = new ArrayList<>();
    private List<String> weishiList = new ArrayList<>();
    private List<String> otherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 配置recycleview
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        liveAdapter = new LiveAdapter(this);
        liveAdapter.setItemClickListener(this);
        recyclerView.setAdapter(liveAdapter);

        //配置tab
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);
        //解析数据源
        parseM3uFiles();
        //添加标签
        addTabs();
    }

    /**
     * 处理m3u文件
     */
    private void parseM3uFiles() {
        String externalAppFilesPath = PathUtils.getExternalAppMoviesPath();
        String m3uFilePath = "";
        try {
            String[] streams = getResources().getAssets().list("streams");
            assert streams != null;
            for (String m3uFile : streams) {
                m3uFilePath = externalAppFilesPath + File.separator + m3uFile;
                if (!FileUtils.isFileExists(m3uFilePath)) {
                    Log.e(TAG, "ready to copy file from assets: " + m3uFilePath);
                    ResourceUtils.copyFileFromAssets("streams" + File.separator + m3uFile, m3uFilePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String m3uFile = "cn.m3u";
        m3uFilePath = externalAppFilesPath + File.separator + m3uFile;
        Log.e(TAG, "parse m3u file path: " + m3uFilePath);
        try {
            List<M3U_Entry> playlist = new M3UParser().parse(m3uFilePath);
            for (M3U_Entry m3UEntry : playlist) {
                Log.e(TAG, "m3UEntry: " + m3UEntry);
                String channelName = m3UEntry.getTvgId();
                List<M3U_Entry> m3U_entries = mChannel.get(channelName);
                if (m3U_entries == null) m3U_entries = new ArrayList<>();
                m3U_entries.add(m3UEntry);
                mChannel.put(channelName, m3U_entries);
                if (!TextUtils.isEmpty(channelName) && channelName.toLowerCase().contains("cctv")) {
                    if (!cctvList.contains(channelName))
                        cctvList.add(channelName);
                } else if (!TextUtils.isEmpty(channelName) && channelName.toLowerCase().contains("卫视")) {
                    if (!weishiList.contains(channelName))
                        weishiList.add(channelName);
                } else {
                    if (!otherList.contains(channelName))
                        otherList.add(channelName);
                }
            }
            Log.e(TAG, "频道数: " + mChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTabs() {
        addTab("cctv", "CCTV");
        addTab("weishi", "卫视");
        addTab("other", "其他");
    }

    private void addTab(String tag, String title) {
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setTag(tag);
        tab.setText(title);
        tabLayout.addTab(tab);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabLayout.removeOnTabSelectedListener(this);
    }

    /**
     * 切换标签回调
     *
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tag = tab.getTag().toString();
        if (tag.equals("cctv")) {
            liveAdapter.setLiveList(cctvList);
        } else if (tag.equals("weishi")) {
            liveAdapter.setLiveList(weishiList);
        } else {
            liveAdapter.setLiveList(otherList);
        }
        liveAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(String id) {
        Log.e(TAG, "onItemClick: " + id);
        List<M3U_Entry> list = mChannel.get(id);
        M3U_Entry m3U_entry = list.get(0);
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra("url", m3U_entry.getUrl());
//        intent.putExtra("url", "http://183.207.249.15/PLTV/3/224/3221225530/index.m3u8");
        startActivity(intent);
    }
}
