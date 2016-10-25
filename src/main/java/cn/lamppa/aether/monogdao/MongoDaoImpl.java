package cn.lamppa.aether.monogdao;

import cn.lamppa.aether.util.ImgFileUtil;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liupd on 16-2-25.
 **/
@Repository
public class MongoDaoImpl  implements MongoDao{

    private static final Logger log = LoggerFactory.getLogger(MongoDaoImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String save(File resource) {
        try {
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            GridFSInputFile inputFile = fs.createFile(resource);
            inputFile.setFilename(resource.getName());
            inputFile.setContentType(getContentType(resource.getAbsolutePath()));
            inputFile.save();
            return  inputFile.getId().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String save(String filename, String contentType, InputStream ins) {
        try {
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            GridFSInputFile inputFile = fs.createFile(ins,filename,false);
            inputFile.setFilename(filename);
            inputFile.setContentType(contentType);
            inputFile.save();
            return  inputFile.getId().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String update(String fileId, File resource) {
        try {
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            GridFSInputFile inputFile = fs.createFile(resource);
            inputFile.setFilename(resource.getName());
            inputFile.setContentType(getContentType(resource.getAbsolutePath()));
            inputFile.save();
            if(fileId!=null){
                ObjectId oid = new ObjectId(fileId);
                fs.remove(oid);
            }
            return  inputFile.getId().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String update(String fileId, String filename, InputStream ins) {
        try {
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            GridFSInputFile inputFile = fs.createFile(ins,filename,false);
            inputFile.setFilename(filename);
            inputFile.setContentType(getContentType(filename));
            inputFile.save();
            if(fileId!=null){
                ObjectId oid = new ObjectId(fileId);
                fs.remove(oid);
            }
            return  inputFile.getId().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean remove(String fileId) {
        boolean succ = false;
        try{
            DB db = mongoTemplate.getDb();
            GridFS fs = new GridFS(db);
            ObjectId oid = new ObjectId(fileId);
            fs.remove(oid);
            succ = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return succ;
    }

    @Override
    public Boolean checkById(String resFileId) {
        try{
            DBCollection dbc= mongoTemplate.getCollection("fs");
            DB db=dbc.getDB();
            GridFS myFS = new GridFS(db);
            GridFSDBFile gfsFile = myFS.find(new ObjectId(resFileId));
            if(null!=gfsFile)
            {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String,Object> getMapById(String resFileId) {
        DBCollection dbc= mongoTemplate.getCollection("fs");
        DB db=dbc.getDB();
        GridFS myFS = new GridFS(db);
        GridFSDBFile gfsFile = myFS.find(new ObjectId(resFileId));
        Map<String,Object> map=new HashMap<>();
        if(null!=gfsFile){
            map.put("contentType",gfsFile.get("contentType"));
            map.put("fileName",gfsFile.get("filename"));
            map.put("md5",gfsFile.getMD5());
            return map;
        }
        return null;
    }

    @Override
    public String getById(String resId, OutputStream outputStream) {
        try {
            DBCollection dbc= this.mongoTemplate.getCollection("fs");
            DB db=dbc.getDB();
            GridFS myFS = new GridFS(db);
            GridFSDBFile gfsFile = myFS.find(new ObjectId(resId));
            if(null!=gfsFile)
            {
                gfsFile.writeTo(outputStream);
                return gfsFile.getFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean getById(String resId,File outfile) {
        try {
            DBCollection dbc= this.mongoTemplate.getCollection("fs");
            DB db=dbc.getDB();
            GridFS myFS = new GridFS(db);
            GridFSDBFile gfsFile = myFS.find(new ObjectId(resId));
            if(null!=gfsFile){
                gfsFile.writeTo(outfile);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private String getContentType(String pathStr)throws IOException{
        Path path = Paths.get(pathStr);
        return Files.probeContentType(path);
    }
}
