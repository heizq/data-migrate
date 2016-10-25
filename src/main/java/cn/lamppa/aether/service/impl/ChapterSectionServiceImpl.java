package cn.lamppa.aether.service.impl;

import cn.lamppa.aether.dao.ChapterSectionDao;
import cn.lamppa.aether.domain.ChapterSection;
import cn.lamppa.aether.domain.ChapterSectionKnowledge;
import cn.lamppa.aether.service.ChapterSectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
@Service
public class ChapterSectionServiceImpl implements ChapterSectionService {
    @Resource
    private ChapterSectionDao chapterSectionDao;
    public List<ChapterSection> findChapterSectionByTextbook(String textbookId){
        ChapterSection chapterSection= new ChapterSection();
        chapterSection.setTextbookId(textbookId);
        chapterSection.setParentId(null);
        List<ChapterSection> chapterSections = chapterSectionDao.findChapterSectionByTextbookAndParentId(chapterSection);
        List<ChapterSection> resultList = new ArrayList<ChapterSection>();
        for (ChapterSection v:chapterSections){
            List<ChapterSectionKnowledge> knowledgeVoList = chapterSectionDao.findChapterSectionKnowledge(v.getId());
            v.setKnowledge(knowledgeVoList);
            ChapterSection v1 = new ChapterSection();
            v1.setTextbookId(textbookId);
            v1.setParentId(v.getId());
            List<ChapterSection> childrenList = chapterSectionDao.findChapterSectionByTextbookAndParentId(v1);
            List<ChapterSection> cList = new ArrayList<ChapterSection>();
            cList =  getChapterSectionChildren(cList, childrenList);
            v.setChildren(cList);

            resultList.add(v);

        }
        return  resultList;
    }
    private List<ChapterSection> getChapterSectionChildren(List<ChapterSection> resultList,
                                                           List<ChapterSection> childrenList){

        if(childrenList != null && childrenList.size() > 0){
            for(ChapterSection v:childrenList){
                List<ChapterSectionKnowledge> knowledgeVoList = chapterSectionDao.findChapterSectionKnowledge(v.getId());
                v.setKnowledge(knowledgeVoList);

                ChapterSection vo1 = new ChapterSection();
                vo1.setTextbookId(v.getTextbookId());
                vo1.setParentId(v.getId());
                List<ChapterSection> cList = chapterSectionDao.findChapterSectionByTextbookAndParentId(vo1);
                if(cList != null && cList.size() > 0){
                    List<ChapterSection> rList = new ArrayList<ChapterSection>();
                    rList = getChapterSectionChildren(rList, cList);
                    v.setChildren(rList);
                }else {
                    List<ChapterSection> rList = new ArrayList<ChapterSection>();
                    v.setChildren(rList);
                }
                resultList.add(v);
            }

        }
        return resultList;
    }

}

