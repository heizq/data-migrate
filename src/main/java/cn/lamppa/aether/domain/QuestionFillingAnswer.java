package cn.lamppa.aether.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xufeng on 15-12-16.
 **/
public class QuestionFillingAnswer implements Serializable{

    //填空题Id
    private  String id;
    //填空题答案
    private  String topic;
    //填空题排序
    private  Integer sort;
    //基础表Id
    private  String fillingId;

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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getFillingId() {
        return fillingId;
    }

    public void setFillingId(String fillingId) {
        this.fillingId = fillingId;
    }
}
