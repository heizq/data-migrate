package cn.lamppa.aether.service;

import cn.lamppa.aether.domain.ImageData;

import java.io.File;
import java.util.List;

/**
 * Created by liupd on 16-2-25.
 **/
public interface ImageDataService {

    public void saveImageToMongo(String path,String fileName);

    public void createTempTable() throws Exception;


    public List<ImageData> getImageDatas();

    public void imageDataPlay(List<ImageData> list_cache);

    public String getMongoId(String path);

}
