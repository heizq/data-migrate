package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public class QuestionKnowledge implements Serializable {

    //ID
    private  String id;
    //题目ID
    private  String questionId;
    //题类型
    private  String type;
    //知识点ID
    private  String knowledgeId;
    //是否主知识点
    private  Boolean isMain;
    //认识层次ID
    private  String cognizeLevelId;
    //能力体系
    private  String abilityId;
    /**
     * 序号
     */
    private Integer sort;

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

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public String getCognizeLevelId() {
        return cognizeLevelId;
    }

    public void setCognizeLevelId(String cognizeLevelId) {
        this.cognizeLevelId = cognizeLevelId;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

