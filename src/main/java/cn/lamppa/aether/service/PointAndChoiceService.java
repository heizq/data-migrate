package cn.lamppa.aether.service;


import cn.lamppa.aether.domain.QuestionChoice;
import cn.lamppa.aether.domain.QuestionFilling;

import java.util.List;

/**
 * Created by liupd on 16-2-26.
 **/
public interface PointAndChoiceService {

    public void createSynQuestion()throws Exception;

    public void createSynQuestionMiddleTable()throws Exception;

    public void synAbility()throws Exception;

    public void synAbility2(int start, int end)throws Exception;

    public void synCognizelevel() throws Exception;

    public void synKnowledge()throws Exception;

    public void synKnowledgeRelation()throws Exception;


    public String transferData(List<QuestionChoice> resList);

    public int getCount();

    public List<QuestionChoice> findQuestionByPage(int start, int end);

    public void syncItemQuestion(String questionId);


}
