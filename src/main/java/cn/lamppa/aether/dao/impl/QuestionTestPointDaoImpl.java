package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionTestPointDao;
import cn.lamppa.aether.domain.QuestionTestPoint;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by heizhiqiang on 2016/2/25 0025.
 */
@Configurable
@Repository(value="questionTestPointDao")
public class QuestionTestPointDaoImpl implements QuestionTestPointDao {

    @Resource
    private JdbcTemplate resJdbcTemplate;

    @Resource
    private JdbcTemplate platformJdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;

    public int[] addQuestionTestPoint(List<QuestionTestPoint> list) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(platformJdbcTemplate);

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());

        StringBuilder sql = new StringBuilder("");
        sql.append("INSERT INTO ks_question_kl_testpoint (id,question_id,knowledge_id,question_type,testpoint_id) VALUES");
        sql.append("(:id,:questionId,:knowledgeId,:questionType,:testPointId)");
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql.toString(),batch);
        return updateCounts;
    }

    public List<QuestionTestPoint> findQuestionTestPoints(String id) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, question_id, knowledge_id, question_type, testpoint_id FROM  ks_question_kl_testpoint  where question_id = ?");
        List<QuestionTestPoint> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{id},
                new RowMapper<QuestionTestPoint>() {
                    public QuestionTestPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionTestPoint model = new QuestionTestPoint();
                        model.setId(rs.getString("id"));
                        model.setQuestionId(rs.getString("question_id"));
                        model.setKnowledgeId(rs.getString("knowledge_id"));
                        model.setQuestionType(rs.getString("question_type"));
                        model.setTestPointId(rs.getString("testpoint_id"));
                        return model;
                    }
                });

        return list;
    }
}
