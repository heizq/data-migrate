package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by Liupd on 15-12-17.
 **/
public class QuestionTestPoint implements Serializable{

    //ID
    private  String id;
    //题目ID
    private  String questionId;
    //题类型
    private  String questionType;
    //知识点ID
    private  String knowledgeId;
    //考点id
    private  String testPointId;

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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getTestPointId() {
        return testPointId;
    }

    public void setTestPointId(String testPointId) {
        this.testPointId = testPointId;
    }
}
