package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.dao.*;
import cn.lamppa.aether.domain.*;
import cn.lamppa.aether.enums.QuestionAuditStatus;
import cn.lamppa.aether.enums.QuestionStatus;
import cn.lamppa.aether.enums.QuestionType;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.service.QuestionFillingService;
import cn.lamppa.aether.util.Constants;
import cn.lamppa.aether.util.HtmlStrUtil;
import cn.lamppa.aether.util.IdSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by heizhiqiang on 2016/3/28 0028.
 */

@Service
public class QuestionFillingServiceImpl implements QuestionFillingService {

    private static Logger logger = LoggerFactory.getLogger(QuestionFillingServiceImpl.class);

    @Resource
    private QuestionBaseDao questionBaseDao;

    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Resource
    private QuestionTestPointDao questionTestPointDao;

    @Resource
    private QuestionCategoryDao questionCategoryDao;

    @Resource
    private QuestionFillingDao questionFillingDao;

    @Resource
    private CommonDao commonDao;

    @Resource
    private ImageDataService imageDataService;

    public void syncItemQuestion(String questionId) {
        logger.info("start transfer filling item  ");
        List<QuestionFilling> list = questionFillingDao.findQuestionFilling(questionId,true);

        if(list != null && list.size() > 0){
            transferItemData(list);
        }else {
            logger.error("Synthetical filling item  id={} not found",questionId);
        }
    }

    public String transferData(List<QuestionFilling> resList)  {
        String errerId = "";
        int successCount = 0;
        try{
            for(QuestionFilling res:resList){
                errerId = res.getId();
                System.out.println("start import filling id="+res.getId());

                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getBaseId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getBaseId());
                List<QuestionFillingAnswer> answers = questionFillingDao.findQuestionFillingAnswer(res.getId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setType(QuestionType.FILLING.toString());
                    base.setAuditStatus(QuestionAuditStatus.APPROVED.toString());
                    base.setCreateTime(new Date());
                    base.setCreateUserId(Constants.user_key);
                    base.setCreateUserName(Constants.user_name);
                    questionBaseDao.addQuestionBase(base);
                }
                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setType(QuestionType.FILLING.toString());
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }
                for(QuestionKnowledge v:minor){
                    v.setType(QuestionType.FILLING.toString());
                    questionKnowledgeDao.addQuestionKnowledge(v);
                }

                if(answers == null){
                    logger.error("error on question No={} 缺少 answer 信息",errerId);
                }else{
                    for(QuestionFillingAnswer v: answers){
                        v.setFillingId(res.getBaseId());
                        List<String> imgPath3 = HtmlStrUtil.getImgSrc(v.getTopic());
                        String str3=v.getTopic();
                        for(String s:imgPath3){
                            if(imageDataService.getMongoId(s) != null){
                                str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                            }
                        }
                        v.setTopic(str3);

                        questionFillingDao.addQuestionAnswer(v);
                    }
                }


                List<String> imgPath = HtmlStrUtil.getImgSrc(res.getTopic());
                String str=res.getTopic();
                for(String s:imgPath){
                    if(imageDataService.getMongoId(s) != null){
                        str = str.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setTopic(str);

                List<String> imgPath2 = HtmlStrUtil.getImgSrc(res.getAnalysis());
                String str2=res.getAnalysis();
                for(String s:imgPath2){
                    if(imageDataService.getMongoId(s) != null){
                        str2 = str2.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setAnalysis(str2);
                res.setId(res.getBaseId());
                questionFillingDao.addQuestion(res);
                successCount++;
            }
        }catch (Exception e){
            logger.error("error on question No={} ",errerId);
            logger.error(e.getMessage());

        }
        return String.valueOf(successCount);
    }

    private void transferItemData(List<QuestionFilling> resList){
        try{
            QuestionFilling res = resList.get(0);
            List<QuestionFillingAnswer> answers = questionFillingDao.findQuestionFillingAnswer(res.getId());

            for(QuestionFillingAnswer v: answers){
                v.setFillingId(res.getBaseId());
                List<String> imgPath3 = HtmlStrUtil.getImgSrc(v.getTopic());
                String str3=v.getTopic();
                for(String s:imgPath3){
                    if(imageDataService.getMongoId(s) != null){
                        str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                v.setTopic(str3);

                questionFillingDao.addQuestionAnswer(v);
            }

            List<String> imgPath = HtmlStrUtil.getImgSrc(res.getTopic());
            String str=res.getTopic();
            for(String s:imgPath){
                if(imageDataService.getMongoId(s) != null){
                    str = str.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                }
            }
            res.setTopic(str);

            List<String> imgPath2 = HtmlStrUtil.getImgSrc(res.getAnalysis());
            String str2=res.getAnalysis();
            for(String s:imgPath2){
                if(imageDataService.getMongoId(s) != null){
                    str2 = str2.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                }
            }
            res.setAnalysis(str2);
            res.setId(res.getBaseId());
            questionFillingDao.addQuestion(res);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public int getCount() {
        return questionFillingDao.getCount();
    }

    @Override
    public List<QuestionFilling> findQuestionByPage(int start, int end) {
        return questionFillingDao.findQuestionByPage(start, end);
    }

}