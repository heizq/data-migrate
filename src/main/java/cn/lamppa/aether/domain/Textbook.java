package cn.lamppa.aether.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by heizhiqiang on 2015/11/12
 */
public class Textbook implements Serializable{

    /**
     * primary key
     */
    private String  id;

    /**
     * 学科id
     */
    private String subjectId;

    /**
     * 学科名称
     */
    private String subjectName;

    /**
     * 年级ID
     */
    private String gradeId;

    /**
     * 年级名称
     */
    private String gradeName;

    /**
     * 学段ID
     */
    private String phaseId;

    /**
     * 学段名称
     */
    private String phaseName;

    /**
     * 教材版本id
     */
    private String textbookVersionId;

    /**
     * 教材版本名
     */
    private String textbookVersionName;

    /**
     * 册id
     */
    private String fasciculeId;

    /**
     * 册
     */
    private String fasciculeName;
    /**
     * 出版年月
     */
    private Date publishTime;

    /**
     * 封面
     */
    private String cover;

    /**
     *  （是否启用）状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public String getFasciculeId() {
        return fasciculeId;
    }

    public void setFasciculeId(String fasciculeId) {
        this.fasciculeId = fasciculeId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getTextbookVersionId() {
        return textbookVersionId;
    }

    public void setTextbookVersionId(String textbookVersionId) {
        this.textbookVersionId = textbookVersionId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getTextbookVersionName() {
        return textbookVersionName;
    }

    public void setTextbookVersionName(String textbookVersionName) {
        this.textbookVersionName = textbookVersionName;
    }

    public String getFasciculeName() {
        return fasciculeName;
    }

    public void setFasciculeName(String fasciculeName) {
        this.fasciculeName = fasciculeName;
    }
}
