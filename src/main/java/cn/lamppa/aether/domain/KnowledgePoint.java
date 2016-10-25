package cn.lamppa.aether.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liupd on 15-11-12.
 **/
public class KnowledgePoint implements Serializable{

    //知识点Id
    private  String id;
    //知识点名称
    private  String name;
    //学科Id
    private  String subjectId;
    //学科名称
    private  String subjectName;
    //学段Id
    private  String phasesId;
    //学段名称
    private  String phasesName;
    //知识点说明
    private  String instruction;
    //知识点备注
    private  String remark;
    //更新时间
    private  Date   updateTime;
    //更新人
    private  String updateUser;

    public KnowledgePoint() {}

    public String getSubjectName() {return subjectName;}

    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}

    public String getPhasesName() {return phasesName;}

    public void setPhasesName(String phasesName) {this.phasesName = phasesName;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUpdateUser() {return updateUser;}

    public void setUpdateUser(String updateUser) { this.updateUser = updateUser;}

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {return remark;}

    public void setRemark(String remark) {this.remark = remark;}

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getPhasesId() {
        return phasesId;
    }

    public void setPhasesId(String phasesId) {
        this.phasesId = phasesId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", phasesId='" + phasesId + '\'' +
                ", phasesName='" + phasesName + '\'' +
                ", instruction='" + instruction + '\'' +
                ", remark='" + remark + '\'' +
                ", updateTime=" + updateTime +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }
}
