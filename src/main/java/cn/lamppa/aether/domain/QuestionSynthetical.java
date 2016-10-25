package cn.lamppa.aether.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dell on 2015/12/18.
 * 综合题
 */
public class QuestionSynthetical implements Serializable {

    /**
     * primary key
     */
    private String  id;

    /**
     * 题干
     */
    private String topic;

    /**
     * 基础表ID
     */
    private String baseId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

}
