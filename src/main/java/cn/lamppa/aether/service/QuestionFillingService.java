package cn.lamppa.aether.service;



import cn.lamppa.aether.domain.QuestionFilling;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
public interface QuestionFillingService {


    public void syncItemQuestion(String questionId);

    public String transferData(List<QuestionFilling> resList);

    public int  getCount();

    public List<QuestionFilling> findQuestionByPage(int start, int end);



}
