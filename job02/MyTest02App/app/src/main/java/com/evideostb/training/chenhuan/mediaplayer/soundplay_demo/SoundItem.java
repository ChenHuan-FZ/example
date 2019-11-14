package com.evideostb.training.chenhuan.mediaplayer.soundplay_demo;

/**
 * Created by ChenHuan on 2018/2/5.
 */

public class SoundItem {
    /**
     * 文件路径
     */
    private String mPath;

    /**
     * 歌曲名
     */
    private String mFileName;

    private int id;

    public String getPath() {
        return this.mPath;
    }

    public void setPath(String strPath) {
        this.mPath = strPath;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setFileName(String strFileName) {
        this.mFileName = strFileName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
