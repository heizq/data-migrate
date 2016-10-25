package cn.lamppa.aether.service;


import cn.lamppa.aether.domain.QuestionShortanswer;

import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 **/
public interface QuestionShortanswerService {


    public void syncItemQuestion(String questionId);

    public int getCount();

    public List<QuestionShortanswer> findShortanswerByPage(int start, int end);

    public String transferData(List<QuestionShortanswer> list);
}
