package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

import android.os.AsyncTask;

import com.evideostb.training.chenhuan.mediaplayer.utils.FileUtils;
import com.evideostb.training.chenhuan.mediaplayer.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundLoader {
    private static int id = 0;
    public interface OnMusicLoadListener {
        void onLoading();

        void onLoaded(List<SoundItem> items);
    }

    public SoundLoader() {
    }

    public void load(final String parentDir, final OnMusicLoadListener listener) {
        listener.onLoading();
        new AsyncTask<Void, Void, List<SoundItem>>() {
            @Override
            protected List<SoundItem> doInBackground(Void... voids) {
                List<File> mp3Files = FileUtils.getFilesUnderFolder(parentDir, "wav");
                List<SoundItem> items = new ArrayList<SoundItem>();
                for (File mp3 : mp3Files) {
                    try {
                        items.add(loadSingalFile(mp3));
                    } catch (Exception e) {
                        e.printStackTrace();//解析错误
                    }
                }
                return items;
            }

            @Override
            protected void onPostExecute(List<SoundItem> list) {
                LogUtil.d("onPostExecute");
                listener.onLoaded(list);
            }
        }.execute();

    }

    /**
     * 解析SD卡上的MP3,把MP3文件转换成一个MusicItem的对象.
     * @param mp3File
     * @return
     * @throws Exception
     */
    private SoundItem loadSingalFile(File mp3File) throws Exception {
        SoundItem item = new SoundItem();
        item.setId(id++);
        item.setFileName(mp3File.getName());
        item.setPath(mp3File.getAbsolutePath());
        LogUtil.d(item.getPath());
        return item;
    }

}
