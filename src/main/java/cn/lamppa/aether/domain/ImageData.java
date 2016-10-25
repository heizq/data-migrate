package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class ImageData implements Serializable{
    private String path;

    private String mongoId;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }
}
