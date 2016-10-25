package cn.lamppa.aether.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by liupd on 16-2-26.
 **/
public class ImgFileUtil {

    private static String imgfilepath = null;

    private static void init(){
        try {
            imgfilepath = PropertyConfigUtil.getPropertyValue("img.resource.mongoId");
            File file = new File(imgfilepath);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeToSqlFile(String imgfilejson)throws Exception{
        if(imgfilepath == null){
            init();
        }
        synchronized (imgfilepath){
            BufferedWriter bw = null;
            try {
                FileWriter writer = new FileWriter(imgfilepath,true);
                bw = new BufferedWriter(writer);
                bw.write(imgfilejson);
                bw.newLine();
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(bw!=null){
                    bw.close();
                }
            }
        }
    }
}
