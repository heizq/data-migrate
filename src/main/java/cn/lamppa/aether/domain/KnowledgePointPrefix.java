package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by liupd on 15-11-13.
 **/
public class KnowledgePointPrefix implements Serializable{

   //Id
   private String id;
   //当前知识点Id
   private String knowledgeId;
   //前置Id
   private String prefixId;

   public KnowledgePointPrefix() {}

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

    public String getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(String prefixId) {
        this.prefixId = prefixId;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", knowledgeId='" + knowledgeId + '\'' +
                ", prefixId='" + prefixId + '\'' +
                '}';
    }
}
