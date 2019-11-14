package com.evideostb.training.chenhuan.mediaplayer.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardUtils {
    private static String SDPATH; //SD卡路径
    private static String FILESPATH; //文件路径

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**判断SDCard是否存在？是否可以进行读写*/
    public static boolean SDCardState() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {//表示SDCard存在并且可以读写
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    public static String getSdCardPath() {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "不适用";
        }
        return sdpath;
    }

    /**
     * 获取默认的文件路径
     *
     * @return
     */
    public static String getDefaultFilePath() {
        String filepath = "";
        File file = new File(Environment.getExternalStorageDirectory(),
                "abc.txt");
        if (file.exists()) {
            filepath = file.getAbsolutePath();
        } else {
            filepath = "不适用";
        }
        return filepath;
    }

    // write SDCard
    private void writeFileSdcardFile(String fileName, String writeStr) throws IOException {
        try {

            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = writeStr.getBytes();

            fout.write(bytes);
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**获取SDCard文件路径*/
    public static String SDCardPath(){
        if(SDCardState()){//如果SDCard存在并且可以读写
            SDPATH = Environment.getExternalStorageDirectory().getPath();
            return SDPATH;
        }else{
            return null;
        }
    }

    /**
     * 判断文件是否已经存在
     *
     * @param fileName
     *            要检查的文件名
     * @return boolean, true表示存在，false表示不存在
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**获取SDCard 总容量大小(MB)*/
    public long SDCardTotal(){
        if(null != SDCardPath()&&SDCardPath().equals("")){
            StatFs statfs = new StatFs(SDCardPath());
            //获取SDCard的Block总数
            long totalBlocks = statfs.getBlockCount();
            //获取每个block的大小
            long blockSize = statfs.getBlockSize();
            //计算SDCard 总容量大小MB
            long SDtotalSize = totalBlocks*blockSize/1024/1024;
            return SDtotalSize;
        }else{
            return 0;
        }
    }

    /**获取SDCard 可用容量大小(MB)*/
    public long SDCardFree(){
        if(null != SDCardPath()&&SDCardPath().equals("")){
            StatFs statfs = new StatFs(SDCardPath());
            //获取SDCard的Block可用数
            long availaBlocks = statfs.getAvailableBlocks();
            //获取每个block的大小
            long blockSize = statfs.getBlockSize();
            //计算SDCard 可用容量大小MB
            long SDFreeSize = availaBlocks*blockSize/1024/1024;
            return SDFreeSize;
        }else{
            return 0;
        }
    }
}
