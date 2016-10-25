package cn.lamppa.aether.dao;


import cn.lamppa.aether.domain.QuestionSynthetical;
import cn.lamppa.aether.domain.QuestionSyntheticalItem;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/29 0029.
 */
public interface QuestionSyntheticalDao {

    public String  addQuestion(QuestionSynthetical model);

    public String  addItem(QuestionSyntheticalItem model);


    public List<QuestionSyntheticalItem> findItems(String questionId);

    public List<QuestionSynthetical> findQuestionByPage(int start, int end);

    public int getCount();
}
