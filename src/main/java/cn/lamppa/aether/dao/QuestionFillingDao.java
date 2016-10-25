package cn.lamppa.aether.dao;


import cn.lamppa.aether.domain.QuestionFilling;
import cn.lamppa.aether.domain.QuestionFillingAnswer;

import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
public interface QuestionFillingDao {


    public String addQuestion(QuestionFilling model);

    public String addQuestionAnswer(QuestionFillingAnswer model);

    public List<QuestionFilling> findQuestionFilling(String questionId, Boolean isSmall);

    public List<QuestionFillingAnswer> findQuestionFillingAnswer(String questionId);

    public List<QuestionFilling> findQuestionByPage(int start, int end);

    public int getCount();

}
