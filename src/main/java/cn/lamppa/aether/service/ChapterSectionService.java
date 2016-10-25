package cn.lamppa.aether.service;


import cn.lamppa.aether.domain.ChapterSection;

import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public interface ChapterSectionService {
    /**
     * find list of chapterSeciton  by textbook's id
     * **/
    public List<ChapterSection> findChapterSectionByTextbook(String textbookId);
}
