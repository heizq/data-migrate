package cn.lamppa.aether.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by heizhiqiang on 2015/11/12
 */
public class ChapterSection implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父节点ID
     */
    private String parentId;

    /**
     * 特殊标志
     */
    private String specialType;

    /**
     * 教材ID
     */
    private String textbookId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 排序
     */
    private int sort;


    /**
     * 知识点
     */
    private List<ChapterSectionKnowledge> knowledge;

    /**
     * 子节点
     */
    private List<ChapterSection>  children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }
    public String getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(String textbookId) {
        this.textbookId = textbookId;
    }

    public List<ChapterSectionKnowledge> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<ChapterSectionKnowledge> knowledge) {
        this.knowledge = knowledge;
    }

    public List<ChapterSection> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterSection> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChapterSection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", textbookId='" + textbookId + '\'' +
                ", updateTime=" + updateTime +
                ", sort=" + sort +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}
