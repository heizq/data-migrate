package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.dao.*;
import cn.lamppa.aether.domain.*;
import cn.lamppa.aether.enums.QuestionAuditStatus;
import cn.lamppa.aether.enums.QuestionStatus;
import cn.lamppa.aether.enums.QuestionType;
import cn.lamppa.aether.service.ImageDataService;
import cn.lamppa.aether.service.QuestionShortanswerService;
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
 * Created by Administrator on 2016/2/25.
 **/
@Service
public class QuestionShortanswerServiceImpl implements QuestionShortanswerService {

    private static Logger logger = LoggerFactory.getLogger(QuestionShortanswerServiceImpl.class);

    @Resource
    private QuestionShortanswerDao questionShortanswerDao;

    @Resource
    private QuestionBaseDao questionBaseDao;

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
        logger.info("start transfer shortanswer item ");
        List<QuestionShortanswer> list = questionShortanswerDao.findQuestionShortanswer(questionId,true);

        if(list != null && list.size() > 0){
            transferItemData(list);
        }else {
            logger.error("Synthetical item shortanswer id={} not found",questionId);
        }
    }

    @Override
    public int getCount() {
        return questionShortanswerDao.getCount();
    }

    @Override
    public List<QuestionShortanswer> findShortanswerByPage(int start, int end) {
        return  questionShortanswerDao.findShortanswerByPage(start,end);
    }


    public  String transferData(List<QuestionShortanswer> resList){
        int successcount=0;
        String errerId = "";
        try{
            for(QuestionShortanswer res:resList){
                errerId = res.getId();
                System.out.println("start import shortanswer id="+res.getId());

                QuestionBase base = questionBaseDao.findQuestionBase(res.getBaseId());
                QuestionKnowledge major = questionKnowledgeDao.findMajorKnowledge(res.getBaseId());
                List<QuestionKnowledge> minor = questionKnowledgeDao.findMinorKnowledges(res.getBaseId());

                if(base == null){
                    logger.error("error on question No={} 缺少 base 信息",errerId);
                }else{
                   base.setType(QuestionType.SHORTANSWER.toString());
                    base.setAuditStatus(QuestionAuditStatus.APPROVED.toString());
                    base.setCreateTime(new Date());
                    base.setCreateUserId(Constants.user_key);
                    base.setCreateUserName(Constants.user_name);
                   questionBaseDao.addQuestionBase(base);
               }
                if(major == null){
                    logger.error("error on question No={} 缺少 knowledge 信息",errerId);
                }else{
                    major.setType(QuestionType.SHORTANSWER.toString());
                    questionKnowledgeDao.addQuestionKnowledge(major);
                }

                for(QuestionKnowledge v:minor){
                    v.setType(QuestionType.SHORTANSWER.toString());
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

                List<String> imgPath3 = HtmlStrUtil.getImgSrc(res.getAnswer());
                String str3=res.getAnswer();
                for(String s:imgPath3){
                    if(imageDataService.getMongoId(s) != null){
                        str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                    }
                }
                res.setAnswer(str3);
                res.setId(res.getBaseId());
                questionShortanswerDao.addQuestionShortanswer(res);
                successcount++;
            }
        }catch (Exception e){
            logger.error("run question No={} errer",errerId);
            logger.error(e.getMessage());
        }
        return String.valueOf(successcount);
    }


    private void transferItemData(List<QuestionShortanswer> resList){
        try{
            QuestionShortanswer res = resList.get(0);

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

            List<String> imgPath3 = HtmlStrUtil.getImgSrc(res.getAnswer());
            String str3=res.getAnswer();
            for(String s:imgPath3){
                if(imageDataService.getMongoId(s) != null){
                    str3 = str3.replace(s, Constants.MONGO_PREFIX+imageDataService.getMongoId(s));
                }
            }
            res.setAnswer(str3);
            res.setId(res.getBaseId());
            questionShortanswerDao.addQuestionShortanswer(res);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }


}
