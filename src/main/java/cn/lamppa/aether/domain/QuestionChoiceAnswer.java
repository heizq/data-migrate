package cn.lamppa.aether.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by heizhiqiang on 2015/11/16
 * 选择题答案表
 *
 */
public class QuestionChoiceAnswer implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     *
     */
    private String topic;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否正确答案
     */
    private Boolean isCorrectAnswer;

    /**
     * 选择题ID
     */
    private String choiceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {return topic;}

    public void setTopic(String topic) {this.topic = topic;}

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(Boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public String getChoiceId() {return choiceId;}

    public void setChoiceId(String choiceId) {this.choiceId = choiceId;}

}
