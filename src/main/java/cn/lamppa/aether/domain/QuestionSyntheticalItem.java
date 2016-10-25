package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by dell on 2015/12/18.
 * 综合题题目关系
 */
public class QuestionSyntheticalItem implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 综合题ID
     */
    private String  syntheticalId;

    /**
     * 基础题ID
     */
    private String  questionId;
    /**
     * 基础题类型
     */
    private String  type;
    /**
     * 排序
     */
    private String  sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSyntheticalId() {
        return syntheticalId;
    }

    public void setSyntheticalId(String syntheticalId) {
        this.syntheticalId = syntheticalId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
