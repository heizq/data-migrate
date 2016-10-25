package cn.lamppa.aether.dao;


import cn.lamppa.aether.domain.QuestionKnowledge;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionKnowledgeDao {

    public int addQuestionKnowledge(QuestionKnowledge model);

    public int[] addQuestionKnowledge(List<QuestionKnowledge> list);

    public QuestionKnowledge findMajorKnowledge(String questionId);

    public List<QuestionKnowledge> findMajorKnowledgeList(String questionId);

    public List<QuestionKnowledge> findMinorKnowledges(String questionId);
}
