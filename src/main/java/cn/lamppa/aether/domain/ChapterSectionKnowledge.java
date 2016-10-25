package cn.lamppa.aether.domain;

import java.io.Serializable;

/**
 * Created by heizhiqiang on 2015/11/12
 */
public class ChapterSectionKnowledge implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     * 章节id
     */
    private String chapterSectionId;

    /**
     * 知识点id
     */
    private String knowledgeId;

    /**
     * 认知层次id
     */
    private String cognizeLevelId;

    /**
     * 能力类型id
     */
    private String abilityId;
    /**
     * 知识点名称
     */
    private String name;

    /**
     * 排序
     */
    private int sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterSectionId() {
        return chapterSectionId;
    }

    public void setChapterSectionId(String chapterSectionId) {
        this.chapterSectionId = chapterSectionId;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getCognizeLevelId() {
        return cognizeLevelId;
    }

    public void setCognizeLevelId(String cognizeLevelId) {
        this.cognizeLevelId = cognizeLevelId;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ChapterSectionKnowledge{" +
                "id='" + id + '\'' +
                ", chapterSectionId='" + chapterSectionId + '\'' +
                ", knowledgeId='" + knowledgeId + '\'' +
                ", cognizeLevelId='" + cognizeLevelId + '\'' +
                ", abilityId='" + abilityId + '\'' +
                ", sort=" + sort +
                '}';
    }
}
