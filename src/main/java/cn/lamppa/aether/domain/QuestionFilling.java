package cn.lamppa.aether.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xufeng on 15-12-16.
 **/
public class QuestionFilling  implements Serializable{

    //填空题Id
    private  String id;
    //填空题题干
    private  String topic;
    //填空题解析
    private  String analysis;
    //基础表Id
    private  String baseId;
    //是否小题
    private  Boolean isSmall;

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

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
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
