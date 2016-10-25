package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by liupd on 15-11-12.
 **/
public class TestPoint implements Serializable{

    //考点Id
    String id;
    //考点名称
    String name;
    //知识点Id
    String knowledgeId;
    //考点说明
    String instruction;
    //考点排序
    String sort;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", knowledgeId='" + knowledgeId + '\'' +
                ", instruction='" + instruction + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
