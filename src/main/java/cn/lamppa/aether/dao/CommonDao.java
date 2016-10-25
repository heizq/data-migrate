package cn.lamppa.aether.dao;

/**
 * Created by Liupd on 16-3-1.
 **/
public interface CommonDao {

    public String getKnowledgeId(String srcKnowledgeId);

    public void insertMiddleTable(String questionId);

    public String getAbliity(String id);

    public String getCognizeLevel(String id);

}
