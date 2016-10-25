package cn.lamppa.aether.monogdao;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public interface MongoDao {

    public String save(File resource);

    public String save(String filename,String contentType,InputStream ins);

    public String update(String fileId, File resource);

    public String update(String fileId,String filename,InputStream ins);

    public boolean remove(String fileId);


    public Boolean checkById(String resFileId);

    public Map<String,Object> getMapById(String resFileId);


    String getById(String resId, OutputStream outputStream);

    /**
     * 调用此接口，outfile必须是存在的
     * @param resId
     * @param outfile
     * @return
     */
    public boolean getById(String resId,File outfile);


}
