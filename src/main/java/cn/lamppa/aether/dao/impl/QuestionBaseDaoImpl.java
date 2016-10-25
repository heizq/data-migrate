package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionBaseDao;
import cn.lamppa.aether.domain.QuestionBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/24 0024.
 */
@Configurable
@Repository(value="questionBaseDao")
public class QuestionBaseDaoImpl implements QuestionBaseDao {
    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    public int addQuestionBase(QuestionBase model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("difficulty",model.getDifficulty());
        params.put("year",model.getYear());
        params.put("use_num",model.getUseNum());
        params.put("accuracy",model.getAccuracy());
        params.put("source",model.getSource());
        params.put("type",model.getType());
        params.put("level",model.getLevel());
        params.put("school_id",model.getSchoolId());
        params.put("question_type_id",model.getQuestionTypeId());
        params.put("category_code",model.getCategoryCode());
        params.put("category_name",model.getCategoryName());
        params.put("audit_status",model.getAuditStatus());
        params.put("audit_time",model.getAuditTime());
        params.put("audit_user_id",model.getAuditUserId());
        params.put("audit_opinion",model.getAuditOpinion());
        params.put("create_time",model.getCreateTime());
        params.put("create_user_id",model.getCreateUserId());
        params.put("create_user_name",model.getCreateUserName());
        params.put("audit_user_name",model.getAuditUserName());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);
        insert.withTableName("ks_question_base");
        int num =  insert.execute(params);

        return num;
    }

    public QuestionBase findQuestionBase(String id) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, difficulty, YEAR, source, use_num, accuracy, TYPE ");
        sql.append(" ,level,school_id,question_type_id,category_code,category_name");
        sql.append(" ,audit_status,audit_time,audit_user_id,audit_opinion,create_time,create_user_id,audit_user_name,create_user_name");
        sql.append(" FROM  ks_question_base  where id = ?");
        List<QuestionBase> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{id},
                new RowMapper<QuestionBase>() {
                    public QuestionBase mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionBase model = new QuestionBase();
                        model.setId(rs.getString("id"));
                        model.setYear(rs.getString("year"));
                        model.setType(rs.getString("type"));
                        model.setUseNum(rs.getInt("use_num"));
                        model.setSource(rs.getString("source"));
                        model.setAccuracy(rs.getString("accuracy"));
                        model.setDifficulty(rs.getInt("difficulty"));

                        model.setLevel(rs.getString("level"));
                        model.setSchoolId(rs.getString("school_id"));
                        model.setQuestionTypeId(rs.getString("question_type_id"));
                        model.setCategoryCode(rs.getString("category_code"));
                        model.setCategoryName(rs.getString("category_name"));
                        model.setAuditStatus(rs.getString("audit_status"));
                        model.setAuditTime(rs.getTime("audit_time"));
                        model.setAuditUserId(rs.getString("audit_user_id"));
                        model.setAuditOpinion(rs.getString("audit_opinion"));
                        model.setCreateTime(rs.getTime("create_time"));
                        model.setCreateUserId(rs.getString("create_user_id"));
                        model.setCreateUserName(rs.getString("create_user_name"));
                        model.setAuditUserName(rs.getString("audit_user_name"));
                        return model;
                    }
                });

        if(list!=null &&list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }
}
