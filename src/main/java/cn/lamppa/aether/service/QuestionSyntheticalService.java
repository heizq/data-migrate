package cn.lamppa.aether.service;


import cn.lamppa.aether.domain.QuestionSynthetical;

import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/3/1 0001.
 */
public interface QuestionSyntheticalService {

    public String transferData(List<QuestionSynthetical> resList);

    public int  getCount();

    public List<QuestionSynthetical> findQuestionByPage(int start, int end);
}
