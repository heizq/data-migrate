package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by dell on 15-11-13.
 **/
public class KnowledgePointRelation implements Serializable{

    //id
    private String id;
    //当前知识点Id
    private String knowledgeId;
    //上级Id
    private String parentId;

    public KnowledgePointRelation() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", knowledgeId='" + knowledgeId + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
