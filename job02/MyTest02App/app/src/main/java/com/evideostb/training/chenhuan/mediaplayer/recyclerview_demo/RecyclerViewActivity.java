package com.evideostb.training.chenhuan.mediaplayer.recyclerview_demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.evideostb.training.chenhuan.mediaplayer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class RecyclerViewActivity extends Activity {
    private final String TAG = "RecyclerViewActivity";

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    //图标解压路径
    private final String ICON_LOCAL_PATH = Environment.getExternalStorageDirectory() + File.separator + "ICON_PATH";
    //歌手目录在Assets下的文件名
    private final String SINGER_LIST_PATH = "singer_list.sin";
    //歌手图片在Assets下的文件名
    private final String SINGER_ICON_PATH = "singer_image.zip";
    //分页参数
    private final Integer PAGE_SIZE = 12;
    private Integer PAGE_NUM = 1;
    //数据参数
    private List<RecyclerViewSinger> mSourceDatas,mAdapterDatas;
    private AssetManager assetManager;
    private Thread unZipThread;
    private Boolean isLoading = false;
    //数据更新handle
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_grid);

        assetManager = getAssets();
        mSourceDatas = new ArrayList<>();
        mAdapterDatas = new ArrayList<>();

        initData(SINGER_LIST_PATH);
        initView();
        startUnZipThread();
    }

    private void initView() {
        // 横向方向的网格样式，每行四个Item
        mLayoutManager = new GridLayoutManager(this,2, OrientationHelper.HORIZONTAL,false);
        mAdapter = new RecyclerViewAdapter(this,mAdapterDatas);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new GridSpaceDecoration(10));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是否滑动到底部，是则加载一页数据。
                if (isSlideToBottom(recyclerView) && !isLoading) {
                    Integer temp = PAGE_NUM * PAGE_SIZE ;
                    //判断是否已经没有更多数据了，如果还有则加载。
                    if (mSourceDatas.size() >= temp) {
                        isLoading = true;
                        mAdapterDatas.addAll(mSourceDatas.subList(temp - PAGE_SIZE,temp));
                        PAGE_NUM++;
                        //模拟请求数据延迟1秒后更新数据
                        mHandle.sendMessageDelayed(mHandle.obtainMessage(),1000);
                    } else if (mAdapterDatas.size() < mSourceDatas.size()) {
                        //判断最后一页不够一页PAGE_SIZE的情况。
                        isLoading = true;
                        Integer preCount = temp - PAGE_SIZE;
                        Integer rest = mSourceDatas.size() - preCount;
                        mAdapterDatas.addAll(mSourceDatas.subList(preCount,preCount + rest));
                        mHandle.sendMessageDelayed(mHandle.obtainMessage(),1000);
                    }
                }
            }

        });
    }
    /**
     * 判断是否滑动到底部
     * @param recyclerView
     * @return
     */
    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return false;
        }
        if (recyclerView.computeHorizontalScrollExtent() + recyclerView.computeHorizontalScrollOffset() >= recyclerView.computeHorizontalScrollRange()) {
            return true;
        }
        return false;
    }
    /**
     * 初始化数据
     * @param filename assets目录下的文件名
     */
    private void initData(String filename){
        boolean bfalg = true;
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = assetManager.open(filename);
            byte[] buffer = new byte[1024*128];
            int length;
            while ((length = is.read(buffer)) > 0){
                sb.append(new String(buffer,0,length));
            }
        } catch (IOException e) {
            e.printStackTrace();
            bfalg = false;
        }
        if (bfalg) {
            String[] str = sb.toString().split("\\r\\n");
            for (int i = 0; i < str.length; i++) {
                Log.d(TAG, str[i]);
                String[] temp = str[i].split("\\|\\|");
                RecyclerViewSinger recyclerViewSinger = new RecyclerViewSinger();
                recyclerViewSinger.setName(temp[1]);
                recyclerViewSinger.setIcon(ICON_LOCAL_PATH + File.separator + "singer_image" + File.separator + temp[1] + ".jpg");
                mSourceDatas.add(recyclerViewSinger);
            }
            //源数据初始化完加载第一页数据
            mAdapterDatas.addAll(mSourceDatas.subList(0, PAGE_SIZE));
            PAGE_NUM++;
        }
    }

    /**
     * 开启解压缩线程解压缩Icon
     */
    private void startUnZipThread(){
        unZipThread = new Thread(new Runnable() {
            @Override
            public void run() {
                unZipIcon(SINGER_ICON_PATH,ICON_LOCAL_PATH,false);
            }
        });
        unZipThread.start();
    }

    /**
     * 解压图标文件到ICON_PATH
     * @param assetsName
     * @param outputDirectory
     * @param isReWrite
     */
    private void unZipIcon(String assetsName,String outputDirectory,Boolean isReWrite){
        // 创建解压目标目录
        File file = new File(outputDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            // 打开压缩文件
            InputStream inputStream = assetManager.open(assetsName);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            // 读取一个进入点
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            // 使用1Mbuffer
            byte[] buffer = new byte[1024 * 1024];
            // 解压时字节计数
            int count = 0;
            // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
            while (zipEntry != null) {
                // 如果是一个目录
                if (zipEntry.isDirectory()) {
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者是文件不存在
                    if (isReWrite || !file.exists()) {
                        file.mkdir();
                    }
                } else {
                    // 如果是文件
                    file = new File(outputDirectory + File.separator + zipEntry.getName());
                    // 文件需要覆盖或者文件不存在，则解压文件
                    if (isReWrite || !file.exists()) {
                        file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        while ((count = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, count);
                        }
                        fileOutputStream.close();
                    }
                }
                // 定位到下一个文件入口
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
