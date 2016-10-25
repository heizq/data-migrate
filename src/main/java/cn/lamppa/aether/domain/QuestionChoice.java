package cn.lamppa.aether.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by heizhiqiang on 2015/11/16
 * 选择题
 *
 */
public class QuestionChoice  implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     * 题干
     */
    private String topic;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 是否多选
     */
    private Boolean isMulti;

    /**
     * 基础表ID
     */
    private String baseId;

    /**
     * 是否小题
     */
    private Boolean isSmall;


    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Boolean getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Boolean isMulti) {
        this.isMulti = isMulti;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    public Boolean getIsSmall() {
        return isSmall;
    }

    public void setIsSmall(Boolean isSmall) {
        this.isSmall = isSmall;
    }


}
