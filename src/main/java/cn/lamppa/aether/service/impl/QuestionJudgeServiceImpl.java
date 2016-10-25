package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.dao.*;
import cn.lamppa.aether.domain.QuestionBase;
import cn.lamppa.aether.domain.QuestionCategory;
import cn.lamppa.aether.domain.QuestionJudge;
import cn.lamppa.aether.domain.QuestionKnowledge;
import cn.lamppa.aether.enums.QuestionAuditStatus;
import cn.lamppa.aether.enums.QuestionStatus;
import cn.lamppa.aether.enums.QuestionType;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.service.QuestionJudgeService;
import cn.lamppa.aether.util.Constants;
import cn.lamppa.aether.util.HtmlStrUtil;
import cn.lamppa.aether.util.IdSequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/27.
 */
@Service
public class QuestionJudgeServiceImpl implements QuestionJudgeService {
    private static Logger logger = LoggerFactory.getLogger(QuestionJudgeServiceImpl.class);
    @Resource
    private QuestionBaseDao questionBaseDao;
    @Resource
    private QuestionJudgeDao questionJudgeDao;
    @Resource
    private QuestionKnowledgeDao questionKnowledgeDao;
    @Resource
    private QuestionTestPointDao questionTestPointDao;
    @Resource
    private QuestionCategoryDao questionCategoryDao;
    @Resource
    private CommonDao commonDao;
    @Resource
    private ImageDataService imageDataService;


    public void syncItemQuestion(String questionId) {
        logger.info("start transfer judge item  ");
        List<QuestionJudge> list = questionJudgeDao.findQuestionJudges(questionId,true);
        if(list != null && list.size() > 0){
            transferItemData(list);
        }else {
            logger.error("Synthetical judge item id={} not found",questionId);
        }
    }

    @Override
    public List<QuestionJudge> findJudgeByPage(int start, int end) {
        return questionJudgeDao.findJudgeByPage(start, end);
    }

    public String  transferData(List<QuestionJudge> resList){
        String errerId = "";
        int successcount=0;
        try{
            for(QuestionJudge res:resList){
                errerId = res.getId();
                System.out.println("start import judge id="+res.getId());

                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getBaseId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getBaseId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                    base.setType(QuestionType.JUDGE.toString());
                    base.setAuditStatus(QuestionAuditStatus.APPROVED.toString());
                    base.setCreateTime(new Date());
                    base.setCreateUserId(Constants.user_key);
                    base.setCreateUserName(Constants.user_name);
                    questionBaseDao.addQuestionBase(base);
                }
                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setType(QuestionType.JUDGE.toString());
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }


                for(QuestionKnowledge v:minor){
                    v.setType(QuestionType.JUDGE.toString());
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

                List<String> imgPath2 = HtmlStrUtil.getImgSrc(res.getAnalysis());
                String str2=res.getAnalysis();
                for(String s:imgPath2){
                    if(imageDataService.getMongoId(s) != null){
                        str2 = str2.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setAnalysis(str2);
                res.setId(res.getBaseId());
                questionJudgeDao.addQuestion(res);
                successcount++;
            }
        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successcount);
    }

    @Override
    public int getCount() {
       return questionJudgeDao.count();
    }

    private void transferItemData(List<QuestionJudge> resList){
        try{
            QuestionJudge res = resList.get(0);

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
            questionJudgeDao.addQuestion(res);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

}
