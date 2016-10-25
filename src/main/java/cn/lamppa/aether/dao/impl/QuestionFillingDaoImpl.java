package cn.lamppa.aether.dao.impl;

import cn.lamppa.aether.dao.QuestionFillingDao;
import cn.lamppa.aether.domain.QuestionFilling;
import cn.lamppa.aether.domain.QuestionFillingAnswer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heizhiqiang on 2016/2/29 0029.
 */
@Configurable
@Repository(value="questionFillingDao")
public class QuestionFillingDaoImpl implements QuestionFillingDao {

    @Resource
    private JdbcTemplate resJdbcTemplate ;

    @Resource
    private JdbcTemplate platformJdbcTemplate ;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public String addQuestion(QuestionFilling model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("analysis",model.getAnalysis());
        params.put("base_id",model.getBaseId());
        params.put("is_small",model.getIsSmall());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_filling");
        insert.execute(params);
        return model.getId();
    }

    public String addQuestionAnswer(QuestionFillingAnswer model) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",model.getId());
        params.put("topic",model.getTopic());
        params.put("sort",model.getSort());
        params.put("filling_id",model.getFillingId());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(platformJdbcTemplate);

        insert.withTableName("ks_question_filling_answer");
        insert.execute(params);
        return model.getId();
    }

    public List<QuestionFilling> findQuestionFilling(String questionId,Boolean isSmall) {
        StringBuilder sql = new StringBuilder("");
        List<Object> params = new ArrayList<Object>();
        params.add(isSmall);
        sql.append("SELECT  id,  topic,  analysis,  base_id,  is_small");
        sql.append(" FROM  ks_question_filling  where is_small=?");

        if(StringUtils.isNotEmpty(questionId)){
            sql.append(" and base_id=?");
            params.add(questionId);
        }
        List<QuestionFilling> list = this.resJdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                new RowMapper<QuestionFilling>() {
                    public QuestionFilling mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionFilling model = new QuestionFilling();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));

                        return model;
                    }
                });

        return list;
    }

    public List<QuestionFillingAnswer> findQuestionFillingAnswer(String questionId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id, topic, sort, filling_id FROM  ks_question_filling_answer   where filling_id = ?");
        List<QuestionFillingAnswer> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{questionId},
                new RowMapper<QuestionFillingAnswer>() {
                    public QuestionFillingAnswer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionFillingAnswer model = new QuestionFillingAnswer();
                        model.setId(rs.getString("id"));
                        model.setFillingId(rs.getString("filling_id"));
                        model.setSort(rs.getInt("sort"));
                        model.setTopic(rs.getString("topic"));
                        return model;
                    }
                });

        return list;
    }

    @Override
    public List<QuestionFilling> findQuestionByPage(int start, int end) {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  id,  topic,  analysis,  base_id,  is_small ");
        sql.append(" FROM  ks_question_filling where is_small = false  limit ?,?");

        List<QuestionFilling> list = this.resJdbcTemplate.query(
                sql.toString(),
                new Object[]{start,end},
                new RowMapper<QuestionFilling>() {
                    public QuestionFilling mapRow(ResultSet rs, int rowNum) throws SQLException {
                        QuestionFilling model = new QuestionFilling();
                        model.setId(rs.getString("id"));
                        model.setTopic(rs.getString("topic"));
                        model.setAnalysis(rs.getString("analysis"));
                        model.setBaseId(rs.getString("base_id"));
                        model.setIsSmall(rs.getBoolean("is_small"));
                        return model;
                    }
                });

        return list;
    }

    @Override
    public int getCount() {
        StringBuilder sql = new StringBuilder("");
        sql.append("SELECT  count(1) ");
        sql.append(" FROM  ks_question_filling  where is_small= false");

        int count = this.resJdbcTemplate.queryForObject(sql.toString(),Integer.class);
        return count;
    }
}
