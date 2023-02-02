package com.ruubypay.miss.basic.utils;


import com.ruubypay.log.util.LogUtil;
import com.ruubypay.miss.basic.biz.refund.service.RbpsCoreService;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * GetUserInfoByCardNum
 * 2022/1/26 11:33
 *
 * @author wangsicheng
 * @since
 **/
@Slf4j
public class FileUtil {
    public static String getRootPath() {
        String rootPath = RbpsCoreService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (rootPath.endsWith(".jar")) {
            rootPath = rootPath.substring(0, rootPath.lastIndexOf("/") + 1);
        }
        return rootPath;
    }

    public static List<String> readLines(File file) throws IOException {
        try {
            List<String> list = new ArrayList<>();
            //读取文件里每一行信息
            //按行写入文件
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            int i = 0;
            while ((str = bf.readLine()) != null) {
                list.add(str);
            }
            fr.close();
            bf.close();
            return list;
        } catch (Exception e) {
            log.debug("文件读取异常");
            throw e;
        }
    }

    public static File[] getDirectoryList(String path){
        File file = new File(path);
        if(!file.isDirectory()){
            return null;
        }
        return file.listFiles();
    }

    public static String readLastLine(File file) {
        try {
            List<String> list = new ArrayList<>();
            //读取文件里每一行信息
            //按行写入文件
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String str = null;
            String ans = null;
            int i = 0;
            while ((str = bf.readLine()) != null) {
                ans = str;
            }
            fr.close();
            bf.close();
            return ans;
        } catch (Exception e) {
            log.debug("文件读取异常");
            throw new RuntimeException();
        }
    }


    public static void writeList(List<String> list, String path) {
        try {
            File file = new File(path);
            //按行写入文件
            FileWriter fw = new FileWriter(file, true);
            //写入中文字符时会出现乱码
            BufferedWriter bw = new BufferedWriter(fw);
            for (String lineString : list) {
                bw.write(lineString + "\t\n");
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            log.debug("文件写入异常");
        }
    }

    public static void writeString(String str, String path) {
        try {
            File file = new File(path);
            if (!makeParentFile(file)) {
                throw new RuntimeException();
            }
            //按行写入文件
            FileWriter fw = new FileWriter(file, true);
            //写入中文字符时会出现乱码
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str + "\t\n");
            bw.close();
            fw.close();
        } catch (Exception e) {
            log.error(LogUtil.exceptionMarker(), "文件写入异常", e);
        }
    }

    public static boolean makeParentFile(File file) {
        if (!file.getParentFile().exists()) {
            return file.getParentFile().mkdirs();
        }
        return true;
    }


    private static ClassLoader getClassLoader() {
        ClassLoader ret = Thread.currentThread().getContextClassLoader();
        return ret != null ? ret : FileUtil.class.getClassLoader();
    }

    public static File crealFile(String path){
        try{
            File file = new File(path);
            if(file.createNewFile()){
                System.out.println("文件创建成功！");
            }
            else {
                System.out.println("出错了，该文件已经存在。");
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }



}
