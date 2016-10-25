package cn.lamppa.aether.domain;


import java.io.Serializable;

/**
 * Created by heizhiqiang on 2015/11/16
 * 题目分类关系表
 *
 */
public class QuestionCategory implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     * 题目ID
     */
    private String questionId;

    /**
     * 分类
     */
    private String categroy;

    /**
     * 分类名称
     */
    private String categroyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy;
    }

    public String getCategroyName() {
        return categroyName;
    }

    public void setCategroyName(String categroyName) {
        this.categroyName = categroyName;
    }
}
