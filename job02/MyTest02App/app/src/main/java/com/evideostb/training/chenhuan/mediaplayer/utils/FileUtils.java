package com.evideostb.training.chenhuan.mediaplayer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	/**
	 * 遍历文件夹
	 * @param dir 指定目录
	 * @param sufix 指定后缀名
	 * @return 文件列表
	 */
    public static List<File> getFilesUnderFolder(String dir, String sufix) {
        List<File> files = new ArrayList<>();
        File file = new File(dir);
        if (!file.isDirectory()) {
            return files;
        }
        File[] fs = file.listFiles();
        if (fs == null) {
            return files;
        }
        for (File f : fs) {
            if (f.isDirectory()) {
                files.addAll(getFilesUnderFolder(f.getAbsolutePath(), sufix));
            } else {
                if (f.getAbsolutePath().endsWith(sufix)) {
                    files.add(f);
                }
            }
        }
        return files;
    }

    public static void saveBufferToFile(String path, byte[] buffer) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(buffer);
        fout.close();
    }

    public static boolean deleteFile(File file) {
        boolean bProcessRes = false;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                bProcessRes = file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    bProcessRes = deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
        } else {
            LogUtil.e("删除文件：" + file + "失败,文件不存在！");
        }
        return bProcessRes;
    }

    public static boolean fileDelete(String filePath) {
        File file = new File(filePath);
        if (file.exists() == false) {
            return false;
        }
        return file.delete();
    }
}
