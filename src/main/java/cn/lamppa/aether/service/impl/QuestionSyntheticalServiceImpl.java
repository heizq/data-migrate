package cn.lamppa.aether.service.impl;


import cn.lamppa.aether.dao.*;
import cn.lamppa.aether.domain.*;
import cn.lamppa.aether.enums.CategoryType;
import cn.lamppa.aether.enums.QuestionAuditStatus;
import cn.lamppa.aether.enums.QuestionStatus;
import cn.lamppa.aether.enums.QuestionType;
import cn.lamppa.aether.service.*;
import cn.lamppa.aether.util.Constants;
import cn.lamppa.aether.util.HtmlStrUtil;
import cn.lamppa.aether.util.IdSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by heizhiqiang on 2016/3/1 0001.
 */
@Service
public class QuestionSyntheticalServiceImpl implements QuestionSyntheticalService {

    private static Logger logger = LoggerFactory.getLogger(QuestionSyntheticalServiceImpl.class);

    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Resource
    private QuestionTestPointDao questionTestPointDao;

    @Resource
    private QuestionCategoryDao questionCategoryDao;

    @Resource
    private QuestionFillingService questionFillingService;

    @Resource
    private CommonDao commonDao;

    @Resource
    private QuestionJudgeService questionJudgeService;

    @Resource
    private QuestionShortanswerService questionShortanswerService;

    @Resource
    private QuestionSyntheticalDao questionSyntheticalDao;

    @Resource
    private PointAndChoiceService pointAndChoiceService;

    @Resource
    private ImageDataService imageDataService;

    @Override
    public String transferData(List<QuestionSynthetical> resList){
        String errerId = "";
        int successCount = 0;
        try{
            for(QuestionSynthetical res:resList){
                errerId = res.getId();
                System.out.println("start import filling id="+res.getId());

                List<QuestionSyntheticalItem> items = questionSyntheticalDao.findItems(res.getId());
                for(QuestionSyntheticalItem item: items){

                    if(QuestionType.FILLING.toString().equals(item.getType().toUpperCase())){
                        questionFillingService.syncItemQuestion(item.getQuestionId());
                        item.setType(QuestionType.FILLING.toString());
                    }else if(QuestionType.JUDGE.toString().equals(item.getType().toUpperCase())){
                        questionJudgeService.syncItemQuestion(item.getQuestionId());
                        item.setType(QuestionType.JUDGE.toString());
                    }else if(QuestionType.SHORTANSWER.toString().equals(item.getType().toUpperCase())){
                        questionShortanswerService.syncItemQuestion(item.getQuestionId());
                        item.setType(QuestionType.SHORTANSWER.toString());
                    }else if(QuestionType.CHOICE.toString().equals(item.getType().toUpperCase())){
                        pointAndChoiceService.syncItemQuestion(item.getQuestionId());
                        item.setType(QuestionType.CHOICE.toString());
                    }
                    questionSyntheticalDao.addItem(item);
                }


                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                List<QuestionKnowledge> major = questionKnowledgeDao.findMajorKnowledgeList(res.getBaseId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getBaseId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setType(QuestionType.SYNTHETICAL.toString());
                    base.setAuditStatus(QuestionAuditStatus.APPROVED.toString());
                    base.setCreateTime(new Date());
                    base.setCreateUserId(Constants.user_key);
                    base.setCreateUserName(Constants.user_name);
                    questionBaseDao.addQuestionBase(base);
                }

                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    for(QuestionKnowledge v:major){
                        v.setType(QuestionType.SYNTHETICAL.toString());
                        questionKnowledgeDao.addQuestionKnowledge(v);
                    }
                }

                for(QuestionKnowledge v:minor){
                    v.setType(QuestionType.SYNTHETICAL.toString());
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }


                List<String> imgPath = HtmlStrUtil.getImgSrc(res.getTopic());
                String str=res.getTopic();
                for(String s:imgPath){
                    if(imageDataService.getMongoId(s) != null){
                        str = str.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setTopic(str);
                res.setId(res.getBaseId());
                questionSyntheticalDao.addQuestion(res);
                successCount++;
            }

        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successCount);
    }

    @Override
    public int getCount() {
        return questionSyntheticalDao.getCount();
    }

    @Override
    public List<QuestionSynthetical> findQuestionByPage(int start, int end) {
        return questionSyntheticalDao.findQuestionByPage(start, end);
    }


}
